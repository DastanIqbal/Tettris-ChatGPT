package com.dastanapps.opengles.tuts.c1

import android.content.Context
import android.opengl.GLES20
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

    var tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
        // Triangle 2
        -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
        // Line 1
        -0.5f, 0f, 0.5f, 0f,
        // Mallets
        0f, -0.25f, 0f, 0.25f
    )

    private var vertexShaderSource = """
        attribute vec4 a_Position;     		

        void main()                    
        {                              
            gl_Position = a_Position;
            gl_PointSize = 10.0;
        }   

    """.trimIndent()

    private val A_POSITION: String = "a_Position"
    private var aPositionLocation = 0

    private var fragmentShaderSource = """
        precision mediump float; 
      	 								
        uniform vec4 u_Color;          	   								
          
        void main()                    		
        {                              	
            gl_FragColor = u_Color;                                  		
        }
    """.trimIndent()

    private val U_COLOR: String = "u_Color"
    private var uColorLocation = 0

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

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)


        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Draw the table.
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        // Draw the center dividing line.
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        // Draw the first mallet blue.
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        // Draw the second mallet red.
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }

}