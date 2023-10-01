/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.dastanapps.opengles.tuts.flappybird.data

import android.opengl.GLES20
import com.dastanapps.opengles.tuts.particles.Constants.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexArray(vertexData: FloatArray) {
    private val floatBuffer: FloatBuffer

    init {
        floatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
    }

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(
            attributeLocation, componentCount,
            GLES20.GL_FLOAT, false, stride, floatBuffer
        )
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }

    /**
     * Updates the float buffer with the specified vertex data, assuming that
     * the vertex data and the float buffer are the same size.
     */
    fun updateBuffer(vertexData: FloatArray?, start: Int, count: Int) {
        floatBuffer.position(start)
        floatBuffer.put(vertexData, start, count)
        floatBuffer.position(0)
    }
}
