package com.ite.aoc

fun String.readFile() = AocDay::class.java.getResource(this)?.readText()
    ?: throw IllegalArgumentException("Invalid file $this")

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