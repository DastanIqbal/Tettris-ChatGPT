package com.dastanapps.opengles.tuts.c1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLSurfaceView
import com.dastanapps.opengles.tuts.LoggerConfig
import com.dastanapps.opengles.tuts.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 2:20 AM
 *
 */

class AirHockeyRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private val POSITION_COMPONENT_COUNT = 2
    private val BYTES_PER_FLOAT = 4

//    var tableVerticesWithTriangles = floatArrayOf(
//        // Triangle 1
//        -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
//        // Triangle 2
//        -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
//        // Line 1
//        -0.5f, 0f, 0.5f, 0f,
//        // Mallets
//        0f, -0.25f, 0f, 0.25f
//    )

    var tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B
        // Triangle Fan
        0f,
        0f,
        1f,
        1f,
        1f,
        -0.5f,
        -0.5f,
        0.7f,
        0.7f,
        0.7f,
        0.5f,
        -0.5f,
        0.7f,
        0.7f,
        0.7f,
        0.5f,
        0.5f,
        0.7f,
        0.7f,
        0.7f,
        -0.5f,
        0.5f,
        0.7f,
        0.7f,
        0.7f,
        -0.5f,
        -0.5f,
        0.7f,
        0.7f,
        0.7f,
        // Line 1
        -0.5f,
        0f,
        1f,
        0f,
        0f,
        0.5f,
        0f,
        1f,
        0f,
        0f,
        // Mallets
        0f,
        -0.25f,
        0f,
        0f,
        1f,
        0f,
        0.25f,
        1f,
        0f,
        0f
    )

    private var vertexShaderSource = """
        attribute vec4 a_Position;
        attribute vec4 a_Color;
        
        varying vec4 v_Color;
        
        void main()
        {
            v_Color = a_Color;
            
            gl_Position = a_Position;
            gl_PointSize = 10.0;
        }

    """.trimIndent()

    private val A_POSITION: String = "a_Position"
    private var aPositionLocation = 0

    private var fragmentShaderSource = """
        precision mediump float; 
        
        varying vec4 v_Color;
        
        void main()
        {
            gl_FragColor = v_Color;
        }
    """.trimIndent()

    private val A_COLOR: String = "a_Color"
    private val COLOR_COMPONENT_COUNT: Int = 3
    private val STRIDE: Int = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    private var aColorLocation = 0


    private var program = 0

    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()

    init {
        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(glUnused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program)
        }

        GLES20.glUseProgram(program)

        aColorLocation = glGetAttribLocation(program, A_COLOR)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)


        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData
        )

        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexData.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(
            aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
            false, STRIDE, vertexData
        )

        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Draw the table.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)

        // Draw the center dividing line.
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        // Draw the first mallet blue.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }

}