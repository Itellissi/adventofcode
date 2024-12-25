package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201525Input = Pair<Int, Int>

class Day25 : AocDay<Day201525Input>(
    day = 25,
    year = 2015,
    withFile = false,
) {

    private val m = 252533L
    private val d = 33554393L

    override fun part1(entries: Day201525Input): Long {
        val diagonalStart = entries.first + entries.second - 1
        val firstValue = ((diagonalStart - 1) * diagonalStart) / 2 // arithmetic sum
        val index = firstValue + entries.second // codeIndex

        var current = 20151125L // start value
        repeat(index - 1) {
            current = next(current)
        }
        return current
    }

    override fun part2(entries: Day201525Input): String {
        return "https://adventofcode.com/2015"
    }

    private fun next(n: Long) = (n * m) % d

    override fun convert(file: String): Day201525Input = 2978 to 3083

}

fun main() {
    Day25().solve(copyResult = true)
}