package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202411Input = List<String>

class Day11 : AocDay<Day202411Input>(
    day = 11,
    year = 2024,
) {

    override fun part1(entries: Day202411Input): Long {
        return countStones(entries, 25)
    }

    override fun part2(entries: Day202411Input): Long {
        return countStones(entries, 75)
    }

    private fun countStones(stones: Day202411Input, iter: Int): Long {
        return mutableMapOf<Pair<Long, Int>, Long>().let { cache ->
            stones.sumOf { countStones(it.toLong(), iter, cache) }.also {
                println("Cache size for $iter iterations : ${cache.size}")
            }
        }
    }

    private fun countStones(
        stone: Long,
        iter: Int,
        cache: MutableMap<Pair<Long, Int>, Long>
    ): Long {
        if (iter == 0) {
            return 1
        }
        val stoneS = stone.toString()
        return cache[stone to iter] ?: when {
            stone == 0L -> countStones(1L, iter - 1, cache)
            stoneS.length % 2 == 0 -> countStones(stoneS.substring(0, stoneS.length / 2).toLong(), iter - 1, cache) +
                    countStones(stoneS.substring(stoneS.length / 2).toLong(), iter - 1, cache)

            else -> countStones(stone * 2024, iter - 1, cache)
        }.also { cache[stone to iter] = it }
    }

    override fun convert(file: String): Day202411Input =
        file.mapLines { _, l -> l.split(" ") }.first()

}

fun main() {
    Day11().solve(copyResult = true, test = true)
    Day11().solve(copyResult = true)
}