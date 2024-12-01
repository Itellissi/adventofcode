package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import ocm.ite.adventofcode.AocUtils
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors
import kotlin.math.abs

class Day01 : AocDay<List<Pair<Int, Int>>>(
    day = 1,
    year = 2024,
) {
    override fun part1(entries: List<Pair<Int, Int>>): Any? {
        val left = entries.map { it.first }.toList().sorted()
        val right = entries.map { it.second }.toList().sorted()
        var sum = 0;
        for (i in entries.indices) {
            sum += abs(left[i] - right[i])
        }
        return sum
    }

    override fun part2(entries: List<Pair<Int, Int>>): Any? {
        val left = entries.map { it.first }.toSet()
        val occ = entries.map { it.second }.stream()
            .collect(Collectors.groupingBy { it })
            .map { it.key to it.value.size }
            .toMap()
        val sum = AtomicLong(0)
        left.forEach { occ[it]?.let { oc -> sum.addAndGet(it * oc.toLong()) } }
        return sum
    }

    override fun convert(file: String): List<Pair<Int, Int>> =
        AocUtils.mapLines(file) { _, l -> l.split("   ").let { it[0].toInt() to it[1].toInt() } }
}

fun main() {
    Day01().solve()
}