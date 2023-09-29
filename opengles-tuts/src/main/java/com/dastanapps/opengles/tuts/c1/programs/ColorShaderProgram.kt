package com.dastanapps.opengles.tuts.c1.programs

import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniformMatrix4fv

class ColorShaderProgram(
    vertexShaderSource: String,
    fragmentShaderSource: String
) : ShaderProgram(vertexShaderSource, fragmentShaderSource) {
    // Uniform locations
    private var uMatrixLocation = 0

    // Attribute locations
    private var aPositionLocation: Int = 0
    private var aColorLocation = 0

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray?) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }


}