package dev.nyon.skylper.extensions

import dev.nyon.skylper.skyblock.data.online.Regexes

val sentWarnings: MutableList<String> = mutableListOf()
fun regex(key: String): Regex {
    val regex = Regexes.regexes[key]
    if (regex == null && !sentWarnings.contains(key)) {
        sentWarnings.add(key)
        println("No regex found for key $key.")
    }
    return regex ?: "\\A(?!x)x".toRegex()
}

fun Regex.singleGroup(text: String): String? {
    return groups(text)?.getOrNull(1)
}

fun Regex.groups(text: String): List<String>? {
    return find(text)?.groupValues
}