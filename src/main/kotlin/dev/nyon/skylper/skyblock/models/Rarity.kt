package dev.nyon.skylper.skyblock.models

enum class Rarity(private val colorCode: Char) {
    COMMON('f'),
    UNCOMMON('a'),
    RARE('9'),
    EPIC('5'),
    LEGENDARY('6'),
    MYTHIC('d'),
    DIVINE('b'),
    SPECIAL('c'),
    VERY_SPECIAL('c'),
    ULTIMATE('4'),
    ADMIN('4');

    companion object {
        fun byColor(char: Char): Rarity? {
            return entries.find { it.colorCode == char }
        }
    }
}