package com.ite.aoc.y2016

import com.ite.aoc.AocDay
import com.ite.aoc.mapLines

private typealias Day201604Input = List<String>

class Day04 : AocDay<Day201604Input>(
    day = 4,
    year = 2016,
) {
    private data class Room(
        val name: String,
        val sectorId: Int,
        val checksum: String,
    )

    private val roomRegex = """((?:\w+-)+)(\d+)\[(\w{5})]""".toRegex()


    override fun part1(entries: Day201604Input): Int {
        val rooms = parseRooms(entries)
        return rooms.filter { room ->
            val actualChecksum = calculateChecksum(room.name)
            actualChecksum == room.checksum
        }.sumOf { it.sectorId }
    }

    override fun part2(entries: Day201604Input): Int {
        val keywords = "north pole object storage".split(" ").toList()
        val rooms = parseRooms(entries)
        return rooms.first { room ->
            val rotationCount = room.sectorId.mod(26)
            val decrypted = room.name
                .map {
                    when {
                        it == '-' -> " "
                        else -> 'a' + (rotationCount + (it - 'a')).mod(26)
                    }
                }
                .joinToString("")
                .trim()
            keywords.all { decrypted.contains(it) }
                .also { if (it) println("$room : decrypted as '$decrypted'") }
        }.sectorId
    }

    private fun calculateChecksum(name: String): String {
        val chars = name.toCharArray().toList()
        val occ = chars.filter { it in 'a'..'z' }.groupingBy { it }.eachCount()
        val actualChecksum = occ
            .entries
            .sortedWith(compareBy({ -it.value }, { it.key }))
            .map { it.key }
            .take(5)
            .joinToString("")
        return actualChecksum
    }

    private fun parseRooms(entries: List<String>): List<Room> = entries.map {
        roomRegex.find(it)!!.groupValues.let { g ->
            Room(
                name = g[1],
                sectorId = g[2].toInt(),
                checksum = g[3]
            )
        }
    }

    override fun convert(file: String): Day201604Input =
        file.mapLines { _, l -> l }

}

fun main() {
    Day04().solve(copyResult = true)
}