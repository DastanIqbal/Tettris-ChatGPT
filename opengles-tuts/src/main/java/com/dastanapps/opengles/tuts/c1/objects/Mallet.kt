package com.dastanapps.opengles.tuts.c1.objects

import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.dastanapps.opengles.tuts.c1.data.BYTES_PER_FLOAT
import com.dastanapps.opengles.tuts.c1.data.VertexArray
import com.dastanapps.opengles.tuts.c1.programs.ColorShaderProgram


/**
 *
 * Created by Iqbal Ahmed on 30/09/2023 12:09 AM
 *
 */

class Mallet {

    private val POSITION_COMPONENT_COUNT: Int = 2
    private val COLOR_COMPONENT_COUNT: Int = 3
    private val STRIDE: Int = ((POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT)

    private val VERTEX_DATA = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B
        0f, -0.4f, 0f, 0f, 1f, 0f, 0.4f, 1f, 0f, 0f
    )
    private var vertexArray: VertexArray = VertexArray(VERTEX_DATA)

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            colorProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, 2)
    }
}