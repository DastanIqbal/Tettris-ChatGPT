package com.dastanapps.opengles.tuts.c1.objects

import com.dastanapps.opengles.tuts.c1.data.VertexArray
import com.dastanapps.opengles.tuts.c1.programs.ColorShaderProgram


/**
 *
 * Created by Iqbal Ahmed on 30/09/2023 3:20 PM
 *
 */

class Puck(
    val radius: Float,
    val height: Float,
    val numPointsAroundPuck: Int
) {

    private val POSITION_COMPONENT_COUNT = 3
    private val vertexArray: VertexArray
    private val drawList: List<DrawCommand>

    init {
        val generatedData = ObjectBuilder.createPuck(
            Cylinder(Point(0f, 0f, 0f), radius, height), numPointsAroundPuck
        )

        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        for (drawCommand in drawList) {
            drawCommand.draw()
        }
    }
}