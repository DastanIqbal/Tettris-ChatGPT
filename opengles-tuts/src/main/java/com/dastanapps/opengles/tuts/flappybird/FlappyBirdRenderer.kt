package com.dastanapps.opengles.tuts.flappybird

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.dastanapps.opengles.tuts.flappybird.objects.Background
import com.dastanapps.opengles.tuts.flappybird.objects.Bird
import com.dastanapps.opengles.tuts.flappybird.programs.ColorShaderProgram
import com.dastanapps.opengles.tuts.flappybird.programs.TextureShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 *
 * Created by Iqbal Ahmed on 01/10/2023 10:32 AM
 *
 */

class FlappyBirdRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private val background by lazy { Background() }
    private val bird by lazy { Bird() }

    private val colorProgram by lazy {
        ColorShaderProgram(context)
    }

    private val textureProgram by lazy {
        TextureShaderProgram(context)
    }

    private var bgTexture = 0
    var bgScroll = 0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        // Initialize Shader, Texture and Objects

        bgTexture = background.loadTexture(context)

//        bird
//        colorProgram
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        // Set the camera
        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the screen
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw a background
        textureProgram.useProgram()
        textureProgram.setUniforms(viewProjectionMatrix, bgTexture)
        textureProgram.scroll(bgScroll)
        if (bgScroll == Float.MAX_VALUE) {
            bgScroll = 0f
        }
        bgScroll += .001f

        background.bindData(textureProgram)
        background.draw()

        // Draw a bird
//        colorProgram.useProgram()
//        colorProgram.setUniforms(viewProjectionMatrix, 1f, 0f, 0f)
//        bird.bindData(colorProgram)
//        bird.draw()
    }
}