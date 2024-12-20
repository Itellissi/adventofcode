package com.ite.aoc.y2024

import com.ite.aoc.*

data class Day202420Input(
    val grid: List<List<Char>>,
    val start: Position,
    val end: Position,
)

class Day20(val gain: Int) : AocDay<Day202420Input>(
    day = 20,
    year = 2024,
) {

    private val directions = setOf(
        AocUtils.Directions.E,
        AocUtils.Directions.S,
        AocUtils.Directions.W,
        AocUtils.Directions.N,
    )

    override fun part1(entries: Day202420Input): Int {
        val (_, costs) = getMinCosts(entries)
        return costs.entries
            .map { it.key to it.value }
            .sumOf { hasShortCut(it, costs, 2) }
    }

    override fun part2(entries: Day202420Input): Int {
        val (_, costs) = getMinCosts(entries)
        return costs.entries
            .map { it.key to it.value }
            .sumOf { hasShortCut(it, costs, 20) }
    }

    private fun getMinCosts(entries: Day202420Input) = dijkstra(
        startCosts = listOf(entries.start to 0),
        isFinal = { p -> p == entries.end },
        pathCost = { _, _ -> 1 }
    ) { p ->
        directions.map { it + p }
            .filter { it.inRange(entries.grid) && entries.grid.atPos(it) != '#' }
    }

    private fun hasShortCut(
        posCost: Pair<Position, Long>,
        costs: Map<Position, Long>,
        cheatDuration: Int,
    ): Int {
        return costs
            .map { it.value to it.key.distance(posCost.first) }
            .count { (cost, distance) ->
                distance <= cheatDuration && cost - posCost.second >= gain + distance
            }
    }

    override fun convert(file: String): Day202420Input =
        file.mapLines { _, l -> l.toCharArray().toMutableList() }
            .let { grid ->
                var start = -1 to -1
                var end = -1 to -1
                grid.forEachCell { i, j, c ->
                    when (c) {
                        'S' -> start = i to j
                        'E' -> end = i to j
                    }
                }
                Day202420Input(
                    grid = grid,
                    start = start,
                    end = end,
                )
            }

}

fun main() {
    Day20(gain = 20).solve(copyResult = true, test = true)
    Day20(gain = 100).solve(copyResult = true)
}