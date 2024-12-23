package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import kotlin.math.min

private typealias Day201514Input = List<ReindeerData>

data class ReindeerData(
    val name: String,
    val flySpeed: Int,
    val flyDuration: Int,
    val restDuration: Int,
    var points: Int = 0
)

class Day14 : AocDay<Day201514Input>(
    day = 14,
    year = 2015,
) {

    private val inputRegex =
        """(\w) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

    override fun part1(entries: Day201514Input): Int {
        return entries.maxOf { distanceAfter(it, 2503) }
    }

    override fun part2(entries: Day201514Input): Int {
        repeat(2502) { t ->
            val state = entries.map { it to distanceAfter(it, t + 1) }
            val max = state.maxOf { it.second }
            state.filter { it.second == max }
                .forEach { it.first.points += 1 }
        }
        return entries.maxOf { it.points }
    }

    private fun distanceAfter(reindeer: ReindeerData, t: Int): Int {
        val cycleDuration = reindeer.flyDuration + reindeer.restDuration
        val cycleCount = t / cycleDuration
        val remain = t % cycleDuration
        return reindeer.flySpeed * (cycleCount * reindeer.flyDuration + min(reindeer.flyDuration, remain))
    }

    override fun convert(file: String): Day201514Input =
        file.mapLines { _, l ->
            inputRegex.find(l)!!.groupValues.let { g ->
                ReindeerData(
                    name = g[1],
                    flySpeed = g[2].toInt(),
                    flyDuration = g[3].toInt(),
                    restDuration = g[4].toInt()
                )
            }
        }

}

fun main() {
    Day14().solve(copyResult = true, test = true)
    Day14().solve(copyResult = true)
}