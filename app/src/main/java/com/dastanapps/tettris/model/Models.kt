package com.dastanapps.tettris.model


data class Block(val x: Int, val y: Int)

enum class TetrisShapeGridState(val state: Int) {
    NONE(0),
    MARK(1),
    LOCK(-1)
}

enum class ShapeDirection(val value: Int) {
    LEFT(0),
    RIGHT(1),
    DOWN(2)
}

data class TetrisShape(
    val blocks: List<Block>,
    var positionX: Int = 0,
    var positionY: Int = 0
) {
    fun moveLeft() {
        positionX -= 1
    }

    fun moveRight() {
        positionX += 1
    }

    fun moveDown() {
        positionY += 1
    }

    companion object {
        fun randomShape(columns: Int): TetrisShape {
            val shapeFunctions = listOf(
                ::createIShape,
                ::createJShape,
                ::createLShape,
                ::createOShape,
                ::createSShape,
                ::createTShape,
                ::createZShape
            )
            val randomShapeIndex = (shapeFunctions.indices).random()
            val randomShape = shapeFunctions[randomShapeIndex].invoke()

            // Calculate the initial positionX value to center the shape
            val centerColumn = (columns - 1) / 2 // Assuming odd number of columns
            val shapeWidth =
                randomShape.blocks.maxOf { it.x } - randomShape.blocks.minOf { it.x } + 1
            randomShape.positionX = centerColumn - shapeWidth / 2

            return randomShape
        }
    }
}

// I Shape
fun createIShape(): TetrisShape {
    val blocks = listOf(
        Block(0, 0),
        Block(0, 1),
        Block(0, 2),
        Block(0, 3)
    )
    return TetrisShape(blocks)
}

// J Shape
fun createJShape(): TetrisShape {
    val blocks = listOf(
        Block(1, 0),
        Block(1, 1),
        Block(1, 2),
        Block(0, 2)
    )
    return TetrisShape(blocks)
}

// L Shape
fun createLShape(): TetrisShape {
    val blocks = listOf(
        Block(0, 0),
        Block(0, 1),
        Block(0, 2),
        Block(1, 2)
    )
    return TetrisShape(blocks)
}

// O Shape
fun createOShape(): TetrisShape {
    val blocks = listOf(
        Block(0, 0),
        Block(0, 1),
        Block(1, 0),
        Block(1, 1)
    )
    return TetrisShape(blocks)
}

// S Shape
fun createSShape(): TetrisShape {
    val blocks = listOf(
        Block(1, 0),
        Block(1, 1),
        Block(0, 1),
        Block(0, 2)
    )
    return TetrisShape(blocks)
}

// T Shape
fun createTShape(): TetrisShape {
    val blocks = listOf(
        Block(0, 0),
        Block(0, 1),
        Block(0, 2),
        Block(1, 1)
    )
    return TetrisShape(blocks)
}

// Z Shape
fun createZShape(): TetrisShape {
    val blocks = listOf(
        Block(0, 0),
        Block(0, 1),
        Block(1, 1),
        Block(1, 2)
    )
    return TetrisShape(blocks)
}
