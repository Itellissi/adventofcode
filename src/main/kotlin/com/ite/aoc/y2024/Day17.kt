package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.readFile

data class Day202417Input(
    var a: Long,
    var b: Long,
    var c: Long,
    val program: List<Int>,
    var pointer: Int,
)

class Day17 : AocDay<Day202417Input>(
    day = 17,
    year = 2024,
) {

    override fun part1(entries: Day202417Input): List<Int> {
        val out = mutableListOf<Int>()
        runProgram(entries) { _, i ->
            out += i
            false
        }
        return out
    }

    override fun part2(entries: Day202417Input): Long {
        var idx = entries.program.size - 1
        var ranges = listOf(0L..7L)
        var found = false
        while (idx >= 0) {
            val targetDigit = entries.program[idx]
            val newRanges = mutableListOf<LongRange>()
            ranges.flatten().forEach { a ->
                val input = Day202417Input(a, entries.b, entries.c, entries.program, 0)
                runProgram(input) { _, v -> // callback on each output (_ : index, v : value to output)
                    if (v == targetDigit) {
                        newRanges += a * 8..(a * 8 + 7)
                        found = idx == 0
                    }
                    // stop on first program output (no need to continue)
                    true
                }
                if (found) return a
            }
            ranges = newRanges
            idx--
        }
        throw IllegalStateException("No solution found")
    }

    private fun runProgram(
        entries: Day202417Input,
        output: (Int, Int) -> Boolean,
    ): Int {
        var idx = 0
        while (entries.pointer in entries.program.indices) {
            when (entries.program[entries.pointer]) {
                0 -> adv(entries)
                1 -> bxl(entries)
                2 -> bst(entries)
                3 -> jnz(entries)
                4 -> bxc(entries)
                5 -> out(entries).also {
                    if (output(idx, it.toInt())) {
                        return@runProgram -1
                    }
                    idx++
                }

                6 -> bdv(entries)
                else -> cdv(entries)
            }
        }
        return idx
    }

    // 0
    private fun adv(input: Day202417Input): Long {
        val operand = combo(input.program[input.pointer + 1], input)
        return (input.a / pow2(operand)).also {
            input.a = it
            input.pointer += 2
        }
    }

    // 1
    private fun bxl(input: Day202417Input): Long {
        val operand = input.program[input.pointer + 1].toLong()
        return (input.b.xor(operand)).also {
            input.b = it
            input.pointer += 2
        }
    }

    // 2
    private fun bst(input: Day202417Input): Long {
        val operand = combo(input.program[input.pointer + 1], input)
        return (operand % 8).also {
            input.b = it
            input.pointer += 2
        }
    }

    // 3
    private fun jnz(input: Day202417Input) {
        val operand = input.program[input.pointer + 1]
        when (input.a) {
            0L -> input.pointer += 2
            else -> input.pointer = operand
        }
    }

    // 4
    private fun bxc(input: Day202417Input): Long {
        return input.b.xor(input.c).also {
            input.b = it
            input.pointer += 2
        }
    }

    // 5
    private fun out(input: Day202417Input): Long {
        val operand = combo(input.program[input.pointer + 1], input)
        return (operand % 8).also {
            input.pointer += 2
        }
    }

    // 6
    private fun bdv(input: Day202417Input): Long {
        val operand = combo(input.program[input.pointer + 1], input)
        return (input.a / pow2(operand)).also {
            input.b = it
            input.pointer += 2
        }
    }

    // 7
    private fun cdv(input: Day202417Input): Long {
        val operand = combo(input.program[input.pointer + 1], input)
        return (input.a / pow2(operand)).also {
            input.c = it
            input.pointer += 2
        }
    }


    private fun combo(operand: Int, input: Day202417Input): Long = when {
        operand < 4 -> operand.toLong()
        operand == 4 -> input.a
        operand == 5 -> input.b
        operand == 6 -> input.c
        else -> throw IllegalArgumentException("Invalid operand")
    }

    private val pow2Cache = mutableMapOf<Long, Long>()

    private fun pow2(e: Long): Long {
        return pow2Cache[e] ?: run {
            var result = 1L
            repeat((1..e).count()) { result *= 2L }
            return result.also {
                pow2Cache[e] = it
            }
        }
    }

    override fun convert(file: String): Day202417Input =
        file.readFile().lines().let { l ->
            val a = l.first { it.startsWith("Register A: ") }.substring("Register A: ".length).trim().toLong()
            val b = l.first { it.startsWith("Register B: ") }.substring("Register B: ".length).trim().toLong()
            val c = l.first { it.startsWith("Register C: ") }.substring("Register C: ".length).trim().toLong()

            val p = l.first { it.startsWith("Program: ") }
                .substring("Program:".length).trim().split(",")
                .map { it.toInt() }
                .toList()
            Day202417Input(a, b, c, p, 0)
        }

}

fun main() {
    // Day17().solve(copyResult = true, test = true)
    Day17().solve(copyResult = true)
}