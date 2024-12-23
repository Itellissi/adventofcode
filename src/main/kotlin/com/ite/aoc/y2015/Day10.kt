package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201510Input = String

class Day10 : AocDay<Day201510Input>(
    day = 10,
    year = 2015,
    withFile = false,
) {

    override fun part1(entries: Day201510Input): Int {
        return resolve(entries, 40)
    }

    override fun part2(entries: Day201510Input): Int {
        return resolve(entries, 50)
    }

    private fun resolve(entries: Day201510Input, times: Int): Int {
        var current = entries.toCharArray().toList()
        repeat(times) {
            val next = mutableListOf<Char>()
            var count = 0
            var prev = current.first()
            current.forEach { c ->
                if (prev == c) {
                    count++
                } else {
                    next.addAll("$count".toCharArray().toList())
                    next += prev
                    prev = c
                    count = 1
                }
            }
            if (count > 0) {
                next.addAll("$count".toCharArray().toList())
                next += prev
            }
            current = next
        }
        return current.size
    }

    override fun convert(file: String): Day201510Input = "1113222113"

}

fun main() {
    Day10().solve(copyResult = true)
}