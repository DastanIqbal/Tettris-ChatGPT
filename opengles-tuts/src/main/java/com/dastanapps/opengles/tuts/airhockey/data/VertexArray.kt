package com.dastanapps.opengles.tuts.airhockey.data

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


/**
 *
 * Created by Iqbal Ahmed on 29/09/2023 11:55 PM
 *
 */

class VertexArray(
    private val triangleData: FloatArray
) {

    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(triangleData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(triangleData)

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        vertexData.position(dataOffset)
        glVertexAttribPointer(
            attributeLocation, componentCount, GL_FLOAT,
            false, stride, vertexData
        )
        glEnableVertexAttribArray(attributeLocation)
        vertexData.position(0)
    }
}