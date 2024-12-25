package com.ite.aoc.y2024

import com.ite.aoc.*

data class Day202418Input(
    val dimension: Position,
    val bytes: List<Position>,
)

class Day18 : AocDay<Day202418Input>(
    day = 18,
    year = 2024,
) {

    private val directions = setOf(
        AocUtils.Directions.E,
        AocUtils.Directions.S,
        AocUtils.Directions.W,
        AocUtils.Directions.N,
    )

    override fun part1(entries: Day202418Input): Long? {
        return minSteps(entries, 1024).let { if (it == Long.MAX_VALUE) null else it }
    }

    override fun part2(entries: Day202418Input): Any {
        // one liner
        /*return (1..<entries.bytes.size)
            .first { minSteps(entries, it) == Int.MAX_VALUE }
            .let { entries.bytes[it - 1] }*/
        // dichotomy
        var min = 1
        var max = entries.bytes.size - 1
        var result = -1
        while (min < max) {
            val mid = (min + max) / 2
            val minSteps = minSteps(entries, mid)
            when {
                minSteps < Long.MAX_VALUE && minSteps(entries, mid + 1) == Long.MAX_VALUE -> {
                    result = mid
                    break
                }

                minSteps < Long.MAX_VALUE -> min = mid + 1
                else -> max = mid
            }
        }
        return entries.bytes[result]
    }

    private fun minSteps(entries: Day202418Input, after: Int) = entries.bytes
        .take(after)
        .toSet()
        .let { corrupted ->
            dijkstra(
                startCosts = listOf((0 to 0) to 1L),
                isFinal = { p -> p == entries.dimension - (1 to 1) },
                pathCost = { _, _ -> 1 }
            ) { node ->
                directions.map { it + node }
                    .filter { it.inDimension(entries.dimension) && !corrupted.contains(it) }
            }
        }.first


    override fun convert(file: String): Day202418Input =
        file.readFile().lines().let { l ->
            Day202418Input(
                dimension = l.first().split(" ").let { it[0].toInt() to it[1].toInt() },
                bytes = l.filter { it.contains(",") }.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }
            )
        }

}

fun main() {
    Day18().solve(copyResult = true, test = true)
    Day18().solve(copyResult = true)
}
