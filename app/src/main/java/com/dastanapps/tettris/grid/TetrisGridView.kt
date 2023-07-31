package com.dastanapps.tettris.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShapeGridState

class TetrisGridView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // Define variables for grid size
    internal val numRows = 20
    internal val numColumns = 11

    // Define variables for cell size
    private var cellWidth = 0
    private var cellHeight = 0

    // Define the grid
    internal val grid =
        Array(numRows) { Array(numColumns) { TetrisShapeGridState.NONE } }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calculate the cell size based on the view size
        cellWidth = w / numColumns
        cellHeight = h / numRows
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the grid lines
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                val left = j * cellWidth
                val top = i * cellHeight
                val right = left + cellWidth
                val bottom = top + cellHeight

                val paint = Paint()
                paint.style = Paint.Style.STROKE
                paint.color = Color.LTGRAY
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    paint
                )
            }
        }

        // Draw the occupied cells
        for (i in 0 until numRows) {
            for (j in 0 until numColumns) {
                if (grid[i][j] in arrayOf(TetrisShapeGridState.LOCK, TetrisShapeGridState.MARK)) {
                    val left = j * cellWidth
                    val top = i * cellHeight
                    val right = left + cellWidth
                    val bottom = top + cellHeight

                    val paint = Paint()
                    paint.style = Paint.Style.FILL
                    paint.color = Color.BLUE
                    canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                }
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
}
