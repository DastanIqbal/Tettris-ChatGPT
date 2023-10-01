package com.dastanapps.opengles.tuts.flappybird.programs

import android.content.Context
import android.opengl.GLES20

/**
 *
 * Created by Iqbal Ahmed on 01/10/2023 12:18 PM
 *
 */

class ColorShaderProgram(
    context: Context
) : ShaderProgram(context) {

    internal var uColorLocation = 0
    internal var uMatrixLocation = 0
    internal var aPositionLocaiton = 0

    init {
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)

        aPositionLocaiton = GLES20.glGetAttribLocation(program, A_POSITION)
    }


    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f)
    }
}