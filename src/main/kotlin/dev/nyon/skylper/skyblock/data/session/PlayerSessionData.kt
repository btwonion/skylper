package dev.nyon.skylper.skyblock.data.session

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.invokeEvent
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.models.Area
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.seconds

object PlayerSessionData {
    private var isOnHypixel = false
    var isOnSkyblock = false

    var footer: Component = Component.empty()
    var scoreboardLines = emptyList<Component>()
    var scoreboardLineStrings = emptyList<String>()

    var currentArea: Area? = null
    var currentZone: String? = null

    var profile: String? = null

    var currentScreen: AbstractContainerScreen<*>? = null

    fun startUpdaters() {
        startTicker()
    }

    private fun startTicker() {
        independentScope.launch {
            do {
                if (!isOnHypixel) {
                    delay(1.seconds)
                    continue
                }

                footer = minecraft.gui.tabList.footer
                scoreboardLines = minecraft.retrieveScoreboardLines()
                scoreboardLineStrings = scoreboardLines.map {
                    it.string.clean()
                }

                invokeEvent(SideboardUpdateEvent(scoreboardLines, scoreboardLineStrings))
                updateFromSideboard()
                updateFromTabList()

                delay(1.seconds)
            } while (true)
        }
    }

    private val areaRegex get() = regex("tablist.general.area")
    private fun updateFromTabList() {
        val onlinePlayers = minecraft.connection?.onlinePlayers ?: return
        val lines = onlinePlayers.mapNotNull { it.tabListDisplayName?.string?.clean() }
        invokeEvent(TablistUpdateEvent(onlinePlayers.mapNotNull { it.tabListDisplayName }, lines))
        mcScope.launch {
            lines.forEach { line ->
                if (!areaRegex.matches(line)) return@forEach
                val area = Area.entries.find { it.getRegex().matches(line) } ?: return@forEach
                if (area != currentArea) {
                    invokeEvent(AreaChangeEvent(currentArea, area))
                    currentArea = area
                }
            }
        }
    }

    private val skyblockRegex get() = regex("sideboard.general.skyblock")
    private val zoneRegex get() = regex("sideboard.general.zone")
    private fun updateFromSideboard() {
        var containsSkyblockTitle = false
        if (scoreboardLineStrings.isEmpty()) return

        scoreboardLineStrings.forEach { s ->
            when {
                skyblockRegex.matches(s) -> {
                    if (!isOnSkyblock) {
                        invokeEvent(SkyblockEnterEvent)
                        isOnSkyblock = true
                    }

                    containsSkyblockTitle = true
                }

                zoneRegex.matches(s) -> {
                    currentZone = zoneRegex.singleGroup(s) ?: return@forEach
                }
            }
        }

        if (!containsSkyblockTitle) {
            if (isOnSkyblock) invokeEvent(SkyblockQuitEvent)
            isOnSkyblock = false
            clearData()
        }
    }

    @Suppress("unused")
    private val hypixelJoinEvent = listenInfoEvent<HypixelJoinEvent> {
        isOnHypixel = true
    }

    @Suppress("unused")
    private val hypixelQuitEvent = listenInfoEvent<HypixelQuitEvent> {
        clearData(true)
    }

    @Suppress("unused")
    private val screenUpdateEvent = listenInfoEvent<ScreenOpenEvent> {
        currentScreen = screen
    }

    private val profileRegex get() = regex("chat.general.profile")

    @Suppress("unused")
    private val messageEvent = listenInfoEvent<MessageEvent> {
        if (profileRegex.matches(rawText)) {
            profile = profileRegex.singleGroup(rawText)
            invokeEvent(ProfileChangeEvent(null, profile))
        }
    }

    private fun clearData(withHypixel: Boolean = false) {
        currentArea?.also {
            invokeEvent(AreaChangeEvent(it, null))
        }

        if (withHypixel) isOnHypixel = false
        isOnSkyblock = false
        currentArea = null
        currentZone = null
        invokeEvent(ProfileChangeEvent(profile, null))
        profile = null

        scoreboardLines = emptyList()
        footer = Component.empty()

        currentScreen = null
    }
}
