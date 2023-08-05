package com.dastanapps.tettris

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.tettris.grid.TetrisGridView
import com.dastanapps.tettris.model.ShapeDirection
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShapeGridState
import com.dastanapps.tettris.model.createIShape
import com.dastanapps.tettris.model.createJShape
import com.dastanapps.tettris.model.createLShape
import com.dastanapps.tettris.model.createOShape
import com.dastanapps.tettris.model.createSShape
import com.dastanapps.tettris.model.createTShape
import com.dastanapps.tettris.model.createZShape
import com.dastanapps.tettris.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var tetrisGridView: TetrisGridView
    private var currentShape: TetrisShape? = null

    private val handler = Handler(Looper.getMainLooper())
    private val moveDownRunnable = object : Runnable {
        override fun run() {
            moveShapeDown()
            handler.postDelayed(
                this,
                1000 / SHAPE_SPEED
            ) // Adjust the interval (in milliseconds) for the automatic movement
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tetrisGridView = findViewById(R.id.tetrisGridView)
        setupButtons()

        // Start a new game or update the grid with the current shape position
        startNewGame()

        // Start the automatic downward movement
        startAutoMove()
    }

    // Method to start a new game
    private fun startNewGame() {
        // Initialize the current shape to a new random shape
        currentShape = getRandomShape()

        // Update the grid with the current shape position
        tetrisGridView.updateGrid(currentShape!!)
    }

    // Method to get a random Tetris shape

    private fun getRandomShape(): TetrisShape {
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
        val centerColumn = (tetrisGridView.numColumns - 1) / 2 // Assuming odd number of columns
        val shapeWidth = randomShape.blocks.maxOf { it.x } - randomShape.blocks.minOf { it.x } + 1
        randomShape.positionX = centerColumn - shapeWidth / 2

        return randomShape
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

    private fun moveShapeDown() {
        // Check if the shape can move down
        if (canMoveShape(ShapeDirection.DOWN)) {
            currentShape?.moveDown()
            tetrisGridView.updateGrid(currentShape!!)
        } else {
            // If the shape cannot move down, stop the automatic movement and handle row clearing, etc.
            stopAutoMove()
            // Lock the current shape to the grid
            lockShape()
            // Handle row clearing, shape locking, and then either start a new game or create a new Tetris shape
            handleShapeCollision()
        }
    }

    private fun startAutoMove() {
        handler.postDelayed(
            moveDownRunnable,
            1000 / SHAPE_SPEED
        ) // Adjust the interval (in milliseconds) for the automatic movement
    }

    private fun stopAutoMove() {
        handler.removeCallbacks(moveDownRunnable)

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
        stopAutoMove()
    }

    companion object {
        const val SHAPE_SPEED = 2L
    }
}



