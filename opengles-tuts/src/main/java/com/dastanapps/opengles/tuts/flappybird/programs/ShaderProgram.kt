package com.dastanapps.opengles.tuts.flappybird.programs

import android.content.Context
import android.opengl.GLES20
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.particles.util.ShaderHelper
import com.dastanapps.opengles.tuts.particles.util.TextResourceReader

/**
 *
 * Created by Iqbal Ahmed on 01/10/2023 11:54 AM
 *
 */

abstract class ShaderProgram(
    val context: Context
) {
    protected val A_POSITION = "a_Position"
    protected val U_COLOR = "u_Color"
    protected val U_MATRIX = "u_Matrix"

    protected var program = 0


    init {
        program = ShaderHelper.buildProgram(
            vertexShaderSource = TextResourceReader.readTextFileFromResource(
                context,
                R.raw.flappybird_vertex_shader
            ),
            fragmentShaderSource = TextResourceReader.readTextFileFromResource(
                context,
                R.raw.flappybird_fragment_shader
            )
        )
    }


    fun useProgram() {
        GLES20.glUseProgram(program)
    }
}