package dev.nyon.skylper.extensions.math

import java.text.NumberFormat
import java.util.*

private val numberFormat = NumberFormat.getCompactNumberInstance(Locale.ROOT, NumberFormat.Style.SHORT).also {
    it.minimumFractionDigits = 1
}

fun Number.format(): String {
    val value = this.toLong()
    return numberFormat.format(value)
}
