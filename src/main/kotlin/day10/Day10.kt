package day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
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
    part2(manuals).println()
}

private fun part1(manuals: List<Manual>) =
    manuals.sumOf { minButtonPresses(it) }

private fun part2(manuals: List<Manual>) =
    manuals.sumOf { solveJoltages(it) }

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

private fun solveJoltages(config: Manual): Int = Context().use { ctx ->
    val solver = ctx.mkOptimize()
    val zero = ctx.mkInt(0)

    val buttons = config.actions.indices
        .map { ctx.mkIntConst("button#$it") }
        .onEach { button -> solver.Add(ctx.mkGe(button, zero)) }
        .toTypedArray()

    config.joltages.forEachIndexed { counter, targetValue ->
        val buttonsThatIncrement = config.actions
            .withIndex()
            .filter { (_, counters) -> counter in counters }
            .map { buttons[it.index] }
            .toTypedArray()
        val target = ctx.mkInt(targetValue)
        val sumOfPresses = ctx.mkAdd(*buttonsThatIncrement) as IntExpr
        solver.Add(ctx.mkEq(sumOfPresses, target))
    }

    val presses = ctx.mkIntConst("presses")
    solver.Add(ctx.mkEq(presses, ctx.mkAdd(*buttons)))
    solver.MkMinimize(presses)

    if (solver.Check() != Status.SATISFIABLE) error("No solution found for machine: $config.")
    solver.model.evaluate(presses, false).let { it as IntNum }.int
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