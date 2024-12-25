package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201523Input = List<String>

class Day23 : AocDay<Day201523Input>(
    day = 23,
    year = 2015,
) {

    private val hlfRegex = """hlf ([ab])""".toRegex()
    private val tplRegex = """tpl ([ab])""".toRegex()
    private val incRegex = """inc ([ab])""".toRegex()
    private val jmpRegex = """jmp ([+-]\d+)""".toRegex()
    private val jieRegex = """jie ([ab]), ([+-]\d+)""".toRegex()
    private val jioRegex = """jio ([ab]), ([+-]\d+)""".toRegex()

    private data class Instruction(
        val actual: String,
        val operation: (MutableMap<String, Long>, Int) -> Int,
    )

    override fun part1(entries: Day201523Input): Long {
        val registry = mutableMapOf("a" to 0L, "b" to 0L)
        runProgram(entries, registry)
        return registry["b"]!!
    }

    override fun part2(entries: Day201523Input): Any? {
        val registry = mutableMapOf("a" to 1L, "b" to 0L)
        runProgram(entries, registry)
        return registry["b"]!!
    }

    private fun runProgram(
        entries: Day201523Input,
        registry: MutableMap<String, Long>
    ) {
        val instructions = parseInstructions(entries)
        var idx = 0
        while (idx in instructions.indices) {
            val inst = instructions[idx]
            idx = inst.operation(registry, idx)
        }
    }

    private fun parseInstructions(entries: Day201523Input): List<Instruction> = entries.map { i ->
        when {
            // hlf r sets register r to half its current value, then continues with the next instruction.
            hlfRegex.matches(i) -> Instruction(i) { register, index ->
                val key = hlfRegex.find(i)!!.groupValues[1]
                register[key] = register[key]!! / 2
                index + 1
            }
            // tpl r sets register r to triple its current value, then continues with the next instruction.
            tplRegex.matches(i) -> Instruction(i) { register, index ->
                val key = tplRegex.find(i)!!.groupValues[1]
                register[key] = register[key]!! * 3
                index + 1
            }
            // inc r increments register r, adding 1 to it, then continues with the next instruction.
            incRegex.matches(i) -> Instruction(i) { register, index ->
                val key = incRegex.find(i)!!.groupValues[1]
                register[key] = register[key]!! + 1
                index + 1
            }
            // jmp offset is a jump; it continues with the instruction offset away relative to itself.
            jmpRegex.matches(i) -> Instruction(i) { _, index ->
                val offset = jmpRegex.find(i)!!.groupValues[1].toInt()
                index + offset
            }
            // jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
            jieRegex.matches(i) -> Instruction(i) { registry, index ->
                val key = jieRegex.find(i)!!.groupValues[1]
                if (registry[key]!! % 2 == 0L) {
                    val offset = jieRegex.find(i)!!.groupValues[2].toInt()
                    index + offset
                } else index + 1
            }
            // jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
            jioRegex.matches(i) -> Instruction(i) { registry, index ->
                val key = jioRegex.find(i)!!.groupValues[1]
                if (registry[key]!! == 1L) {
                    val offset = jioRegex.find(i)!!.groupValues[2].toInt()
                    index + offset
                } else index + 1
            }

            else -> throw IllegalArgumentException("Invalid instruction $i")
        }
    }

    override fun convert(file: String): Day201523Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day23().solve(copyResult = true)
}