package com.ite.aoc.y2024

import com.ite.aoc.*
import kotlin.math.abs

private typealias Day202421Input = List<String>

class Day21 : AocDay<Day202421Input>(
    day = 21,
    year = 2024,
) {

    private val costCache: MutableMap<Pair<Int, String>, Long> = mutableMapOf()

    private val cache: MutableMap<Pair<Char, Char>, Set<String>> = mutableMapOf()

    private val panicPad = 0 to 0

    private val panicCode = 3 to 0

    override fun part1(entries: Day202421Input): Long {
        return entries.sumOf { complexity(it, 2) }
    }

    override fun part2(entries: Day202421Input): Long {
        return entries.sumOf { complexity(it, 25) }
    }

    private fun complexity(code: String, depth: Int): Long {
        var current = 'A'
        val minCost = code.toCharArray()
            .sumOf { c -> shortestCodePath(current, c, depth).also { current = c } }
        val longValue = code.substring(0, code.length - 1).toLong()
        return (minCost * longValue)
    }

    private fun shortestCodePath(current: Char, target: Char, depth: Int): Long {
        val currentPos = getCodePos(current)
        val targetPos = getCodePos(target)
        val vector = targetPos - currentPos
        val (vert, horizontal) = getMovesPerDirection(targetPos, currentPos)
        val possibilities = when (panicCode) {
            // panic
            (currentPos + (vector.first to 0)) -> setOf("$horizontal${vert}A")
            // panic
            (currentPos + (0 to vector.second)) -> setOf("$vert${horizontal}A")
            else -> setOf(
                "$vert${horizontal}A",
                "$horizontal${vert}A",
            )
        }
        return possibilities.minOf { padPathCost(it, depth) }
    }

    private fun padPathCost(movements: String, depth: Int): Long {
        val key = depth to movements
        if (costCache.containsKey(key)) {
            return costCache[key]!!
        }
        if (depth == 0) return movements.length.toLong()
        var current = 'A'
        return movements.sumOf { c ->
            shortestPadPaths(current, c).minOf { padPathCost(it, depth - 1) }.also { current = c }
        }.also {
            costCache[key] = it
        }
    }

    private fun shortestPadPaths(current: Char, target: Char): Set<String> {
        val key = current to target
        if (cache[key] != null) {
            return cache[key]!!
        }
        val currentPos = getPadPos(current)
        val targetPos = getPadPos(target)
        val vector = targetPos - currentPos

        val (vert, horizontal) = getMovesPerDirection(targetPos, currentPos)
        return when (panicPad) {
            // panic
            (currentPos + (vector.first to 0)) -> setOf("$horizontal${vert}A")
            // panic
            (currentPos + (0 to vector.second)) -> setOf("$vert${horizontal}A")
            else -> setOf(
                "$vert${horizontal}A",
                "$horizontal${vert}A",
            )
        }.also {
            cache[key] = it
        }
    }

    private fun getMovesPerDirection(
        targetPos: Position,
        currentPos: Position
    ): Pair<String, String> {
        val vector = targetPos - currentPos
        val vert = when {
            vector.first < 0 -> "^".repeat(abs(vector.first))
            vector.first > 0 -> "v".repeat(vector.first)
            else -> ""
        }
        val horizontal = when {
            vector.second < 0 -> "<".repeat(abs(vector.second))
            vector.second > 0 -> ">".repeat(vector.second)
            else -> ""
        }
        return Pair(vert, horizontal)
    }

    /**
     * +---+---+---+
     * | 7 | 8 | 9 |
     * +---+---+---+
     * | 4 | 5 | 6 |
     * +---+---+---+
     * | 1 | 2 | 3 |
     * +---+---+---+
     *     | 0 | A |
     *     +---+---+
     */
    private fun getCodePos(code: Char): Position {
        return when (code) {
            'A' -> 3 to 2
            '0' -> 3 to 1
            '1' -> 2 to 0
            '2' -> 2 to 1
            '3' -> 2 to 2
            '4' -> 1 to 0
            '5' -> 1 to 1
            '6' -> 1 to 2
            '7' -> 0 to 0
            '8' -> 0 to 1
            '9' -> 0 to 2
            else -> throw IllegalArgumentException("Unsupported code $code")
        }
    }

    /**
     *     +---+---+
     *     | ^ | A |
     * +---+---+---+
     * | < | v | > |
     * +---+---+---+
     */
    private fun getPadPos(code: Char): Position {
        return when (code) {
            '^' -> 0 to 1
            'A' -> 0 to 2
            '<' -> 1 to 0
            'v' -> 1 to 1
            '>' -> 1 to 2
            else -> throw IllegalArgumentException("Unsupported pad code $code")
        }
    }


    override fun convert(file: String): Day202421Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day21().solve(copyResult = true, test = true)
    Day21().solve(copyResult = true)
}