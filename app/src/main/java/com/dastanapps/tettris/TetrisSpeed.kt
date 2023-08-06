package com.dastanapps.tettris

import android.os.Handler
import android.os.Looper
import com.dastanapps.tettris.util.Log

/**
 *
 * Created by Iqbal Ahmed on 06/08/2023 8:43 PM
 *
 */

class TetrisSpeed(
    private val ops: TetrisOps
) {

    private val handler = Handler(Looper.getMainLooper())
    private val moveDownRunnable = object : Runnable {
        override fun run() {
            ops.moveShapeDown()
            handler.postDelayed(
                this,
                1000 / SHAPE_SPEED
            )
        }
    }

    internal fun startAutoMove() {
        handler.postDelayed(
            moveDownRunnable,
            1000 / SHAPE_SPEED
        )
    }

    internal fun stopAutoMove() {
        Log.d("stopAutoMove")
        handler.removeCallbacks(moveDownRunnable)

    }

    companion object {
        const val SHAPE_SPEED = 2L
    }
}