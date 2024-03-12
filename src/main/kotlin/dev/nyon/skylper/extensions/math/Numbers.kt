package dev.nyon.skylper.extensions.math

import java.util.TreeMap

fun Number.withDot(): String {
    val numString = this.toString()
    val chunks = numString.reversed().chunked(3).map { it.reversed() }.reversed()
    return chunks.joinToString(".")
}

private val suffixes =
    TreeMap<Long, String>().apply {
        this[1000L] = "k"
        this[1000000L] = "M"
        this[1000000000L] = "B"
        this[1000000000000L] = "T"
        this[1000000000000000L] = "P"
        this[1000000000000000000L] = "E"
    }

fun Number.format(preciseBillions: Boolean = false): String {
    val value = this.toLong()

    if (value == Long.MIN_VALUE) return (Long.MIN_VALUE + 1).format(preciseBillions)
    if (value < 0) return "-" + (-value).format(preciseBillions)

    if (value < 1000) return value.toString()

    val (divideBy, suffix) = suffixes.floorEntry(value)

    val truncated = value / (divideBy / 10)

    val truncatedAt =
        if (suffix == "M") {
            1000
        } else if (suffix == "B") {
            1000000
        } else {
            100
        }

    val hasDecimal = truncated < truncatedAt && truncated / 10.0 != (truncated / 10).toDouble()

    return if (value > 1_000_000_000 && hasDecimal && preciseBillions) {
        val decimalPart = (value % 1_000_000_000) / 1_000_000
        "${truncated / 10}.$decimalPart$suffix"
    } else {
        if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }
}
