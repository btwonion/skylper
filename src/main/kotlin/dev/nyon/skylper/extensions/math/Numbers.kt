package dev.nyon.skylper.extensions.math

fun Number.withDot(): String {
    val numString = this.toString()
    val chunks = numString.reversed().chunked(3).map { it.reversed() }.reversed()
    return chunks.joinToString(".")
}
