package com.example

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.MagentaAccent

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);
    fun rotate(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
    fun toAngle(): Float = when (this) {
        UP -> -90f
        RIGHT -> 0f
        DOWN -> 90f
        LEFT -> 180f
    }
}

enum class NodeType { SOURCE, TARGET, ROUTER, NONE }
enum class NodeColor(val color: Color) {
    CYAN(CyanAccent),
    MAGENTA(MagentaAccent),
    LIME(LimeAccent),
    WHITE(Color.White)
}

data class Node(
    val id: String,
    val type: NodeType,
    val x: Int,
    val y: Int,
    val direction: Direction? = null,
    val color: NodeColor = NodeColor.WHITE,
    val isSatisfied: Boolean = false,
    val isLocked: Boolean = false, // If true, router cannot be rotated
    val rotations: Int = 0
)

data class Level(
    val id: Int,
    val moveLimit: Int,
    val gridWidth: Int,
    val gridHeight: Int,
    val initialNodes: List<Node>
)

data class PathSegment(
    val startX: Int,
    val startY: Int,
    val endX: Int,
    val endY: Int,
    val color: NodeColor
)
