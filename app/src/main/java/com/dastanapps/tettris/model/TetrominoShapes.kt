package com.dastanapps.tettris.model

object TetrominoShapes {
    // I shape
    val I = arrayOf(
        intArrayOf(1, 1, 1, 1),
    )

    // J shape
    val J = arrayOf(
        intArrayOf(1, 0, 0),
        intArrayOf(1, 1, 1),
    )

    // L shape
    val L = arrayOf(
        intArrayOf(0, 0, 1),
        intArrayOf(1, 1, 1),
    )

    // O shape
    val O = arrayOf(
        intArrayOf(1, 1),
        intArrayOf(1, 1),
    )

    // S shape
    val S = arrayOf(
        intArrayOf(0, 1, 1),
        intArrayOf(1, 1, 0),
    )

    // T shape
    val T = arrayOf(
        intArrayOf(0, 1, 0),
        intArrayOf(1, 1, 1),
    )

    // Z shape
    val Z = arrayOf(
        intArrayOf(1, 1, 0),
        intArrayOf(0, 1, 1),
    )
}
