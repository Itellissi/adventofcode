package com.ite.aoc.y2024

import com.ite.aoc.*
import java.awt.Color
import java.util.*
import kotlin.math.min

data class Day202416Input(
    val grid: List<MutableList<Char>>,
    val start: Position,
    val end: Position,
)

private typealias PositionWithDirection = Pair<Position, Position>

class Day16 : AocDay<Day202416Input>(
    day = 16,
    year = 2024,
) {

    private companion object {
        const val REFRESH_RATE = 20L
        const val visualize = false
    }

    private val rotations = mapOf(
        AocUtils.Directions.E to listOf(AocUtils.Directions.N, AocUtils.Directions.S),
        AocUtils.Directions.W to listOf(AocUtils.Directions.N, AocUtils.Directions.S),
        AocUtils.Directions.N to listOf(AocUtils.Directions.E, AocUtils.Directions.W),
        AocUtils.Directions.S to listOf(AocUtils.Directions.E, AocUtils.Directions.W),
    )

    private val directions = rotations.keys.toList()

    private val max = Long.MAX_VALUE

    override fun part1(entries: Day202416Input): Long {
        return findLowestScore(entries).first
    }

    override fun part2(entries: Day202416Input): Int {
        val end = entries.end
        val (minCost, costs) = findLowestScore(entries)
        val visited = mutableSetOf<PositionWithDirection>()
        val viz = if (visualize) entries.grid.visualizeNoCopy(
            cellSize = 7,
            borderColorMapper = { _, c -> if (c == '#') Color.BLACK else Color(210, 210, 210) },
            refreshDelay = REFRESH_RATE,
        ) { _, c ->
            when (c) {
                'S' -> Color(100, 150, 255)
                'E' -> Color.GREEN
                '#' -> Color.BLACK
                '.' -> Color.WHITE
                'v' -> Color.ORANGE
                else -> throw IllegalArgumentException("Unsupported")
            }
        } else null
        costs.filter { it.key.first == end && it.value == minCost + 1 }
            .forEach { backTrack(minCost, costs, it.key, visited, viz) }
        return visited.map { it.first }.toSet().size
    }

    private fun backTrack(
        minCost: Long,
        costs: Map<PositionWithDirection, Long>,
        current: PositionWithDirection,
        visited: MutableSet<PositionWithDirection>,
        viz: GridVisualizer<Char>?,
    ) {
        if (visited.contains(current)) return
        val (currentPos, currentD) = current
        visited += current
        viz?.let {
            it.grid[currentPos.first][currentPos.second] = 'v'
            it.refresh(true)
        }
        directions
            .map { (it + currentPos) to it.negate() } // get neighbours to direction (leading to current)
            .map { it to costs[it] } // get cost
            .filter {
                // has cost (is a candidate) and its cost matches the rule
                it.second != null && when {
                    it.second == minCost && it.first.second == currentD -> true // no direction change
                    it.second == minCost - 1000 && it.first.second != currentD -> true // direction change
                    else -> false
                }
            }
            .forEach {
                backTrack(
                    minCost = if (it.first.second == currentD) minCost - 1 else minCost - 1001,
                    costs = costs,
                    current = it.first,
                    visited = visited,
                    viz = viz,
                )
            }
    }


    private fun findLowestScore(entries: Day202416Input): Pair<Long, Map<PositionWithDirection, Long>> {
        val start = entries.start
        val end = entries.end
        val grid = entries.grid
        val startD = AocUtils.Directions.E

        val costs = mutableMapOf<PositionWithDirection, Long>().withDefault { Long.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<PositionWithDirection, Long>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<PositionWithDirection, Long>>()

        priorityQueue.add(start to startD to 1)
        costs[start to startD] = 1
        rotations[startD]!!.forEach {
            costs[start to it] = 1001
            priorityQueue.add(start to it to 1001)
        }

        var result = max

        while (priorityQueue.isNotEmpty()) {
            val (node, cost) = priorityQueue.poll()
            if (visited.add(node to cost)) {
                val (pos, dir) = node
                val next = dir + pos
                if (next.inRange(grid) && grid.atPos(next) != '#') {
                    if (next == end) {
                        result = min(result, cost)
                    }
                    val pathToScores = rotations[dir]!!.map { it to 1001 } + (dir to 1)
                    pathToScores.forEach { (nextDir, weight) ->
                        val totalCost = cost + weight
                        if (totalCost < costs.getValue(next to nextDir)) {
                            costs[next to nextDir] = totalCost
                            priorityQueue.add(next to nextDir to totalCost)
                        }
                    }
                }
            }
        }

        return result to costs
    }

    override fun convert(file: String): Day202416Input =
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
                Day202416Input(
                    grid = grid,
                    start = start,
                    end = end,
                )
            }

}

fun main() {
    Day16().solve(copyResult = true, test = true)
    Day16().solve(copyResult = true)
}