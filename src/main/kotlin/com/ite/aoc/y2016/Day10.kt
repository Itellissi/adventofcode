package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201610Input = List<String>

class Day10 : AocDay<Day201610Input>(
    day = 10,
    year = 2016,
) {

    private val initRegex = """value (\d+) goes to bot (\d+)""".toRegex()
    private val handOverRegex = """bot (\d+) gives low to (output|bot) (\d+) and high to (output|bot) (\d+)""".toRegex()

    override fun part1(entries: Day201610Input): Any? {
        val bots = mutableMapOf<Int, MutableList<Int>>()
        val outputs = mutableMapOf<Int, MutableList<Int>>()
        runInstructions(entries, bots, outputs)
        return bots.filter { it.value.contains(61) && it.value.contains(17) }
            .map { it.key }
            .first()
    }

    override fun part2(entries: Day201610Input): Any? {
        val bots = mutableMapOf<Int, MutableList<Int>>()
        val outputs = mutableMapOf<Int, MutableList<Int>>()
        runInstructions(entries, bots, outputs)
        return outputs[0]!!.first() * outputs[1]!!.first() * outputs[2]!!.first()
    }

    private fun runInstructions(
        entries: Day201610Input,
        bots: MutableMap<Int, MutableList<Int>>,
        outputs: MutableMap<Int, MutableList<Int>>
    ) {
        val handovers = mutableListOf<List<String>>()
        entries.forEach { i ->
            when {
                initRegex.matches(i) -> initRegex.find(i)!!.groupValues.let { g ->
                    val botId = g[2].toInt()
                    val assignments = bots[botId] ?: mutableListOf<Int>().also { bots[botId] = it }
                    assignments += g[1].toInt()
                }

                handOverRegex.matches(i) -> handovers += handOverRegex.find(i)!!.groupValues

                else -> throw IllegalArgumentException("Invalid line")
            }
        }
        while (handovers.isNotEmpty()) {
            val current = handovers.removeFirst()
            val botId = current[1].toInt()
            val currValues = bots[botId]
            if ((currValues?.size ?: 0) < 2) handovers.addLast(current)
            else {
                val lType = current[2]
                val lValue = current[3].toInt()
                val hType = current[4]
                val hValue = current[5].toInt()
                val l = currValues!!.min()
                val h = currValues.max()
                when (lType) {
                    "bot" -> (bots[lValue] ?: mutableListOf<Int>().also { bots[lValue] = it }) += l
                    "output" -> (outputs[lValue] ?: mutableListOf<Int>().also { outputs[lValue] = it }) += l
                }
                when (hType) {
                    "bot" -> (bots[hValue] ?: mutableListOf<Int>().also { bots[hValue] = it }) += h
                    "output" -> (outputs[hValue] ?: mutableListOf<Int>().also { outputs[hValue] = it }) += h
                }
            }
        }
    }

    override fun convert(file: String): Day201610Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day10().solve(copyResult = true)
}