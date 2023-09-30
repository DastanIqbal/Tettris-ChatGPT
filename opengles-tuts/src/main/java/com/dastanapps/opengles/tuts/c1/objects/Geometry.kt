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

    fun translate(vector: Vector): Point {
        return Point(
            x + vector.x,
            y + vector.y,
            z + vector.z
        )
    }
}


data class Circle(val center: Point, val radius: Float) {
    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}


data class Cylinder(val center: Point, val radius: Float, val height: Float)


class Ray(val point: Point, val vector: Vector)


class Vector(val x: Float, val y: Float, val z: Float) {
    fun length(): Float {
        return Math.sqrt(
            (x * x + y * y + z * z).toDouble()
        ).toFloat()
    }

    // http://en.wikipedia.org/wiki/Cross_product
    fun crossProduct(other: Vector): Vector {
        return Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    // http://en.wikipedia.org/wiki/Dot_product
    fun dotProduct(other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
    }

    fun scale(f: Float): Vector {
        return Vector(
            x * f,
            y * f,
            z * f
        )
    }
}

class Sphere(val center: Point, val radius: Float)


class Plane(val point: Point, val normal: Vector)


fun vectorBetween(from: Point, to: Point): Vector {
    return Vector(
        to.x - from.x,
        to.y - from.y,
        to.z - from.z
    )
}

fun intersects(sphere: Sphere, ray: Ray): Boolean {
    return distanceBetween(sphere.center, ray) < sphere.radius
}

fun distanceBetween(point: Point, ray: Ray): Float {
    val p1ToPoint = vectorBetween(ray.point, point)
    val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)

    // The length of the cross product gives the area of an imaginary
    // parallelogram having the two vectors as sides. A parallelogram can be
    // thought of as consisting of two triangles, so this is the same as
    // twice the area of the triangle defined by the two vectors.
    // http://en.wikipedia.org/wiki/Cross_product#Geometric_meaning
    val areaOfTriangleTimesTwo: Float = p1ToPoint.crossProduct(p2ToPoint).length()
    val lengthOfBase: Float = ray.vector.length()
    // The area of a triangle is also equal to (base * height) / 2. In
    // other words, the height is equal to (area * 2) / base. The height
    // of this triangle is the distance from the point to the ray.

    val distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase
    return distanceFromPointToRay
}