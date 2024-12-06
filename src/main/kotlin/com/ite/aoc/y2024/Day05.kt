package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.foldInput

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
        return file.foldInput(Input(mutableMapOf(), mutableListOf())) { input, l ->
            when {
                ruleRegex.matches(l) -> ruleRegex.find(l)!!.groupValues.run {
                    input.rules.compute(this[1].toInt()) { _, v ->
                        v?.also { v.add(this[2].toInt()) } ?: mutableSetOf(this[2].toInt())
                    }
                }

                pagesRegex.containsMatchIn(l) -> pagesRegex.findAll(l)
                    .map { it.groupValues[1].toInt() }
                    .toList()
                    .run { input.pages.add(this) }
            }
            input
        }
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

data class Input(val rules: MutableMap<Int, MutableSet<Int>>, val pages: MutableList<List<Int>>)

fun main() {
    Day05().solve(copyResult = true)
}