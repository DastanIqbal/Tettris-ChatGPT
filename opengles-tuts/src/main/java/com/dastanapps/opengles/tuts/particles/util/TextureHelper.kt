/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.dastanapps.opengles.tuts.particles.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import com.dastanapps.opengles.tuts.LoggerConfig

object TextureHelper {
    private const val TAG = "TextureHelper"

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return
     */
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.")
            }
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        var bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(
                    TAG, "Resource ID " + resourceId + " could not be decoded."
                )
            }
            GLES20.glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])

        // Set filtering: a default must be set, or the texture will be
        // black.
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR
        )

        bitmap = bitmap.flipVertically()
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle()

        // Unbind from the texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return textureObjectIds[0]
    }

    fun loadTextureRepeat(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.")
            }
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        var bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(
                    TAG, "Resource ID " + resourceId + " could not be decoded."
                )
            }
            GLES20.glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])

        // Set filtering: a default must be set, or the texture will be
        // black.
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT.toFloat()
        )

        bitmap = bitmap.flipVertically()
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle()

        // Unbind from the texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return textureObjectIds[0]
    }
}

fun Bitmap.flipVertically(): Bitmap {
    val matrix = Matrix().apply { postScale(1f, -1f, width / 2f, height / 2f) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.flipHorizontally(): Bitmap {
    val matrix = Matrix().apply { postScale(-1f, 1f, width / 2f, height / 2f) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
