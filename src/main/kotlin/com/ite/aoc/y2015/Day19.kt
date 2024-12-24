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
        var current = entries.input
        var count = 0
        // e -> XY
        // XY -> (AB)(CD)
        // ...
        // starting from the end in reverse is the way to go
        while (current != "e") {
            val lastMapping = entries.mappings.maxBy { m -> current.lastIndexOf(m.second) }

            val idx = current.lastIndexOf(lastMapping.second)
            if (idx >= 0) {
                current = replaceAt(current, idx, lastMapping.second, lastMapping.first)
                count++
            }
        }
        return count
    }

    private fun replaceAt(current: String, idx: Int, old: String, new: String) =
        current.substring(0, idx) + new + current.substring(idx + old.length)

    private fun getCalibrations(
        input: String,
        m: Pair<String, String>
    ): MutableSet<String> {
        var idx: Int
        var pos = input.indexOf(m.first)
        val result = mutableSetOf<String>()
        while (pos != -1) {
            result += replaceAt(input, pos, m.first, m.second)
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