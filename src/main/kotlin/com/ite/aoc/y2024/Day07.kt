package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202407Input = List<Pair<Long, List<Long>>>

private typealias Operation = (Long, Long) -> Long

class Day07 : AocDay<Day202407Input>(
    day = 7,
    year = 2024,
) {

    private val mul: Operation = { l, r -> l * r }
    private val add: Operation = { l, r -> l + r }
    private val concat: Operation = { l, r -> concat(l, r) }

    override fun part1(entries: Day202407Input): Long {
        return entries.filter { hasEquation(it.first, it.second, 1, it.second[0], listOf(mul, add)) }
            .sumOf { it.first }
    }

    override fun part2(entries: Day202407Input): Long {
        return entries.filter { hasEquation(it.first, it.second, 1, it.second[0], listOf(mul, add, concat)) }
            .sumOf { it.first }
    }

    private fun hasEquation(
        result: Long,
        members: List<Long>,
        i: Int,
        cumul: Long,
        operations: List<Operation>
    ): Boolean {
        return when {
            i == members.size -> result == cumul
            cumul > result -> false
            else -> operations.any { op -> hasEquation(result, members, i + 1, op(cumul, members[i]), operations) }
        }
    }

    private fun concat(left: Long, right: Long): Long {
        var scale = 10
        while (scale <= right) {
            scale *= 10
        }
        return left * scale + right
    }

    override fun convert(file: String): Day202407Input = file.mapLines { _, l ->
        l.split(':').let {
            it[0].toLong() to it[1].trim().split(' ').map { c -> c.toLong() }.toList()
        }
    }

}

fun main() {
    Day07().solve(copyResult = true, test = true)
    Day07().solve(copyResult = true)
}