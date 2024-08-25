package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.models.Area
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CreationReason
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.HollowsLocation
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.PreDefinedHollowsLocationSpecific
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

@Suppress("unused")
object CrystalHollowsLocationApi {
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea == Area.CRYSTAL_HOLLOWS
            val posMatch = hollowsBox.contains(minecraft.player?.position() ?: return false)
            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsLocation(
        Vec3(513.5, 115.0, 513.5), CreationReason.STATIC, PreDefinedHollowsLocationSpecific.CRYSTAL_NUCLEUS
    )
    private val mutex = Mutex()
    val waypoints: MutableSet<HollowsLocation> = mutableSetOf()

    @SkylperEvent
    fun levelChangeEvent(event: LevelChangeEvent) {
        resetWaypoints()
    }

    @SkylperEvent
    fun areaChangeEvent(event: AreaChangeEvent) {
        resetWaypoints()
    }

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun renderInWorldEvent(event: RenderAfterTranslucentEvent) {
        mcScope.launch {
            mutex.withLock {
                waypoints.forEach { location ->
                    if (!location.isEnabled) return@forEach
                    location.waypoint.render(event.context)
                }
            }
        }
    }

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun locatedStructureEvent(event: LocatedHollowsStructureEvent) {
        val currentPos = minecraft.player?.position() ?: return
        val location = event.location
        independentScope.launch {
            mutex.withLock {
                // Explicitly handle Fairy Grotto
                if (location.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO && waypoints.filter { it.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO }
                        .any { it.pos.distanceTo(currentPos) < 100 }) {
                    return@withLock
                }
                val existing = waypoints.find { it.specific == location.specific }
                if (existing != null) {
                    if (existing.reason.priority <= location.reason.priority) return@withLock
                    waypoints.remove(existing)
                }

                waypoints.add(location)
            }
        }
    }

    private fun resetWaypoints() {
        independentScope.launch {
            mutex.withLock {
                waypoints.clear()
                waypoints.add(nucleusWaypoint)
            }
        }
    }
}

object CrystalHollowsChatLocation {
    private val regex get() = regex("chat.hollows.location")

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun messageEvent(event: MessageEvent) {
        if (!config.mining.crystalHollows.parseLocationChats) return

        val rawMessage = event.rawText.dropWhile { it != ':' }
        val loc = regex.groups(rawMessage)?.let {
            if (it.isEmpty()) return@let null

            return@let Vec3(
                it[2].toDoubleOrNull() ?: 0.0, it[3].toDoubleOrNull() ?: 35.0, it[4].toDoubleOrNull() ?: 0.0
            )
        } ?: return

        if (!CrystalHollowsLocationApi.hollowsBox.contains(loc)) return

        handleRawChatLocation(loc, rawMessage)
    }

    private fun handleRawChatLocation(pos: Vec3, rawMessage: String) {
        if (config.mining.crystalHollows.automaticallyAddLocations) {
            val matchingSpecific = PreDefinedHollowsLocationSpecific.entries.find {
                rawMessage.contains(it.name, true) || rawMessage.contains(
                    it.key, true
                )
            }

            if (matchingSpecific != null) {
                val hollowsLocation = HollowsLocation(pos, CreationReason.CHAT, matchingSpecific)
                invokeEvent(LocatedHollowsStructureEvent(hollowsLocation))

                minecraft.player?.sendSystemMessage(
                    chatTranslatable(
                        "chat.skylper.hollows.locations.found",
                        matchingSpecific.displayName.string,
                        pos.x,
                        pos.y,
                        pos.z
                    )
                )
                return
            }
        }

        val locationsComponent = PreDefinedHollowsLocationSpecific.entries.map { specific ->
            specific.displayName.copy().withStyle {
                val command =
                    "/skylper hollows waypoints set ${specific.key} ${pos.x.toInt()} ${pos.y.toInt()} ${pos.z.toInt()}"
                it.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
            }
        }.fold(Component.empty()) { acc, new ->
            if (acc != Component.empty()) acc.append(Component.literal(", ").withStyle(ChatFormatting.WHITE))
            acc.append(new.withStyle(ChatFormatting.RED))
        }

        val finalComponent = chatTranslatable("chat.skylper.hollows.locations.pick", locationsComponent)
        minecraft.player?.sendSystemMessage(finalComponent)
    }
}

object CrystalHollowsNameTagLocation {
    private val yolkarRegex get() = regex("nametag.hollows.yolkar")
    private val professorRegex get() = regex("nametag.hollows.professor_robot")
    private val doorGuardianRegex get() = regex("nametag.hollows.door_guardian")
    private val odawaRegex get() = regex("nametag.hollows.odawa")
    private val corleoneRegex get() = regex("nametag.hollows.corleone")
    private val keyGuardianRegex get() = regex("nametag.hollows.key_guardian")
    private val balRegex get() = regex("nametag.hollows.bal")
    private val lapisKeeperRegex get() = regex("nametag.hollows.lapis_keeper")

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun tickEvent(event: TickEvent) {
        var firstGuardianLocation: Vec3? = null

        minecraft.level?.entities?.get(minecraft.player!!.radiusBox(50)) { entity ->
            if (entity as? LivingEntity == null) return@get
            val name = entity.displayName?.string?.clean()
            val customName = entity.customName?.string?.clean()
            val entityPos = entity.position()

            val location: List<HollowsLocation> = when {
                yolkarRegex.matches(name ?: "") -> listOf(
                    HollowsLocation(
                        entityPos.add(0.0, 5.0, 0.0), CreationReason.NPC, PreDefinedHollowsLocationSpecific.GOBLIN_KING
                    )
                )
                professorRegex.matches(name ?: "") -> listOf(
                    HollowsLocation(
                        entityPos.add(-16.0, 5.0, 21.0),
                        CreationReason.NPC,
                        PreDefinedHollowsLocationSpecific.PRECURSOR_CITY
                    )
                )
                doorGuardianRegex.matches(name ?: "") -> {
                    if (firstGuardianLocation == null) {
                        firstGuardianLocation = entityPos
                        emptyList()
                    } else {
                        val leftGuardian = listOf(firstGuardianLocation!!, entityPos).minByOrNull { it.x }!!
                        val crystalPos = leftGuardian.add(61.0, -44.0, 18.0)
                        listOf(
                            HollowsLocation(
                                crystalPos, CreationReason.NPC, PreDefinedHollowsLocationSpecific.AMETHYST_CRYSTAL
                            ), HollowsLocation(
                                leftGuardian.add(0.0, 5.0, 0.0),
                                CreationReason.NPC,
                                PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE
                            )
                        )
                    }
                }
                odawaRegex.matches(name ?: "") -> listOf(
                    HollowsLocation(
                        entityPos, CreationReason.NPC, PreDefinedHollowsLocationSpecific.ODAWA
                    )
                )
                lapisKeeperRegex.matches(name ?: "") -> listOf(
                    HollowsLocation(
                        entityPos.add(-33.0, 5.0, -3.0),
                        CreationReason.NPC,
                        PreDefinedHollowsLocationSpecific.MINES_OF_DIVAN
                    )
                )
                corleoneRegex.matches(customName ?: "") && entity.hasMaxHealth(1_000_000f) -> listOf(
                    HollowsLocation(
                        entityPos, CreationReason.NPC, PreDefinedHollowsLocationSpecific.CORLEONE
                    )
                )
                keyGuardianRegex.matches(customName ?: "") -> listOf(
                    HollowsLocation(
                        entityPos, CreationReason.NPC, PreDefinedHollowsLocationSpecific.KEY_GUARDIAN
                    )
                )
                balRegex.matches(customName ?: "") && entity.type == EntityType.MAGMA_CUBE -> listOf(
                    HollowsLocation(
                        entityPos, CreationReason.NPC, PreDefinedHollowsLocationSpecific.KHAZAD_DUM
                    )
                )

                else -> emptyList()
            }

            location.forEach { invokeEvent(LocatedHollowsStructureEvent(it)) }
        }
    }
}

object CrystalHollowsSideboardLocation {
    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun sideBoardUpdateEvent(event: SideboardUpdateEvent) {
        event.cleanLines.forEach { line ->
            val specific = PreDefinedHollowsLocationSpecific.entries.find { it.regex.matches(line) } ?: return@forEach

            invokeEvent(
                LocatedHollowsStructureEvent(
                    HollowsLocation(
                        minecraft.player!!.position(), CreationReason.SIDEBOARD, specific
                    )
                )
            )
        }
    }
}
