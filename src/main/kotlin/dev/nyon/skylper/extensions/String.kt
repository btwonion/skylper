package dev.nyon.skylper.extensions

import java.util.regex.Pattern

fun Pattern.matches(text: String): Boolean {
    return this.matcher(text).matches()
}

private val multipliers = mapOf('k' to 1_000.0, 'm' to 1_000_000.0, 'b' to 1_000_000_000.0)

fun String.doubleOrNull(): Double? {
    val text = lowercase().replace(",", "")

    val foundMultiplier = multipliers[text.last()]
    val multiplier = foundMultiplier ?: 1.0

    return text.dropLast(if (foundMultiplier != null) 1 else 0).toDoubleOrNull()?.let {
        it * multiplier
    }
}