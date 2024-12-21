package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import java.security.MessageDigest

private typealias Day201504Input = List<String>

class Day04 : AocDay<Day201504Input>(
    day = 4,
    year = 2015,
) {

    override fun part1(entries: Day201504Input): Int {
        return find("00000")
    }

    override fun part2(entries: Day201504Input): Int {
        return find("000000")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun find(prefix: String): Int {
        val input = "ckczppom"
        val md = MessageDigest.getInstance("MD5")
        (1..100000000).forEach { i ->
            val digest = md.digest("$input$i".toByteArray())
            if (digest.toHexString().startsWith(prefix)) {
                return i
            }
        }
        return -1
    }

    override fun convert(file: String): Day201504Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day04().solve(copyResult = true)
}