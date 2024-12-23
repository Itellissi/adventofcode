package com.ite.aoc.y2015

import com.ite.aoc.*
import java.awt.Color

private typealias Day201518Input = List<MutableList<Char>>

class Day18 : AocDay<Day201518Input>(
    day = 18,
    year = 2015,
) {

    val directions = AocUtils.Directions.ALL

    override fun part1(entries: Day201518Input): Long {
        return simulate(entries) { _ -> }
    }

    override fun part2(entries: Day201518Input): Long {
        return simulate(entries) { r ->
            r[0][0] = '#'
            r[0][entries.size - 1] = '#'
            r[entries.size - 1][0] = '#'
            r[entries.size - 1][entries.size - 1] = '#'
        }
    }

    private fun simulate(
        entries: Day201518Input,
        visualize: Boolean = false,
        duration: Int = 100,
        onEachIteration: (Day201518Input) -> Unit
    ): Long {
        var current = entries
        onEachIteration(current)
        val viz = if (visualize) current.visualizeNoCopy(refreshDelay = 100, cellSize = 10) { _, c ->
            when (c) {
                '.' -> Color.WHITE
                '#' -> Color.ORANGE.darker()
                else -> throw IllegalArgumentException("Unknown char $c")
            }
        } else null
        repeat(duration) {
            val copy = current.map { it.toMutableList() }
            current.forEachPosition { pos, c ->
                val neighbors = directions.map { it + pos }.filter { it.inRange(current) }.map { current.atPos(it) }
                when {
                    current.atPos(pos) == '#' && neighbors.count { it == '#' } !in 2..3
                        -> copy[pos.first][pos.second] = '.'

                    current.atPos(pos) == '.' && neighbors.count { it == '#' } == 3
                        -> copy[pos.first][pos.second] = '#'
                }
            }
            onEachIteration(copy)
            current = copy
            viz?.apply {
                this.grid = current
                this.refresh(wait = true)
            }
        }
        return current.traverseWithSum { _, _, c -> if (c == '#') 1 else 0 }
    }

    override fun convert(file: String): Day201518Input =
        file.mapLines { _, l -> l.toCharArray().toMutableList() }

}

fun main() {
    Day18().solve(copyResult = true)
}