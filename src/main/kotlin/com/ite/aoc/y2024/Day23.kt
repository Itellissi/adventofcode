package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202423Input = List<Pair<String, String>>

class Day23 : AocDay<Day202423Input>(
    day = 23,
    year = 2024,
) {

    override fun part1(entries: Day202423Input): Any? {
        val setsOf3 = mutableSetOf<Set<String>>()
        val links = extractLinks(entries)
        links.forEach { (e1, set) ->
            set.forEach { e2 ->
                links[e2]!!
                    .filter { it != e1 && set.contains(it) }
                    .forEach { e3 ->
                        setsOf3 += setOf(e1, e2, e3)
                    }
            }
        }
        return setsOf3.count({ it.any { g -> g[0] == 't' } })
    }

    override fun part2(entries: Day202423Input): String {
        val links = extractLinks(entries)
        val terminals = links.keys.toMutableSet()
        var largestNetwork = emptySet<String>()
        while (terminals.isNotEmpty()) {
            val current = terminals.first()
            getLargestNetworkFor(current, links).let {
                if (it.size > largestNetwork.size) largestNetwork = it
                terminals.removeAll(it)
            }
        }
        return largestNetwork.sorted().joinToString(",")
    }

    private fun getLargestNetworkFor(t: String, links: Map<String, Set<String>>): Set<String> {
        var networksN = setOf(setOf(t))
        while (true) {
            val networksN1 = networksN.flatMap { subNetwork ->
                links.filter { e -> e.value.containsAll(subNetwork) }
                    .map { subNetwork + it.key }
            }.toSet()
            if (networksN1.isEmpty()) {
                return networksN.first()
            }
            networksN = networksN1
        }
    }

    private fun extractLinks(entries: Day202423Input): Map<String, Set<String>> {
        val links = entries.flatMap { listOf(it.first to it.second, it.second to it.first) }
            .groupBy { it.first }
            .map { it.key to it.value.map { v -> v.second }.toSet() }
            .associate { it }
        return links
    }

    override fun convert(file: String): Day202423Input =
        file.mapLines { _, l -> l.split("-").let { it[0] to it[1] } }

}

fun main() {
    Day23().solve(copyResult = true, test = true)
    Day23().solve(copyResult = true)
}