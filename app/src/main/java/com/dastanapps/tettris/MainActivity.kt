package com.dastanapps.tettris

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.tettris.grid.TetrisGridView
import com.dastanapps.tettris.model.ShapeDirection
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShape.Companion.randomShape
import com.dastanapps.tettris.model.TetrisShapeGridState
import com.dastanapps.tettris.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var tetrisGridView: TetrisGridView
    private var currentShape: TetrisShape? = null
    private val tetrisSpeed by lazy {
        TetrisSpeed(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tetrisGridView = findViewById(R.id.tetrisGridView)
        setupButtons()

        // Start a new game or update the grid with the current shape position
        startNewGame()
    }

    // Method to start a new game
    private fun startNewGame() {
        // Initialize the current shape to a new random shape
        currentShape = randomShape(tetrisGridView.numColumns)

        // Update the grid with the current shape position
        tetrisGridView.updateGrid(currentShape!!)

        // Start the automatic downward movement
        tetrisSpeed.startAutoMove()
    }

    private fun setupButtons() {
        val leftButton = findViewById<Button>(R.id.leftButton)
        leftButton.setOnClickListener {
            moveShapeLeft()
        }

        val rightButton = findViewById<Button>(R.id.rightButton)
        rightButton.setOnClickListener {
            moveShapeRight()
        }

        val downButton = findViewById<Button>(R.id.downButton)
        downButton.setOnClickListener {
            moveShapeDown()
        }
    }

    private fun moveShapeLeft() {
        if (canMoveShape(ShapeDirection.LEFT)) {
            currentShape?.moveLeft()
            tetrisGridView.updateGrid(currentShape!!)
        } else {

        }
    }

    private fun moveShapeRight() {
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

    private fun canMoveShape(direction: ShapeDirection): Boolean {
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

    private fun handleShapeCollision() {
        val shape = currentShape ?: return

        tetrisGridView.handleRowClearing()

        // Create a new shape and start the automatic movement again
        startNewGame()
    }

    private fun lockShape() {
        tetrisGridView.updateGrid(currentShape!!, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        tetrisSpeed.stopAutoMove()
    }

    companion object {
        const val SHAPE_SPEED = 2L
    }
}



