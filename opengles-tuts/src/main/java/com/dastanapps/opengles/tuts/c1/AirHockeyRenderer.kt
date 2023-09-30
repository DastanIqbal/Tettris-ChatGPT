package com.dastanapps.opengles.tuts.c1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLSurfaceView
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.setLookAtM
import android.opengl.Matrix.translateM
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.c1.objects.Mallet
import com.dastanapps.opengles.tuts.c1.objects.Puck
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
        
        void main()
        {
            gl_Position = u_Matrix * a_Position;
        }

    """.trimIndent()

    private var textureVertexShaderSource = """
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
        
        uniform vec4 u_Color;
        
        void main()
        {
          gl_FragColor = u_Color;
        }
    """.trimIndent()

    private var textureFragmentShaderSource = """
        precision mediump float; 
        
        uniform sampler2D u_TextureUnit;
        varying vec2 v_TextureCoordinates;
        
        void main()
        {
          gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        }
    """.trimIndent()

    // dd

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix: FloatArray = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix: FloatArray = FloatArray(16)

    private val puck by lazy { Puck(0.06f, 0.02f, 32) }
    private val mallet by lazy { Mallet(0.08f, 0.15f, 32) }
    private val table by lazy { Table() }

    private val textureProgram by lazy {
        TextureShaderProgram(
            textureVertexShaderSource, textureFragmentShaderSource
        )
    }
    private val colorProgram by lazy {
        ColorShaderProgram(
            vertexShaderSource, fragmentShaderSource
        )
    }
    private var texture = 0

    override fun onSurfaceCreated(glUnused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        table
        puck
        mallet
        textureProgram
        colorProgram

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)

        perspectiveM(
            projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f
        )

        setLookAtM(viewMatrix, 0, 0f, 2f, 3f, 0f, 0f, 0f, 0f, 1f, 0f)

    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw the table.
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        // Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, x, y, z)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

}