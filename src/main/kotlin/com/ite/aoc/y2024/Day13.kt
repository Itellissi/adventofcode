package com.ite.aoc.y2024

import com.ite.aoc.*

private typealias Day202413Input = List<Game>

class Day13 : AocDay<Day202413Input>(
    day = 13,
    year = 2024,
) {

    private val aRegex = """Button A: X\+(\d+), Y\+(\d+)""".toRegex()
    private val bRegex = """Button B: X\+(\d+), Y\+(\d+)""".toRegex()
    private val pRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

    override fun part1(entries: Day202413Input): Long {
        return entries.sumOf { onlyCost(it, 0L to 0L) }
    }

    override fun part2(entries: Day202413Input): Long {
        return entries.sumOf { onlyCost(it, 10000000000000L to 10000000000000L) }
    }

    private fun onlyCost(game: Game, translation: Pair<Long, Long>): Long {
        val (xa, ya) = game.a
        val (xb, yb) = game.b
        val (xp, yp) = game.p.toLongPair() + translation

        // [m xa + n xb = xp] * ya
        // [m ya + n yb = yp] * xa

        // assuming input is safe enough that (xb * ya - yb * xa) != 0, meaning both equations are not equivalent
        val aCount = (xp * ya - yp * xa) / (xb * ya - yb * xa)
        val bCount = (xp - aCount * xb) / xa
        if ((bCount * xa + aCount * xb) == xp && (bCount * ya + aCount * yb) == yp) {
            return aCount + bCount * 3
        }
        return 0L
    }

    override fun convert(file: String): Day202413Input = mutableListOf<Game>().also { list ->
        val it = file.readFile().lines().iterator()
        while (it.hasNext()) {
            val aInput = it.next()
            val bInput = it.next()
            val pInput = it.next()
            list += Game(
                aRegex.find(aInput)!!.groups.let { g -> g[1]!!.value.toInt() to g[2]!!.value.toInt() },
                bRegex.find(bInput)!!.groups.let { g -> g[1]!!.value.toInt() to g[2]!!.value.toInt() },
                pRegex.find(pInput)!!.groups.let { g -> g[1]!!.value.toInt() to g[2]!!.value.toInt() },
            )
            if (it.hasNext()) it.next()
        }
    }

}

data class Game(
    val a: Position,
    val b: Position,
    val p: Position,
)

fun main() {
    Day13().solve(copyResult = true, test = true)
    Day13().solve(copyResult = true)
}