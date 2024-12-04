package com.ite.aoc.y2024

import com.ite.aoc.*

private typealias Day202404Input = List<List<Char>>

class Day04 : AocDay<Day202404Input>(
    day = 4,
    year = 2024,
) {

    override fun part1(entries: Day202404Input): Long {
        return entries.traverseWithSum { i, j, _ -> count(entries, i, j).toLong() }
    }

    override fun part2(entries: Day202404Input): Long {
        return entries.traverseWithSum { i, j, _ -> crossCount(entries, i, j).toLong() }
    }

    private val directions: List<Pair<Int, Int>> = AocUtils.Directions.ALL

    private val searchWord = "XMAS"

    private fun count(
        entries: Day202404Input,
        i: Int,
        j: Int
    ): Int {
        var sum = 0
        if (entries[i][j] == searchWord[0]) {
            directions.forEach { d ->
                var pos = (i to j)
                for (k in 1..<searchWord.length) {
                    pos = d.navigate(pos).also {
                        if (!(it.inRange(entries) && entries.atPos(it) == searchWord[k])) {
                            return@forEach
                        }
                    }
                }
                sum += 1
            }
        }
        return sum
    }

    private val diagChars = setOf('S', 'M')

    private fun crossCount(entries: List<List<Char>>, i: Int, j: Int): Int {
        if (entries[i][j] == 'A' && i in 1..entries.size - 2 && j in 1..entries[i].size - 2) {
            val f = setOf(entries[i - 1][j - 1], entries[i + 1][j + 1])
            val s = setOf(entries[i - 1][j + 1], entries[i + 1][j - 1])
            if (f == diagChars && s == diagChars) {
                return 1
            }
        }
        return 0
    }

    override fun convert(file: String): Day202404Input =
        file.mapLines { _, l -> l.toCharArray().toList() }

}

fun main() {
    Day04().solve(copyResult = true)
}