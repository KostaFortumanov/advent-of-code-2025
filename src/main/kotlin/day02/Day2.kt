package day02

import println
import readFileLines
import kotlin.math.sqrt

fun main() {
    val input = readFileLines(2).first().split(",").map { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

    part1(input).println()
    part2(input).println()
}

private fun part1(ranges: List<LongRange>): Long = sumInvalidIds(ranges) {
    if (it % 2 == 0) {
        listOf(it / 2)
    } else {
        listOf()
    }
}

private fun part2(ranges: List<LongRange>): Long = sumInvalidIds(ranges, ::findFactors)

private fun sumInvalidIds(ranges: List<LongRange>, factors: (Int) -> List<Int>): Long =
    ranges.flatMap { range ->
        range.filter { isInvalid(it, factors) }
    }.sum()

private fun isInvalid(number: Long, factors: (Int) -> List<Int>): Boolean {
    if (number > 9) {
        val string = number.toString()
        return factors(string.length).any { factor ->
            List(string.length / factor) {
                string.substring(factor * it, factor * (it + 1))
            }.map { it.toLong() }.distinct().size == 1
        }
    } else {
        return false
    }
}

private fun findFactors(number: Int): List<Int> =
    (1..sqrt(number.toDouble()).toInt()).flatMap {
        if (it == 1) {
            listOf(it)
        } else if (number % it == 0) {
            listOf(it, number / it)
        } else {
            listOf()
        }
    }
