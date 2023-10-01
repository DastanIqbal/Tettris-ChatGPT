package com.dastanapps.opengles.tuts.flappybird.objects

import android.content.Context
import android.opengl.GLES20
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.airhockey.data.BYTES_PER_FLOAT
import com.dastanapps.opengles.tuts.flappybird.data.VertexArray
import com.dastanapps.opengles.tuts.flappybird.programs.TextureShaderProgram
import com.dastanapps.opengles.tuts.particles.util.TextureHelper

/**
 *
 * Created by Iqbal Ahmed on 01/10/2023 12:18 PM
 *
 */

class Background {

    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COORDINATES_COMPONENT_COUNT: Int = 2

    private val TOTAL_COMPONENT_COUNT =
        POSITION_COMPONENT_COUNT * TEXTURE_COORDINATES_COMPONENT_COUNT
    private val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT


    private val TOTAL_TEXTURE_COMPONENT_COUNT = TEXTURE_COORDINATES_COMPONENT_COUNT
    private val STRIDE_TEXTURE = TOTAL_TEXTURE_COMPONENT_COUNT * BYTES_PER_FLOAT


    private val vertexData = floatArrayOf(
        // Order of coordinates: X, Y,
        -1f, -1f, -1f, -1f,
        1f, 1f, 1f, 1f,
        -1f, 1f, -1f, 1f,
        -1f, -1f, -1f, -1f,
        1f, -1f, 1f, -1f,
        1f, 1f, 1f, 1f,
    )

    private val vertexArray = VertexArray(vertexData)

    private val textureData = floatArrayOf(
        -1f, 1f,
        -1f, -1f,
        1f, -1f,
        1f, 1f
    )

    private val textureArray = VertexArray(textureData)

    fun loadTexture(context: Context): Int {
        return TextureHelper.loadTexture(context, R.drawable.flappybird_bg)
    }

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.aPositionLocaiton,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            textureProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }
}