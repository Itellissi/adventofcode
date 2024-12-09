package com.ite.aoc.y2024
    
import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202409Input = List<String>

class Day09 : AocDay<Day202409Input>(
    day = 9,
    year = 2024,
) {

    override fun part1(entries: Day202409Input): Any? {
        return null
    }

    override fun part2(entries: Day202409Input): Any? {
        return null
    }

    override fun convert(file: String): Day202409Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day09().solve(copyResult = true)
}