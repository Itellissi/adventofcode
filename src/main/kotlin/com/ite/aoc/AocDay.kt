package com.ite.aoc

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.system.exitProcess
import kotlin.time.measureTime

abstract class AocDay<T>(
    year: Int,
    day: Int,
) {
    protected val file = "/input/$year/day${day.toString().padStart(2, '0')}.txt"

    abstract fun part1(entries: T): Any?

    abstract fun part2(entries: T): Any?

    abstract fun convert(file: String): T

    fun solve(copyResult: Boolean = false, part: Int? = null) {
        val inputFile = File("src/main/resources$file")
        if (!inputFile.exists()) {
            inputFile.parentFile.mkdirs()
            inputFile.createNewFile()
            exitProcess(1)
        }
        val entries = convert(file)
        val result1 = Result(null)
        val duration1 = measureTime {
            result1.value = if (part == null || part == 1) part1(entries) else null
        }
        val result2 = Result(null)
        val duration2 = measureTime {
            result2.value = if (part == null || part == 2) part2(entries) else null
        }
        if (copyResult) {
            val clip = Toolkit.getDefaultToolkit().systemClipboard
            result2.value?.let {
                clip.setContents(StringSelection(result2.value.toString()), null)
            } ?: run {
                clip.setContents(StringSelection(result1.value.toString()), null)
            }
        }
        println("Part ☝️: '${result1.value}' with duration $duration1")
        println("Part ✌️️: '${result2.value}' with duration $duration2")
    }

    data class Result(var value: Any?)
}