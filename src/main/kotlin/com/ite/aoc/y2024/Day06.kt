package com.ite.aoc.y2024

import com.ite.aoc.*

private typealias Day202406Input = List<MutableList<Char>>

class Day06 : AocDay<Day202406Input>(
    day = 6,
    year = 2024,
) {

    override fun part1(entries: Day202406Input): Int {
        val visited = getVisited(entries)
        return visited.size
    }

    private fun getVisited(entries: Day202406Input): MutableSet<Pair<Int, Int>> {
        var pos = entries.traverse(0 to 0) { i, j, c, r -> if (c == '^') (i to j) else r }
        val visited = mutableSetOf<Pair<Int, Int>>()
        var d = AocUtils.Directions.N
        while (pos.inRange(entries)) {
            visited += pos
            var next = d.navigate(pos)
            while (next.inRange(entries) && entries.atPos(next) == '#') {
                d = mapDirection(d)
                next = d.navigate(pos)
            }
            pos = next
        }
        return visited
    }

    override fun part2(entries: Day202406Input): Int {
        val visited = getVisited(entries)
        val start = entries.traverse(0 to 0) { i, j, c, r -> if (c == '^') (i to j) else r }
        visited.remove(start)
        return visited.map { obs ->
            var d = AocUtils.Directions.N
            var pos = start
            val v = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            while (pos.inRange(entries)) {
                if (v.contains(pos to d)) {
                    return@map 1
                }
                var next = d.navigate(pos)
                while (next == obs || (next.inRange(entries) && entries.atPos(next) == '#')) {
                    v += pos to d
                    d = mapDirection(d)
                    next = d.navigate(pos)
                }
                pos = next
            }
            return@map 0
        }.sumOf { it }
    }

    private fun mapDirection(d: Pair<Int, Int>): Pair<Int, Int> {
        var d1 = d
        d1 = when (d1) {
            AocUtils.Directions.N -> AocUtils.Directions.E
            AocUtils.Directions.E -> AocUtils.Directions.S
            AocUtils.Directions.S -> AocUtils.Directions.W
            else -> AocUtils.Directions.N
        }
        return d1
    }

    override fun convert(file: String): Day202406Input =
        file.mapLines { _, l -> l.toCharArray().toMutableList() }

}

fun main() {
    Day06().solve(copyResult = true, test = true)
    Day06().solve(copyResult = true)
}