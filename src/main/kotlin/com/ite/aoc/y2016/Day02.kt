package com.ite.aoc.y2016

import com.ite.aoc.*

private typealias Day201602Input = List<List<Char>>

class Day02 : AocDay<Day201602Input>(
    day = 2,
    year = 2016,
) {

    /**
     * 1 2 3
     * 4 5 6
     * 7 8 9
     */
    private val design1 = mapOf(
        (0 to 0) to '1',
        (0 to 1) to '2',
        (0 to 2) to '3',
        (1 to 0) to '4',
        (1 to 1) to '5',
        (1 to 2) to '6',
        (2 to 0) to '7',
        (2 to 1) to '8',
        (2 to 2) to '9',
    )

    override fun part1(entries: Day201602Input): String {
        return findCode(entries, design1)
    }

    /**
     *     1
     *   2 3 4
     * 5 6 7 8 9
     *   A B C
     *     D
     */
    private val design2 = mapOf(
        (0 to 2) to '1',
        (1 to 1) to '2',
        (1 to 2) to '3',
        (1 to 3) to '4',
        (2 to 0) to '5',
        (2 to 1) to '6',
        (2 to 2) to '7',
        (2 to 3) to '8',
        (2 to 4) to '9',
        (3 to 1) to 'A',
        (3 to 2) to 'B',
        (3 to 3) to 'C',
        (4 to 2) to 'D',
    )

    override fun part2(entries: Day201602Input): String {
        return findCode(entries, design2)
    }

    private fun findCode(entries: Day201602Input, design: Map<Position, Char>): String {
        var pos = design.entries.first { it.value == '5' }.key
        val code = entries.map { inst ->
            inst.forEach { c ->
                val dir = when (c) {
                    'U' -> AocUtils.Directions.N
                    'D' -> AocUtils.Directions.S
                    'L' -> AocUtils.Directions.W
                    'R' -> AocUtils.Directions.E
                    else -> throw IllegalArgumentException("Invalid direct $c")
                }
                if (design.containsKey(dir + pos)) pos += dir
            }
            design[pos]
        }
        return code.joinToString("")
    }

    override fun convert(file: String): Day201602Input =
        file.mapLines { _, l -> l.toCharArray().toList() }

}

fun main() {
    Day02().solve(copyResult = true)
}