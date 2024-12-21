package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201505Input = List<String>

class Day05 : AocDay<Day201505Input>(
    day = 5,
    year = 2015,
) {

    private val vowels = "aeiou".toCharArray().toSet()
    private val forbidden = listOf("ab", "cd", "pq", "xy")

    override fun part1(entries: Day201505Input): Int {
        return entries.count { isNice(it) }
    }

    private fun isNice(word: String): Boolean {
        var vowelCount = 0
        var rule2 = false
        word.forEachIndexed { i, c ->
            if (i > 0) {
                when {
                    forbidden.contains("${word[i - 1]}$c") -> return false
                    c == word[i - 1] -> rule2 = true
                }
            }
            if (vowels.contains(c)) vowelCount++
        }
        return rule2 && vowelCount >= 3
    }

    override fun part2(entries: Day201505Input): Int {
        return entries.count { isNice2(it) }
    }

    private fun isNice2(word: String): Boolean {
        var rule1 = false
        var rule2 = false
        (1..<word.length - 1).forEach { i ->
            rule1 = rule1 || word.substring(i + 1).contains("${word[i - 1]}${word[i]}")
            rule2 = rule2 || word[i - 1] == word[i + 1]
            if (rule1 && rule2) {
                return true
            }
        }
        return false
    }

    override fun convert(file: String): Day201505Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day05().solve(copyResult = true)
}