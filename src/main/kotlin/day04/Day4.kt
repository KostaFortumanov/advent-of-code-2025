package day04

import countGridEntries
import println
import readFileCharMatrix

fun main() {
    val input = readFileCharMatrix(4)

    part1(input).println()
    part2(input).println()
}

private fun part1(inputGrid: List<List<Char>>): Int =
    countRemovedRolls(inputGrid, gridSequence(inputGrid).drop(1).first())

private fun part2(inputGrid: List<List<Char>>): Int =
    countRemovedRolls(inputGrid, gridSequence(inputGrid).last())

private fun countRemovedRolls(grid: List<List<Char>>, otherGrid: List<List<Char>>): Int =
    grid.countGridEntries { it == '@' } - otherGrid.countGridEntries { it == '@' }

private fun gridSequence(inputGrid: List<List<Char>>): Sequence<List<List<Char>>> =
    generateSequence(inputGrid) { grid ->
        grid.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, it ->
                if (it == '@' && countAdjacent(grid, rowIndex, colIndex) < 4) {
                    '.'
                } else {
                    it
                }
            }
        }.takeIf { it != grid }
    }

val steps = (-1..1).flatMap { dx ->
    (-1..1).map { dy -> dx to dy }
}.filter { (dx, dy) -> dx != 0 || dy != 0 }

private fun countAdjacent(grid: List<List<Char>>, x: Int, y: Int): Int =
    steps.count { (dx, dy) ->
        val newRow = x + dx
        val newColumn = y + dy
        newRow in grid.indices && newColumn in grid[newRow].indices && grid[newRow][newColumn] == '@'
    }
