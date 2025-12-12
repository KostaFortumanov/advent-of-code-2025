package day12

import integers
import println
import readFileLines

fun main() {
    val areas = listOf(7, 5, 6, 7, 7, 7)
    val trees = readFileLines(12).map {
        val parts = it.split(":")
        val area = parts[0].integers().reduce { acc, it -> acc * it }

        area to parts[1].integers()
    }

    part1(trees, areas).println()
}

private fun part1(trees: List<Pair<Int, List<Int>>>, areas: List<Int>) =
    trees.count { (area, numPresents) ->
        area >= numPresents.mapIndexed { index, presents -> presents * areas[index] }.sum()
    }
