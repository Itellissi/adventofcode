package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201609Input = String

class Day09 : AocDay<Day201609Input>(
    day = 9,
    year = 2016,
) {

    override fun part1(entries: Day201609Input): Long {
        return decompressSize(entries)
    }

    override fun part2(entries: Day201609Input): Long {
        return decompressSize(entries, version2 = true)
    }

    private fun decompressSize(data: String, range: IntRange = data.indices, version2: Boolean = false): Long {
        var i = range.first
        var j: Int
        var size = 0L
        while (i in range) {
            val curr = data[i]
            if (curr == '(') {
                j = i + 1
                while (j in range && data[j] != ')') j++
                val marker = data.substring(i + 1, j)
                val (s, repeat) = marker.split("x").let { it[0].toInt() to it[1].toInt() }
                val chunkSize =
                    if (version2) decompressSize(data, (j + 1..j + s), true)
                    else s.toLong()
                size += chunkSize * repeat
                i = j + s
            } else {
                size += 1
            }
            i++
        }
        return size
    }

    override fun convert(file: String): Day201609Input =
        file.mapLines { _, l -> l }.first()

}

fun main() {
    Day09().solve(copyResult = true)
}