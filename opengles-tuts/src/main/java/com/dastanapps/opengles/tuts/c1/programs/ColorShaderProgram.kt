package com.dastanapps.opengles.tuts.c1.programs

import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUniformMatrix4fv

class ColorShaderProgram(
    vertexShaderSource: String, fragmentShaderSource: String
) : ShaderProgram(vertexShaderSource, fragmentShaderSource) {
    // Uniform locations
    private var uMatrixLocation = 0
    private var uColorLocation = 0

    // Attribute locations
    private var aPositionLocation: Int = 0
//    private var aColorLocation = 0

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uColorLocation = glGetUniformLocation(program, U_COLOR)

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
//        aColorLocation = glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

//    fun getColorAttributeLocation(): Int {
//        return aColorLocation
//    }


}