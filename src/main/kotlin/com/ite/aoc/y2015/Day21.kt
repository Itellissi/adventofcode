package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import kotlin.math.max
import kotlin.math.min

data class Day201521Input(
    val hitPoints: Int,
    val damage: Int,
    val armor: Int,
)

class Day21 : AocDay<Day201521Input>(
    day = 21,
    year = 2015,
    withFile = false,
) {

    private data class Item(
        val cost: Int,
        val damage: Int,
        val armor: Int,
    )

    /**
     * Weapons:    Cost  Damage  Armor
     * Dagger        8     4       0
     * Shortsword   10     5       0
     * Warhammer    25     6       0
     * Longsword    40     7       0
     * Greataxe     74     8       0
     */
    private val weapons = listOf(
        "Dagger" to Item(8, 4, 0),
        "Shortsword" to Item(10, 5, 0),
        "Warhammer" to Item(25, 6, 0),
        "Longsword" to Item(40, 7, 0),
        "Greataxe" to Item(74, 8, 0),
    )

    /**
     * Armor:      Cost  Damage  Armor
     * Leather      13     0       1
     * Chainmail    31     0       2
     * Splintmail   53     0       3
     * Bandedmail   75     0       4
     * Platemail   102     0       5
     */
    private val armors = listOf(
        "Leather" to Item(13, 0, 1),
        "Chainmail" to Item(31, 0, 2),
        "Splintmail" to Item(53, 0, 3),
        "Bandedmail" to Item(75, 0, 4),
        "Platemail" to Item(102, 0, 5),
    )

    /**
     * Rings:      Cost  Damage  Armor
     * Damage +1    25     1       0
     * Damage +2    50     2       0
     * Damage +3   100     3       0
     * Defense +1   20     0       1
     * Defense +2   40     0       2
     * Defense +3   80     0       3
     */
    private val rings = listOf(
        "Damage +1" to Item(25, 1, 0),
        "Damage +2" to Item(50, 2, 0),
        "Damage +3" to Item(100, 3, 0),
        "Defense +1" to Item(20, 0, 1),
        "Defense +2" to Item(40, 0, 2),
        "Defense +2" to Item(80, 0, 3),
    )

    override fun part1(entries: Day201521Input): Any? {
        var minCost = Int.MAX_VALUE
        simulateFights(entries) { outcome, stats -> if (outcome) minCost = min(stats.cost, minCost) }
        return minCost
    }

    override fun part2(entries: Day201521Input): Any? {
        var maxCost = 0
        simulateFights(entries) { outcome, stats -> if (!outcome) maxCost = max(stats.cost, maxCost) }
        return maxCost
    }

    private fun simulateFights(boss: Day201521Input, onFightOutCome: (Boolean, Item) -> Unit) {
        weapons.map { it.second }.forEach { w ->
            var stats = computeSetStats(setOf(w))
            onFightOutCome(
                winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                stats
            )
            armors.map { it.second }.forEach { a ->
                stats = computeSetStats(setOf(w, a))
                onFightOutCome(
                    winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                    stats
                )
                rings.map { it.second }.forEachIndexed { i, r1 ->
                    stats = computeSetStats(setOf(w, a, r1))
                    onFightOutCome(
                        winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                        stats
                    )
                    (i + 1..<rings.size).forEach { j ->
                        val r2 = rings[j].second
                        stats = computeSetStats(setOf(w, a, r1, r2))
                        onFightOutCome(
                            winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                            stats
                        )
                    }
                }
            }
            rings.map { it.second }.forEachIndexed { i, r1 ->
                stats = computeSetStats(setOf(w, r1))
                onFightOutCome(
                    winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                    stats
                )
                (i + 1..<rings.size).forEach { j ->
                    val r2 = rings[j].second
                    stats = computeSetStats(setOf(w, r1, r2))
                    onFightOutCome(
                        winsFight(Day201521Input(100, stats.damage, stats.armor), boss),
                        stats
                    )
                }
            }
        }
    }

    private fun computeSetStats(set: Set<Item>) = Item(
        cost = set.sumOf { it.cost },
        damage = set.sumOf { it.damage },
        armor = set.sumOf { it.armor }
    )

    private fun winsFight(player: Day201521Input, boss: Day201521Input): Boolean {
        val bossActualDamage = max(1, boss.damage - player.armor)
        val playerActualDamage = max(1, player.damage - boss.armor)

        val playerTurnsToWin =
            (boss.hitPoints / playerActualDamage) + if (boss.hitPoints % playerActualDamage != 0) 1 else 0
        val bossTurnsToWin =
            (player.hitPoints / bossActualDamage) + if (player.hitPoints % bossActualDamage != 0) 1 else 0
        return playerTurnsToWin <= bossTurnsToWin
    }

    override fun convert(file: String): Day201521Input = Day201521Input(
        hitPoints = 109,
        damage = 8,
        armor = 2,
    )

}

fun main() {
    Day21().solve(copyResult = true)
}