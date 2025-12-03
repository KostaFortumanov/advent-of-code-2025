package day03

import println
import readFileLines

fun main() {
    val input = readFileLines(3)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long = sumLargestJoltages(input, 2)

private fun part2(input: List<String>): Long = sumLargestJoltages(input, 12)

private fun sumLargestJoltages(batteryPacks: List<String>, numBatteries: Int): Long =
    batteryPacks.sumOf { largestJoltage(it, numBatteries).toLong() }

private fun largestJoltage(batteryPack: String, numBatteries: Int): String =
    if (numBatteries > 0) {
        val (largestNumber, largestNumberIndex) = (0..batteryPack.lastIndex - numBatteries + 1)
            .map { batteryPack[it] to it }
            .maxBy { (battery, _) -> battery }
        "$largestNumber${largestJoltage(batteryPack.drop(largestNumberIndex + 1), numBatteries - 1)}"
    } else {
        ""
    }
