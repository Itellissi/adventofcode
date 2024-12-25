package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day202425Input = List<List<List<Char>>>

class Day25 : AocDay<Day202425Input>(
    day = 25,
    year = 2024,
) {

    companion object {
        const val LOCK = "lock"
        const val KEY = "key"
    }

    private data class Scheme(
        val type: String,
        val cols: List<Int>,
        val height: Int,
        val grid: List<List<Char>>,
    )

    override fun part1(entries: Day202425Input): Int {
        val parsed = entries.map { resolveTypeToScheme(it) }
        val keys = parsed.filter { it.type == KEY }
        val locks = parsed.filter { it.type == LOCK }
        return keys.sumOf { k ->
            locks.count { areMatching(k, it) }
        }
    }

    override fun part2(entries: Day202425Input): String {
        return "https://adventofcode.com/2024"
    }

    private fun areMatching(l: Scheme, r: Scheme): Boolean {
        return l.height == r.height && l.cols
            .mapIndexed { i, h -> r.cols[i] + h <= l.height }
            .all { it }
    }

    private fun resolveTypeToScheme(scheme: List<List<Char>>): Scheme {
        val type = if (scheme[0][0] == '#') LOCK else KEY
        val sTOStep = when (type) {
            LOCK -> 0 to 1
            else -> scheme.size - 1 to -1
        }
        val cols = IntArray(scheme[0].size) { 0 }
        var (start, step) = sTOStep
        while (start in scheme.indices) {
            scheme[start].forEachIndexed { i, c ->
                when (c) {
                    '#' -> cols[i]++
                }
            }
            start += step
        }
        // println("$type ${cols.toList()}")
        return Scheme(
            type = type,
            cols = cols.toList(),
            height = scheme.size,
            grid = scheme,
        )
    }

    override fun convert(file: String): Day202425Input =
        file.mapLines { _, l -> l.toCharArray().toList() }.let { rows ->
            var current = mutableListOf<List<Char>>()
            val schemas = mutableListOf(current)
            rows.forEach { r ->
                if (r.isEmpty()) when {
                    current.isEmpty() -> {}
                    else -> current = mutableListOf<List<Char>>().also { schemas += it }
                } else current += r
            }
            schemas
        }

}

fun main() {
    Day25().solve(copyResult = true, test = true)
    Day25().solve(copyResult = true)
}