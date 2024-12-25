package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.Position
import com.ite.aoc.mapLines
import kotlin.math.max
import kotlin.math.min

private typealias Day201506Input = List<String>

class Day06 : AocDay<Day201506Input>(
    day = 6,
    year = 2015,
) {

    private val instructionRegex = """(toggle|turn on|turn off) (\d+,\d+) through (\d+,\d+)""".toRegex()

    override fun part1(entries: Day201506Input): Int {
        val grid = Array(1000) { BooleanArray(1000) { false } }
        entries.forEach {
            applyInstruction(it) { instruction ->
                when (instruction) {
                    "toggle" -> { i -> grid[i.first][i.second] = !grid[i.first][i.second] }
                    "turn on" -> { i -> grid[i.first][i.second] = true }
                    "turn off" -> { i -> grid[i.first][i.second] = false }
                    else -> throw IllegalArgumentException("Unsupported operation $instruction")
                }
            }
        }
        return grid.sumOf { it.count { c -> c } }
    }

    override fun part2(entries: Day201506Input): Any? {
        val grid = Array(1000) { IntArray(1000) { 0 } }
        entries.forEach {
            applyInstruction(it) { instruction ->
                when (instruction) {
                    "toggle" -> { i -> grid[i.first][i.second] += 2 }
                    "turn on" -> { i -> grid[i.first][i.second] += 1 }
                    "turn off" -> { i -> grid[i.first][i.second] = max(0, grid[i.first][i.second] - 1) }
                    else -> throw IllegalArgumentException("Unsupported operation $instruction")
                }
            }
        }
        return grid.sumOf { it.sumOf { c -> c } }
    }

    private fun applyInstruction(
        line: String,
        interpreter: (String) -> ((Position) -> Unit)
    ) {
        instructionRegex.find(line)!!.groupValues.let { g ->
            val instruction = g[1]
            val fromRange = g[2].split(",").let { it[0].toInt() to it[1].toInt() }
            val toRange = g[3].split(",").let { it[0].toInt() to it[1].toInt() }
            val action = interpreter(instruction)
            (min(fromRange.first, toRange.first)..max(fromRange.first, toRange.first)).forEach { i ->
                (min(fromRange.second, toRange.second)..max(fromRange.second, toRange.second)).forEach { j ->
                    action(i to j)
                }
            }
        }
    }

    override fun convert(file: String): Day201506Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day06().solve(copyResult = true)
}