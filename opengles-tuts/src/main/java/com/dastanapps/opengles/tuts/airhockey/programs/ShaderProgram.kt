package com.dastanapps.opengles.tuts.airhockey.programs

import android.opengl.GLES20
import com.dastanapps.opengles.tuts.ShaderHelper

/**
 *
 * Created by Iqbal Ahmed on 30/09/2023 12:13 AM
 *
 */

open class ShaderProgram(
    vertexShaderSource: String,
    fragmentShaderSource: String
) {

    // Uniform constants
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"
    protected val U_COLOR = "u_Color"

    // Attributeants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

    // Shader program
    protected var program = 0

    init {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource)
    }

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }
}