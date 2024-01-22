package dev.nyon.skylper.skyblock.hollows

import net.minecraft.world.phys.AABB

@Suppress("SpellCheckingInspection")
enum class HollowsStructure(
    val box: AABB,
    val zone: HollowsZone,
    val minY: Int,
    val maxY: Int,
    val displayName: String,
    val internalWaypointName: String,
    val waypointColor: Int
) {
    GOBLIN_QUEEN(
        AABB(0.0, 0.0, 0.0, 108.0, 114.0, 108.0),
        HollowsZone.GOBLIN_HOLDOUT,
        125,
        140,
        "Goblin Queen",
        "internal_goblin_queen",
        0x9B5206
    ),
    GOBLIN_KING(
        AABB(0.0, 0.0, 0.0, 59.0, 53.0, 56.0),
        HollowsZone.GOBLIN_HOLDOUT,
        82,
        168,
        "Goblin King",
        "internal_goblin_king",
        0xC57800
    ),
    KHAZAD_DUM(
        AABB(0.0, 0.0, 0.0, 110.0, 46.0, 108.0),
        HollowsZone.MAGMA_FIELDS,
        0,
        75,
        "Khazad Dûm",
        "internal_khazad_dum",
        0x800D00
    ),
    JUNGLE_TEMPLE(
        AABB(0.0, 0.0, 0.0, 108.0, 120.0, 108.0),
        HollowsZone.JUNGLE,
        72,
        81,
        "Jungle Temple",
        "internal_jungle_temple",
        0x138012
    ),
    ODAWA(AABB(0.0, 0.0, 0.0, 53.0, 46.0, 54.0), HollowsZone.JUNGLE, 73, 155, "Odawa", "internal_odawa", 0x138012),
    PRECURSOR_CITY(
        AABB(0.0, 0.0, 0.0, 107.0, 122.0, 107.0),
        HollowsZone.PRECURSOR_REMNANTS,
        121,
        130,
        "Precursor City",
        "internal_precursor_city",
        0xACABAF
    ),
    MINES_OF_DIVAN(
        AABB(0.0, 0.0, 0.0, 108.0, 125.0, 108.0),
        HollowsZone.MITHRIL_DEPOSITS,
        97,
        102,
        "Mines of Divan",
        "internal_mines_of_divan",
        0x084C9B
    ),
    CRYSTAL_NUCLEUS(
        HollowsZone.CRYSTAL_NUCLEUS.box,
        HollowsZone.CRYSTAL_NUCLEUS,
        0,
        200,
        "Crystal Nucleus",
        "internal_crystal_nucleus",
        0x7E0B7F
    )
}