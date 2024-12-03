package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils
import kotlin.math.abs

typealias Day02Input = List<List<Int>>

class Day02 : AocDay<Day02Input>(
    day = 2,
    year = 2024,
) {

    override fun part1(entries: Day02Input): Int = common(entries, 0)
    override fun part2(entries: Day02Input): Int = common(entries, 1)

    private fun common(entries: List<List<Int>>, maxDumpCount: Int): Int {
        var count = 0
        for (r in entries) {
            var dumpCount = 0
            var safe = true
            var asCount = 0
            var descCount = 0
            // vote direction
            for (i in 1..3) {
                if (r[i] > r[i - 1]) asCount++
                if (r[i] < r[i - 1]) descCount++
            }
            val asc = asCount > descCount
            var i = 0
            while (safe && i < r.size - 1) {
                safe = isSafe(r, i, i + 1, asc)
                if (!safe && dumpCount < maxDumpCount) {
                    dumpCount++
                    safe = i + 2 !in r.indices || isSafe(r, i, i + 2, asc)
                    if (safe) {
                        i++
                    } else {
                        safe = i - 1 !in r.indices || isSafe(r, i - 1, i + 1, asc)
                    }
                }
                i++
            }
            if (safe) {
                count++
            }
        }

        return count
    }

    private fun isSafe(r: List<Int>, i: Int, j: Int, asc: Boolean): Boolean {
        val diff = r[j] - r[i]
        return abs(diff) in 1..3 && ((diff > 0 && asc) || diff < 0 && !asc)
    }

    override fun convert(file: String): Day02Input =
        AocUtils.mapLines(file) { _, l -> l.split(" ").map { it.toInt() } }

}

fun main() {
    Day02().solve(copyResult = true)
}