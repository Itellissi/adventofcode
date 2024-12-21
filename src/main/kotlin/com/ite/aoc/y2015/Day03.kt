package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.AocUtils
import com.ite.aoc.mapLines
import com.ite.aoc.plus

private typealias Day201503Input = List<Char>

class Day03 : AocDay<Day201503Input>(
    day = 3,
    year = 2015,
) {

    override fun part1(entries: Day201503Input): Int {
        var pos = 0 to 0
        return entries.map {
            pos += asDirection(it)
            pos
        }.toSet().size
    }

    override fun part2(entries: Day201503Input): Int {
        var santa = 0 to 0
        var robotSanta = 0 to 0
        return entries.mapIndexed { i, c ->
            val direction = asDirection(c)
            when {
                i % 2 == 1 -> (santa + direction).also { santa = it }
                else -> (robotSanta + direction).also { robotSanta = it }
            }
        }.toSet().size
    }

    private fun asDirection(it: Char) = when (it) {
        '^' -> AocUtils.Directions.N
        '<' -> AocUtils.Directions.W
        '>' -> AocUtils.Directions.E
        'v' -> AocUtils.Directions.S
        else -> throw IllegalArgumentException("Invalid direction $it")
    }

    override fun convert(file: String): Day201503Input =
        file.mapLines { _, l -> l }.flatMap { it.toCharArray().toList() }

}

fun main() {
    Day03().solve(copyResult = true)
}