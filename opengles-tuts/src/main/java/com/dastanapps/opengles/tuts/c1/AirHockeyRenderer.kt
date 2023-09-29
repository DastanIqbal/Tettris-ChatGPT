package com.dastanapps.opengles.tuts.c1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.c1.objects.Mallet
import com.dastanapps.opengles.tuts.c1.objects.Table
import com.dastanapps.opengles.tuts.c1.programs.ColorShaderProgram
import com.dastanapps.opengles.tuts.c1.programs.TextureShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 2:20 AM
 *
 */

class AirHockeyRenderer(
    val context: Context
) : GLSurfaceView.Renderer {

    private var vertexShaderSource = """
        uniform mat4 u_Matrix;
        
        attribute vec4 a_Position;
        attribute vec2 a_TextureCoordinates;
        
        varying vec2 v_TextureCoordinates;
        
        void main()
        {
            v_TextureCoordinates = a_TextureCoordinates;
            gl_Position = u_Matrix * a_Position;
        }

    """.trimIndent()

    private var fragmentShaderSource = """
        precision mediump float; 
        
        uniform sampler2D u_TextureUnit;
        varying vec2 v_TextureCoordinates;
        
        void main()
        {
            gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        }
    """.trimIndent()

    // dd
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix: FloatArray = FloatArray(16)
    private val table by lazy { Table() }
    private val mallet by lazy { Mallet() }
    private val textureProgram by lazy {
        TextureShaderProgram(
            vertexShaderSource,
            fragmentShaderSource
        )
    }
    private val colorProgram by lazy {
        ColorShaderProgram(
            vertexShaderSource,
            fragmentShaderSource
        )
    }
    private var texture = 0

    override fun onSurfaceCreated(glUnused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        table
        mallet
        textureProgram
        colorProgram

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)

        perspectiveM(
            projectionMatrix, 90f, width.toFloat() / height.toFloat(), 1f, 10f
        )

        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, 0f, 0f, -1.8f)
        rotateM(modelMatrix, 0, -40f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        multiplyMM(
            temp, 0, projectionMatrix, 0, modelMatrix, 0
        )
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)

    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)

        // Draw the table.
        textureProgram.useProgram()
        textureProgram.setUniforms(projectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        colorProgram.useProgram()
        colorProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }

}