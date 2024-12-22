package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201508Input = List<String>

class Day08 : AocDay<Day201508Input>(
    day = 8,
    year = 2015,
) {

    private val escapeRegex = """(\\"|\\\\)""".toRegex()
    private val hexRegex = """\\x[a-f0-9]{2}""".toRegex()

    private val escapable = setOf('\\', '"')

    override fun part1(entries: Day201508Input): Int {
        return entries.sumOf {
            2 + escapeRegex.findAll(it).count() + hexRegex.findAll(it).count() * 3
        }
    }

    override fun part2(entries: Day201508Input): Int {
        return entries.sumOf {
            2 + it.count { c -> escapable.contains(c) }
        }
    }

    override fun convert(file: String): Day201508Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day08().solve(copyResult = true, test = true)
    Day08().solve(copyResult = true)
}