package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

private typealias Day201524Input = List<Int>

class Day24 : AocDay<Day201524Input>(
    day = 24,
    year = 2015,
) {

    override fun part1(entries: Day201524Input): Long {
        val groupWeight = entries.sum() / 3
        return findMinQe(entries.reversed(), groupWeight, 1, mutableSetOf())
    }

    override fun part2(entries: Day201524Input): Long {
        val groupWeight = entries.sum() / 4
        return findMinQe(entries.reversed(), groupWeight, 1, mutableSetOf())
    }

    private fun findMinQe(
        entries: List<Int>,
        groupWeight: Int,
        qe: Long,
        visited: MutableSet<Int>,
        minSize: AtomicInteger = AtomicInteger(Int.MAX_VALUE),
        minQE: AtomicLong = AtomicLong(Long.MAX_VALUE),
        // the power of primes
        cache: MutableMap<Long, Long> = mutableMapOf(),
    ): Long {
        when {
            cache.containsKey(qe) -> return cache[qe]!!

            groupWeight < 0 -> return Long.MAX_VALUE

            visited.size > minSize.get() -> return Long.MAX_VALUE

            groupWeight == 0 -> when {
                visited.size < minSize.get() -> {
                    minSize.set(visited.size)
                    minQE.set(qe)
                    return minQE.get().also {
                        cache[qe] = it
                    }
                }

                else -> {
                    minQE.set(min(qe, minQE.get()))
                    return minQE.get().also {
                        cache[qe] = it
                    }
                }
            }
        }
        entries.filter { !visited.contains(it) }
            .forEach { w ->
                visited += w
                findMinQe(entries, groupWeight - w, qe * w, visited, minSize, minQE, cache)
                visited -= w
            }
        return minQE.get().also {
            cache[qe] = it
        }
    }

    override fun convert(file: String): Day201524Input =
        file.mapLines { _, l -> l.toInt() }

}

fun main() {
    Day24().solve(copyResult = true)
}