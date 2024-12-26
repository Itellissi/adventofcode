package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.AocUtils

private typealias Day201605Input = String

class Day05 : AocDay<Day201605Input>(
    day = 5,
    year = 2016,
    withFile = false,
) {

    override fun part1(entries: Day201605Input): String {
        var s = 0
        return (1..8).map {
            val p = findCode(entries, "00000", s)
            s = p.second
            p.first[5]
        }.joinToString("")
    }

    override fun part2(entries: Day201605Input): Any? {
        var s = 0
        val positions = mutableMapOf<Int, Char>()
        val result = CharArray(8) { '_' }.toMutableList()
        while (result.any { it == '_' }) {
            var p: Pair<String, Int>
            var pos: Char
            var c: Char
            do {
                p = findCode(entries, "00000", s)
                s = p.second
                pos = p.first[5]
                c = p.first[6]
            } while (pos !in ('0'..'7') || result[pos.digitToInt()] != '_')
            result[pos.digitToInt()] = c
            println(result)
        }
        positions.forEach { result[it.key] = it.value }
        return result.joinToString("")
    }

    private fun findCode(input: Day201605Input, prefix: String, from: Int): Pair<String, Int> {
        var i = from
        while (true) {
            val digest = AocUtils.Crypto.md5("$input$i")
            if (digest.startsWith(prefix)) {
                return digest to i + 1
            }
            i++
        }
    }


    override fun convert(file: String): Day201605Input = "ffykfhsq"

}

fun main() {
    Day05().solve(copyResult = true)
}