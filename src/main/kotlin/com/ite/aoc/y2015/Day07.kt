package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import java.util.LinkedList

private typealias Day201507Input = List<String>

class Day07 : AocDay<Day201507Input>(
    day = 7,
    year = 2015,
) {

    private data class Operation(
        val operands: List<String>,
        val target: String,
        val operation: (List<Int>) -> Int,
    )

    private val andRegex = """(\w+) AND (\w+) -> (\w+)""".toRegex()
    private val orRegex = """(\w+) OR (\w+) -> (\w+)""".toRegex()
    private val notRegex = """NOT (\w+) -> (\w+)""".toRegex()
    private val initRegex = """(\d+) -> (\w+)""".toRegex()
    private val affectRegex = """(\w+) -> (\w+)""".toRegex()
    private val rShiftRegex = """(\w+) RSHIFT (\w+) -> (\w+)""".toRegex()
    private val lShiftRegex = """(\w+) LSHIFT (\w+) -> (\w+)""".toRegex()

    private val numberRegex = """\d+""".toRegex()

    override fun part1(entries: Day201507Input): Int {
        val registry = mutableMapOf<String, Int>()
        val q = LinkedList<Operation>()
        buildInstructions(entries, q, registry)
        runInstructions(q, registry)
        return registry["a"]!!
    }

    override fun part2(entries: Day201507Input): Int {
        val q = LinkedList<Operation>()
        val registry = mutableMapOf<String, Int>()
        buildInstructions(entries, q, registry)
        registry["b"] = 956
        runInstructions(q, registry)
        return registry["a"]!!
    }

    private fun runInstructions(
        q: LinkedList<Operation>,
        registry: MutableMap<String, Int>,
    ) {
        while (!q.isEmpty()) {
            val op = q.poll()
            val operands = op.operands.map {
                when {
                    numberRegex.matches(it) -> it.toInt()
                    else -> registry[it]
                }
            }
            if (operands.any { it == null }) {
                q.addLast(op)
            } else {
                val result = op.operation(operands.map { it!! })
                registry[op.target] = result
            }
        }
    }

    private fun buildInstructions(
        entries: Day201507Input,
        q: LinkedList<Operation>,
        registry: MutableMap<String, Int>
    ) {
        entries.map { instruction ->
            when {
                andRegex.matches(instruction) -> andRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0].and(i[1]) }
                    )
                }

                orRegex.matches(instruction) -> orRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0].or(i[1]) }
                    )
                }

                notRegex.matches(instruction) -> notRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0].inv() }
                    )
                }

                rShiftRegex.matches(instruction) -> rShiftRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0].shr(i[1]) }
                    )
                }

                lShiftRegex.matches(instruction) -> lShiftRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0].shl(i[1]) }
                    )
                }

                initRegex.matches(instruction) -> initRegex.find(instruction)!!.also {
                    registry[it.groupValues[2]] = it.groupValues[1].toInt()
                }

                affectRegex.matches(instruction) -> affectRegex.find(instruction)!!.also {
                    q += Operation(
                        operands = it.groupValues.dropLast(1).drop(1),
                        target = it.groupValues.last(),
                        operation = { i -> i[0] }
                    )
                }

                else -> throw IllegalArgumentException("Unsupported instruction $instruction")
            }
        }
    }

    override fun convert(file: String): Day201507Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day07().solve(copyResult = true)
}