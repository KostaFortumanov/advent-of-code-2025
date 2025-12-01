package day01

import println
import readFileLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readFileLines(1).map { it.take(1) to it.drop(1).toInt() }

    part1(input).println()
    part2(input).println()
}

private fun part1(instructions: List<Pair<String, Int>>): Int =
    safeStates(instructions).count { (dial, _) -> dial == 0 }

private fun part2(instructions: List<Pair<String, Int>>): Int =
    safeStates(instructions).last().second

private fun safeStates(instructions: List<Pair<String, Int>>, start: Int = 50) =
    instructions.runningFold(start to 0) { (acc, zeroPasses), (direction, amount) ->
        val result = when (direction) {
            "L" -> acc - amount
            else -> acc + amount
        }
        val normalizedResult = normalizeResult(result)
        val zeroPasses = zeroPasses + (min(result, acc)..max(result, acc))
            .map { normalizeResult(it) }
            .count { it == 0 } - if (normalizedResult == 0) 1 else 0

        normalizedResult to zeroPasses
    }

private fun normalizeResult(result: Int): Int {
    val normalizedResult = result % 100
    return if (normalizedResult < 0) {
        100 + normalizedResult
    } else {
        normalizedResult
    }
}
