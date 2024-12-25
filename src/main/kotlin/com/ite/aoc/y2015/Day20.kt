package com.ite.aoc.y2015

import com.ite.aoc.AocDay

private typealias Day201520Input = Int

class Day20 : AocDay<Day201520Input>(
    day = 20,
    year = 2015,
    withFile = false
) {

    override fun part1(entries: Day201520Input): Int = part1Guess(entries)

    private fun part1Guess(entries: Day201520Input): Int {
        var i = 1
        val offset = 0
        // assumption based on the input
        // the solution has to be a multiple of set of primes
        val increment = 840 // 2 * 2 * 3 * 5 * 7
        val threshold = entries / 10
        while (true) {
            val c = offset + increment * i
            val divisorSum = divisorSum(c)
            if (divisorSum >= threshold) return c.also { println(divisorSum) }
            i++
        }
    }

    override fun part2(entries: Day201520Input): Int {
        val threshold = entries / 11
        var result = threshold
        val houses = IntArray(threshold) { 0 }
        (1..threshold)
            .filter { it < result }
            .forEach { elf ->
                (1..50)
                    .map { it * elf }
                    .filter { it < result }
                    .forEach { j ->
                        houses[j] += 11 * elf
                        if (houses[j] > entries) result = j
                    }
            }
        return result
    }

    private fun divisorSum(n: Int) = (1..n).filter { n % it == 0 }.sum()

    override fun convert(file: String): Day201520Input = 34000000

}

fun main() {
    Day20().solve(copyResult = true)
}