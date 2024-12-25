package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201517Input = List<CupContainer>

data class CupContainer(
    val id: Int,
    val size: Int,
)

class Day17 : AocDay<Day201517Input>(
    day = 17,
    year = 2015,
) {

    override fun part1(entries: Day201517Input): Int {
        return combinations(entries, 150).size
    }

    override fun part2(entries: Day201517Input): Int {
        val combinations = combinations(entries, 150)
        val min = combinations.min()
        return combinations.count { it == min }
    }

    private fun combinations(
        entries: Day201517Input,
        target: Int,
        from: Int = 0,
        count: Int = 0,
    ): List<Int> {
        when {
            target < 0 -> return emptyList()
            target == 0 -> return listOf(count)
        }
        return (from..<entries.size)
            .flatMap { i ->
                val cup = entries[i]
                combinations(entries, target - cup.size, i + 1, count + 1)
            }
    }

    override fun convert(file: String): Day201517Input =
        file.mapLines { i, l -> CupContainer(i, l.toInt()) }

}

fun main() {
    Day17().solve(copyResult = true)
}