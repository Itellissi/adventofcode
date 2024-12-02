package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils


typealias Day03Input = String

class Day03 : AocDay<Day03Input>(
    day = 3,
    year = 2024,
) {

    private val mulRegex = """mul\((?<left>\d+),(?<right>\d+)\)""".toRegex()
    private val dontRegex = """don't\(\)(?<dont>(?:.(?!do\(\)))*)""".toRegex()

    override fun part1(entries: String): Long {
        return parseMem(entries)
    }

    override fun part2(entries: Day03Input): Long {
        return part1(entries.replace(dontRegex, ""))
    }

    private fun parseMem(line: Day03Input): Long {
        return mulRegex.findAll(line)
            .map { it.groups }
            .sumOf { it["left"]!!.value.toLong() * it["right"]!!.value.toLong() }
    }

    override fun convert(file: String): Day03Input =
        AocUtils.mapLines(file) { _, l -> l }
            .joinToString("")

}

fun main() {
    Day03().solve(copyResult = true)
}
