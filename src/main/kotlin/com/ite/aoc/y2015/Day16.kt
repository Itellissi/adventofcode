package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201516Input = Map<String, Map<String, Int>>

class Day16 : AocDay<Day201516Input>(
    day = 16,
    year = 2015,
) {

    private val inputRegex = """Sue (\d+):(.*)""".toRegex()
    private val categoryRegex = """(\w+): (\d+)""".toRegex()
    private val search = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    override fun part1(entries: Day201516Input): Int {
        return entries.filter {
            it.value.all { e -> search[e.key] == e.value }
        }.map { it.key.toInt() }.first()
    }

    override fun part2(entries: Day201516Input): Int {
        return entries.filter {
            it.value.all { e ->
                when (e.key) {
                    "cats", "trees" -> e.value > search[e.key]!!
                    "pomeranians", "goldfish" -> e.value < search[e.key]!!
                    else -> search[e.key] == e.value
                }
            }
        }.map { it.key.toInt() }.first()
    }

    override fun convert(file: String): Day201516Input =
        file.mapLines { _, l ->
            inputRegex.find(l)!!.groupValues.let { g ->
                g[1] to g[2].split(",")
                    .map { cat ->
                        categoryRegex.find(cat.trim())!!.groupValues.let { cg ->
                            cg[1] to cg[2].toInt()
                        }
                    }
                    .associate { it }
            }
        }.associate { it }

}

fun main() {
    Day16().solve(copyResult = true)
}