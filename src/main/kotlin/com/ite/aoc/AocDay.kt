package com.ite.aoc

abstract class AocDay(
    year: Int,
    day: Int,
) {
    protected val file = "/input/$year/day${day.toString().padStart(2, '0')}.txt"

    abstract fun part1()

    abstract fun part2()

    fun solve() {
        println("#".repeat(40))
        println(" Part1 ".padStart(25, ' ').padEnd(50, ' '))
        println("#".repeat(40))
        part1()
        println("#".repeat(40))
        println(" Part2 ".padStart(25, ' ').padEnd(40, ' '))
        println("#".repeat(40))
        part2()
    }
}