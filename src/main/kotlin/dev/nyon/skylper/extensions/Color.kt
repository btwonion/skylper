package dev.nyon.skylper.extensions

import java.awt.Color

val Int.color: Color
    get() {
        return Color(this)
    }