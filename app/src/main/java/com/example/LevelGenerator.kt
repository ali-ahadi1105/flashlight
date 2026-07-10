package com.example

import kotlin.random.Random

object LevelGenerator {
    fun generate(levelId: Int): Level {
        if (levelId <= LevelData.levels.size) {
            return LevelData.levels[levelId - 1]
        }
        
        val random = Random(levelId)
        val width = minOf(5 + ((levelId - 15) / 30), 8)
        val height = minOf(5 + ((levelId - 15) / 30), 8)
        val colors = listOf(NodeColor.CYAN, NodeColor.MAGENTA, NodeColor.LIME)
        val numPaths = minOf(1 + ((levelId - 15) / 20), 3)
        
        val nodes = mutableListOf<Node>()
        val used = mutableSetOf<Pair<Int, Int>>()
        
        var minMoves = 0
        
        for (i in 0 until numPaths) {
            val color = colors[i]
            var sx = random.nextInt(width)
            var sy = random.nextInt(height)
            while (used.contains(sx to sy)) {
                sx = random.nextInt(width)
                sy = random.nextInt(height)
            }
            used.add(sx to sy)
            
            var dir = Direction.values().random(random)
            nodes.add(Node("s$i", NodeType.SOURCE, sx, sy, dir, color))
            
            var cx = sx
            var cy = sy
            
            val pathLen = 3 + random.nextInt((levelId - 15) / 10 + 2)
            var validTarget = false
            
            for (step in 0 until pathLen) {
                val nx = cx + dir.dx
                val ny = cy + dir.dy
                if (nx !in 0 until width || ny !in 0 until height || used.contains(nx to ny)) {
                    if (cx != sx || cy != sy) {
                        nodes.add(Node("t$i", NodeType.TARGET, cx, cy, color = color))
                        validTarget = true
                    }
                    break
                }
                
                cx = nx
                cy = ny
                used.add(cx to cy)
                
                if (random.nextFloat() < 0.6f && step < pathLen - 1) {
                    val turns = listOf(dir.rotate(), dir.rotate().rotate().rotate())
                    val newDir = turns.random(random)
                    val scrambleDir = Direction.values().random(random)
                    nodes.add(Node("r${i}_${step}", NodeType.ROUTER, cx, cy, scrambleDir))
                    if (scrambleDir != newDir) minMoves++
                    dir = newDir
                }
            }
            
            if (!validTarget) {
                if (cx != sx || cy != sy) {
                    nodes.add(Node("t$i", NodeType.TARGET, cx, cy, color = color))
                }
            }
        }
        
        val noiseCount = random.nextInt((levelId - 15) / 5 + 3)
        for (i in 0 until noiseCount) {
            val nx = random.nextInt(width)
            val ny = random.nextInt(height)
            if (!used.contains(nx to ny)) {
                used.add(nx to ny)
                val isLocked = random.nextFloat() < 0.1f
                nodes.add(Node("rn_$i", NodeType.ROUTER, nx, ny, Direction.values().random(random), isLocked = isLocked))
            }
        }
        
        val moveLimit = maxOf(minMoves + 5 + random.nextInt(6), 8)
        
        return Level(levelId, moveLimit, width, height, nodes)
    }
}
