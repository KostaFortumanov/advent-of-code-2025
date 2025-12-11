package day09

import longs
import println
import readFileLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readFileLines(9).map {
        val (x, y) = it.longs()
        Point(x, y)
    }
    val pointCombinations = (0..<input.lastIndex).flatMap { index ->
        (index + 1..input.lastIndex).map { otherIndex ->
            input[index] to input[otherIndex]
        }
    }

    part1(pointCombinations).println()
    part2(input, pointCombinations).println()
}

private fun part1(pointCombinations: List<Pair<Point, Point>>): Long =
    pointCombinations.maxOf { (firstPoint, secondPoint) -> rectangleArea(firstPoint, secondPoint) }

private fun part2(polygon: List<Point>, pointCombinations: List<Pair<Point, Point>>): Long {
    val (firstPoint, secondPoint) = pointCombinations
        .sortedByDescending { (firstPoint, secondPoint) -> rectangleArea(firstPoint, secondPoint) }
        .first { (firstPoint, secondPoint) ->
            rectangleInPolygon(polygon, firstPoint, secondPoint)
        }

    return rectangleArea(firstPoint, secondPoint)
}

private fun rectangleArea(firstPoint: Point, secondPoint: Point): Long {
    val (x1, y1) = firstPoint
    val (x2, y2) = secondPoint

    return (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
}

private fun rectangleInPolygon(polygon: List<Point>, firstPoint: Point, secondPoint: Point): Boolean {
    val minX = minOf(firstPoint.x, secondPoint.x) + 1
    val maxX = maxOf(firstPoint.x, secondPoint.x) - 1
    val minY = minOf(firstPoint.y, secondPoint.y) + 1
    val maxY = maxOf(firstPoint.y, secondPoint.y) - 1

    return (minX..maxX).all { x ->
        listOf(Point(x, minY), Point(x, maxY)).all { pointInPolygon(polygon, it) }
    } && (minY..maxY).all { y ->
        listOf(Point(minX, y), Point(maxX, y)).all { pointInPolygon(polygon, it) }
    }
}

private val cache = mutableMapOf<Point, Boolean>()
private fun pointInPolygon(polygon: List<Point>, point: Point): Boolean =
    cache.getOrPut(point) {
        val intersections = polygon.mapIndexed { index, start ->
            val end = polygon[(index + 1) % polygon.size]

            val minY = min(start.y, end.y)
            val maxY = max(start.y, end.y)

            if (start.y == end.y || point.y <= minY || point.y > maxY) {
                0
            } else {
                val xIntersect = (point.y - start.y).toDouble() *
                        (end.x - start.x) /
                        (end.y - start.y) + start.x

                if (point.x < xIntersect) 1 else 0
            }
        }.sum()

        intersections % 2 == 1
    }

private data class Point(val x: Long, val y: Long)
