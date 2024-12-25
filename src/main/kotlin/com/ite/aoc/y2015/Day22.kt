package com.ite.aoc.y2015

import com.ite.aoc.AocDay
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

data class Day201522Input(
    val hp: Int,
    val damage: Int,
)

class Day22 : AocDay<Day201522Input>(
    day = 22,
    year = 2015,
    withFile = false,
) {

    private data class CharacterStats(
        var hp: Int,
        var armor: Int,
        var damage: Int,
        var mana: Int,
    )

    private data class ActiveEffect(
        val type: String,
        val onTurn: (CharacterStats, CharacterStats) -> Unit,
        val onEffectEnd: (CharacterStats, CharacterStats) -> Unit,
        var remaining: Int,
    )


    private data class Spell(
        val mana: Int,
        val duration: Int,
        val onEffectStart: (CharacterStats, CharacterStats) -> Unit = { _, _ -> },
        val onTurn: (CharacterStats, CharacterStats) -> Unit = { _, _ -> },
        val onEffectEnd: (CharacterStats, CharacterStats) -> Unit = { _, _ -> },
    )

    private val spells = listOf(
        // Magic Missile costs 53 mana. It instantly does 4 damage.
        "Magic Missile" to Spell(
            mana = 53,
            onEffectStart = { player, _ -> player.mana -= 53 },
            onTurn = { _, boss -> boss.hp -= 4 },
            duration = 1,
        ),
        // Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
        "Drain" to Spell(
            mana = 73,
            onEffectStart = { player, _ -> player.mana -= 73 },
            onTurn = { player, boss -> boss.hp -= 2; player.hp += 2 },
            duration = 1,
        ),
        // Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
        "Shield" to Spell(
            mana = 113,
            onEffectStart = { player, _ ->
                player.armor += 7
                player.mana -= 113
            },
            onEffectEnd = { player, _ -> player.armor -= 7 },
            duration = 6,
        ),
        // Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
        "Poison" to Spell(
            mana = 173,
            onEffectStart = { player, _ -> player.mana -= 173 },
            onTurn = { _, boss -> boss.hp -= 3 },
            duration = 6,
        ),
        // Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
        "Recharge" to Spell(
            mana = 229,
            onEffectStart = { player, _ -> player.mana -= 229 },
            onTurn = { player, _ -> player.mana += 101 },
            duration = 5,
        ),
    ).sortedBy { it.second.mana }


    override fun part1(entries: Day201522Input): Int {
        val player = CharacterStats(
            hp = 50,
            armor = 0,
            damage = 0,
            mana = 500,
        )
        val boss = CharacterStats(
            hp = entries.hp,
            damage = entries.damage,
            mana = 0,
            armor = 0,
        )
        return findLeastMana(player, boss, mutableListOf(), true)
    }

    override fun part2(entries: Day201522Input): Any? {
        val player = CharacterStats(
            hp = 50,
            armor = 0,
            damage = 0,
            mana = 500,
        )
        val boss = CharacterStats(
            hp = entries.hp,
            damage = entries.damage,
            mana = 0,
            armor = 0,
        )
        return findLeastMana(
            player,
            boss,
            mutableListOf(),
            true,
            hardMode = true
        )
    }

    private fun findLeastMana(
        player: CharacterStats,
        boss: CharacterStats,
        activeEffects: MutableList<ActiveEffect>,
        isPlayerTurn: Boolean,
        manaCost: Int = 0,
        minCost: AtomicInteger = AtomicInteger(Int.MAX_VALUE),
        moves: List<String> = listOf(),
        hardMode: Boolean = false,
    ): Int {
        if (manaCost >= minCost.get()) {
            return Int.MAX_VALUE
        }
        if (hardMode && isPlayerTurn) {
            player.hp--
            if (player.hp == 0) {
                return Int.MAX_VALUE
            }
        }
        activeEffects.forEach { effect ->
            effect.onTurn(player, boss)
            effect.remaining--
            if (effect.remaining == 0) effect.onEffectEnd(player, boss)
        }
        when {
            boss.hp <= 0 -> return minCost.also { it.set(manaCost) }.get()
        }
        activeEffects.removeIf { it.remaining == 0 }
        return when {
            isPlayerTurn -> {
                val availableSpells = spells
                    .filter { !activeEffects.any { e -> e.type == it.first } }
                    .filter { it.second.mana <= player.mana }
                availableSpells.minOfOrNull { spell ->
                    val copyPlayer = player.copy()
                    val copyBoss = boss.copy()
                    val effect = ActiveEffect(
                        type = spell.first,
                        remaining = spell.second.duration,
                        onTurn = spell.second.onTurn,
                        onEffectEnd = spell.second.onEffectEnd,
                    )
                    spell.second.onEffectStart(copyPlayer, copyBoss)
                    findLeastMana(
                        copyPlayer,
                        copyBoss,
                        (activeEffects.map { it.copy() } + effect).toMutableList(),
                        false,
                        spell.second.mana + manaCost,
                        minCost,
                        moves + spell.first,
                        hardMode
                    )
                } ?: Int.MAX_VALUE
            }

            else -> {
                val copyBoss = boss.copy()
                val copyPlayer = player.copy(
                    hp = player.hp - max(1, boss.damage - player.armor)
                )
                when {
                    copyPlayer.hp <= 0 -> return Int.MAX_VALUE
                    else -> findLeastMana(
                        copyPlayer,
                        copyBoss,
                        activeEffects.map { it.copy() }.toMutableList(),
                        true,
                        manaCost,
                        minCost,
                        moves,
                        hardMode
                    )
                }
            }
        }
    }

    override fun convert(file: String): Day201522Input = Day201522Input(
        hp = 71,
        damage = 10,
    )

}

fun main() {
    Day22().solve(copyResult = true)
}