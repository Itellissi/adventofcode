package com.ite.aoc.y2024

import com.ite.aoc.*
import java.util.concurrent.atomic.AtomicInteger

private typealias Day202410Input = Pair<List<List<Int>>, List<Position>>

class Day10 : AocDay<Day202410Input>(
    day = 10,
    year = 2024,
) {

    private val directions = listOf(
        AocUtils.Directions.N,
        AocUtils.Directions.S,
        AocUtils.Directions.E,
        AocUtils.Directions.W,
    )

    override fun part1(entries: Day202410Input): Long {
        val grid = entries.first
        return entries.second.sumOf { pos ->
            mutableSetOf<Position>()
                .also { paths -> explorePaths(grid, pos, grid.atPos(pos), mutableSetOf(pos)) { p -> paths += p } }
                .size
                .toLong()
        }
    }

    override fun part2(entries: Day202410Input): Long {
        val grid = entries.first
        return entries.second.sumOf { pos ->
            AtomicInteger(0)
                .also { count ->
                    explorePaths(
                        grid,
                        pos,
                        grid.atPos(pos),
                        mutableSetOf(pos)
                    ) { _ -> count.incrementAndGet() }
                }
                .toLong()
        }
    }

    private fun explorePaths(
        entries: List<List<Int>>,
        pos: Position,
        c: Int,
        visited: MutableSet<Position>,
        onPeak: (Position) -> Unit,
    ) {
        if (c == 9) {
            onPeak(pos)
        }
        return directions.forEach { d ->
            val next = d.navigate(pos)
            if (next.inRange(entries) && !visited.contains(next) && entries.atPos(next) - c == 1) {
                visited += next
                explorePaths(entries, next, entries.atPos(next), visited, onPeak)
                visited -= next
            }
        }
    }

    override fun convert(file: String): Day202410Input =
        file.mapLines { _, l -> l.toCharArray().map { it.digitToInt() }.toMutableList() }
            .let {
                it to it.traverse(mutableListOf<Position>()) { i, j, c, list ->
                    if (c == 0) list += i to j
                    list
                }.toList()
            }

}

fun main() {
    Day10().solve(copyResult = true, test = true)
    Day10().solve(copyResult = true)
}