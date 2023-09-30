package com.dastanapps.opengles.tuts.c1.objects

/**
 *
 * Created by Iqbal Ahmed on 30/09/2023 9:52 AM
 *
 */


data class Point(val x: Float, val y: Float, val z: Float) {
    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }
}


data class Circle(val center: Point, val radius: Float) {
    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}


data class Cylinder(val center: Point, val radius: Float, val height: Float)