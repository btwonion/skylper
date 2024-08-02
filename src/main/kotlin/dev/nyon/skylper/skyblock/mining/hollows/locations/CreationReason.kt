package dev.nyon.skylper.skyblock.mining.hollows.locations

enum class CreationReason(val priority: Int) {
    STATIC(0),
    NPC(0),
    CRYSTAL(1),
    MANUAL(2),
    SIDEBOARD(3),
    CHAT(4)
}