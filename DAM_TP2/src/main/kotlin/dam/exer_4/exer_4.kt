package org.example.dam.exer_4

import kotlin.math.sqrt

data class Vec2(val x: Double, val y: Double) : Comparable<Vec2> {

    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Double) = Vec2(x * scalar, y * scalar)
    operator fun unaryMinus() = Vec2(-x, -y)

    override fun compareTo(other: Vec2): Int =
        magnitude().compareTo(other.magnitude())

    fun magnitude(): Double = sqrt(x * x + y * y)
    fun dot(other: Vec2): Double = x * other.x + y * other.y
    fun normalized(): Vec2 {
        val mag = magnitude()
        check(mag != 0.0) { "Cannot normalize the zero vector" }
        return Vec2(x / mag, y / mag)
    }

    operator fun get(index: Int): Double = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Index $index out of bounds for Vec2 (valid: 0, 1)")
    }
}

operator fun Double.times(v: Vec2) = Vec2(this * v.x, this * v.y)

fun main() {
    val a = Vec2(3.0, 4.0)
    val b = Vec2(1.0, 2.0)

    println("a         = $a")
    println("b         = $b")
    println("a + b     = ${a + b}")
    println("a - b     = ${a - b}")
    println("a * 2.0   = ${a * 2.0}")
    println("-a        = ${-a}")
    println("|a|       = ${a.magnitude()}")
    println("a dot b   = ${a.dot(b)}")
    println("norm(a)   = ${a.normalized()}")
    println("a[0]      = ${a[0]}")
    println("a[1]      = ${a[1]}")
    println("a > b     = ${a > b}")
    println("a < b     = ${a < b}")

    val vectors = listOf(Vec2(1.0, 0.0), Vec2(3.0, 4.0), Vec2(0.0, 2.0))
    println("Longest   = ${vectors.max()}")
    println("Shortest  = ${vectors.min()}")

    println("\n--- Challenge (left-hand scalar multiplication) ---")
    println("2.0 * a = ${2.0 * a}")

    println("\n--- Challenge (destructuring) ---")
    val (x, y) = a
    println("Destructured a -> x=$x, y=$y")
}