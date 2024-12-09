package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.Position
import com.ite.aoc.mapLines
import kotlin.math.min

private typealias Day202409Input = List<Char>

class Day09 : AocDay<Day202409Input>(
    day = 9,
    year = 2024,
) {

    override fun part1(entries: Day202409Input): Long {
        val memory = extractMemory(entries)
        var j = memory.size - 1
        var i = 1
        val newMem = mutableListOf(memory[0])
        while (i < j) {
            while (memory[i].second == 0) {
                i += 2
                newMem += memory[i - 1]
            }
            while (memory[j].second == 0) j -= 2
            while (i < j && memory[i].second > 0) {
                val free = min(memory[i].second, memory[j].second)
                memory[i] = memory[i].first to memory[i].second - free
                memory[j] = memory[j].first to memory[j].second - free
                newMem += memory[j].first to free
                if (memory[i].second > 0) j -= 2
            }
        }
        // 0099811188827773336446555566
        var offset = 0
        return newMem.sumOf { p ->
            val (idx, count) = p
            offset += count
            return@sumOf idx * count.toLong() * (2 * offset - count - 1) / 2
        }
    }

    override fun part2(entries: Day202409Input): Long? {
        val memory = extractMemory(entries)
        var j = memory.size - 1
        while (j > 0) {
            val file = memory[j]
            var i = 1
            while (i < j && memory[i].second < file.second) {
                i += 2
            }
            if (i < j) {
                memory[j] = -1 to file.second
                val remaining = memory[i].second - file.second
                memory[i] = -1 to 0
                memory.add(i + 1, -1 to remaining)
                memory.add(i + 1, file)
            }
            j -= 2
        }
        val newMem = memory.filter { it.second != 0 }
        // 0099811188827773336446555566
        // 00 99 2 111 777.44.333....5555.6666.....8888..
        var offset = 0L
        return newMem.sumOf { p ->
            val (idx, count) = p
            offset += count
            return@sumOf if (idx != -1) idx * count.toLong() * (2 * offset - count - 1) / 2 else 0
        }
    }

    private fun extractMemory(entries: Day202409Input): MutableList<Pair<Int, Int>> {
        val memory = entries.mapIndexed { i, c ->
            when {
                i % 2 == 0 -> i / 2 to c.digitToInt()
                else -> -1 to c.digitToInt()
            }
        }.toMutableList()
        return memory
    }

    override fun convert(file: String): Day202409Input =
        file.mapLines { _, l -> l.toCharArray().toList() }.first()

}

fun main() {
    Day09().solve(copyResult = true, test = true)
    Day09().solve(copyResult = true)
}