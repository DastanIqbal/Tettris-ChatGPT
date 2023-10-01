package com.dastanapps.opengles.tuts.flappybird.programs

import android.content.Context
import android.opengl.GLES20
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.particles.util.ShaderHelper
import com.dastanapps.opengles.tuts.particles.util.TextResourceReader

/**
 *
 * Created by Iqbal Ahmed on 01/10/2023 12:18 PM
 *
 */

class TextureShaderProgram(
    context: Context
) : ShaderProgram(context) {

    protected val A_TEXTURECOORDINATES = "a_TextureCoordinates"

    protected val U_TEXTUREUNIT = "u_TextureUnit"

    // Uniform locations
    internal var uMatrixLocation = 0
    internal var uTextureUnitLocation = 0

    // Attribute locations
    internal var aPositionLocaiton = 0
    internal var aTextureCoordinatesLocation = 0

    init {
        program = ShaderHelper.buildProgram(
            vertexShaderSource = TextResourceReader.readTextFileFromResource(
                context, R.raw.flappybird_texture_vertex_shader
            ), fragmentShaderSource = TextResourceReader.readTextFileFromResource(
                context, R.raw.flappybird_texture_fragment_shader
            )
        )

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTUREUNIT)

        aPositionLocaiton = GLES20.glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURECOORDINATES)
    }


    fun setUniforms(matrix: FloatArray, textureId: Int) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0.
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    fun scroll(scroll: Float) {
        val fsScroll = GLES20.glGetUniformLocation(program, "scroll")
        GLES20.glUniform1f(fsScroll, scroll)

    }
}