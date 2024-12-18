package com.ite.aoc.y2024
    
import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202419Input = List<String>

class Day19 : AocDay<Day202419Input>(
    day = 19,
    year = 2024,
) {

    override fun part1(entries: Day202419Input): Any? {
        return null
    }

    override fun part2(entries: Day202419Input): Any? {
        return null
    }

    override fun convert(file: String): Day202419Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day19().solve(copyResult = true)
}