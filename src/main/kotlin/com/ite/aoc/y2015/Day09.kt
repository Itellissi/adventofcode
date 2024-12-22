package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import java.util.LinkedList

private typealias Day201509Input = Map<String, List<Pair<String, Int>>>

class Day09 : AocDay<Day201509Input>(
    day = 9,
    year = 2015,
) {

    private val distanceRegex = """(\w+) to (\w+) = (\d+)""".toRegex()

    override fun part1(entries: Day201509Input): Int {
        return entries.keys.minOf { shortestDistance(entries, it, 0, LinkedList()) }
    }

    override fun part2(entries: Day201509Input): Int {
        return entries.keys.maxOf { longestDistance(entries, it, 0, LinkedList()) }
    }

    private fun longestDistance(
        distanceMap: Map<String, List<Pair<String, Int>>>,
        current: String,
        cost: Int,
        visited: LinkedList<String>,
    ): Int {
        try {
            visited.addLast(current)
            if (visited.size == distanceMap.keys.size) return cost
            val result = distanceMap[current]!!.filter { !visited.contains(it.first) }
                .maxOf { longestDistance(distanceMap, it.first, it.second + cost, visited) }
            return result
        } finally {
            visited.removeLast()
        }
    }

    private fun shortestDistance(
        distanceMap: Map<String, List<Pair<String, Int>>>,
        current: String,
        cost: Int,
        visited: LinkedList<String>,
    ): Int {
        try {
            visited.addLast(current)
            if (visited.size == distanceMap.keys.size) return cost
            val result = distanceMap[current]!!.filter { !visited.contains(it.first) }
                .minOf { shortestDistance(distanceMap, it.first, it.second + cost, visited) }
            return result
        } finally {
            visited.removeLast()
        }
    }

    override fun convert(file: String): Day201509Input =
        file.mapLines { _, l -> l }
            .asSequence().map {
                distanceRegex.find(it)!!.groupValues.let { g -> (g[1] to g[2]) to g[3].toInt() }
            }.flatMap {
                listOf(
                    it.first.first to (it.first.second to it.second),
                    it.first.second to (it.first.first to it.second),
                )
            }.groupBy { it.first }
            .map { it.key to it.value.map { p -> p.second } }
            .associate { it }

}

fun main() {
    Day09().solve(copyResult = true, test = true)
    Day09().solve(copyResult = true)
}