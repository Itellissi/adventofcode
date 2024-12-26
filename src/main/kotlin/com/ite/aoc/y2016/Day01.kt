package com.ite.aoc.y2016

import com.ite.aoc.*
import kotlin.math.abs

private typealias Day201601Input = List<String>

class Day01 : AocDay<Day201601Input>(
    day = 1,
    year = 2016,
) {

    private val directions = listOf(
        AocUtils.Directions.N,
        AocUtils.Directions.E,
        AocUtils.Directions.S,
        AocUtils.Directions.W,
    )

    override fun part1(entries: Day201601Input): Int {
        return navigate(entries).let { abs(it.first) + abs(it.second) }
    }

    override fun part2(entries: Day201601Input): Int {
        return navigate(entries, true).let { abs(it.first) + abs(it.second) }
    }

    private fun navigate(entries: Day201601Input, stopOnFirst: Boolean = false): Position {
        val path = entries.map { it[0] to it.substring(1).toInt() }.toMutableList()
        var currentStep = path.removeFirst()
        var directionIndex = when (currentStep.first) {
            'R' -> 1
            else -> 3
        }
        var dir = directions[directionIndex]
        var pos = 0 to 0
        val visited = mutableSetOf(pos)
        repeat(currentStep.second) {
            pos += dir
            visited.add(pos)
        }
        while (path.isNotEmpty()) {
            currentStep = path.removeFirst()
            directionIndex += when (currentStep.first) {
                'R' -> 1
                else -> -1
            }
            dir = directions[directionIndex.mod(directions.size)]
            repeat(currentStep.second) {
                pos += dir
                if (!visited.add(pos) && stopOnFirst) return pos
            }
        }
        return pos
    }

    override fun convert(file: String): Day201601Input =
        file.mapLines { _, l -> l.split(",").map { it.trim() } }.first()

}

fun main() {
    Day01().solve(copyResult = true, test = true)
    Day01().solve(copyResult = true)
}