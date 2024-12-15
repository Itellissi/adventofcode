package com.ite.aoc.y2024

import com.ite.aoc.*
import java.awt.Color

data class Day202415Input(
    val grid: List<List<Char>>,
    val movements: List<Char>,
)

data class Element(
    val type: Char
)

class Day15 : AocDay<Day202415Input>(
    day = 15,
    year = 2024,
) {
    private companion object {
        const val BLOCK = 'O'
        const val ROBOT = '@'
        const val WALL = '#'
        const val SPACE = '.'
        const val REFRESH_RATE = 10L
        const val visualize = false
    }

    private val movementsMap = mapOf(
        '^' to AocUtils.Directions.N,
        'v' to AocUtils.Directions.S,
        '>' to AocUtils.Directions.E,
        '<' to AocUtils.Directions.W,
    )

    override fun part1(entries: Day202415Input): Long {
        val gridCopy = entries.grid.map { it.toMutableList() }
        var current: Position = -1 to -1
        gridCopy.forEachCell { i, j, c -> if (c == ROBOT) current = i to j }
        val blockColor = Color(200, 120, 0)
        val viz = (if (visualize) gridCopy else null)?.visualizeNoCopy(
            cellSize = 15,
            borderColor = Color(210, 210, 210),
            refreshDelay = REFRESH_RATE,
        ) { _, c ->
            when (c) {
                '#' -> Color.BLACK
                'O' -> blockColor
                '@' -> Color(0, 125, 245)
                '.' -> Color.WHITE
                else -> throw IllegalArgumentException("Unsupported")
            }
        }

        entries.movements.map { it to movementsMap[it]!! }
            .forEach { pair ->
                val d = pair.second
                viz?.refresh(wait = true)
                val next = current + d
                when (gridCopy.atPos(next)) {
                    WALL -> {}
                    SPACE -> {
                        gridCopy[current.first][current.second] = SPACE
                        gridCopy[next.first][next.second] = ROBOT
                        current = next
                    }

                    BLOCK -> {
                        var b = next + d
                        while (gridCopy.atPos(b) == BLOCK) {
                            b += d
                        }
                        if (gridCopy.atPos(b) == SPACE) {
                            gridCopy[current.first][current.second] = SPACE
                            gridCopy[next.first][next.second] = ROBOT
                            current = next
                            gridCopy[b.first][b.second] = BLOCK
                        }
                    }
                }
            }
        return gridCopy.traverseWithSum { i, j, c -> if (c == BLOCK) i * 100L + j else 0L }
    }

    override fun part2(entries: Day202415Input): Long {
        val expandedGrid = entries.grid.map { l ->
            l.flatMap {
                when (it) {
                    ROBOT -> listOf(ROBOT, SPACE)
                    BLOCK -> listOf('[', ']')
                    else -> listOf(it, it)
                }
            }.toMutableList()
        }
        var current: Position = -1 to -1
        expandedGrid.forEachCell { i, j, c -> if (c == ROBOT) current = i to j }

        val blockColor = Color(200, 120, 0)
        val viz = (if (visualize) expandedGrid else null)?.visualizeNoCopy(
            cellSize = 15,
            borderColor = Color(210, 210, 210),
            refreshDelay = REFRESH_RATE,
        ) { _, c ->
            when (c) {
                '#' -> Color.BLACK
                '[' -> blockColor
                ']' -> blockColor.brighter()
                '@' -> Color(0, 125, 245)
                '.' -> Color.WHITE
                else -> throw IllegalArgumentException("Unsupported")
            }
        }

        entries.movements.map { it to movementsMap[it]!! }
            .forEach { pair ->
                val d = pair.second
                viz?.refresh(wait = true)
                val next = current + d
                when (expandedGrid.atPos(next)) {
                    WALL -> {}
                    SPACE -> {
                        expandedGrid[current.first][current.second] = SPACE
                        expandedGrid[next.first][next.second] = ROBOT
                        current = next
                    }

                    '[', ']' -> {
                        if (d == AocUtils.Directions.E || d == AocUtils.Directions.W) {
                            var b = next + d
                            while (expandedGrid.atPos(b) == '[' || expandedGrid.atPos(b) == ']') {
                                b += d
                            }
                            if (expandedGrid.atPos(b) == SPACE) {
                                var x = b
                                val reverse = d.negate()
                                do {
                                    expandedGrid[x.first][x.second] =
                                        expandedGrid[(x + reverse).first][(x + reverse).second]
                                    x += reverse
                                } while (x != next)
                                expandedGrid[current.first][current.second] = SPACE
                                expandedGrid[next.first][next.second] = ROBOT
                                current = next
                            }
                        } else {
                            val currentBlock = getBlocks(next, expandedGrid)
                            val blocksToMove = mutableSetOf<List<Position>>()
                            if (move(currentBlock, d, expandedGrid, blocksToMove)) {
                                // free block space
                                blocksToMove.forEach {
                                    expandedGrid[it[0].first][it[0].second] = '.'
                                    expandedGrid[it[1].first][it[1].second] = '.'
                                }
                                // set blocks new place
                                blocksToMove.map { it.map { e -> e + d } }.forEach {
                                    expandedGrid[it[0].first][it[0].second] = '['
                                    expandedGrid[it[1].first][it[1].second] = ']'
                                }
                                expandedGrid[current.first][current.second] = SPACE
                                current = next
                                expandedGrid[current.first][current.second] = ROBOT
                            }
                        }
                    }
                }
            }
        return expandedGrid.traverseWithSum { i, j, c -> if (c == '[') i * 100L + j else 0L }
    }

    private fun move(
        currentBlock: List<Position>,
        direction: Position,
        expandedGrid: List<MutableList<Char>>,
        blocksToMove: MutableSet<List<Position>>
    ): Boolean {
        check(currentBlock.size == 2)
        blocksToMove += currentBlock
        val nextPos = currentBlock.map { it + direction }
        val nextChars = nextPos.map { expandedGrid.atPos(it) }
        if (nextChars.contains(WALL)) {
            return false
        }
        val nextBlocks = nextPos
            .filter { expandedGrid.atPos(it) == '[' || expandedGrid.atPos(it) == ']' }
            .map { getBlocks(it, expandedGrid) }
            .toSet()

        return nextBlocks.isEmpty() || nextBlocks.all { move(it, direction, expandedGrid, blocksToMove) }
    }

    private fun getBlocks(
        pos: Position,
        expandedGrid: List<MutableList<Char>>
    ): List<Position> = when (expandedGrid.atPos(pos)) {
        '[' -> listOf(pos, pos + (0 to 1))
        else -> listOf(pos + (0 to -1), pos)
    }


    override fun convert(file: String): Day202415Input {
        val lines = file.readFile().lines()
        val gridLines = lines.filter { it.contains('#') }
        val movementLines = lines.filter { !it.contains('#') && it.isNotBlank() }
        return Day202415Input(
            grid = gridLines.map { it.toCharArray().toList() },
            movements = movementLines.flatMap { it.toCharArray().toList() }
        )
    }

}

fun main() {
    Day15().solve(copyResult = true, test = true)
    Day15().solve(copyResult = true)
}