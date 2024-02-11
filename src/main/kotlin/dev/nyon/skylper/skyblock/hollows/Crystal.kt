package dev.nyon.skylper.skyblock.hollows

enum class Crystal(val displayName: String) {
    AMBER("Amber"),
    AMETHYST("Amethyst"),
    JADE("Jade"),
    SAPPHIRE("Sapphire"),
    TOPAZ("Topaz");

    fun associatedStructure(): HollowsStructure {
        return when (this) {
            AMBER -> HollowsStructure.GOBLIN_QUEEN
            AMETHYST -> HollowsStructure.JUNGLE_TEMPLE
            JADE -> HollowsStructure.MINES_OF_DIVAN
            SAPPHIRE -> HollowsStructure.PRECURSOR_CITY
            TOPAZ -> HollowsStructure.KHAZAD_DUM
        }
    }
}