package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

data class HollowsLocation(var pos: Vec3, val specific: HollowsLocationSpecific) {
    val waypoint = Waypoint(specific.displayName, pos, WaypointType.BEAM, specific.color)

    private val locationConfig: Config.CrystalHollowsConfig.HollowsWaypoints
        get() {
            return config.mining.crystalHollows.hollowsWaypoints
        }

    val isEnabled: Boolean =
        when (specific) {
            PreDefinedHollowsLocationSpecific.CRYSTAL_NUCLEUS -> locationConfig.nucleus
            PreDefinedHollowsLocationSpecific.PRECURSOR_CITY -> locationConfig.precursorCity
            PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE -> locationConfig.jungleTemple
            PreDefinedHollowsLocationSpecific.ODAWA -> locationConfig.odawa
            PreDefinedHollowsLocationSpecific.KEY_GUARDIAN -> locationConfig.keyGuardian
            PreDefinedHollowsLocationSpecific.MINES_OF_DIVAN -> locationConfig.minesOfDivan
            PreDefinedHollowsLocationSpecific.CORLEONE -> locationConfig.corleone
            PreDefinedHollowsLocationSpecific.FAIRY_GROTTO -> locationConfig.fairyGrotto
            PreDefinedHollowsLocationSpecific.GOBLIN_KING -> locationConfig.goblinKing
            PreDefinedHollowsLocationSpecific.GOBLIN_QUEEN -> locationConfig.goblinQueen
            PreDefinedHollowsLocationSpecific.KHAZAD_DUM -> locationConfig.khazadDum
            else -> true
        }
}

interface HollowsLocationSpecific {
    val key: String
    val color: Int
    val displayName: Component
}

data class CustomHollowsLocationSpecific(
    override val key: String,
    override val color: Int = 0x844B00,
    override val displayName: Component = Component.literal(key)
) : HollowsLocationSpecific

enum class PreDefinedHollowsLocationSpecific(override val key: String, override val color: Int) : HollowsLocationSpecific {
    CRYSTAL_NUCLEUS("nucleus", 0x7E0B7F),
    PRECURSOR_CITY("precursor_city", 0xACABAF),
    JUNGLE_TEMPLE("jungle_temple", 0x138012),
    ODAWA("odawa", 0x138012),
    KEY_GUARDIAN("key_guardian", 0xFF0003),
    MINES_OF_DIVAN("divan_mines", 0x0E8208),
    CORLEONE("corleone", 0xFF0003),
    FAIRY_GROTTO("fairy_grotto", 0xFF096E),
    GOBLIN_KING("goblin_king", 0xC57800),
    GOBLIN_QUEEN("goblin_queen", 0x9B5206),
    KHAZAD_DUM("khazad_dum", 0x800D00);

    override val displayName: Component = Component.translatable("chat.skylper.hollows.locations.${this.key}", this.key)
}
