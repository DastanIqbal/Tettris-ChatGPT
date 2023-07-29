package com.dastanapps.tettris

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.tettris.adapter.GridAdapter
import com.dastanapps.tettris.model.TetrominoShape
import com.dastanapps.tettris.model.shapes

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var startButton: Button

    private lateinit var currentShape: TetrominoShape
    private var currentX = 0
    private var currentY = 0
    private var isGameOver = false
    private var isPaused = false

    private lateinit var gameGrid: Array<IntArray>

    companion object {
        const val GRID_WIDTH = 10
        const val GRID_HEIGHT = 20
    }

    private val gridAdapter: GridAdapter by lazy {
        GridAdapter(this, createEmptyGrid())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        startButton = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            if (!isGameOver) {
                startGame()
                startGravity()
//                startButton.isEnabled = false
            }
        }

        // Set up the GridView and game grid
        gridView.numColumns = GRID_WIDTH
        gridView.adapter = gridAdapter

        // Initialize the game
//        startGame()

        // Start gravity for falling shapes
//        startGravity()
    }

    private fun startGame() {
        gameGrid.forEach { row ->
            row.fill(0)
        }
        spawnShape()
//        isGameOver = false
    }

    private fun gameOver() {
        // Other game over logic
        isGameOver = true
        startButton.isEnabled = true

        // Stop gravity for falling shapes
        stopGravity()
    }

    private fun pauseGame() {
        // Pause the game (add pause logic here)

        // Stop gravity when the game is paused
        if (isPaused) {
            stopGravity()
        } else {
            // Resume the game
            startGravity()
        }

        isPaused = !isPaused
    }


    private fun createEmptyGrid(): Array<IntArray> {
        gameGrid = Array(GRID_HEIGHT) { IntArray(GRID_WIDTH) }
        return gameGrid
    }

    private fun spawnShape() {
        currentShape = shapes.random()
        currentX = (GRID_WIDTH - currentShape.shape[0].size) / 2
        currentY = 0

        if (canMove(0, 0)) {
            drawShapeOnGrid()
        } else {
            // Game over
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
            isGameOver = true
            startButton.isEnabled = true
        }

        // Start gravity for the newly spawned shape
        startGravity()
    }

    private fun moveShape(deltaX: Int, deltaY: Int) {
        if (canMove(deltaX, deltaY)) {
            clearShapeFromGrid()
            currentX += deltaX
            currentY += deltaY
            drawShapeOnGrid()
        } else if (deltaY != 0) {
            // If deltaY is not zero (i.e., moving down) and can't move down further,
            // the shape has reached the bottom and should be locked in place.
            lockShape()
        }
    }

    private fun moveShapeDown(deltaX: Int, deltaY: Int) {
        if (canMoveDown(deltaX, deltaY)) {
            clearShapeFromGrid()
            currentX += deltaX
            currentY += deltaY
            drawShapeOnGrid()
        } else if (deltaY != 0) {
            // If deltaY is not zero (i.e., moving down) and can't move down further,
            // the shape has reached the bottom and should be locked in place.
            lockShape()
        }
    }

    private fun canMoveDown(deltaX: Int, deltaY: Int): Boolean {
        for (row in 0 until currentShape.shape.size) {
            for (col in 0 until currentShape.shape[row].size) {
                if (currentShape.shape[row][col] != 0) {
                    val newRow = currentY + row + deltaY
                    val newCol = currentX + col + deltaX

                    // Check if the new position is within the grid boundaries
                    if (newRow >= GRID_HEIGHT || newCol < 0 || newCol >= GRID_WIDTH) {
                        return false
                    }
                }
            }
        }
        return true
    }


    private fun canMove(deltaX: Int, deltaY: Int): Boolean {
        for (row in 0 until currentShape.shape.size) {
            for (col in 0 until currentShape.shape[row].size) {
                if (currentShape.shape[row][col] != 0) {
                    val newRow = currentY + row + deltaY
                    val newCol = currentX + col + deltaX

                    // Check if the new position is within the grid boundaries
                    if (newRow < 0 || newRow >= GRID_HEIGHT || newCol < 0 || newCol >= GRID_WIDTH) {
                        return false
                    }

                    // Check if the new position collides with a locked shape
                    if (deltaY != 0 && gameGrid[newRow][newCol] != 0) {
                        return false
                    }

                    // Check if the new position collides with the left or right boundaries
                    if (deltaX != 0 && (newCol < 0 || newCol >= GRID_WIDTH)) {
                        return false
                    }
                }
            }
        }
        return true
    }


    private fun clearShapeFromGrid() {
        for (row in 0 until currentShape.shape.size) {
            for (col in 0 until currentShape.shape[row].size) {
                if (currentShape.shape[row][col] != 0) {
                    val newRow = currentY + row
                    val newCol = currentX + col
                    gameGrid[newRow][newCol] = 0
                }
            }
        }

        gridAdapter.notifyDataSetChanged()
    }

    private fun drawShapeOnGrid() {
        for (row in 0 until currentShape.shape.size) {
            for (col in 0 until currentShape.shape[row].size) {
                if (currentShape.shape[row][col] != 0) {
                    val newRow = currentY + row
                    val newCol = currentX + col

                    if (newRow in 0 until GRID_HEIGHT && newCol >= 0 && newCol < GRID_WIDTH) {
                        gameGrid[newRow][newCol] = currentShape.shape[row][col]
                    }
                }
            }
        }

        gridAdapter.notifyDataSetChanged()
    }

    // Function to handle the left button click
    fun onLeftButtonClick(view: View) {
        moveShape(-1, 0) // Move shape left by deltaX -1 and deltaY 0
    }

    // Function to handle the right button click
    fun onRightButtonClick(view: View) {
        moveShape(1, 0) // Move shape right by deltaX 1 and deltaY 0
    }

    // Function to handle the right button click
    fun onDownButtonClick(view: View) {
        moveShapeDown(0, 1) // Move shape right by deltaX 1 and deltaY 0
    }

    private fun lockShape() {
        // Lock the current shape in the game grid
        for (row in 0 until currentShape.shape.size) {
            for (col in 0 until currentShape.shape[row].size) {
                if (currentShape.shape[row][col] != 0) {
                    val newRow = currentY + row
                    val newCol = currentX + col
                    gameGrid[newRow][newCol] = currentShape.shape[row][col]
                }
            }
        }

        // Check for completed lines and clear them
        checkLines()

        // Spawn a new shape
        spawnShape()
    }

    private fun checkLines() {
        val completedRows = mutableListOf<Int>()

        // Check for completed rows
        for (row in 0 until GRID_HEIGHT) {
            var isRowCompleted = true
            for (col in 0 until GRID_WIDTH) {
                if (gameGrid[row][col] == 0) {
                    isRowCompleted = false
                    break
                }
            }

            if (isRowCompleted) {
                completedRows.add(row)
            }
        }

        // Clear completed rows and move rows above down
        if (completedRows.isNotEmpty()) {
            for (row in completedRows) {
                // Clear the completed row
                for (col in 0 until GRID_WIDTH) {
                    gameGrid[row][col] = 0
                }

                // Move all rows above the completed row one step down
                for (i in row downTo 1) {
                    for (col in 0 until GRID_WIDTH) {
                        gameGrid[i][col] = gameGrid[i - 1][col]
                    }
                }
            }

            // Notify the adapter that data has changed
            gridAdapter?.notifyDataSetChanged()
        }
    }


    private val gravityHandler = Handler(Looper.getMainLooper())
    private val gravityRunnable = object : Runnable {
        override fun run() {
            if (isGameOver || isPaused) {
                gravityHandler.removeCallbacks(this)
                return
            }

            moveShapeDown(0, 1) // Move shape down by deltaX 0 and deltaY 1
            gravityHandler.postDelayed(this, 500) // Repeat every 500 milliseconds
        }
    }

    // Start auto-movement (e.g., call this when starting the game)
    private fun startGravity() {
        gravityHandler.postDelayed(gravityRunnable, 500)
    }

    // Stop auto-movement (e.g., call this when the game is over)
    private fun stopGravity() {
        gravityHandler.removeCallbacks(gravityRunnable)
    }


}



