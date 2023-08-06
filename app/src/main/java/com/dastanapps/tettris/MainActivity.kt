package com.dastanapps.tettris

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.tettris.databinding.ActivityMainBinding
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

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        tetrisGridView = binding.tetrisGridView

        ui()

        // Start a new game or update the grid with the current shape position
        startNewGame()
    }

    // Method to start a new game
    internal fun startNewGame() {
        if (tetrisGridView.isGameOver()) {
            tetrisSpeed.stopAutoMove()
            binding.incGameover.root.visibility = View.VISIBLE
            return
        }

        // Initialize the current shape to a new random shape
        currentShape = randomShape(tetrisGridView.numColumns)

        // Update the grid with the current shape position
        tetrisGridView.updateGrid(currentShape!!)

        // Start the automatic downward movement
        tetrisSpeed.startAutoMove()
    }

    private fun ui() {
        val leftButton = binding.incControl.leftButton
        leftButton.setOnClickListener {
            tetrisOps.moveShapeLeft()
        }

        val rightButton = binding.incControl.rightButton
        rightButton.setOnClickListener {
            tetrisOps.moveShapeRight()
        }

        val downButton = binding.incControl.downButton
        downButton.setOnClickListener {
            tetrisOps.moveShapeDown()
        }

        binding.incGameover.btnRetry.setOnClickListener {
            binding.incGameover.root.visibility = View.GONE
            tetrisGridView.resetGrid()
            startNewGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tetrisSpeed.stopAutoMove()
    }
}



