package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

data class Day201519Input(
    val mappings: List<Pair<String, String>>,
    val input: String,
)

class Day19 : AocDay<Day201519Input>(
    day = 19,
    year = 2015,
) {

    private val mappingRegex = """(\w+) => (\w+)""".toRegex()

    override fun part1(entries: Day201519Input): Int {
        val input = entries.input
        return entries.mappings
            .flatMap { m -> getCalibrations(input, m) }
            .toSet()
            .size
    }

    override fun part2(entries: Day201519Input): Int {
        val target = entries.input
        return find("e", target, entries.mappings, 0)
    }

    private fun find(current: String, target: String, mappings: List<Pair<String, String>>, steps: Int): Int {
        when {
            current.length > target.length -> return Int.MAX_VALUE
            current == target -> return steps
        }
        return mappings.flatMap { getCalibrations(current, it) }
            .sortedBy { it.length }
            .toSet()
            .minOf { find(it, target, mappings, steps + 1) }
    }

    private fun getCalibrations(
        input: String,
        m: Pair<String, String>
    ): MutableSet<String> {
        var idx: Int
        var pos = input.indexOf(m.first)
        val result = mutableSetOf<String>()
        while (pos != -1) {
            result += input.substring(0, pos) + m.second + input.substring(pos + m.first.length)
            idx = pos + m.first.length
            pos = input.indexOf(m.first, idx)
        }
        return result
    }


    override fun convert(file: String): Day201519Input =
        file.mapLines { _, l -> l }.let { lines ->
            val mappings = lines.filter { it.matches(mappingRegex) }
                .map { mappingRegex.find(it)!!.groupValues.let { g -> g[1] to g[2] } }
            val input = lines.last()
            Day201519Input(
                mappings = mappings,
                input = input,
            )
        }

}

fun main() {
    Day19().solve(copyResult = true)
}