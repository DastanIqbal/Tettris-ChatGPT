package com.dastanapps.tettris.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.dastanapps.tettris.R
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShapeGridState
import com.dastanapps.tettris.util.px

class TetrisGridView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // Define variables for grid size
    internal val numRows = GRID_ROWS
    internal val numColumns = GRID_COLUMNS

    // Define the grid
    internal val grid =
        Array(numRows) { Array(numColumns) { TetrisShapeGridState.NONE } }

    // Define variables for cell size
    private var cellWidth = 0
    private var cellHeight = 0

    private val grayPain by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            color = Color.LTGRAY
        }
    }

    private val unlockedShapePaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = context.getColor(R.color.tetromino_color)
        }
    }

    private val lockedShapePaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = context.getColor(R.color.locked_tetromino_color)
        }
    }

    private val dp8 by lazy { 4.px(context) }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calculate the cell size based on the view size
        cellWidth = w / numColumns
        cellHeight = h / numRows
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the grid lines
        drawGrid(canvas)

        // Draw the occupied cells
        drawShape(canvas)
    }

    private fun drawShape(canvas: Canvas) {
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                val left = j * cellWidth
                val top = i * cellHeight
                val right = left + cellWidth
                val bottom = top + cellHeight

                if (grid[i][j] in arrayOf(TetrisShapeGridState.MARK)) {
                    canvas.drawRect(
                        left.toFloat() + dp8,
                        top.toFloat() + dp8,
                        right.toFloat() - dp8,
                        bottom.toFloat() - dp8,
                        unlockedShapePaint
                    )
                } else if (grid[i][j] == TetrisShapeGridState.LOCK) {
                    canvas.drawRect(
                        left.toFloat() + dp8,
                        top.toFloat() + dp8,
                        right.toFloat() - dp8,
                        bottom.toFloat() - dp8,
                        lockedShapePaint
                    )
                }
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                val left = j * cellWidth
                val top = i * cellHeight
                val right = left + cellWidth
                val bottom = top + cellHeight

                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    grayPain
                )
            }
        }
    }

    // Add a method to update the grid with new Tetris shape positions
    fun updateGrid(shape: TetrisShape, isLock: Boolean = false) {
        // Clear the previous shape from the grid
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                if (grid[i][j] == TetrisShapeGridState.MARK) {
                    grid[i][j] = TetrisShapeGridState.NONE
                }
            }
        }

        if (isLock) {
            // Add the current shape to the grid
            for (block in shape.blocks) {
                val row = shape.positionY + block.y
                val col = shape.positionX + block.x
                grid[row][col] = TetrisShapeGridState.LOCK
            }

        } else {
            // Add the new shape to the grid
            for (block in shape.blocks) {
                val row = shape.positionY + block.y
                val col = shape.positionX + block.x
                grid[row][col] = TetrisShapeGridState.MARK
            }
        }

        // Invalidate the view to trigger a redraw
        invalidate()
    }


    private fun isRowFilled(row: Int): Boolean {
        for (col in 0 until numColumns) {
            if (grid[row][col] != TetrisShapeGridState.LOCK) {
                return false
            }
        }
        return true
    }

    fun handleRowClearing() {
        val rowsToClear = mutableListOf<Int>()

        for (row in 0 until numRows) {
            if (isRowFilled(row)) {
                rowsToClear.add(row)
            }
        }

        if (rowsToClear.isNotEmpty()) {
            clearRows(rowsToClear)
            moveRowsDown(rowsToClear)
        }

        invalidate()
    }

    private fun clearRows(rowsToClear: List<Int>) {
        for (row in rowsToClear) {
            for (col in 0 until numColumns) {
                grid[row][col] = TetrisShapeGridState.NONE
            }
        }
    }

    private fun moveRowsDown(rowsToClear: List<Int>) {
        for (i in rowsToClear) {
            for (row in i downTo 1) {
                for (col in 0 until numColumns) {
                    grid[row][col] = grid[row - 1][col]
                }
            }
        }
    }

    fun isGameOver(): Boolean {
        for (col in 0 until numColumns) {
            if (grid[0][col] == TetrisShapeGridState.LOCK) {
                return true
            }
        }
        return false
    }

    fun resetGrid() {
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                grid[i][j] = TetrisShapeGridState.NONE
            }
        }
    }

    companion object {
        val GRID_ROWS = 20
        val GRID_COLUMNS = 11
    }
}
