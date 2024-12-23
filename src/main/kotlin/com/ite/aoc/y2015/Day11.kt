package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201511Input = String

class Day11 : AocDay<Day201511Input>(
    day = 11,
    year = 2015,
    withFile = false,
) {

    override fun part1(entries: Day201511Input): String {
        var curr = entries
        while (true) {
            curr = (curr.toLong(36) + 1).toString(36).replace('0', 'a')
            val rule1 = (1..<curr.length - 1).any { i -> curr[i - 1] + 1 == curr[i] && curr[i] + 1 == curr[i + 1] }
            val rule2 = !curr.any { c -> c == 'i' || c == 'l' || c == 'o' }
            var count = 0
            var i = 1
            while (i < curr.length) {
                if (curr[i - 1] == curr[i]) {
                    i++
                    count++
                }
                i++
            }
            val rule3 = count > 1
            if (rule1 && rule2 && rule3) return curr
        }
    }

    override fun part2(entries: Day201511Input): String {
        return part1(part1(entries))
    }

    override fun convert(file: String): Day201511Input = "cqjxjnds"

}

fun main() {
    Day11().solve(copyResult = true)
}