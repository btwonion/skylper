package dev.nyon.skylper.extensions

import dev.nyon.skylper.skyblock.data.online.Regexes

fun regex(key: String): Regex {
    val regex = Regexes.regexes[key]
    println("No regex found for key $key.")
    return regex ?: "".toRegex()
}

fun Regex.singleGroup(text: String): String? {
    return groups(text).firstOrNull()
}

fun Regex.groups(text: String): List<String> {
    val results = findAll(text)
    return results.map { it.value }.toList()
}