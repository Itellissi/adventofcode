package com.ite.aoc.y2024

import com.ite.aoc.*
import java.awt.Color

private typealias Day202414Input = List<Robot>

class Day14 : AocDay<Day202414Input>(
    day = 14,
    year = 2024,
) {

    private val inputRegex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()

    override fun part1(entries: Day202414Input): Int {
        // test : 11 x 7
        // actual : 101 x 103
        val dimensions = 101 to 103
        val mid = dimensions.first / 2 to dimensions.second / 2
        return entries.asSequence().map { predictRobot(it, 100, dimensions) }
            .filter { it.first != mid.first && it.second != mid.second }
            .groupBy {
                when {
                    it.first < mid.first && it.second < mid.second -> 0
                    it.first < mid.first && it.second > mid.second -> 1
                    it.first > mid.first && it.second < mid.second -> 2
                    else -> 3
                }
            }
            .map { it.value.size }
            .reduce { l, r -> l * r }
    }

    override fun part2(entries: Day202414Input): Int {
        val dimensions = 101 to 103
        var seconds = 1
        var result = 0
        val grid = Array(dimensions.second) { IntArray(dimensions.first) { 0 }.toList() }.toList()
        val viz = grid.visualize(cellSize = 7) { _, i ->
            val b = .2f + (i % 12) / 25f
            when (i) {
                0 -> Color.WHITE
                else -> Color.getHSBColor(.3f, 0.9f, b)
            }
        }

        while (result == 0) {
            val robotPositions = entries.map { r -> r to predictRobot(r, seconds, dimensions) }
            val distribution = robotPositions
                .map { r -> r.second }
                .map { p -> p.second to p.first }
                .toSet()
            distribution.forEach { rp ->
                if (distribution.containsAll(AocUtils.Directions.ALL.map { it + rp })) {
                    seconds.also { result = it }
                }
            }
            part2Display(robotPositions, seconds, viz)
            seconds++
        }
        viz.addSubmitAction { t ->
            reset(viz)
            displayAt(
                entries.map { r -> r to predictRobot(r, t.toInt(), dimensions) },
                viz,
                t.toInt(),
            )
        }
        return result
    }

    private fun part2Display(robotPositions: List<Pair<Robot, Position>>, seconds: Int, viz: GridVisualizer<Int>) {
        reset(viz)
        displayAt(robotPositions, viz, seconds)
    }

    private fun predictRobot(robot: Robot, time: Int, dimensions: Pair<Int, Int>): Position {
        return (robot.p + (robot.v.first * time to robot.v.second * time)).let {
            Math.floorMod(it.first, dimensions.first) to Math.floorMod(it.second, dimensions.second)
        }
    }

    private fun reset(viz: GridVisualizer<Int>) {
        viz.updateCells(viz.grid.indices.flatMap { i -> viz.grid[i].indices.map { i to it } }, { _ -> 0 })
    }

    private fun displayAt(robotPositions: List<Pair<Robot, Position>>, viz: GridVisualizer<Int>, time: Int) {
        val posMap = robotPositions
            .associate { r -> (r.second.second to r.second.first) to r.first.id }
        viz.updateCells(posMap.keys, { p -> posMap[p]!! }, wait = true, customRefresh = 5)
    }


    private var idGen: Int = 0

    override fun convert(file: String): Day202414Input =
        file.mapLines { _, l ->
            inputRegex.find(l)!!.groups.let { g ->
                Robot(
                    id = idGen++,
                    g[1]!!.value.toInt() to g[2]!!.value.toInt(),
                    g[3]!!.value.toInt() to g[4]!!.value.toInt(),
                )
            }
        }

}

data class Robot(
    val id: Int,
    val p: Position,
    val v: Position,
)

fun main() {
    // Day14().solve(copyResult = true, test = true)
    Day14().solve(copyResult = true)
}