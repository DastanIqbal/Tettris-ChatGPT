package com.dastanapps.tettris.model

import com.dastanapps.tettris.model.blocks.BlockI
import com.dastanapps.tettris.model.blocks.BlockJ
import com.dastanapps.tettris.model.blocks.BlockL
import com.dastanapps.tettris.model.blocks.BlockO


data class Block(val x: Int, val y: Int)

enum class TetrisShapeGridState(val state: Int) {
    NONE(0),
    MARK(1),
    LOCK(-1)
}

enum class ShapeDirection(val value: Int) {
    LEFT(0),
    RIGHT(1),
    DOWN(2),
    ROTATE(3)
}

enum class TetrominoShape {
    I, J, L, O, S, T, Z
}

abstract class ITetrisShape {
    open val shapeType = TetrominoShape.I
    abstract fun rotate0(): List<Block>
    abstract fun rotate90(): List<Block>
    abstract fun rotate180(): List<Block>
    abstract fun rotate270(): List<Block>
}

data class TetrisShape(
    val shape: ITetrisShape,
    var blocks: List<Block>,
    var positionX: Int = 0,
    var positionY: Int = 0,
    var angle: Int = 0,
) {
    fun moveLeft(offset: Int = 1) {
        positionX -= offset
    }

    fun moveRight(offset: Int = 1) {
        positionX += offset
    }

    fun moveDown() {
        positionY += 1
    }

    fun moveUp() {
        positionY -= 1
    }

    fun rotate() {
        angle += 90
        if (angle == 360) {
            angle = 0
        }
        blocks = when (angle) {
            0 -> shape.rotate0()
            90 -> shape.rotate90()
            180 -> shape.rotate180()
            else -> shape.rotate270()
        }
    }

    companion object {
        fun randomShape(columns: Int): TetrisShape {
            val shapeFunctions = listOf(
                ::createIShape,
                ::createJShape,
                ::createLShape,
                ::createOShape,
//                ::createSShape,
//                ::createTShape,
//                ::createZShape
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

        fun TetrisShape.shapeWidth(): Int {
            val randomShape = this
            return randomShape.blocks.maxOf { it.x } - randomShape.blocks.minOf { it.x } + 1
        }
    }
}

// I Shape
fun createIShape(): TetrisShape {
    val block = BlockI()
    return TetrisShape(block, block.rotate0())
}

// J Shape
fun createJShape(): TetrisShape {
    val block = BlockJ()
    return TetrisShape(block, block.rotate0())
}

// L Shape
fun createLShape(): TetrisShape {
    val block = BlockL()
    return TetrisShape(block, block.rotate0())
}

// O Shape
fun createOShape(): TetrisShape {
    val block = BlockO()
    return TetrisShape(block, block.rotate0())
}

// S Shape
//fun createSShape(): TetrisShape {
//    val blocks = listOf(
//        Block(1, 0),
//        Block(1, 1),
//        Block(0, 1),
//        Block(0, 2)
//    )
//    return TetrisShape(TetrominoShape.S, blocks)
//}
//
//// T Shape
//fun createTShape(): TetrisShape {
//    val blocks = listOf(
//        Block(0, 0),
//        Block(0, 1),
//        Block(0, 2),
//        Block(1, 1)
//    )
//    return TetrisShape(TetrominoShape.T, blocks)
//}
//
//// Z Shape
//fun createZShape(): TetrisShape {
//    val blocks = listOf(
//        Block(0, 0),
//        Block(0, 1),
//        Block(1, 1),
//        Block(1, 2)
//    )
//    return TetrisShape(TetrominoShape.Z, blocks)
//}
