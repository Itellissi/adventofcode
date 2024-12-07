package com.ite.aoc.y2024

import com.ite.aoc.*
import java.awt.Color
import javax.swing.JFrame

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

    override fun part2(entries: Day202406Input): Int = part2Solution(entries)

    fun part2Solution(entries: Day202406Input): Int {
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

    private fun part2Visual(entries: Day202406Input): Int {
        val visited = getVisited(entries)
        val visualizeLoops = true
        val start = entries.traverse(0 to 0) { i, j, c, r -> if (c == '^') (i to j) else r }
        val viz = entries.visualize(refreshDelay = 5, cellSize = 7) { _, c ->
            when (c) {
                '#' -> Color.BLACK
                '.' -> Color.WHITE
                '^' -> Color.BLUE
                'o' -> Color.DARK_GRAY
                'x' -> Color.RED
                'K' -> Color(255, 255, 200)
                '_' -> Color(255, 200, 200)
                'O' -> Color.GREEN
                'P' -> Color(0, 100, 0)
                else -> Color.LIGHT_GRAY
            }
        }
        visited.remove(start)
        return visited.map { obs ->
            val v = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            val result = viz.updateThenResetCell(obs, 'o', wait = true) {
                var d = AocUtils.Directions.N
                var pos = start
                var result = 0
                while (pos.inRange(entries)) {
                    if (v.contains(pos to d)) {
                        result = 1
                        break
                    }
                    var next = d.navigate(pos)
                    while (next == obs || (next.inRange(entries) && entries.atPos(next) == '#')) {
                        v += pos to d
                        d = mapDirection(d)
                        next = d.navigate(pos)
                    }
                    pos = next
                }
                return@updateThenResetCell result
            }
            if (result > 0) viz.updateCell(obs, 'O')
                .also {
                    if (visualizeLoops) viz.updateThenResetCell(obs, 'P') {
                        visualizeLoop(viz, entries, start, obs)
                    }
                }
            else viz.updateCell(obs, 'K')
            return@map result
        }.sumOf { it }
    }

    private fun visualizeLoop(
        viz: GridVisualizer<Char>,
        entries: List<MutableList<Char>>,
        start: Pair<Int, Int>,
        obs: Pair<Int, Int>
    ) {
        var d = AocUtils.Directions.N
        var pos = start
        val v = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        var carryOver = 10
        while (carryOver > 0 && pos.inRange(entries)) {
            viz.updateThenResetCell(pos, 'x', wait = true, customRefresh = if (carryOver == 10) 5 else 50) {
                if (v.contains(pos to d)) {
                    carryOver--
                }
                var next = d.navigate(pos)
                while (next == obs || (next.inRange(entries) && entries.atPos(next) == '#')) {
                    v += pos to d
                    d = mapDirection(d)
                    next = d.navigate(pos)
                }
                pos = next
            }
            viz.updateCellIf(pos, '_') { it == '.' }
        }
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
    // Day06().solve(copyResult = true, test = true)
    Day06().solve(copyResult = true)
}