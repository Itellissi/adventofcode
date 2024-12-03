package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils
import kotlin.math.abs

typealias Day01Input = Pair<List<Int>, List<Int>>

class Day01 : AocDay<Day01Input>(
    day = 1,
    year = 2024,
) {
    override fun part1(entries: Day01Input): Number {
        val (left, right) = entries
        return left.sorted().zip(right.sorted())
            .sumOf { abs(it.first - it.second) }
    }

    override fun part2(entries: Day01Input): Number {
        val (left, right) = entries
        val occ = right.groupingBy { it }.eachCount()
        return left.sumOf { (occ[it]?.toLong() ?: 0) * it }
    }

    override fun convert(file: String): Day01Input =
        AocUtils.mapLines(file) { _, l -> l.substringBefore(" ").toInt() to l.substringAfterLast(" ").toInt() }.unzip()
}

fun main() {
    Day01().solve()
}