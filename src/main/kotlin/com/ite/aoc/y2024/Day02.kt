package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils

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
        var dumpCount = 0
        var safe = true
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
        var i = 0
        while (safe && i < r.size - 1) {
            safe = isSafe(r, i, i + 1, validRange)
            if (!safe && dumpCount < maxDumpCount) {
                dumpCount++
                safe = i + 2 !in r.indices || isSafe(r, i, i + 2, validRange).also {
                    if (it) i++
                } || i - 1 !in r.indices || isSafe(r, i - 1, i + 1, validRange)
            }
            i++
        }
        return safe
    }

    private fun isSafe(r: List<Int>, i: Int, j: Int, validRange: IntRange): Boolean = r[j] - r[i] in validRange

    override fun convert(file: String): Day02Input =
        AocUtils.mapLines(file) { _, l -> l.split(" ").map { it.toInt() } }

}

fun main() {
    Day02().solve(copyResult = true)
}