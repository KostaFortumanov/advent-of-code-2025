package day07

import println
import readFileCharMatrix

fun main() {
    val input = readFileCharMatrix(7)
    val start = 0 to input.first().indexOf('S')

    part1(input, start).println()
    part2(input, start).println()
}

private fun part1(grid: List<List<Char>>, start: Pair<Int, Int>): Int =
    generateSequence(listOf(start) to 0) { (beams, splits) ->
        val newSplits = beams
            .flatMap { nextBeams(grid, it, applySplit = false) }
            .count { (row, col) -> grid[row][col] == '^' }
        beams
            .flatMap { nextBeams(grid, it) }
            .distinct()
            .takeIf { it.isNotEmpty() }
            ?.let { it to (splits + newSplits) }
    }.last().second

private fun part2(grid: List<List<Char>>, start: Pair<Int, Int>): Long = numberOfTimelines(grid, start)

private fun numberOfTimelines(grid: List<List<Char>>, beamPosition: Pair<Int, Int>): Long {
    val cache = mutableMapOf<Pair<Int, Int>, Long>()
    fun numberOfTimelinesInner(grid: List<List<Char>>, beamPosition: Pair<Int, Int>): Long {
        val newBeams = nextBeams(grid, beamPosition)
        return if (newBeams.isEmpty()) {
            1
        } else {
            newBeams.sumOf { cache.getOrPut(it) { numberOfTimelinesInner(grid, it) } }
        }
    }
    return numberOfTimelinesInner(grid, beamPosition)
}

private fun nextBeams(
    grid: List<List<Char>>,
    beamPosition: Pair<Int, Int>,
    applySplit: Boolean = true,
): List<Pair<Int, Int>> {
    val (row, col) = beamPosition
    val nextRow = row + 1
    return if (applySplit && grid.inBounds(nextRow, col) && grid[nextRow][col] == '^') {
        listOf(nextRow to col - 1, nextRow to col + 1)
    } else {
        listOf(nextRow to col)
    }.filter { (row, col) -> grid.inBounds(row, col) }
}

private fun List<List<Char>>.inBounds(row: Int, col: Int): Boolean =
    row in this.indices && col in this[row].indices
