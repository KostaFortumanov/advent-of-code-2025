package day08

import integers
import println
import readFileLines
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val input = readFileLines(8).map { it.integers() }
    val distances = (0..<input.lastIndex).flatMap { index ->
        (index + 1..input.lastIndex).map { otherIndex ->
            "$index-$otherIndex" to distance(input[index], input[otherIndex])
        }
    }.toMap()

    part1(input, distances).println()
    part2(input, distances).println()
}

private fun part1(boxes: List<List<Int>>, distances: Map<String, Double>) =
    connectCircuits(boxes, distances, boxes.size, lastMergeResult = false)

private fun part2(boxes: List<List<Int>>, distances: Map<String, Double>) =
    connectCircuits(boxes, distances, distances.size, lastMergeResult = true)

private fun connectCircuits(
    boxes: List<List<Int>>,
    distances: Map<String, Double>,
    take: Int,
    lastMergeResult: Boolean,
): Int = distances.entries
    .sortedBy { (_, distance) -> distance }
    .take(take)
    .fold(listOf<List<String>>()) { acc, (path, _) ->
        val (from, to) = path.split("-")
        val fromCircuitIndex = acc.indexOfFirst { it.contains(from) }
        val toCircuitIndex = acc.indexOfFirst { it.contains(to) }

        val newCircuits = when {
            fromCircuitIndex == -1 && toCircuitIndex == -1 -> {
                val newCircuit = listOf(from, to)
                acc + listOf(newCircuit)
            }

            fromCircuitIndex != -1 && toCircuitIndex == -1 -> {
                val mergedCircuit = acc[fromCircuitIndex] + to
                acc.filterIndexed { index, _ -> index != fromCircuitIndex } + listOf(mergedCircuit)
            }

            fromCircuitIndex == -1 && toCircuitIndex != -1 -> {
                val mergedCircuit = acc[toCircuitIndex] + from
                acc.filterIndexed { index, _ -> index != toCircuitIndex } + listOf(mergedCircuit)
            }

            fromCircuitIndex != toCircuitIndex -> {
                val mergedCircuit = acc[fromCircuitIndex] + acc[toCircuitIndex]
                acc.filterIndexed { index, _ -> index != fromCircuitIndex && index != toCircuitIndex } +
                        listOf(mergedCircuit)
            }

            else -> acc
        }

        if (lastMergeResult && newCircuits.size == 1 && newCircuits.first().size == boxes.size) {
            return boxes[from.toInt()].first() * boxes[to.toInt()].first()
        }

        newCircuits
    }.sortedByDescending { it.size }.take(3).fold(1) { acc, circuit -> acc * circuit.size }

private fun distance(box: List<Int>, otherBox: List<Int>): Double {
    val (x1, y1, z1) = box
    val (x2, y2, z2) = otherBox

    return sqrt((x2 - x1).toDouble().pow(2.0) + (y2 - y1).toDouble().pow(2.0) + (z2 - z1).toDouble().pow(2.0))
}
