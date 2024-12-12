package com.ite.aoc.y2024

import com.ite.aoc.*
import javax.swing.text.html.HTML.Tag.OL

private typealias Day202412Input = List<List<Char>>

class Day12 : AocDay<Day202412Input>(
    day = 12,
    year = 2024,
) {

    private val directions = listOf(
        AocUtils.Directions.N,
        AocUtils.Directions.S,
        AocUtils.Directions.E,
        AocUtils.Directions.W,
    )

    private val diags = listOf(
        AocUtils.Directions.NE,
        AocUtils.Directions.NW,
        AocUtils.Directions.SE,
        AocUtils.Directions.SW,
    )

    override fun part1(entries: Day202412Input): Any? {
        val visited = entries.map { entries.map { false }.toMutableList() }
        return entries.traverseWithSum { i, j, g ->
            val pos = i to j
            if (!visited.atPos(pos)) countNoDiscount(pos, g, visited, entries)
                .let { it.first * it.second }
            else 0
        }
    }

    private fun countNoDiscount(
        pos: Pair<Int, Int>,
        type: Char,
        visited: List<MutableList<Boolean>>,
        entries: Day202412Input
    ): Pair<Long, Long> {
        var a = 1L
        var p = 0L
        visited[pos.first][pos.second] = true
        directions.forEach { d ->
            val next = d.navigate(pos)
            if (next.inRange(entries)) {
                val nextG = entries.atPos(next)
                when {
                    !visited.atPos(next) && nextG == type -> countNoDiscount(next, type, visited, entries).let {
                        p += it.first
                        a += it.second
                    }

                    nextG != type -> p += 1

                    else -> {}
                }
            } else {
                p += 1
            }
        }
        return p to a
    }

    // wrong 1506703
    override fun part2(entries: Day202412Input): Any? {
        val visited = entries.map { entries.map { false }.toMutableList() }
        return entries.traverseWithSum { i, j, g ->
            val pos = i to j
            if (!visited.atPos(pos)) countDiscount(pos, g, visited, entries)
                .also { println("$g $it") }
                .let { it.first * it.second }
            else 0
        }
    }

    private fun countDiscount(
        pos: Position,
        type: Char,
        visited: List<MutableList<Boolean>>,
        entries: Day202412Input
    ): Pair<Long, Long> {

        fun areMirrors(p1: Position, p2: Position) = p1.first == p2.first || p1.second == p2.second

        if (visited.atPos(pos)) {
            return 0L to 0L
        }

        var a = 1L
        var p = 0L
        visited[pos.first][pos.second] = true
        val neighbors = directions.map { it.navigate(pos) }
            .filter { it.inRange(entries) && entries.atPos(it) == type }
        when {
            neighbors.isEmpty() -> {
                p += 4
            }

            neighbors.size == 1 -> {
                p += 2
            }

            neighbors.size == 4 -> {
                p += diags.map { it.navigate(pos) }
                    .map { entries.atPos(it) }
                    .count { it != type }
            }

            neighbors.size == 3 -> {
                val det = when {
                    areMirrors(neighbors[0], neighbors[1]) -> neighbors[2]
                    areMirrors(neighbors[0], neighbors[2]) -> neighbors[1]
                    else -> neighbors[0]
                }
                when {
                    det.first == pos.first -> listOf(
                        det.navigate(AocUtils.Directions.N),
                        det.navigate(AocUtils.Directions.S)
                    )

                    else -> listOf(det.navigate(AocUtils.Directions.E), det.navigate(AocUtils.Directions.W))
                }.map { entries.atPos(it) }
                    .count { it != type }
            }

            neighbors.size == 2 && areMirrors(neighbors[0], neighbors[1]) -> {}

            neighbors.size == 2 -> {
                val i = if (neighbors[0].first == pos.first) neighbors[1].first else neighbors[0].first
                val j = if (neighbors[0].second == pos.second) neighbors[1].second else neighbors[0].second
                p += if (entries.atPos(i to j) == type) 1 else 2
            }
        }
        neighbors.filter { !visited.atPos(it) && entries.atPos(it) == type }
            .forEach { n ->
                countDiscount(n, type, visited, entries).let {
                    p += it.first
                    a += it.second
                }
            }
        return p to a
    }


    override fun convert(file: String): Day202412Input =
        file.mapLines { _, l -> l.toCharArray().toList() }

}

fun main() {
    Day12().solve(copyResult = true, test = true)
    //Day12().solve(copyResult = true)
}