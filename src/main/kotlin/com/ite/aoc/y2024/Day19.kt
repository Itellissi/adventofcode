package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.readFile

data class Day202419Input(
    val patterns: Map<Int, Set<String>>,
    val designs: List<String>,
)

class Day19 : AocDay<Day202419Input>(
    day = 19,
    year = 2024,
) {

    override fun part1(entries: Day202419Input): Int = part1DP(entries)

    private fun part1DP(entries: Day202419Input): Int {
        return entries.designs
            .count { isPossibleDesign(entries.patterns, it, mutableMapOf()) }
    }

    private fun part1Regex(entries: Day202419Input): Int {
        val regex = entries.patterns.values
            .flatten()
            .joinToString("|", "^(?:", ")+$")
            .toRegex()
        return entries.designs
            .count { regex.matches(it) }
    }

    override fun part2(entries: Day202419Input): Long {
        return entries.designs
            .sumOf { countPossiblePatterns(entries.patterns, it, mutableMapOf()) }
    }

    private fun countPossiblePatterns(
        patterns: Map<Int, Set<String>>,
        design: String,
        cache: MutableMap<String, Long>,
    ): Long {
        if (cache.containsKey(design)) {
            return cache[design]!!
        }
        return patterns
            .filter { it.key <= design.length }
            .entries
            .sumOf { p ->
                val left = design.substring(0, p.key)
                val right = design.substring(p.key)
                val count = p.value.count { it == left }.toLong()
                when {
                    right.isEmpty() -> count
                    else -> count * countPossiblePatterns(patterns, right, cache)
                }
            }.also {
                cache[design] = it
            }
    }

    private fun isPossibleDesign(
        patterns: Map<Int, Set<String>>,
        design: String,
        cache: MutableMap<String, Boolean>,
    ): Boolean {
        if (cache.containsKey(design)) {
            return cache[design]!!
        }
        return patterns
            .filter { it.key <= design.length }
            .any { p ->
                val left = design.substring(0, p.key)
                val right = design.substring(p.key)
                p.value.contains(left) && (right.isEmpty() || isPossibleDesign(patterns, right, cache))
            }.also {
                cache[design] = it
            }
    }

    override fun convert(file: String): Day202419Input =
        file.readFile().lines().let { l ->
            Day202419Input(
                patterns = l.first().split(",").map { it.trim() }
                    .groupBy { it.length }
                    .map { it.key to it.value.toSet() }
                    .associate { it },
                designs = l.drop(2).map { it.trim() }
            )
        }

}

fun main() {
    Day19().solve(copyResult = true, test = true)
    Day19().solve(copyResult = true)
}