package dev.nyon.skylper.extensions

import dev.nyon.skylper.minecraft
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.PlayerTeam

fun Minecraft.retrieveScoreboardLines(): List<Component> {
    val scoreboard = player?.scoreboard ?: return emptyList()

    val objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR) ?: return emptyList()
    return scoreboard.listPlayerScores(objective)
        .asSequence()
        .filter { !it.isHidden }
        .sortedWith(minecraft.gui.orderComparator)
        .take(15)
        .map {
            val team = scoreboard.getPlayersTeam(it.owner)
            val text = it.ownerName()
            PlayerTeam.formatNameForTeam(team, text)
        }
        .toMutableList()
        .also { it.add(objective.displayName.copy()) }
}