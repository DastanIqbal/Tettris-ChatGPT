package com.dastanapps.tettris.model

import android.graphics.Color

data class TetrominoShape(val shape: Array<IntArray>, val color: Int)

private val shapeColors = listOf(
    Color.CYAN,
    Color.BLUE,
    Color.YELLOW,
    Color.GREEN,
    Color.RED,
    Color.MAGENTA,
    Color.MAGENTA
)

val shapes = arrayOf(
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1, 1, 1)
        ),
        Color.CYAN
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1, 1),
            intArrayOf(0, 0, 1)
        ),
        Color.BLUE
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1, 1),
            intArrayOf(1, 0, 0)
        ),
        Color.YELLOW
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1, 1),
            intArrayOf(0, 1, 0)
        ),
        Color.GREEN
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1),
            intArrayOf(1, 1)
        ),
        Color.RED
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 1)
        ),
        Color.MAGENTA
    ),
    TetrominoShape(
        arrayOf(
            intArrayOf(0, 1, 1),
            intArrayOf(1, 1, 0)
        ),
        Color.DKGRAY
    )
)

val shapes1 = arrayOf(
    TetrominoShape(
        TetrominoShapes.I,
        shapeColors[0]
    ),
    TetrominoShape(
        TetrominoShapes.J,
        shapeColors[1]
    ),
    TetrominoShape(
        TetrominoShapes.L,
        shapeColors[2]
    ),
    TetrominoShape(
        TetrominoShapes.O,
        shapeColors[3]
    ),
    TetrominoShape(
        TetrominoShapes.S,
        shapeColors[4]
    ),
    TetrominoShape(
        TetrominoShapes.T,
        shapeColors[5]
    ),
    TetrominoShape(
        TetrominoShapes.Z,
        shapeColors[6]
    ),
)


