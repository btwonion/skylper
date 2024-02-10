package dev.nyon.skylper.skyblock

import dev.nyon.skylper.skyblock.hollows.appendCrystalHollowsSubCommand
import net.silkmc.silk.commands.clientCommand

fun registerRootCommand() = clientCommand("skylper") {
    appendCrystalHollowsSubCommand()
}