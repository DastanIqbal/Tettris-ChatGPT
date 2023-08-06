package com.dastanapps.tettris

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.tettris.grid.TetrisGridView
import com.dastanapps.tettris.model.TetrisShape
import com.dastanapps.tettris.model.TetrisShape.Companion.randomShape

class MainActivity : AppCompatActivity() {

    internal lateinit var tetrisGridView: TetrisGridView
    internal var currentShape: TetrisShape? = null

    internal val tetrisOps by lazy {
        TetrisOps(this)
    }

    internal val tetrisSpeed by lazy {
        TetrisSpeed(tetrisOps)
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
    internal fun startNewGame() {
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
            tetrisOps.moveShapeLeft()
        }

        val rightButton = findViewById<Button>(R.id.rightButton)
        rightButton.setOnClickListener {
            tetrisOps.moveShapeRight()
        }

        val downButton = findViewById<Button>(R.id.downButton)
        downButton.setOnClickListener {
            tetrisOps.moveShapeDown()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tetrisSpeed.stopAutoMove()
    }
}



