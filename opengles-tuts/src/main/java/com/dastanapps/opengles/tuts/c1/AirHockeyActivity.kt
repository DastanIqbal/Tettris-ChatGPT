package com.dastanapps.opengles.tuts.c1

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 12:40 AM
 *
 */

class AirHockeyActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet: Boolean = false

    private val activityManager by lazy {
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }
    private val configurationInfo by lazy {
        activityManager.deviceConfigurationInfo
    }

    val supportsEs2 by lazy {
        configurationInfo.reqGlEsVersion >= 0x20000 || ((Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith(
            "unknown"
        ) || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains(
            "Android SDK built for x86"
        )))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)

        val airHockeyRenderer = AirHockeyRenderer(this)
        rendererSet = if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible
            glSurfaceView.setEGLContextClientVersion(2)
            // Assign our renderer.
            glSurfaceView.setRenderer(airHockeyRenderer)
            true
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG)
                .show()
            return
        }

        glSurfaceView.setOnTouchListener { v, event ->
            if (event != null) {
                // Convert touch coordinates into normalized device
                // coordinates, keeping in mind that Android's Y
                // coordinates are inverted.
                val normalizedX: Float = event.x.toFloat() / v.width.toFloat() * 2f - 1f
                val normalizedY: Float = -(event.y.toFloat() / v.height.toFloat() * 2f - 1f)
                if (event.action === MotionEvent.ACTION_DOWN) {
                    glSurfaceView.queueEvent {
                        airHockeyRenderer.handleTouchPress(
                            normalizedX, normalizedY
                        )
                    }
                } else if (event.action === MotionEvent.ACTION_MOVE) {
                    glSurfaceView.queueEvent {
                        airHockeyRenderer.handleTouchDrag(
                            normalizedX, normalizedY
                        )
                    }
                }
                true
            } else {
                false
            }
        }

        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            glSurfaceView.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            glSurfaceView.onResume()
        }
    }

}

