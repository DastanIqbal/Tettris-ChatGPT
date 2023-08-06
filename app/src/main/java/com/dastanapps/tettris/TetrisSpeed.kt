package com.dastanapps.tettris

import android.os.Handler
import android.os.Looper
import com.dastanapps.tettris.util.Log

class TetrisSpeed(
    private val activity: MainActivity
) {

    private val handler = Handler(Looper.getMainLooper())
    private val moveDownRunnable = object : Runnable {
        override fun run() {
            activity.moveShapeDown()
            handler.postDelayed(
                this,
                1000 / MainActivity.SHAPE_SPEED
            )
        }
    }

    internal fun startAutoMove() {
        handler.postDelayed(
            moveDownRunnable,
            1000 / MainActivity.SHAPE_SPEED
        ) // Adjust the interval (in milliseconds) for the automatic movement
    }

    internal fun stopAutoMove() {
        Log.d("stopAutoMove")
        handler.removeCallbacks(moveDownRunnable)

    }
}