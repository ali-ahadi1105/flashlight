package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameState(
    val currentLevel: Level? = null,
    val nodes: List<Node> = emptyList(),
    val activePaths: List<PathSegment> = emptyList(),
    val movesLeft: Int = 0,
    val isLevelCleared: Boolean = false,
    val isLevelFailed: Boolean = false,
    val extraMovesItemCount: Int = 0,
    val bypassItemCount: Int = 0,
    val bypassModeActive: Boolean = false,
    val earnedStars: Int = 0
)

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    val unlockedLevel = repository.unlockedLevel
    val levelScores = repository.levelScores

    init {
        viewModelScope.launch {
            repository.extraMovesItemCount.collect { count ->
                _gameState.value = _gameState.value.copy(extraMovesItemCount = count)
            }
        }
        viewModelScope.launch {
            repository.bypassItemCount.collect { count ->
                _gameState.value = _gameState.value.copy(bypassItemCount = count)
            }
        }
    }

    fun loadLevel(levelId: Int) {
        val level = LevelGenerator.generate(levelId)
        val random = kotlin.random.Random(levelId + 100)
        
        var scrambledNodes = level.initialNodes.map { node ->
            if (node.type == NodeType.ROUTER && !node.isLocked) {
                val rotations = random.nextInt(1, 4)
                var newDir = node.direction
                for (r in 0 until rotations) {
                    newDir = newDir?.rotate()
                }
                node.copy(direction = newDir, rotations = 0)
            } else {
                node.copy()
            }
        }

        _gameState.value = _gameState.value.copy(
            currentLevel = level,
            nodes = scrambledNodes,
            movesLeft = level.moveLimit,
            isLevelCleared = false,
            isLevelFailed = false,
            bypassModeActive = false,
            activePaths = emptyList(),
            earnedStars = 0
        )
        calculatePaths(initCall = true)
    }

    fun useExtraMoves() {
        val state = _gameState.value
        if (state.extraMovesItemCount > 0 && !state.isLevelCleared && !state.isLevelFailed) {
            viewModelScope.launch {
                if (repository.useExtraMovesItem()) {
                    SoundManager.playItemSound()
                    _gameState.value = _gameState.value.copy(movesLeft = _gameState.value.movesLeft + 5)
                }
            }
        }
    }

    fun toggleBypassMode() {
        val state = _gameState.value
        if (state.bypassItemCount > 0 && !state.isLevelCleared && !state.isLevelFailed) {
            SoundManager.playItemSound()
            _gameState.value = state.copy(bypassModeActive = !state.bypassModeActive)
        }
    }

    fun rotateRouter(nodeId: String) {
        val state = _gameState.value
        if (state.isLevelCleared || state.isLevelFailed || state.movesLeft <= 0) return

        val targetNode = state.nodes.find { it.id == nodeId } ?: return
        if (targetNode.type != NodeType.ROUTER) return

        if (state.bypassModeActive) {
            if (targetNode.isLocked) {
                viewModelScope.launch {
                    if (repository.useBypassItem()) {
                        SoundManager.playItemSound()
                        val updatedNodes = state.nodes.map { node ->
                            if (node.id == nodeId) node.copy(isLocked = false) else node
                        }
                        _gameState.value = state.copy(
                            nodes = updatedNodes,
                            bypassModeActive = false
                        )
                        calculatePaths()
                    }
                }
            } else {
                _gameState.value = state.copy(bypassModeActive = false)
            }
            return
        }

        if (targetNode.isLocked) return

        SoundManager.playRotateSound()

        val updatedNodes = state.nodes.map { node ->
            if (node.id == nodeId && node.type == NodeType.ROUTER && !node.isLocked) {
                node.copy(
                    direction = node.direction?.rotate(),
                    rotations = node.rotations + 1
                )
            } else {
                node
            }
        }

        _gameState.value = state.copy(
            nodes = updatedNodes,
            movesLeft = state.movesLeft - 1
        )
        calculatePaths()
    }

    private fun calculatePaths(initCall: Boolean = false) {
        val state = _gameState.value
        if (state.currentLevel == null) return
        val nodes = state.nodes.toMutableList()
        val segments = mutableListOf<PathSegment>()
        var activeTargetsCount = 0
        val targetsTotal = nodes.count { it.type == NodeType.TARGET }

        // Reset all targets to not satisfied
        for (i in nodes.indices) {
            if (nodes[i].type == NodeType.TARGET) {
                nodes[i] = nodes[i].copy(isSatisfied = false)
            }
        }

        val sources = nodes.filter { it.type == NodeType.SOURCE }

        for (source in sources) {
            var currentX = source.x
            var currentY = source.y
            var currentDir = source.direction ?: Direction.RIGHT
            val visited = mutableSetOf<String>()
            visited.add(source.id)

            while (true) {
                // Find next node in currentDir
                val nextNode = findNextNode(currentX, currentY, currentDir, nodes, state.currentLevel.gridWidth, state.currentLevel.gridHeight)

                if (nextNode != null) {
                    segments.add(PathSegment(currentX, currentY, nextNode.x, nextNode.y, source.color))

                    if (visited.contains(nextNode.id)) {
                        break // Loop
                    }
                    visited.add(nextNode.id)

                    if (nextNode.type == NodeType.TARGET) {
                        if (nextNode.color == source.color || nextNode.color == NodeColor.WHITE) {
                            // Target satisfied
                            val targetIndex = nodes.indexOfFirst { it.id == nextNode.id }
                            if (targetIndex != -1 && !nodes[targetIndex].isSatisfied) {
                                nodes[targetIndex] = nodes[targetIndex].copy(isSatisfied = true)
                                activeTargetsCount++
                            }
                        }
                        break // Pulse stops at target
                    } else if (nextNode.type == NodeType.ROUTER) {
                        currentDir = nextNode.direction ?: Direction.RIGHT
                        currentX = nextNode.x
                        currentY = nextNode.y
                    } else {
                        break // Blocked by source or unknown
                    }
                } else {
                    // No node found, pulse flies off grid
                    val edgeX = when (currentDir) {
                        Direction.LEFT -> -1
                        Direction.RIGHT -> state.currentLevel.gridWidth
                        else -> currentX
                    }
                    val edgeY = when (currentDir) {
                        Direction.UP -> -1
                        Direction.DOWN -> state.currentLevel.gridHeight
                        else -> currentY
                    }
                    segments.add(PathSegment(currentX, currentY, edgeX, edgeY, source.color))
                    break
                }
            }
        }

        val isCleared = targetsTotal > 0 && activeTargetsCount == targetsTotal
        val isFailed = !isCleared && state.movesLeft <= 0

        var earnedStars = 0
        if (isCleared && !state.isLevelCleared) {
            val efficiency = state.movesLeft.toFloat() / state.currentLevel.moveLimit.toFloat()
            earnedStars = when {
                efficiency >= 0.5f -> 3
                efficiency >= 0.2f -> 2
                else -> 1
            }
            if (!initCall) {
                SoundManager.playWinSound()
            }
            viewModelScope.launch {
                repository.unlockLevel(state.currentLevel.id + 1)
                repository.saveLevelScore(state.currentLevel.id, earnedStars)
            }
        } else if (isFailed && !state.isLevelFailed) {
            if (!initCall) {
                SoundManager.playLoseSound()
            }
        }

        _gameState.value = state.copy(
            nodes = nodes,
            activePaths = segments,
            isLevelCleared = isCleared,
            isLevelFailed = isFailed,
            earnedStars = if (isCleared) earnedStars else state.earnedStars
        )
    }

    private fun findNextNode(x: Int, y: Int, dir: Direction, nodes: List<Node>, width: Int, height: Int): Node? {
        var currX = x + dir.dx
        var currY = y + dir.dy
        while (currX in 0 until width && currY in 0 until height) {
            val node = nodes.find { it.x == currX && it.y == currY }
            if (node != null) return node
            currX += dir.dx
            currY += dir.dy
        }
        return null
    }
}
