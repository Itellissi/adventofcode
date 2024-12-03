package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

typealias Day02Input = List<List<Int>>

class Day02 : AocDay<Day02Input>(
    day = 2,
    year = 2024,
) {

    // 432
    override fun part1(entries: Day02Input): Int = common(entries, 0)

    // 488
    override fun part2(entries: Day02Input): Int = common(entries, 1)

    private fun common(entries: List<List<Int>>, maxDumpCount: Int): Int {
        var count = 0
        for (r in entries) {
            val safe = isSafe(r, maxDumpCount)
            if (safe) {
                count++
            }
        }

        return count
    }

    private fun isSafe(r: List<Int>, maxDumpCount: Int): Boolean {
        var direction = 0
        // vote direction
        for (i in 1..3) when {
            r[i] > r[i - 1] -> direction++
            r[i] < r[i - 1] -> direction--
        }
        val validRange = when {
            direction > 0 -> 1..3
            direction < 0 -> -3..-1
            else -> return false
        }
        var i = 1
        var prev = r[0]
        var safe = true
        var dumpCount = 0
        while (safe && i in 1..<r.size) {
            safe = isSafe(prev, r[i], validRange)
            if (!safe && dumpCount < maxDumpCount) {
                dumpCount++
                safe = true
                when {
                    i + 1 == r.size -> return true
                    isSafe(prev, r[i + 1], validRange) -> Unit
                    i - 2 < 0 || isSafe(r[i - 2], r[i], validRange) -> prev = r[i]
                }
            } else {
                prev = r[i]
            }
            i++
        }
        return safe
    }

    private fun isSafe(x: Int, y: Int, validRange: IntRange): Boolean = y - x in validRange

    override fun convert(file: String): Day02Input =
        file.mapLines { _, l -> l.split(" ").map { it.toInt() } }

}

fun main() {
    Day02().solve(copyResult = true)
}