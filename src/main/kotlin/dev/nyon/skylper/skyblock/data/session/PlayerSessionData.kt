package dev.nyon.skylper.skyblock.data.session

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.invokeEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.seconds

@Suppress("SpellCheckingInspection")
object PlayerSessionData {
    var isOnHypixel = false
    var isOnSkyblock = false

    var footer: Component = Component.empty()
    var scoreboardLines = emptyList<Component>()
    var scoreboardLineStrings = emptyList<String>()

    var currentArea: String? = null
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
                    it.string.replace("§[^a-f0-9]".toRegex(), "")
                }

                invokeEvent(SideboardUpdateEvent(scoreboardLines, scoreboardLineStrings))
                updateFromSideboard()
                updateFromTabList()

                delay(1.seconds)
            } while (true)
        }
    }

    private fun updateFromTabList() {
        val onlinePlayers = minecraft.connection?.onlinePlayers ?: return
        mcScope.launch {
            onlinePlayers.forEach { entry ->
                val displayName = entry.tabListDisplayName?.string ?: return@forEach
                when {
                    displayName.startsWith("Area: ") -> {
                        val newArea = displayName.drop(6)
                        if (newArea != currentArea) {
                            invokeEvent(AreaChangeEvent(currentArea, newArea))
                            currentArea = newArea
                        }
                    }
                }
            }
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun updateFromSideboard() {
        var containsSkyblockTitle = false
        if (scoreboardLineStrings.isEmpty()) return

        scoreboardLineStrings.forEach { s ->
            when {
                listOf("skyblock", "skiblock").any { s.contains(it, true) } -> {
                    if (!isOnSkyblock) {
                        invokeEvent(SkyblockEnterEvent)
                        isOnSkyblock = true
                    }

                    containsSkyblockTitle = true
                }

                s.contains("⏣") || s.contains("ф") -> {
                    currentZone = s.drop(3)
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
    private val hypixelJoinEvent = listenEvent<HypixelJoinEvent, Unit> {
        isOnHypixel = true
    }

    @Suppress("unused")
    private val hypixelQuitEvent = listenEvent<HypixelQuitEvent, Unit> {
        clearData(true)
    }

    @Suppress("unused")
    private val screenUpdateEvent = listenEvent<ScreenOpenEvent, Unit> {
        currentScreen = screen
    }

    private val profileRegex = regex("chat.general.profile")
    @Suppress("unused")
    private val messageEvent = listenEvent<MessageEvent, Unit> {
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
