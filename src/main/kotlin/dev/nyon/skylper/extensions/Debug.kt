package dev.nyon.skylper.extensions

fun debug(string: String) {
    if (System.getenv().containsKey("skylper.debug")) println(string)
}