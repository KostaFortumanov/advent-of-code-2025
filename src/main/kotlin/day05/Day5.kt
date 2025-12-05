package day05

import println
import readFileLines
import split

fun main() {
    val (ranges, ingredients) = readFileLines(5).split { it == "" }.let { (ranges, ingredients) ->
        ranges.map { range ->
            val (start, end) = range.split("-").map { it.toLong() }

            start..end
        } to ingredients.map { it.toLong() }
    }

    part1(ranges, ingredients).println()
    part2(ranges).println()
}

private fun part1(ranges: List<LongRange>, ingredients: List<Long>): Int =
    ingredients.count { ingredient -> ranges.any { range -> ingredient in range } }

private fun part2(ranges: List<LongRange>): Long =
    mergeRanges(ranges).sumOf { it.last - it.first + 1 }

private fun mergeRanges(ranges: List<LongRange>): List<LongRange> = ranges
    .sortedBy { it.first }
    .fold(listOf()) { acc, range ->
        if (acc.isEmpty()) {
            listOf(range)
        } else {
            val lastRange = acc.last()
            if (range.first <= lastRange.last + 1) {
                val mergedRange = lastRange.first..maxOf(lastRange.last, range.last)
                acc.dropLast(1) + listOf(mergedRange)
            } else {
                acc + listOf(range)
            }
        }
    }
