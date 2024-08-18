package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.registerHollowsLocationHotkey
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
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
    fun init() {
        CrystalHollowsChatLocation
        CrystalHollowsNameTagLocation
        CrystalHollowsSideboardLocation

        registerHollowsLocationHotkey()
    }

    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea?.contains("Crystal Hollows") == true
            val posMatch = hollowsBox.contains(minecraft.player?.position() ?: return false)
            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsLocation(
        Vec3(513.5, 115.0, 513.5), CreationReason.STATIC, PreDefinedHollowsLocationSpecific.CRYSTAL_NUCLEUS
    )
    private val mutex = Mutex()
    val waypoints: MutableSet<HollowsLocation> = mutableSetOf()

    private val levelChangeEvent = listenInfoEvent<LevelChangeEvent> {
        resetWaypoints()
    }
    private val areaChangeEvent = listenInfoEvent<AreaChangeEvent> {
        resetWaypoints()
    }
    private val renderInWorldEvent = listenInfoEvent<RenderAfterTranslucentEvent> {
        if (!isPlayerInHollows) return@listenInfoEvent
        mcScope.launch {
            mutex.withLock {
                waypoints.forEach { location ->
                    if (!location.isEnabled) return@forEach
                    location.waypoint.render(context)
                }
            }
        }
    }
    private val locatedStructureEvent = listenInfoEvent<LocatedHollowsStructureEvent> {
        if (!isPlayerInHollows) return@listenInfoEvent
        val currentPos = minecraft.player?.position() ?: return@listenInfoEvent
        independentScope.launch {
            mutex.withLock { // Explicitly handle Fairy Grotto
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

    @Suppress("unused")
    private val messageEvent = listenInfoEvent<MessageEvent> {
        if (!config.mining.crystalHollows.parseLocationChats) return@listenInfoEvent
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent

        val rawMessage = rawText.dropWhile { it != ':' }
        val loc = regex.groups(rawMessage).let {
            if (it.isEmpty()) return@let null

            return@let Vec3(
                it[2].toDoubleOrNull() ?: 0.0, it[3].toDoubleOrNull() ?: 35.0, it[4].toDoubleOrNull() ?: 0.0
            )
        } ?: return@listenInfoEvent

        if (!CrystalHollowsLocationApi.hollowsBox.contains(loc)) return@listenInfoEvent

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
                EventHandler.invokeEvent(LocatedHollowsStructureEvent(hollowsLocation))

                minecraft.player?.sendSystemMessage(
                    Component.translatable(
                        "chat.skylper.hollows.locations.found", matchingSpecific.displayName.string, pos.x, pos.y, pos.z
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

        val finalComponent = Component.translatable("chat.skylper.hollows.locations.pick", locationsComponent)
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

    @Suppress("unused")
    val tickEvent = listenInfoEvent<TickEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
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

            location.forEach { EventHandler.invokeEvent(LocatedHollowsStructureEvent(it)) }
        }
    }
}

object CrystalHollowsSideboardLocation {
    @Suppress("unused")
    private val sideboardUpdateEvent = listenInfoEvent<SideboardUpdateEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
        cleanLines.forEach { line ->
            val specific = PreDefinedHollowsLocationSpecific.entries.find { it.regex.matches(line) } ?: return@forEach

            EventHandler.invokeEvent(
                LocatedHollowsStructureEvent(
                    HollowsLocation(
                        minecraft.player!!.position(), CreationReason.SIDEBOARD, specific
                    )
                )
            )
        }
    }
}
