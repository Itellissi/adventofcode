package com.ite.aoc.y2015

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201512Input = String

class Day12 : AocDay<Day201512Input>(
    day = 12,
    year = 2015,
) {

    private val digitRegex = """-?\d+""".toRegex()

    override fun part1(entries: Day201512Input): Int {
        return digitRegex.findAll(entries).sumOf { it.groupValues[0].toInt() }
    }

    override fun part2(entries: Day201512Input): Any? {
        val tree = jacksonObjectMapper().readTree(entries)
        return sumOf(tree)
    }

    private fun sumOf(tree: JsonNode): Int {
        val children = when {
            tree.isArray -> tree.toList()
            tree.isObject ->
                if (!tree.properties().any { it.value.let { v -> v.isTextual && v.textValue() == "red" } })
                    tree.toList()
                else emptyList()

            tree.isNumber -> return tree.numberValue().toInt()
            else -> return 0
        }
        return children.sumOf { sumOf(it) }
    }

    override fun convert(file: String): Day201512Input =
        file.mapLines { _, l -> l }.first()

}

fun main() {
    Day12().solve(copyResult = true)
}