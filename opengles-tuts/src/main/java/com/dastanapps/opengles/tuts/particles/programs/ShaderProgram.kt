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
import com.dastanapps.opengles.tuts.particles.util.ShaderHelper
import com.dastanapps.opengles.tuts.particles.util.TextResourceReader

abstract class ShaderProgram protected constructor(
    context: Context,
    vertexShaderResourceId: Int,
    fragmentShaderResourceId: Int
) {
    // Shader program
    protected val program: Int

    init {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        )
    }

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }

    companion object {
        // Uniform constants
        const val U_MATRIX = "u_Matrix"
        const val U_COLOR = "u_Color"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val U_TIME = "u_Time"

        // Attribute constants
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val A_PARTICLE_START_TIME = "a_ParticleStartTime"
    }
}
