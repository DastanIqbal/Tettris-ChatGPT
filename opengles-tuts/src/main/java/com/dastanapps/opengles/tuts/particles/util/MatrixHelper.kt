/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.dastanapps.opengles.tuts.particles.util

import kotlin.math.tan

object MatrixHelper {
    fun perspectiveM(
        m: FloatArray, yFovInDegrees: Float, aspect: Float,
        n: Float, f: Float
    ) {
        val angleInRadians = (yFovInDegrees * Math.PI / 180.0).toFloat()
        val a = (1.0 / tan(angleInRadians / 2.0)).toFloat()
        m[0] = a / aspect
        m[1] = 0f
        m[2] = 0f
        m[3] = 0f
        m[4] = 0f
        m[5] = a
        m[6] = 0f
        m[7] = 0f
        m[8] = 0f
        m[9] = 0f
        m[10] = -((f + n) / (f - n))
        m[11] = -1f
        m[12] = 0f
        m[13] = 0f
        m[14] = -(2f * f * n / (f - n))
        m[15] = 0f
    }
}
