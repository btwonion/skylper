package dev.nyon.skylper.skyblock

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.scores.DisplaySlot
import kotlin.time.Duration.Companion.seconds

object PlayerSessionData {
    var isOnHypixel = false
    var isOnSkyblock = false

    var footer: Component = Component.empty()
    var scoreboardLines = mutableListOf<String>()

    var currentIsland: String? = null
    var currentZone: String? = null

    var profile: String? = null

    fun startTicker() {
        independentScope.launch {
            do {
                if (!isOnHypixel) {
                    delay(1.seconds)
                    continue
                }
                val scoreboard = minecraft.player?.scoreboard ?: continue
                val objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR)

                scoreboard.trackedPlayers.forEach { holder ->
                    if (!scoreboard.listPlayerScores(holder).containsKey(objective)) return@forEach
                    val team = scoreboard.getPlayersTeam(holder.scoreboardName)

                    if (team != null) "${team.playerPrefix.string}${team.playerSuffix.string}".also {
                        if (it.isNotBlank()) scoreboardLines.add(ChatFormatting.stripFormatting(it)!!)
                    }
                }

                if (objective != null) scoreboardLines.add(objective.displayName.string)

                scoreboardLines = scoreboardLines.reversed().toMutableList()

                updateFromSideboard()
                updateFromTabList()

                delay(1.seconds)
            }
            while (true)
        }
    }

    private fun updateFromTabList() {
        val onlinePlayers = minecraft.connection?.onlinePlayers ?: return
        mcScope.launch {
            onlinePlayers.forEach { entry ->
                val displayName = entry.tabListDisplayName?.string ?: return@forEach
                when {
                    displayName.startsWith("Profile: ") -> {
                        val newProfile = displayName.drop(9)
                        if (newProfile != profile) {
                            EventHandler.invokeEvent(ProfileChangeEvent(profile, newProfile))
                            profile = newProfile
                        }
                    }

                    displayName.startsWith("Area: ") -> {
                        val newIsland = displayName.drop(6)
                        if (newIsland != currentIsland) {
                            EventHandler.invokeEvent(IslandChangeEvent(currentIsland, newIsland))
                            currentIsland = newIsland
                        }
                    }
                }
            }
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun updateFromSideboard() {
        scoreboardLines.forEachIndexed { index, s ->
            when {
                index == 0 -> {
                    val skyblockNames = listOf("skyblock", "skiblock")
                    val skyblockFound = skyblockNames.any { s.contains(it, true) }
                    if (skyblockFound != isOnSkyblock) {
                        if (skyblockFound) EventHandler.invokeEvent(SkyblockEnterEvent)
                        isOnSkyblock = skyblockFound
                    }
                }

                s.contains("⏣") || s.contains("ф") -> {

                }
            }
        }
    }
}