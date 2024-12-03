package com.ite.aoc

import ocm.ite.adventofcode.AocUtils
import java.util.function.BiFunction

fun <T> String.mapLines(mapper: BiFunction<Int, String, T>): List<T> = AocUtils.mapLines(this, mapper)