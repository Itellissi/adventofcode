package com.ite.aoc.y2024

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

data class Day202424Input(
    val operations: List<String>,
    val initialValues: MutableMap<String, Int>,
)

class Day24 : AocDay<Day202424Input>(
    day = 24,
    year = 2024,
) {

    private data class Operation(
        val p1: String,
        val p2: String,
        val op: String,
        var res: String,
    )

    private val operationRegex = """(\w+) (AND|OR|XOR) (\w+) -> (\w+)""".toRegex()
    private val initRegex = """(\w+): (\d)""".toRegex()

    override fun part1(entries: Day202424Input): Long {
        val operations = convertOperations(entries)
        val registry = entries.initialValues
        perform(operations, entries.initialValues)
        return registry
            .filter { it.key.startsWith("z") }
            .map { it.key to it.value }
            .sortedBy { it.first }
            .reversed()
            .map { it.second }
            .fold(0) { s, i -> s * 2 + i }
    }

    override fun part2(entries: Day202424Input): String {
        val operations = convertOperations(entries)

        val inputs = operations.filter { it.p1.startsWith("x") || it.p2.startsWith("x") }
            .map { if (it.p1.startsWith("y")) it.copy(p1 = it.p2, p2 = it.p1) else it }
            .sortedWith(compareBy(Operation::p1, Operation::op))
            .reversed()
            .toMutableList()

        var anOp = inputs.removeLast()
        var xorOp = inputs.removeLast()
        var c = anOp.res

        val swaps = mutableSetOf<String>()
        while (swaps.size < 8 && inputs.isNotEmpty()) {
            anOp = inputs.removeLast()
            xorOp = inputs.removeLast()

            val suffix = anOp.p1.substring(1)
            val expectedRes = "z$suffix"

            var w1 = xorOp.res
            val w2 = anOp.res

            // c00 : carry from previous
            // Ripple carry adder
            //
            // x01 XOR y01 -> w1
            // x01 AND y01 -> w2
            //
            // c00 XOR w1 -> z01 (w3)
            // w1 AND c00 -> w4
            // w2 OR w4 -> c01 (carry to next)
            var w3Op = operations.firstOrNull {
                it.op == "XOR" && (it.p1 to it.p2).let { p -> p == (c to w1) || p == (w1 to c) }
            }
            var w4Op = operations.firstOrNull {
                it.op == "AND" && (it.p1 to it.p2).let { p -> p == (c to w1) || p == (w1 to c) }
            }
            if (w3Op == null && w4Op == null) {
                val w1Links = operations.filter { it.p1 == w1 || it.p2 == w1 }
                val cLinks = operations.filter { it.p1 == c || it.p2 == c }
                when {
                    // links involving wire1
                    w1Links.size == 2 -> {
                        c = swapFix(w1Links, w1, c, swaps, operations)
                        w3Op = w1Links.first { it.op == "XOR" }
                        w4Op = w1Links.first { it.op == "AND" }
                    }

                    cLinks.size == 2 -> {
                        w1 = swapFix(cLinks, c, w1, swaps, operations)
                        w3Op = cLinks.first { it.op == "XOR" }
                        w4Op = cLinks.first { it.op == "AND" }
                    }

                    else -> throw IllegalStateException("Unexpected")
                }
            }
            val resultWire = w3Op!!.res
            if (resultWire != expectedRes) {
                // w3 should point to corresponding z value
                val op1 = operations.first { it.res == expectedRes }
                val op2 = w3Op
                swaps += resultWire
                swaps += expectedRes
                op1.res = resultWire
                op2.res = expectedRes
            }
            val w4 = w4Op!!.res
            val w5Op = operations.firstOrNull {
                it.op == "OR" && (it.p1 to it.p2).let { p -> p == (w2 to w4) || p == (w4 to w2) }
            }
            if (w5Op == null) {

                val w4Links = operations.filter { it.op == "OR" && (it.p1 == w4 || it.p2 == w4) }
                val w2Links = operations.filter { it.op == "OR" && (it.p1 == w2 || it.p2 == w2) }

                c = when {
                    w2Links.isEmpty() -> {
                        // w2 is wrong
                        swapFix(w4Links, w4, w2, swaps, operations)
                        w4Links.first().res
                    }

                    else -> {
                        // w4 is wrong
                        swapFix(w2Links, w2, w4, swaps, operations)
                        w2Links.first().res
                    }
                }

            } else {
                c = w5Op.res
            }
        }

        return swaps.sorted().joinToString(",")
    }

    private fun swapFix(
        validOperations: List<Operation>,
        determinantWire: String,
        badWire: String,
        swaps: MutableSet<String>,
        operations: MutableList<Operation>
    ): String {
        val actualWire = validOperations.flatMap { listOf(it.p1, it.p2) }
            .filter { it != determinantWire }
            .toSet()
            .also { check(it.size == 1) }
            .first()
        swaps += actualWire
        swaps += badWire
        val op1 = operations.first { it.res == actualWire }
        val op2 = operations.first { it.res == badWire }
        op1.res = badWire
        op2.res = actualWire
        return actualWire
    }

    private fun convertOperations(entries: Day202424Input): MutableList<Operation> {
        val operations = entries.operations.map {
            operationRegex.find(it)!!.groupValues.let { g ->
                Operation(
                    g[1],
                    g[3],
                    g[2],
                    g[4],
                )
            }
        }.toMutableList()
        return operations
    }

    private fun perform(
        operations: MutableList<Operation>,
        registry: MutableMap<String, Int>,
    ) {
        while (operations.isNotEmpty()) {
            val current = operations.removeFirst()
            val op1 = current.p1
            val op2 = current.p2
            when {
                !registry.containsKey(op1) || !registry.containsKey(op2) -> operations.addLast(current)
                current.op == "AND" -> registry[current.res] = registry[op1]!!.and(registry[op2]!!)
                current.op == "OR" -> registry[current.res] = registry[op1]!!.or(registry[op2]!!)
                current.op == "XOR" -> registry[current.res] = registry[op1]!!.xor(registry[op2]!!)
            }
        }
    }

    override fun convert(file: String): Day202424Input =
        file.mapLines { _, l -> l }.let { lines ->
            Day202424Input(
                operations = lines.filter { it.matches(operationRegex) },
                initialValues = lines.filter { it.matches(initRegex) }
                    .map { initRegex.find(it)!!.groupValues.let { g -> g[1] to g[2].toInt() } }
                    .associate { it }
                    .toMutableMap()
            )
        }

}

fun main() {
    // Day24().solve(copyResult = true, test = true)
    Day24().solve(copyResult = true)
}