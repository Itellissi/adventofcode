package com.ite.aoc

import kotlin.math.abs

fun String.readFile() = AocUtils::class.java.getResource(this)?.readText()
    ?: throw IllegalArgumentException("Invalid file $this")

fun <R> String.foldInput(initial: R, accumulator: (R, String) -> R): R = readFile()
    .lines()
    .fold(initial, accumulator)

fun <T> String.mapLines(mapper: (Int, String) -> T): List<T> = readFile()
    .lines()
    .mapIndexed(mapper)

fun <T, R> List<List<T>>.traverse(initial: R, accumulator: (Int, Int, T, R) -> R): R {
    var result = initial
    this.forEachIndexed { i, r ->
        r.forEachIndexed { j, c ->
            result = accumulator(i, j, c, result)
        }
    }
    return result
}

fun <T> List<List<T>>.traverseWithSum(mapper: (Int, Int, T) -> Long): Long = traverse(0) { i, j, c, s ->
    s + mapper(i, j, c)
}

fun <T> List<List<T>>.forEachPosition(consumer: (Position, T) -> Unit) = traverse(null) { i, j, c, s ->
    consumer(i to j, c)
    return@traverse null
}

fun <T> List<List<T>>.forEachCell(consumer: (Int, Int, T) -> Unit) = forEachPosition { p, c ->
    consumer(p.first, p.second, c)
}

fun <T> List<List<T>>.printGrid(marker: (Int, Int, T) -> String = { _, _, c -> c.toString() }) =
    forEachCell { i, j, c ->
        print(marker(i, j, c))
        if (j == this[i].size - 1) {
            println()
        }
    }

fun Position.navigate(currentPos: Position): Position =
    Pair(currentPos.first + this.first, currentPos.second + this.second)

fun Position.negate(): Position = Pair(-this.first, -this.second)

fun <T> Position.inRange(grid: List<List<T>>) =
    this.first in grid.indices && this.second in grid[this.first].indices

fun <T> List<List<T>>.atPos(pos: Position) = this[pos.first][pos.second]

typealias Position = Pair<Int, Int>

class AocUtils {

    object Directions {
        val NW = -1 to -1
        val N = -1 to 0
        val NE = -1 to 1
        val E = 0 to 1
        val SE = 1 to 1
        val S = 1 to 0
        val SW = 1 to -1
        val W = 0 to -1

        val ALL = listOf(NW, N, NE, E, SE, S, SW, W)
    }

    object Math {

        fun gcd(l: Int, r: Int): Long = gcd(l.toLong(), r.toLong())

        fun gcd(l: Long, r: Long): Long {
            var n1 = abs(l)
            var n2 = abs(r)
            while (n1 != n2) {
                if (n1 > n2)
                    n1 -= n2
                else
                    n2 -= n1
            }
            return n1
        }

        fun ppcm(l: Int, r: Int): Long = ppcm(l.toLong(), r.toLong())

        fun ppcm(l: Long, r: Long): Long = ocm.ite.adventofcode.AocUtils.ppcm(l, r)

        fun areInLine(a: Position, b: Position, c: Position): Boolean {
            val area =
                a.first * (b.second - c.second) + b.first * (c.second - a.second) + c.first * (a.second - b.second)
            return area == 0
        }

    }
}