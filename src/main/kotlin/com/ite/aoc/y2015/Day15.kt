package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines
import kotlin.math.max

private typealias Day201515Input = List<IngredientData>

data class IngredientData(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int,
)

class Day15 : AocDay<Day201515Input>(
    day = 15,
    year = 2015,
) {

    private data class Scale(
        var capacity: Int = 0,
        var durability: Int = 0,
        var flavor: Int = 0,
        var texture: Int = 0,
        var calories: Int = 0,
    )

    private val regex =
        """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (\d+)""".toRegex()

    override fun part1(entries: Day201515Input): Long {
        return score(0, entries, 100, Scale())
    }

    override fun part2(entries: Day201515Input): Long {
        return score(0, entries, 100, Scale(), 500)
    }

    private fun score(
        index: Int,
        entries: Day201515Input,
        q: Int,
        scale: Scale,
        exactCalories: Int? = null,
    ): Long {
        if (entries.size == index) {
            return when {
                exactCalories?.let { it == scale.calories } ?: true -> max(scale.capacity, 0).toLong() *
                        max(scale.durability, 0) * max(scale.flavor, 0) * max(scale.texture, 0)

                else -> 0
            }
        }
        val current = entries[index]
        return (0..q).maxOf {
            val currentScale = Scale(
                capacity = it * current.capacity + scale.capacity,
                durability = it * current.durability + scale.durability,
                flavor = it * current.flavor + scale.flavor,
                texture = it * current.texture + scale.texture,
                calories = it * current.calories + scale.calories,
            )
            score(index + 1, entries, q - it, currentScale, exactCalories)
        }
    }

    override fun convert(file: String): Day201515Input =
        file.mapLines { _, l ->
            regex.find(l)!!.groupValues.let { g ->
                IngredientData(
                    name = g[1],
                    capacity = g[2].toInt(),
                    durability = g[3].toInt(),
                    flavor = g[4].toInt(),
                    texture = g[5].toInt(),
                    calories = g[6].toInt(),
                )
            }
        }

}

fun main() {
    Day15().solve(copyResult = true, test = true)
    Day15().solve(copyResult = true)
}