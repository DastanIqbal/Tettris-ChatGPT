package com.dastanapps.opengles.tuts.c1

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)

        rendererSet = if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible
            glSurfaceView.setEGLContextClientVersion(2)
            // Assign our renderer.
            glSurfaceView.setRenderer(AirHockeyRenderer(this))
            true
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG)
                .show()
            return
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

