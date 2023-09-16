package com.dastanapps.tettris

import com.dastanapps.tettris.grid.TetrisGridView.Companion.GRID_COLUMNS
import com.dastanapps.tettris.model.ShapeDirection
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShapeGridState
import com.dastanapps.tettris.model.TetrominoShape
import com.dastanapps.tettris.util.Log

/**
 *
 * Created by Iqbal Ahmed on 06/08/2023 8:42 PM
 *
 */

class TetrisOps(
    private val mainActivity: TettrisMainActivity
) {

    private val currentShape get() = mainActivity.currentShape
    private val tetrisGridView get() = mainActivity.tetrisGridView
    private val tetrisSpeed get() = mainActivity.tetrisSpeed

    internal fun moveShapeLeft() {
        if (canMoveShape(ShapeDirection.LEFT)) {
            currentShape?.moveLeft()
            tetrisGridView.updateGrid(currentShape!!)
        } else {

        }
    }

    internal fun moveShapeRight() {
        if (canMoveShape(ShapeDirection.RIGHT)) {
            currentShape?.moveRight()
            tetrisGridView.updateGrid(currentShape!!)
        } else {

        }
    }

    internal fun moveShapeDown() {
        // Check if the shape can move down
        if (canMoveShape()) {
            currentShape?.moveDown()
            tetrisGridView.updateGrid(currentShape!!)
        } else {
            // If the shape cannot move down, stop the automatic movement and handle row clearing, etc.
            tetrisSpeed.stopAutoMove()
            // Lock the current shape to the grid
            lockShape()
            // Handle row clearing, shape locking, and then either start a new game or create a new Tetris shape
            handleShapeCollision()
        }
    }

    fun rotateShape(rowOffset: Int = 0, colOffset: Int = 0) {
        val shape = currentShape ?: return
        val rotatedShape = getRotatedShape(shape.copy())

        // Check if the shape can rotate without colliding with other shapes
        if (canMoveShape(rotatedShape, rowOffset, colOffset)) {
            mainActivity.currentShape = rotatedShape
            tetrisGridView.updateGrid(currentShape!!)
        }
    }

    private fun getRotatedShape(shape: TetrisShape): TetrisShape {
        shape.rotate()
        val blocks = shape.blocks
        val maxX = blocks.maxOf { it.x }
        val maxY = blocks.maxOf { it.y }
        val movedPosition = listOf(maxX, maxY).maxOf { it } + 1
        if (shape.positionX >= tetrisGridView.numColumns - movedPosition) {
            when (shape.angle) {
                0, 180 -> {
                    if (shape.shape.shapeType == TetrominoShape.I) {
                        if (shape.positionX == GRID_COLUMNS - 1) {
                            shape.moveLeft(maxX)
                        } else {
                            shape.moveLeft(movedPosition / 2)
                        }
                    } else {
                        shape.moveLeft(maxX - 1)
                    }
                }

                else -> {
                    if (shape.shape.shapeType == TetrominoShape.I) {
                        shape.moveRight(movedPosition / 2)
                    } else {
                        shape.moveRight(maxX)
                    }
                }
            }
        } else {
            when (shape.angle) {
                0, 180 -> {
                    if (shape.shape.shapeType == TetrominoShape.I) {
                        if (shape.positionX <= 1) {
                            shape.positionX = 0
                        } else {
                            shape.moveLeft(movedPosition / 2)
                        }
                    }
                }

                else -> {
                    if (shape.shape.shapeType == TetrominoShape.I) {
                        if (shape.positionX == 0) {
                            shape.moveRight((movedPosition / 2) - 1)
                        } else {
                            shape.moveRight(movedPosition / 2)
                        }
                    }
                }
            }
        }

        return TetrisShape(shape.shape, shape.blocks, shape.positionX, shape.positionY, shape.angle)
    }

    // Helper function to check if the shape can move to the specified row and column
    private fun canMoveShape(shape: TetrisShape, rowOffset: Int, colOffset: Int): Boolean {
        for (block in shape.blocks) {
            val row = shape.positionY + rowOffset + block.y
            val col = shape.positionX + colOffset + block.x

            if (row >= tetrisGridView.numRows) {
                return false
            }
//
//            if (col < 0) {
//                val movedPosition = shape.shapeWidth() / 2
//                shape.moveRight(movedPosition)
//                return true
//            }

            // Check if the shape collides with other shapes or goes out of the grid's boundaries
            if (row < 0 || row >= tetrisGridView.numRows ||
                col < 0 || col >= tetrisGridView.numColumns ||
                tetrisGridView.grid[row][col] == TetrisShapeGridState.LOCK
            ) {
                return false
            }
        }
        return true
    }


    internal fun canMoveShape(direction: ShapeDirection = ShapeDirection.DOWN): Boolean {
        Log.d("canMoveShape")
        val shape = currentShape ?: return false

        for (block in shape.blocks) {
            val row = shape.positionY + block.y + if (direction == ShapeDirection.DOWN) 1 else 0
            val col =
                shape.positionX + block.x + if (direction == ShapeDirection.LEFT) -1 else if (direction == ShapeDirection.RIGHT) 1 else 0

            Log.d("Row $row")

            // Check Left/Right boundary
            if (col >= tetrisGridView.numColumns || col < 0) {
                return false
            }

            // Check Bottom boundary
            if (row >= tetrisGridView.numRows) {
                return false
            }

            // Check if Lock
            if (tetrisGridView.grid[row][col].state == TetrisShapeGridState.LOCK.state) {
                return false
            }
        }

        return true
    }

    internal fun handleShapeCollision() {
        val shape = currentShape ?: return

        tetrisGridView.handleRowClearing()

        // Create a new shape and start the automatic movement again
        mainActivity.startNewGame()
    }

    internal fun lockShape() {
        tetrisGridView.updateGrid(currentShape!!, true)
    }
}