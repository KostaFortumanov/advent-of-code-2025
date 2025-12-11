package day10

import integers
import println
import readFileLines
import kotlin.math.min

fun main() {
    val manuals = readFileLines(10).map { line ->
        val parts = line.split(" ")
        val targetDiagram = parts[0].removeSurrounding("[", "]")
        val actions = parts.drop(1).dropLast(1).map { it.integers().toSet() }
        val joltages = parts.last().integers()
        Manual(targetDiagram, actions, joltages)
    }

    part1(manuals).println()
}

private fun part1(manuals: List<Manual>) =
    manuals.sumOf { minButtonPresses(it) }

private fun minButtonPresses(manual: Manual): Int {
    val visited = mutableMapOf<String, Int>()
    fun minButtonPressesInner(currentPosition: String, steps: Int = 0): Int {
        if (currentPosition == manual.targetDiagram) {
            return steps
        }

        val newPositions = manual.actions
            .map { pressButton(currentPosition, it) }
            .filter { (visited[it] ?: Int.MAX_VALUE) > steps }
            .toSet()

        newPositions.forEach {
            visited[it] = min(visited[it] ?: Int.MAX_VALUE, steps)
        }

        return newPositions.minOfOrNull { minButtonPressesInner(it, steps + 1) } ?: Int.MAX_VALUE
    }

    val start = List(manual.targetDiagram.length) { "." }.joinToString("")
    return minButtonPressesInner(start, 0)
}

private fun pressButton(currentPosition: String, button: Set<Int>): String =
    currentPosition.mapIndexed { index, lightState ->
        if (index in button) {
            if (lightState == '#') "." else "#"
        } else {
            lightState
        }
    }.joinToString("")

private data class Manual(
    val targetDiagram: String,
    val actions: List<Set<Int>>,
    val joltages: List<Int>,
)