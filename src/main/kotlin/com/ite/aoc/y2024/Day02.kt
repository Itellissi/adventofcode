package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils

class Day02 : AocDay<List<String>>(
    day = 2,
    year = 2024,
) {
    override fun part1(entries: List<String>): Any? {
        return "Hello"
    }

    override fun part2(entries: List<String>): Any? {
        return "Aoc"
    }

    override fun convert(file: String): List<String> = AocUtils.mapLines(file) { _, l -> l }

}

fun main() {
    Day02().solve(copyResult = true)
}