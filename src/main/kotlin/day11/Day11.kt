package day11

import println
import readFileLines

fun main() {
    val graph = readFileLines(11).associate {
        val (start, destinations) = it.split(": ")
        start to destinations.split(" ")
    }

    part1(graph).println()
    part2(graph).println()
}

private fun part1(graph: Map<String, List<String>>): Long = countPaths(graph, "you", "out")

private fun part2(graph: Map<String, List<String>>): Long =
    countPaths(graph, "svr", "fft") *
            countPaths(graph, "fft", "dac") *
            countPaths(graph, "dac", "out")

fun countPaths(graph: Map<String, List<String>>, start: String, end: String): Long {
    val visited = mutableSetOf<String>()
    val order = mutableListOf<String>()

    fun dfs(node: String) {
        if (!visited.add(node)) {
            return
        }
        graph[node].orEmpty().forEach { dfs(it) }
        order += node
    }

    graph.keys.forEach { dfs(it) }

    val ways = mutableMapOf<String, Long>().withDefault { 0L }
    ways[end] = 1L

    order.forEach {
        val sum = graph[it].orEmpty().sumOf { next -> ways[next] ?: 0L }
        if (sum > 0) {
            ways[it] = sum
        }
    }

    return ways[start] ?: 0L
}
