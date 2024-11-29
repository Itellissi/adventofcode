package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils

class Day01 : AocDay(
    day = 1,
    year = 2024,
) {
    override fun part1() {
        val lines = AocUtils.mapLines(file) { i, l -> i to l }
    }

    override fun part2() {
        val lines = AocUtils.mapLines(file) { i, l -> i to l }
    }
}

fun main() {
    Day01().solve()
}