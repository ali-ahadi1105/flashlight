package com.example

object LevelData {
    val levels = listOf(
        // Level 1: Simple right
        Level(1, 3, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 0, 2, Direction.RIGHT, NodeColor.CYAN),
            Node("r1", NodeType.ROUTER, 2, 2, Direction.UP),
            Node("t1", NodeType.TARGET, 4, 2, color = NodeColor.CYAN)
        )),
        // Level 2: Simple bend
        Level(2, 4, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 1, 0, Direction.DOWN, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 1, 3, Direction.RIGHT),
            Node("r2", NodeType.ROUTER, 3, 3, Direction.UP),
            Node("t1", NodeType.TARGET, 3, 1, color = NodeColor.MAGENTA)
        )),
        // Level 3: Two targets, one source
        Level(3, 6, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.LIME),
            Node("r1", NodeType.ROUTER, 2, 0, Direction.DOWN),
            Node("r2", NodeType.ROUTER, 2, 4, Direction.LEFT),
            Node("r3", NodeType.ROUTER, 4, 4, Direction.UP),
            Node("t1", NodeType.TARGET, 4, 2, color = NodeColor.LIME)
        )),
        // Level 4: Two sources, two targets
        Level(4, 8, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.CYAN),
            Node("s2", NodeType.SOURCE, 0, 4, Direction.RIGHT, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 2, 0, Direction.UP),
            Node("r2", NodeType.ROUTER, 2, 4, Direction.DOWN),
            Node("t1", NodeType.TARGET, 2, 2, color = NodeColor.CYAN),
            Node("t2", NodeType.TARGET, 4, 4, color = NodeColor.MAGENTA)
        )),
        // Level 5: Locked Router
        Level(5, 5, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 0, 2, Direction.RIGHT, NodeColor.CYAN),
            Node("r1", NodeType.ROUTER, 2, 2, Direction.UP, isLocked = true),
            Node("r2", NodeType.ROUTER, 2, 0, Direction.RIGHT),
            Node("t1", NodeType.TARGET, 4, 0, color = NodeColor.CYAN)
        )),
        // Level 6: Locked Router 2
        Level(6, 6, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 3, 0, Direction.DOWN, isLocked = true),
            Node("r2", NodeType.ROUTER, 3, 3, Direction.LEFT),
            Node("r3", NodeType.ROUTER, 1, 3, Direction.UP),
            Node("t1", NodeType.TARGET, 1, 1, color = NodeColor.MAGENTA)
        )),
        // Level 7: Bigger board
        Level(7, 8, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 2, 0, Direction.DOWN, NodeColor.LIME),
            Node("r1", NodeType.ROUTER, 2, 3, Direction.RIGHT),
            Node("r2", NodeType.ROUTER, 4, 3, Direction.UP, isLocked = true),
            Node("r3", NodeType.ROUTER, 4, 1, Direction.LEFT),
            Node("t1", NodeType.TARGET, 3, 1, color = NodeColor.LIME)
        )),
        // Level 8: Two locked
        Level(8, 10, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 0, 2, Direction.RIGHT, NodeColor.CYAN),
            Node("s2", NodeType.SOURCE, 5, 2, Direction.LEFT, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 2, 2, Direction.DOWN, isLocked = true),
            Node("r2", NodeType.ROUTER, 3, 2, Direction.UP, isLocked = true),
            Node("t1", NodeType.TARGET, 2, 5, color = NodeColor.CYAN),
            Node("t2", NodeType.TARGET, 3, 0, color = NodeColor.MAGENTA)
        )),
        // Level 9
        Level(9, 8, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 0, 4, Direction.UP, NodeColor.LIME),
            Node("r1", NodeType.ROUTER, 0, 0, Direction.RIGHT),
            Node("r2", NodeType.ROUTER, 4, 0, Direction.DOWN),
            Node("r3", NodeType.ROUTER, 4, 4, Direction.LEFT, isLocked = true),
            Node("r4", NodeType.ROUTER, 2, 4, Direction.UP),
            Node("t1", NodeType.TARGET, 2, 2, color = NodeColor.LIME)
        )),
        // Level 10
        Level(10, 12, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.CYAN),
            Node("r1", NodeType.ROUTER, 3, 0, Direction.DOWN),
            Node("r2", NodeType.ROUTER, 3, 3, Direction.LEFT, isLocked = true),
            Node("r3", NodeType.ROUTER, 1, 3, Direction.DOWN),
            Node("r4", NodeType.ROUTER, 1, 5, Direction.RIGHT),
            Node("r5", NodeType.ROUTER, 5, 5, Direction.UP),
            Node("t1", NodeType.TARGET, 5, 2, color = NodeColor.CYAN)
        )),
        // Level 11
        Level(11, 10, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 2, 5, Direction.UP, NodeColor.MAGENTA),
            Node("s2", NodeType.SOURCE, 3, 5, Direction.UP, NodeColor.LIME),
            Node("r1", NodeType.ROUTER, 2, 2, Direction.LEFT),
            Node("r2", NodeType.ROUTER, 3, 2, Direction.RIGHT, isLocked = true),
            Node("t1", NodeType.TARGET, 0, 2, color = NodeColor.MAGENTA),
            Node("t2", NodeType.TARGET, 5, 2, color = NodeColor.LIME)
        )),
        // Level 12
        Level(12, 14, 7, 7, listOf(
            Node("s1", NodeType.SOURCE, 0, 3, Direction.RIGHT, NodeColor.CYAN),
            Node("r1", NodeType.ROUTER, 3, 3, Direction.UP),
            Node("r2", NodeType.ROUTER, 3, 0, Direction.RIGHT, isLocked = true),
            Node("r3", NodeType.ROUTER, 6, 0, Direction.DOWN),
            Node("r4", NodeType.ROUTER, 6, 6, Direction.LEFT),
            Node("t1", NodeType.TARGET, 3, 6, color = NodeColor.CYAN)
        )),
        // Level 13
        Level(13, 10, 5, 5, listOf(
            Node("s1", NodeType.SOURCE, 2, 0, Direction.DOWN, NodeColor.LIME),
            Node("r1", NodeType.ROUTER, 2, 2, Direction.RIGHT, isLocked = true),
            Node("r2", NodeType.ROUTER, 4, 2, Direction.UP),
            Node("r3", NodeType.ROUTER, 4, 0, Direction.LEFT),
            Node("t1", NodeType.TARGET, 0, 0, color = NodeColor.LIME)
        )),
        // Level 14
        Level(14, 15, 6, 6, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.CYAN),
            Node("s2", NodeType.SOURCE, 0, 5, Direction.RIGHT, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 5, 0, Direction.DOWN, isLocked = true),
            Node("r2", NodeType.ROUTER, 5, 5, Direction.UP, isLocked = true),
            Node("t1", NodeType.TARGET, 5, 2, color = NodeColor.CYAN),
            Node("t2", NodeType.TARGET, 5, 3, color = NodeColor.MAGENTA)
        )),
        // Level 15
        Level(15, 20, 7, 7, listOf(
            Node("s1", NodeType.SOURCE, 0, 0, Direction.RIGHT, NodeColor.CYAN),
            Node("s2", NodeType.SOURCE, 6, 6, Direction.LEFT, NodeColor.MAGENTA),
            Node("r1", NodeType.ROUTER, 3, 0, Direction.DOWN),
            Node("r2", NodeType.ROUTER, 3, 3, Direction.RIGHT, isLocked = true),
            Node("r3", NodeType.ROUTER, 6, 3, Direction.UP),
            Node("r4", NodeType.ROUTER, 3, 6, Direction.UP, isLocked = true),
            Node("t1", NodeType.TARGET, 6, 0, color = NodeColor.CYAN),
            Node("t2", NodeType.TARGET, 3, 1, color = NodeColor.MAGENTA)
        ))
    )

    val allLevels = levels
}
