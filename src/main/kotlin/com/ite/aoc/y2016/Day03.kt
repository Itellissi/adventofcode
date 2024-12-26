package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201603Input = List<List<Int>>

class Day03 : AocDay<Day201603Input>(
    day = 3,
    year = 2016,
) {

    private val inputRegex = """\s*(\d+)\s+(\d+)\s+(\d+)""".toRegex()

    override fun part1(entries: Day201603Input): Int {
        return entries.count { sides ->
            sides.sorted().let { it[0] + it[1] > it[2] }
        }
    }

    override fun part2(entries: Day201603Input): Int {
        return (0..<entries.size / 3).sumOf { x ->
            val i = x * 3
            val j = x * 3 + 1
            val k = x * 3 + 2
            var count = 0
            if (listOf(entries[i][0], entries[j][0], entries[k][0]).sorted().let { it[0] + it[1] > it[2] }) count++
            if (listOf(entries[i][1], entries[j][1], entries[k][1]).sorted().let { it[0] + it[1] > it[2] }) count++
            if (listOf(entries[i][2], entries[j][2], entries[k][2]).sorted().let { it[0] + it[1] > it[2] }) count++
            count
        }
    }

    override fun convert(file: String): Day201603Input =
        file.mapLines { _, l ->
            inputRegex.find(l)!!.groupValues.let { g ->
                listOf(g[1].toInt(), g[2].toInt(), g[3].toInt())
            }
        }

}

fun main() {
    Day03().solve(copyResult = true)
}