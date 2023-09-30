/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.dastanapps.opengles.tuts.particles.programs

import android.content.Context
import android.opengl.GLES20
import com.dastanapps.opengles.tuts.R

class ParticleShaderProgram(context: Context) : ShaderProgram(
    context,
    R.raw.particle_vertex_shader,
    R.raw.particle_fragment_shader
) {
    // Uniform locations
    private val uMatrixLocation: Int
    private val uTimeLocation: Int

    // Attribute locations
    val positionAttributeLocation: Int
    val colorAttributeLocation: Int
    val directionVectorAttributeLocation: Int
    val particleStartTimeAttributeLocation: Int
    private val uTextureUnitLocation: Int

    init {

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = GLES20.glGetUniformLocation(program, U_TIME)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        positionAttributeLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        colorAttributeLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        directionVectorAttributeLocation = GLES20.glGetAttribLocation(program, A_DIRECTION_VECTOR)
        particleStartTimeAttributeLocation =
            GLES20.glGetAttribLocation(program, A_PARTICLE_START_TIME)
    }

    /*
    public void setUniforms(float[] matrix, float elapsedTime) {
     */
    fun setUniforms(matrix: FloatArray?, elapsedTime: Float, textureId: Int) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform1f(uTimeLocation, elapsedTime)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }
}
