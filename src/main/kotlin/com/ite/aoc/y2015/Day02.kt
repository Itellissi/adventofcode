package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201502Input = List<List<Int>>

class Day02 : AocDay<Day201502Input>(
    day = 2,
    year = 2015,
) {

    override fun part1(entries: Day201502Input): Int {
        return entries.sumOf {
            2 * it[0] * it[1] + 2 * it[2] * it[1] + 2 * it[0] * it[2] + it.sorted().let { s -> s[0] * s[1] }
        }
    }

    override fun part2(entries: Day201502Input): Int {
        return entries.sumOf {
            it[0] * it[1] * it[2] + it.sorted().let { s -> 2 * s[0] + 2 * s[1] }
        }
    }

    override fun convert(file: String): Day201502Input =
        file.mapLines { _, l -> l.split("x").map { it.toInt() } }

}

fun main() {
    Day02().solve(copyResult = true)
}