package com.dastanapps.opengles.tuts.c1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLSurfaceView
import android.opengl.Matrix.invertM
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.multiplyMV
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.setLookAtM
import android.opengl.Matrix.translateM
import com.dastanapps.opengles.tuts.R
import com.dastanapps.opengles.tuts.c1.objects.Mallet
import com.dastanapps.opengles.tuts.c1.objects.Plane
import com.dastanapps.opengles.tuts.c1.objects.Point
import com.dastanapps.opengles.tuts.c1.objects.Puck
import com.dastanapps.opengles.tuts.c1.objects.Ray
import com.dastanapps.opengles.tuts.c1.objects.Sphere
import com.dastanapps.opengles.tuts.c1.objects.Table
import com.dastanapps.opengles.tuts.c1.objects.Vector
import com.dastanapps.opengles.tuts.c1.objects.intersects
import com.dastanapps.opengles.tuts.c1.objects.vectorBetween
import com.dastanapps.opengles.tuts.c1.programs.ColorShaderProgram
import com.dastanapps.opengles.tuts.c1.programs.TextureShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max
import kotlin.math.min


/**
 *
 * Created by Iqbal Ahmed on 24/09/2023 2:20 AM
 *
 */

class AirHockeyRenderer(
    val context: Context
) : GLSurfaceView.Renderer {

    private var vertexShaderSource = """
        uniform mat4 u_Matrix;
        
        attribute vec4 a_Position;
        
        void main()
        {
            gl_Position = u_Matrix * a_Position;
        }

    """.trimIndent()

    private var textureVertexShaderSource = """
        uniform mat4 u_Matrix;
        
        attribute vec4 a_Position;
        attribute vec2 a_TextureCoordinates;
        
        varying vec2 v_TextureCoordinates;
        
        void main()
        {
            v_TextureCoordinates = a_TextureCoordinates;
            gl_Position = u_Matrix * a_Position;
        }

    """.trimIndent()

    private var fragmentShaderSource = """
        precision mediump float; 
        
        uniform vec4 u_Color;
        
        void main()
        {
          gl_FragColor = u_Color;
        }
    """.trimIndent()

    private var textureFragmentShaderSource = """
        precision mediump float; 
        
        uniform sampler2D u_TextureUnit;
        varying vec2 v_TextureCoordinates;
        
        void main()
        {
          gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        }
    """.trimIndent()

    // dd

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix: FloatArray = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix: FloatArray = FloatArray(16)

    private val puck by lazy { Puck(0.06f, 0.02f, 32) }
    private val mallet by lazy { Mallet(0.08f, 0.15f, 32) }
    private val table by lazy { Table() }

    private val textureProgram by lazy {
        TextureShaderProgram(
            textureVertexShaderSource, textureFragmentShaderSource
        )
    }
    private val colorProgram by lazy {
        ColorShaderProgram(
            vertexShaderSource, fragmentShaderSource
        )
    }
    private var texture = 0

    private val leftBound = -0.5f
    private val rightBound = 0.5f
    private val farBound = -0.8f
    private val nearBound = 0.8f

    private var previousBlueMalletPosition: Point? = null

    private var puckPosition: Point? = null
    private var puckVector: Vector? = null

    private var malletPressed: Boolean = false
    private lateinit var blueMalletPosition: Point

    private val invertedViewProjectionMatrix = FloatArray(16)

    override fun onSurfaceCreated(glUnused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        blueMalletPosition = Point(0f, mallet.height / 2f, 0.4f)
        puckPosition = Point(0f, puck.height / 2f, 0f)
        puckVector = Vector(0f, 0f, 0f)

        table
        puck
        mallet
        textureProgram
        colorProgram

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)

        perspectiveM(
            projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f
        )

        setLookAtM(viewMatrix, 0, 0f, 2f, 3f, 0f, 0f, 0f, 0f, 1f, 0f)

    }

    override fun onDrawFrame(glUnused: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)

        // Translate the puck by its vector
        puckPosition = puckPosition!!.translate(puckVector!!)

        // If the puck struck a side, reflect it off that side.
        if (puckPosition!!.x < leftBound + puck.radius
            || puckPosition!!.x > rightBound - puck.radius
        ) {
            puckVector = Vector(-puckVector!!.x, puckVector!!.y, puckVector!!.z)
            puckVector = puckVector!!.scale(0.9f)
        }
        if (puckPosition!!.z < farBound + puck.radius
            || puckPosition!!.z > nearBound - puck.radius
        ) {
            puckVector = Vector(puckVector!!.x, puckVector!!.y, -puckVector!!.z)
            puckVector = puckVector!!.scale(0.9f)
        }

        // Clamp the puck position.
        puckPosition = Point(
            clamp(puckPosition!!.x, leftBound + puck.radius, rightBound - puck.radius),
            puckPosition!!.y,
            clamp(puckPosition!!.z, farBound + puck.radius, nearBound - puck.radius)
        )

        // Friction factor
        puckVector = puckVector!!.scale(0.99f)

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)

        // Draw the table.
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        // Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, x, y, z)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        // Now test if this ray intersects with the mallet by creating a
        // bounding sphere that wraps the mallet.
        val malletBoundingSphere = Sphere(
            Point(
                blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z
            ), mallet.height / 2f
        )
        // If the ray intersects (if the user touched a part of the screen that
        // intersects the mallet's bounding sphere), then set malletPressed = // true.
        malletPressed = intersects(malletBoundingSphere, ray)
    }

    fun handleTouchDrag(normalizedX: Float, normalizedY: Float) {
        if (malletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            // Define a plane representing our air hockey table.
            val plane = Plane(Point(0f, 0f, 0f), Vector(0f, 1f, 0f))
            // Find out where the touched point intersects the plane
            // representing our table. We'll move the mallet along this plane.
            val (x, _, z) = intersectionPoint(ray, plane)
            // Clamp to bounds
            previousBlueMalletPosition = blueMalletPosition
            /*
            blueMalletPosition =
                new Point(touchedPoint.x, mallet.height / 2f, touchedPoint.z);
            */
            // Clamp to bounds
            blueMalletPosition = Point(
                clamp(
                    x, leftBound + mallet.radius, rightBound - mallet.radius
                ), mallet.height / 2f, clamp(
                    z, 0f + mallet.radius, nearBound - mallet.radius
                )
            )

            // Now test if mallet has struck the puck.
            val distance: Float = vectorBetween(blueMalletPosition, puckPosition!!).length()
            if (distance < puck.radius + mallet.radius) {
                // The mallet has struck the puck. Now send the puck flying
                // based on the mallet velocity.
                puckVector = vectorBetween(
                    previousBlueMalletPosition!!, blueMalletPosition
                )
            }
        }
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return min(max.toDouble(), max(value.toDouble(), min.toDouble())).toFloat()
    }


    private fun convertNormalized2DPointToRay(
        normalizedX: Float, normalizedY: Float
    ): Ray {
        // We'll convert these normalized device coordinates into world-space
        // coordinates. We'll pick a point on the near and far planes, and draw a
        // line between them. To do this transform, we need to first multiply by
        // the inverse matrix, and then we need to undo the perspective divide.
        val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f)
        val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f)
        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)
        multiplyMV(
            nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0
        )
        multiplyMV(
            farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0
        )

        // Why are we dividing by W? We multiplied our vector by an inverse
        // matrix, so the W value that we end up is actually the *inverse* of
        // what the projection matrix would create. By dividing all 3 components
        // by W, we effectively undo the hardware perspective divide.
        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        // We don't care about the W value anymore, because our points are now
        // in world coordinates.
        val nearPointRay = Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])
        return Ray(
            nearPointRay, vectorBetween(nearPointRay, farPointRay)
        )
    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }

    fun intersectionPoint(ray: Ray, plane: Plane): Point {
        val rayToPlaneVector = vectorBetween(ray.point, plane.point)
        val scaleFactor =
            rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
        return ray.point.translate(ray.vector.scale(scaleFactor))
    }

}