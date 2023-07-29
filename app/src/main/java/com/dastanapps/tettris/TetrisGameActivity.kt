package com.dastanapps.tettris

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class TetrisGameActivity : AppCompatActivity() {

    private lateinit var gameBoard: GridLayout
    private lateinit var tvScore: TextView
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var btnRotate: Button
    private lateinit var btnDrop: Button

    // Variables to represent the game board
    private val boardWidth = 10
    private val boardHeight = 20
    private var gameBoardCells: Array<Array<View?>> =
        Array(boardHeight) { arrayOfNulls<View>(boardWidth) }

    // Variables to represent the tetrominoes
    private val tetrominoes = arrayOf(
        // I Tetromino
        arrayOf(
            intArrayOf(1, 1, 1, 1)
        ),

        // J Tetromino
        arrayOf(
            intArrayOf(1, 0, 0),
            intArrayOf(1, 1, 1)
        ),

        // L Tetromino
        arrayOf(
            intArrayOf(0, 0, 1),
            intArrayOf(1, 1, 1)
        ),

        // O Tetromino
        arrayOf(
            intArrayOf(1, 1),
            intArrayOf(1, 1)
        ),

        // S Tetromino
        arrayOf(
            intArrayOf(0, 1, 1),
            intArrayOf(1, 1, 0)
        ),

        // T Tetromino
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 1)
        ),

        // Z Tetromino
        arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 1)
        )
    )


    // Variables to track the current tetromino
    private var currentTetromino: Array<IntArray>? = null
    private var currentTetrominoRow = 0
    private var currentTetrominoCol = 0

    // Other game variables
    private var score = 0
    private var isGameOver = false

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val gravityInterval = 1000L // The time interval in milliseconds for the tetromino to move down one row


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tetris)

        gameBoard = findViewById(R.id.gameBoard)
        tvScore = findViewById(R.id.tvScore)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        btnRotate = findViewById(R.id.btnRotate)
        btnDrop = findViewById(R.id.btnDrop)

        btnLeft.setOnClickListener { moveTetrominoLeft() }
        btnRight.setOnClickListener { moveTetrominoRight() }
        btnRotate.setOnClickListener { rotateTetromino() }
        btnDrop.setOnClickListener { dropTetromino() }

        createGameBoard()
        startGame()
    }

    private fun createGameBoard() {
        for (row in 0 until boardHeight) {
            for (col in 0 until boardWidth) {
                val cell = View(this)
                val layoutParams = GridLayout.LayoutParams()
                layoutParams.rowSpec = GridLayout.spec(row)
                layoutParams.columnSpec = GridLayout.spec(col)
                cell.layoutParams = layoutParams
                cell.setBackgroundResource(R.drawable.shape_empty_cell) // Set the initial empty cell background
                gameBoard.addView(cell)
                gameBoardCells[row][col] = cell
            }
        }
    }

    private fun initializeGameBoard() {
        // Clear the game board cells
        for (row in 0 until boardHeight) {
            for (col in 0 until boardWidth) {
                gameBoardCells[row][col]?.setBackgroundResource(R.drawable.shape_empty_cell)
                gameBoardCells[row][col]?.tag = null
            }
        }
    }

    private fun startGame() {
        // Initialize the game board and spawn the initial tetromino
//        initializeGameBoard()
        spawnTetromino()

        // Create and schedule the game loop
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // Move the tetromino down
                    moveTetrominoDown()
                }
            }
        }
        timer?.scheduleAtFixedRate(timerTask, gravityInterval, gravityInterval)
    }

    private fun stopGame() {
        // Cancel the game loop
        timer?.cancel()
        timerTask?.cancel()
    }


    private fun spawnTetromino() {
        // Randomly select a tetromino shape
        val randomTetrominoIndex = (0 until tetrominoes.size).random()
        currentTetromino = tetrominoes[randomTetrominoIndex]

        // Set the position of the tetromino at the top-middle of the game board
        currentTetrominoRow = 0
        currentTetrominoCol = (boardWidth - currentTetromino!![0].size) / 2

        // Draw the tetromino on the game board
        drawTetromino()
    }


    private fun drawTetromino() {
        // Clear the previous position of the tetromino
        for (row in 0 until boardHeight) {
            for (col in 0 until boardWidth) {
                if (gameBoardCells[row][col]?.tag == "tetromino") {
                    gameBoardCells[row][col]?.setBackgroundResource(R.drawable.shape_empty_cell)
                }
            }
        }

        // Draw the current position of the tetromino
        if (currentTetromino != null) {
            for (row in currentTetromino!!.indices) {
                for (col in currentTetromino!![0].indices) {
                    if (currentTetromino!![row][col] == 1) {
                        val cell =
                            gameBoardCells[currentTetrominoRow + row][currentTetrominoCol + col]
                        cell?.setBackgroundResource(R.drawable.shape_tetromino) // Set the tetromino cell background
                        cell?.tag = "tetromino" // Set a tag to identify tetromino cells
                    }
                }
            }
        }
    }

    private fun moveTetrominoLeft() {
        if (canMoveLeft()) {
            // Clear the tetromino from its current position
            clearTetromino()
            // Move the tetromino left
            currentTetrominoCol--
            // Draw the tetromino in its new position
            drawTetromino()
        }
    }


    private fun moveTetrominoRight() {
        if (canMoveRight()) {
            // Clear the tetromino from its current position
            clearTetromino()
            // Move the tetromino right
            currentTetrominoCol++
            // Draw the tetromino in its new position
            drawTetromino()
        }
    }


    private fun moveTetrominoDown() {
        if (canMoveDown()) {
            // Clear the tetromino from its current position
            clearTetromino()
            // Move the tetromino down
            currentTetrominoRow++
            // Draw the tetromino in its new position
            drawTetromino()
        } else {
            // The tetromino can't move down anymore, so we lock it in place and check for completed lines
            lockTetromino()
            checkCompletedLines()
            // Spawn a new tetromino
            spawnTetromino()
            // Check if the game is over
            checkGameOver()
        }
    }


    private fun rotateTetromino() {
        val rotatedTetromino = createRotatedTetromino()

        // Check if the rotated tetromino can fit within the game board and doesn't collide with other tetrominoes
        if (rotatedTetromino != null && canMoveTo(
                rotatedTetromino,
                currentTetrominoRow,
                currentTetrominoCol
            )
        ) {
            // Clear the current tetromino from its current position
            clearTetromino()
            // Update the current tetromino with the rotated tetromino
            currentTetromino = rotatedTetromino
            // Draw the tetromino in its new rotated position
            drawTetromino()
        }
    }

    private fun canMoveDown2(): Boolean {
        if (currentTetromino == null) return false
        for (row in currentTetromino!!.indices) {
            for (col in currentTetromino!![0].indices) {
                if (currentTetromino!![row][col] == 1) {
                    // Check if the cell is at the bottom edge or there is a block below it
                    if (currentTetrominoRow + row == boardHeight - 1 || gameBoardCells[currentTetrominoRow + row + 1][currentTetrominoCol + col]?.tag == "locked_tetromino") {
                        return false
                    }
                }
            }
        }
        return true
    }


    private fun dropTetromino() {
        while (canMoveDown2()) {
            // Move the tetromino down
            currentTetrominoRow++
        }

        // Lock the tetromino in place
        lockTetromino()

        // Check for completed lines
        checkCompletedLines()

        // Spawn a new tetromino
        spawnTetromino()

        // Check if the game is over
        checkGameOver()
    }


    // Implement other necessary game logic functions here

    private fun gameOver() {
        isGameOver = true
        // Show game over message and handle game restart
    }

    private fun updateScore() {
        // Update the score on the UI
        tvScore.text = "Score: $score"
    }

    private fun canMoveLeft(): Boolean {
        if (currentTetromino == null) return false
        for (row in currentTetromino!!.indices) {
            for (col in currentTetromino!![0].indices) {
                if (currentTetromino!![row][col] == 1) {
                    // Check if the cell is at the left edge or there is a block to its left
                    if (currentTetrominoCol + col == 0 || gameBoardCells[currentTetrominoRow + row][currentTetrominoCol + col - 1]?.tag == "tetromino") {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun clearTetromino() {
        for (row in currentTetromino!!.indices) {
            for (col in currentTetromino!![0].indices) {
                if (currentTetromino!![row][col] == 1) {
                    gameBoardCells[currentTetrominoRow + row][currentTetrominoCol + col]?.setBackgroundResource(
                        R.drawable.shape_empty_cell
                    )
                }
            }
        }
    }

    private fun canMoveRight(): Boolean {
        if (currentTetromino == null) return false
        for (row in currentTetromino!!.indices) {
            for (col in currentTetromino!![0].indices) {
                if (currentTetromino!![row][col] == 1) {
                    // Check if the cell is at the right edge or there is a block to its right
                    if (currentTetrominoCol + col == boardWidth - 1 || gameBoardCells[currentTetrominoRow + row][currentTetrominoCol + col + 1]?.tag == "tetromino") {
                        return false
                    }
                }
            }
        }
        return true
    }


    private fun canMoveDown(): Boolean {
        if (currentTetromino == null) return false
        for (row in currentTetromino!!.indices) {
            for (col in currentTetromino!![0].indices) {
                if (currentTetromino!![row][col] == 1) {
                    // Check if the cell is at the bottom edge or there is a block below it
                    if (currentTetrominoRow + row == boardHeight - 1 || gameBoardCells[currentTetrominoRow + row + 1][currentTetrominoCol + col]?.tag == "tetromino") {
                        return false
                    }

//                    if (currentTetrominoRow + row == boardHeight - 1 || gameBoardCells[currentTetrominoRow + row + 1][currentTetrominoCol + col]?.tag == "locked_tetromino") {
//                        return false
//                    }
                }
            }
        }
        return true
    }


    private fun lockTetromino() {
        if (currentTetromino != null) {
            // Lock the current tetromino on the game board
            for (row in currentTetromino!!.indices) {
                for (col in currentTetromino!![0].indices) {
                    if (currentTetromino!![row][col] == 1) {
                        val cell =
                            gameBoardCells[currentTetrominoRow + row][currentTetrominoCol + col]
                        cell?.setBackgroundResource(R.drawable.shape_locked_tetromino)
                        cell?.tag =
                            "locked_tetromino" // Set a tag to identify locked tetromino cells
                    }
                }
            }
        }
    }

    private fun checkCompletedLines() {
        var completedLines = 0
        for (row in boardHeight - 1 downTo 0) {
            var lineCompleted = true
            for (col in 0 until boardWidth) {
                if (gameBoardCells[row][col]?.tag != "locked_tetromino") {
                    lineCompleted = false
                    break
                }
            }
            if (lineCompleted) {
                completedLines++
                // Remove the completed line
                for (col in 0 until boardWidth) {
                    gameBoardCells[row][col]?.setBackgroundResource(R.drawable.shape_empty_cell)
                    gameBoardCells[row][col]?.tag = null
                }
                // Shift all lines above the completed line downwards
                for (i in row downTo 1) {
                    for (col in 0 until boardWidth) {
                        gameBoardCells[i][col]?.background = gameBoardCells[i - 1][col]?.background
                        gameBoardCells[i][col]?.tag = gameBoardCells[i - 1][col]?.tag
                    }
                }
            }
        }
        if (completedLines > 0) {
            // Update the score based on the number of completed lines
            score += 100 * completedLines
            updateScore()
        }
    }


    private fun checkGameOver() {
        // Check if the new tetromino can be placed at the top of the game board
        if (canPlaceNewTetromino()) {
            // If the game is not over, spawn a new tetromino and continue the game
            spawnTetromino()
        } else {
            // If the game is over, trigger game over actions
            gameOver()
        }
    }

    private fun canPlaceNewTetromino(): Boolean {
        // Check if the top row of the game board is empty, allowing the new tetromino to be placed
        for (col in 0 until boardWidth) {
            if (gameBoardCells[0][col]?.tag == "locked_tetromino") {
                return false
            }
        }
        return true
    }

    private fun createRotatedTetromino(): Array<IntArray>? {
        if (currentTetromino == null) return null

        val rows = currentTetromino!!.size
        val cols = currentTetromino!![0].size
        val rotatedTetromino = Array(cols) { IntArray(rows) }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                rotatedTetromino[col][rows - 1 - row] = currentTetromino!![row][col]
            }
        }

        return rotatedTetromino
    }

    private fun canMoveTo(tetromino: Array<IntArray>, row: Int, col: Int): Boolean {
        // Check if the tetromino can be placed at the specified row and column position on the game board
        for (r in tetromino.indices) {
            for (c in tetromino[0].indices) {
                if (tetromino[r][c] == 1) {
                    // Check if the cell is outside the game board or there is a block in the way
                    if (row + r < 0 || row + r >= boardHeight || col + c < 0 || col + c >= boardWidth || gameBoardCells[row + r][col + c]?.tag == "locked_tetromino") {
                        return false
                    }
                }
            }
        }
        return true
    }


}
