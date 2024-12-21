package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.readFile

private typealias Day201501Input = String

class Day01 : AocDay<Day201501Input>(
    day = 1,
    year = 2015,
) {

    override fun part1(entries: Day201501Input): Int {
        return entries.count { it == '(' } - entries.count { it == ')' }
    }

    override fun part2(entries: Day201501Input): Int {
        var f = 0
        entries.toCharArray()
            .onEachIndexed { i, c ->
                when (c) {
                    '(' -> f++
                    ')' -> f--
                }
                if (f < 0) {
                    return@part2 i + 1
                }
            }
        return -1
    }

    override fun convert(file: String): Day201501Input =
        file.readFile().lines().first()

}

fun main() {
    Day01().solve(copyResult = true)
}