package com.ite.aoc.y2016

import com.ite.aoc.*
import java.awt.Color

private typealias Day201608Input = List<Pair<String, Position>>

class Day08 : AocDay<Day201608Input>(
    day = 8,
    year = 2016,
) {

    private val rect = "rect"
    private val rotateRow = "rotateRow"
    private val rotateCol = "rotateCol"

    private val rectRegex = """rect (\d+)x(\d+)""".toRegex()
    private val rotateRowRegex = """rotate row y=(\d+) by (\d+)""".toRegex()
    private val rotateColRegex = """rotate column x=(\d+) by (\d+)""".toRegex()

    override fun part1(entries: Day201608Input): Any? {
        val grid = resolveGrid(entries)
        return grid.sumOf { it.count { c -> c == 1 } }
    }

    override fun part2(entries: Day201608Input): Any? {
        val grid = resolveGrid(entries)
        grid.printGrid { _, _, c ->
            when (c) {
                1 -> "#"
                else -> "."
            }
        }
        grid.visualize { _, c ->
            when (c) {
                1 -> Color.BLACK
                else -> Color.WHITE
            }
        }
        return "RURUCEOEIL"
    }

    private fun resolveGrid(entries: Day201608Input): MutableList<MutableList<Int>> {
        val grid = Array(6) { IntArray(50) { 0 }.toMutableList() }
            .toMutableList()
        entries.forEach { inst ->
            when (inst.first) {
                rect -> drawRect(grid, inst.second)
                rotateRow -> rotateRow(grid, inst.second)
                rotateCol -> rotateCol(grid, inst.second)
            }
        }
        return grid
    }

    private fun drawRect(grid: MutableList<MutableList<Int>>, dimension: Pair<Int, Int>) {
        (0..<dimension.second).forEach { i ->
            (0..<dimension.first).forEach { j ->
                grid[i][j] = 1
            }
        }
    }

    private fun rotateCol(grid: MutableList<MutableList<Int>>, rotateInfo: Pair<Int, Int>) {
        val (j, d) = rotateInfo
        val copy = grid.map { it[j] }
        grid.forEachIndexed { i, it ->
            it[j] = copy[(i - d).mod(grid.size)]
        }
    }

    private fun rotateRow(grid: MutableList<MutableList<Int>>, rotateInfo: Pair<Int, Int>) {
        val (i, d) = rotateInfo
        val row = grid[i]
        val copy = List(row.size) { j -> row[(j - d).mod(row.size)] }
            .toMutableList()
        grid[i] = copy
    }

    override fun convert(file: String): Day201608Input =
        file.mapLines { _, l ->
            when {
                rectRegex.matches(l) -> rectRegex.find(l)!!.groupValues.let { g -> rect to (g[1].toInt() to g[2].toInt()) }
                rotateRowRegex.matches(l) -> rotateRowRegex.find(l)!!.groupValues.let { g -> rotateRow to (g[1].toInt() to g[2].toInt()) }
                rotateColRegex.matches(l) -> rotateColRegex.find(l)!!.groupValues.let { g -> rotateCol to (g[1].toInt() to g[2].toInt()) }
                else -> throw IllegalArgumentException("Invalid line $l")
            }
        }

}

fun main() {
    Day08().solve(copyResult = true)
}