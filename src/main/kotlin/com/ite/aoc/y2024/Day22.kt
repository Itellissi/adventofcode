package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import kotlin.math.max

private typealias Day202422Input = List<Long>

class Day22 : AocDay<Day202422Input>(
    day = 22,
    year = 2024,
) {

    private data class Sequence(
        val s1: Int,
        val s2: Int,
        val s3: Int,
        val s4: Int,
    )

    override fun part1(entries: Day202422Input): Long {
        return entries.sumOf { s ->
            var secret = s
            repeat(2000) {
                secret = nextSecret(secret)
            }
            secret
        }
    }

    override fun part2(entries: Day202422Input): Int {
        val cache = LinkedHashMap<Sequence, Int>(1996 * entries.size)
        var max = 0
        entries.forEach { s ->
            var secret = s
            var prev = (secret % 10).toInt() to Sequence(0, 0, 0, 0)
            LinkedHashMap<Sequence, Int>(2000).also { sequenceMap ->
                repeat(2000) { i ->
                    secret = nextSecret(secret)
                    val current = (secret % 10).toInt()
                    prev = current to Sequence(prev.second.s2, prev.second.s3, prev.second.s4, current - prev.first)
                    if (i > 3) {
                        sequenceMap.putIfAbsent(prev.second, prev.first)
                    }
                }
            }.forEach { p ->
                val old = cache[p.key] ?: 0
                cache[p.key] = (old + p.value).also {
                    max = max(max, it)
                }
            }
        }
        return max
    }

    private fun nextSecret(secret: Long): Long {
        val s1 = mixAndPrune(secret, secret * 64)
        val s2 = mixAndPrune(s1, s1 / 32)
        return mixAndPrune(s2, s2 * 2048)
    }

    private fun mixAndPrune(prev: Long, n: Long): Long {
        return prev.xor(n) % 16777216
    }

    override fun convert(file: String): Day202422Input =
        file.mapLines { _, l -> l.toLong() }

}

fun main() {
    Day22().solve(copyResult = true, test = true)
    Day22().solve(copyResult = true)
}