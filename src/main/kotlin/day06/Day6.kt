package day06

import longs
import println
import readFileLines
import split
import transpose

fun main() {
    val input = readFileLines(6)
    val numbers = input.dropLast(1)
    val instructions = input.last().split("\\s+".toRegex()).filter { it.isNotBlank() }

    part1(numbers, instructions).println()
    part2(numbers, instructions).println()
}

private fun part1(numbers: List<String>, instructions: List<String>): Long {
    val problems = numbers.map { it.longs() }.transpose()
    return sumProblems(problems, instructions)
}

private fun part2(numbers: List<String>, instructions: List<String>): Long {
    val problems = numbers
        .map { it.toCharArray().toList() }
        .transpose()
        .split { line -> line.all { it.isWhitespace() } }
        .map { problem -> problem.map { it.joinToString("").trim().toLong() } }
    return sumProblems(problems, instructions)
}

private fun sumProblems(problems: List<List<Long>>, instructions: List<String>) =
    problems.mapIndexed { index, problem ->
        when (instructions[index]) {
            "+" -> problem.sum()
            else -> problem.reduce { acc, number -> acc * number }
        }
    }.sum()
