package com.ite.aoc

import java.util.*
import kotlin.math.min

fun <T> dijkstra(
    startCosts: List<Pair<T, Long>>,
    isFinal: (T) -> Boolean,
    pathCost: (T, T) -> Long,
    neighbors: (T) -> List<T>,
): Pair<Long, Map<T, Long>> {
    val costs = mutableMapOf<T, Long>().withDefault { Long.MAX_VALUE }
    val priorityQueue = PriorityQueue<Pair<T, Long>>(compareBy { it.second })
    val visited = mutableSetOf<Pair<T, Long>>()

    priorityQueue += startCosts
    costs += startCosts

    var result = Long.MAX_VALUE

    while (priorityQueue.isNotEmpty()) {
        val (node, cost) = priorityQueue.poll()
        if (visited.add(node to cost)) {
            neighbors(node).forEach { neighbor ->
                if (isFinal(neighbor)) {
                    result = min(result, cost)
                }
                val totalCost = cost + pathCost(node, neighbor)
                if (totalCost < costs.getValue(neighbor)) {
                    costs[neighbor] = totalCost
                    priorityQueue.add(neighbor to totalCost)
                }
            }
        }
    }

    return result to costs
}