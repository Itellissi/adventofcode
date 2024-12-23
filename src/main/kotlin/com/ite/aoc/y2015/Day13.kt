package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201513Input = Map<Pair<String, String>, Int>

class Day13 : AocDay<Day201513Input>(
    day = 13,
    year = 2015,
) {

    private val lossRegex = """(\w+) would lose (\d+) happiness units by sitting next to (\w+).""".toRegex()
    private val gainRegex = """(\w+) would gain (\d+) happiness units by sitting next to (\w+).""".toRegex()

    private val leMe = "leMe"

    override fun part1(entries: Day201513Input): Int {
        val members = entries.keys.map { it.first }
            .toSet()
        val first = members.first()

        return maxHappiness(mutableListOf(first), members, entries, 0)
    }

    override fun part2(entries: Day201513Input): Int {
        val members = entries.keys.map { it.first }
            .toSet() + setOf(leMe)
        val first = leMe

        return maxHappiness(mutableListOf(first), members, entries, 0)
    }

    private fun maxHappiness(
        members: MutableList<String>,
        allMembers: Set<String>,
        happyMeter: Day201513Input,
        happiness: Int,
    ): Int {
        val current = members.last()
        val previous = if (members.size > 1) members[members.size - 2] else null
        var currentHappiness = previous?.let {
            (happyMeter[current to previous] ?: 0) + (happyMeter[previous to current] ?: 0)
        } ?: 0
        if (members.size == allMembers.size) {
            val first = members.first()
            currentHappiness += (happyMeter[current to first] ?: 0) + (happyMeter[first to current] ?: 0)
        }
        return allMembers.filter { !members.contains(it) }
            .maxOfOrNull { next ->
                members.addLast(next)
                currentHappiness + maxHappiness(
                    members,
                    allMembers,
                    happyMeter,
                    happiness
                ).also { members.removeLast() }
            } ?: currentHappiness
    }

    override fun convert(file: String): Day201513Input =
        file.mapLines { _, l ->
            when {
                lossRegex.matches(l) -> lossRegex.find(l)!!.groupValues.let { (it[1] to it[3]) to -it[2].toInt() }
                gainRegex.matches(l) -> gainRegex.find(l)!!.groupValues.let { (it[1] to it[3]) to it[2].toInt() }
                else -> throw IllegalArgumentException("Unsupported $l")
            }
        }.associate { it }
}

fun main() {
    Day13().solve(copyResult = true, test = true)
    Day13().solve(copyResult = true)
}