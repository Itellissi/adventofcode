package com.ite.aoc.y2024

import com.ite.aoc.*
import java.util.*
import kotlin.math.cos
import kotlin.math.min

private typealias Day202416Input = List<List<Char>>

private typealias PositionWithDirection = Pair<Position, Position>

class Day16 : AocDay<Day202416Input>(
    day = 16,
    year = 2024,
) {

    private val rotations = mapOf(
        AocUtils.Directions.E to listOf(AocUtils.Directions.N, AocUtils.Directions.S),
        AocUtils.Directions.W to listOf(AocUtils.Directions.N, AocUtils.Directions.S),
        AocUtils.Directions.N to listOf(AocUtils.Directions.E, AocUtils.Directions.W),
        AocUtils.Directions.S to listOf(AocUtils.Directions.E, AocUtils.Directions.W),
    )

    private val max = 100000000000L

    override fun part1(entries: Day202416Input): Long {
        var start = -1 to -1
        var end = -1 to -1
        entries.forEachCell { i, j, c ->
            when (c) {
                'S' -> start = i to j
                'E' -> end = i to j
            }
        }

        return findLowestScore(start, end, entries).first
    }

    override fun part2(entries: Day202416Input): Int {
        var start = -1 to -1
        var end = -1 to -1
        entries.forEachCell { i, j, c ->
            when (c) {
                'S' -> start = i to j
                'E' -> end = i to j
            }
        }

        val (minCost, costs) = findLowestScore(start, end, entries)
        val cleanCosts = costs.filter {
            val next = it.key.first + it.key.second
            next.inRange(entries) && entries[next.first][next.second] != '#'
        }
        val final = costs.filter { it.key.first == end && it.value == minCost + 1 }.keys
        val visited = mutableSetOf<Position>()
        final.forEach { backTrackCount(cleanCosts, it, visited) }
        return visited.size
    }

    private fun backTrackCount(
        cleanCosts: Map<PositionWithDirection, Long>,
        current: PositionWithDirection,
        visited: MutableSet<Position>,
    ) {
        if (visited.contains(current.first)) return
        visited.add(current.first)
        val next = current.first + current.second
        val min = cleanCosts.filter { it.key.first == next }
            .minOf { it.value }
        cleanCosts.filter { it.key.first == next && it.value == min }
            .forEach { backTrackCount(cleanCosts, it.key, visited) }
    }


    private fun findLowestScore(
        start: Position,
        end: Position,
        entries: List<List<Char>>,
    ): Pair<Long, Map<PositionWithDirection, Long>> {
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
                if (next.inRange(entries) && entries.atPos(next) != '#') {
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
        file.mapLines { _, l -> l.toCharArray().toList() }

}

fun main() {
    Day16().solve(copyResult = true, test = true)
    Day16().solve(copyResult = true)
}