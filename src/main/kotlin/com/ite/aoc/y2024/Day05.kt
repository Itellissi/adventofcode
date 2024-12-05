package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import com.ite.aoc.readFile

private typealias Day202405Input = Input

class Day05 : AocDay<Day202405Input>(
    day = 5,
    year = 2024,
) {
    private val ruleRegex = """(\d+)\|(\d+)""".toRegex()
    private val pagesRegex = """(\d+),?""".toRegex()

    override fun part1(entries: Day202405Input): Int {
        return entries.pages.filter { p -> isSorted(p, entries.rules) }.sumOf { it[it.size / 2] }
    }

    override fun part2(entries: Day202405Input): Int {
        return entries.pages
            .filter { p -> !isSorted(p, entries.rules) }
            .map {
                it.sortedWith { l, r ->
                    when {
                        entries.rules[r]?.contains(l) == true -> -1
                        entries.rules[l]?.contains(r) == true -> 1
                        else -> 0
                    }
                }
            }
            .sumOf { it[it.size / 2] }
    }

    override fun convert(file: String): Day202405Input {
        val rules = mutableListOf<Pair<Int, Int>>()
        val pages = mutableListOf<List<Int>>()
        file.readFile().lines().forEach { l ->
            when {
                ruleRegex.matches(l) -> ruleRegex.find(l)!!.groupValues.run { rules.add(this[1].toInt() to this[2].toInt()) }
                pagesRegex.containsMatchIn(l) -> pagesRegex.findAll(l)
                    .map { it.groupValues[1].toInt() }
                    .toList()
                    .run { pages.add(this) }
            }
        }
        return Input(
            rules = rules.groupBy { it.first }
                .map { e -> e.key to e.value.map { v -> v.second }.toSet() }
                .toMap(),
            pages = pages.toList(),
        )
    }

    private fun isSorted(p: List<Int>, rules: Map<Int, Set<Int>>): Boolean {
        for (i in 1..<p.size) {
            val l = p[i - 1]
            val r = p[i]
            if (rules[r]?.contains(l) == true) {
                return false
            }
        }
        return true
    }
}

data class Input(val rules: Map<Int, Set<Int>>, val pages: List<List<Int>>)

fun main() {
    Day05().solve(copyResult = true)
}