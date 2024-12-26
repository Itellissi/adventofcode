package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201607Input = List<String>

class Day07 : AocDay<Day201607Input>(
    day = 7,
    year = 2016,
) {

    private val hyperNetRegex = """\[(\w+)]""".toRegex()
    private val nonHyperNetRegex = """(\w+)(?:\[\w+])?""".toRegex()

    override fun part1(entries: Day201607Input): Int {
        return entries.count { supportsTLS(it) }
    }

    override fun part2(entries: Day201607Input): Int {
        return entries.count { supportsSSL(it) }
    }

    private fun supportsTLS(ip: String): Boolean {
        var c0 = ip[0]
        var c1 = ip[1]
        var c2 = ip[2]
        var openBracket = false
        var candidate = false
        (3..<ip.length).forEach { idx ->
            val curr = ip[idx]
            when (curr) {
                '[' -> openBracket = true
                ']' -> openBracket = false
            }
            when {
                openBracket && curr == c0 && c1 == c2 -> return false
                curr == c0 && c1 == c2 -> candidate = c1 != c0
            }
            c0 = c1
            c1 = c2
            c2 = curr
        }
        return candidate
    }

    private fun supportsSSL(ip: String): Boolean {
        val abasToLookFor = hyperNetRegex.findAll(ip).map { it.groupValues.let { g -> g[1] } }
            .filter { it.length > 2 }
            .flatMap { seq ->
                (1..<seq.length - 1).map {
                    val c = seq[it]
                    val c0 = seq[it - 1]
                    val c1 = seq[it + 1]
                    when {
                        c != c0 && c0 == c1 -> "$c$c0$c"
                        else -> null
                    }
                }
            }
            .filter { it != null }
            .map { it!! }
            .toSet()
        if (abasToLookFor.isEmpty()) return false
        return nonHyperNetRegex.findAll(ip).map { it.groupValues.let { g -> g[1] } }
            .filter { it.length > 2 }
            .flatMap { seq ->
                (1..<seq.length - 1).map {
                    val c = seq[it]
                    val c0 = seq[it - 1]
                    val c1 = seq[it + 1]
                    when {
                        c != c0 && c0 == c1 -> "$c0$c$c0"
                        else -> false
                    }
                }
            }
            .any { abasToLookFor.contains(it) }
    }

    override fun convert(file: String): Day201607Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day07().solve(copyResult = true)
}