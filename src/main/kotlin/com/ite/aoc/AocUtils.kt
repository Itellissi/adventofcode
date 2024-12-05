package com.ite.aoc

fun String.readFile() = AocUtils::class.java.getResource(this)?.readText()
    ?: throw IllegalArgumentException("Invalid file $this")

fun <T, R> String.foldInput(initial: R, accumulator: (R, String) -> R): R = readFile()
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

fun <T> List<List<T>>.forEachCell(consumer: (Int, Int, T) -> Nothing) = traverse(null) { i, j, c, s ->
    consumer(i, j, c)
}

fun Pair<Int, Int>.navigate(currentPos: Pair<Int, Int>): Pair<Int, Int> =
    Pair(currentPos.first + this.first, currentPos.second + this.second)

fun <T> Pair<Int, Int>.inRange(grid: List<List<T>>) =
    this.first in grid.indices && this.second in grid[this.first].indices

fun <T> List<List<T>>.atPos(pos: Pair<Int, Int>) = this[pos.first][pos.second]

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
}