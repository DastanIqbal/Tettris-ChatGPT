package com.dastanapps.opengles.tuts

import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_VALIDATE_STATUS
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glCompileShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glCreateShader
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glGetProgramInfoLog
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glGetShaderInfoLog
import android.opengl.GLES20.glGetShaderiv
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glShaderSource
import android.opengl.GLES20.glValidateProgram
import android.util.Log


/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 1:31 AM
 *
 */

object ShaderHelper {
    private const val TAG = "ShaderHelper"
    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GL_VERTEX_SHADER, shaderCode)
    }

    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode)
    }

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programObjectId = glCreateProgram()
        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program")
            }
            return 0
        }

        glAttachShader(programObjectId, vertexShaderId)
        glAttachShader(programObjectId, fragmentShaderId)

        glLinkProgram(programObjectId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)

        if (LoggerConfig.ON) {
            // Print the program info log to the Android log output.
            Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId))
        }

        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            glDeleteProgram(programObjectId)
            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed.")
            }
            return 0
        }

        return programObjectId
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
        val shaderObjectId = glCreateShader(type)
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader.")
            }
            return 0

        }

        glShaderSource(shaderObjectId, shaderCode)
        glCompileShader(shaderObjectId)

        val compileStatus = IntArray(1)
        glGetShaderiv(
            shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0
        )

        if (LoggerConfig.ON) {
            // Print the shader info log to the Android log output.
            Log.v(
                TAG, "Results of compiling source:\n$shaderCode\n:" + glGetShaderInfoLog(
                    shaderObjectId
                )
            )
        }

        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object. glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed.")
            }
            return 0
        }

        return shaderObjectId
    }

    fun validateProgram(programObjectId: Int): Boolean {
        glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)
        Log.v(
            TAG, "Results of validating program: ${validateStatus[0]} Log:${
                glGetProgramInfoLog(programObjectId)
            }"
        )
        return validateStatus[0] != 0
    }


    fun buildProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
        val program: Int
        // Compile the shaders.
        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader)
        if (LoggerConfig.ON) {
            validateProgram(program)
        }
        return program
    }
}