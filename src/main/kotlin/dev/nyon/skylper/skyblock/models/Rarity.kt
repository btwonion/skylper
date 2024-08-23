package dev.nyon.skylper.skyblock.models

import kotlinx.serialization.Serializable

@Serializable
enum class Rarity(val colorCode: Char) {
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
    ADMIN('4')
}