package com.dastanapps.tettris

import com.dastanapps.tettris.model.ShapeDirection
import com.dastanapps.tettris.model.TetrisShapeGridState
import com.dastanapps.tettris.util.Log

/**
 *
 * Created by Iqbal Ahmed on 06/08/2023 8:42 PM
 *
 */

class TetrisOps(
    private val mainActivity: MainActivity
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
        if (canMoveShape(ShapeDirection.DOWN)) {
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

    internal fun canMoveShape(direction: ShapeDirection): Boolean {
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