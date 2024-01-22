package dev.nyon.skylper.extensions.math

fun Double.doubleToLongBits(): Long {
    return if (this == 0.0) 0L
    else this.toBits()
}