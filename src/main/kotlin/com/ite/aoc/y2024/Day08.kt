package com.ite.aoc.y2024

import com.ite.aoc.*

private typealias Day202408Input = List<MutableList<Char>>

class Day08 : AocDay<Day202408Input>(
    day = 8,
    year = 2024,
) {

    override fun part1(entries: Day202408Input): Int {
        val locations = mutableSetOf<Position>()
        val antennas = getAntennaLocations(entries)
        antennas.values.forEach { v ->
            v.forEachIndexed { i, l ->
                ((i + 1)..<v.size).forEach { j ->
                    val r = v[j]
                    val v1 = l.first - r.first to l.second - r.second
                    val v2 = v1.negate()
                    val x1 = v1.navigate(l)
                    val x2 = v2.navigate(r)
                    if (x1.inRange(entries)) locations += x1
                    if (x2.inRange(entries)) locations += x2
                }
            }
        }
        return locations.size
    }

    override fun part2(entries: Day202408Input): Int = part2Optimized(entries)

    private fun part2Optimized(entries: Day202408Input): Int {
        val locations = mutableSetOf<Position>()
        val antennas = getAntennaLocations(entries)
        antennas.values.forEach { v ->
            v.forEachIndexed { i, l ->
                ((i + 1)..<v.size).forEach { j ->
                    val r = v[j]
                    val v1 = l.first - r.first to l.second - r.second
                    val v2 = v1.negate()
                    var x1 = v1.navigate(l)
                    var x2 = l
                    while (x1.inRange(entries)) {
                        locations += x1
                        x1 = v1.navigate(x1)
                    }
                    while (x2.inRange(entries)) {
                        locations += x2
                        x2 = v2.navigate(x2)
                    }
                }
            }
        }
        return locations.size
    }

    private fun part2BF(entries: Day202408Input): Int {
        val locations = mutableSetOf<Position>()
        val antennas = getAntennaLocations(entries)
        entries.forEachPosition { p, c ->
            when {
                c == '.' -> antennas.values.forEach { v ->
                    v.indices.takeWhile { !locations.contains(p) }
                        .forEach { i ->
                            ((i + 1)..<v.size)
                                .takeWhile { !locations.contains(p) }
                                .firstOrNull { AocUtils.Math.areInLine(v[it], v[i], p) }
                                ?.let { locations += p }
                        }
                }

                else -> locations.add(p)
            }
        }
        return locations.size
    }

    private fun getAntennaLocations(entries: Day202408Input): Map<Char, List<Position>> {
        val result = mutableMapOf<Char, List<Position>>()
        entries.forEachCell { i, j, c ->
            if (c != '.') result.compute(c) { _, old ->
                (old ?: listOf()) + (i to j)
            }
        }
        return result
    }

    override fun convert(file: String): Day202408Input =
        file.mapLines { _, l -> l.toCharArray().toMutableList() }

}

fun main() {
    Day08().solve(copyResult = true)
}