package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201606Input = List<String>

class Day06 : AocDay<Day201606Input>(
    day = 6,
    year = 2016,
) {

    override fun part1(entries: Day201606Input): Any? {
        return entries[0].indices.map { c ->
            entries.groupingBy { it[c] }
                .eachCount()
                .maxBy { it.value }
                .key
        }.joinToString("")
    }

    override fun part2(entries: Day201606Input): Any? {
        return entries[0].indices.map { c ->
            entries.groupingBy { it[c] }
                .eachCount()
                .minBy { it.value }
                .key
        }.joinToString("")
    }

    override fun convert(file: String): Day201606Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day06().solve(copyResult = true)
}