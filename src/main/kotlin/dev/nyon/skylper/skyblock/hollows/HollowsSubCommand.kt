package dev.nyon.skylper.skyblock.hollows

import dev.nyon.skylper.extensions.math.vec3
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.network.chat.Component
import net.silkmc.silk.commands.*

fun LiteralCommandBuilder<ClientCommandSourceStack>.appendCrystalHollowsSubCommand() {
    literal("hollows") {
        literal("waypoints") {
            literal("set") {
                argument<String>("internal_name") { structureName ->
                    suggestList { HollowsStructure.entries.map { it.internalWaypointName } }
                    argument("location", BlockPosArgument.blockPos()) { location ->
                        runsAsync {
                            val name = structureName()
                            val loc = location().vec3(source.player.position())

                            val structure = HollowsStructure.entries.find { it.internalWaypointName == name }
                            if (structure == null || loc == null) {
                                source.sendFailure(Component.translatable("chat.skylper.hollows.command.no_internal_waypoint_name"))
                                return@runsAsync
                            }

                            HollowsModule.waypoints[structure.internalWaypointName] = HollowsStructureWaypoint(
                                loc, structure
                            )
                            source.sendSuccess(
                                Component.translatable(
                                    "chat.skylper.hollows.command.waypoint_created", structure.displayName
                                )
                            )
                        }
                    }
                }
            }

            literal("remove") {
                argument<String>("internal_name") { structureName ->
                    suggestList { HollowsStructure.entries.map { it.internalWaypointName } }
                    runsAsync {
                        val name = structureName()

                        val structure = HollowsStructure.entries.find { it.internalWaypointName == name }
                            ?: return@runsAsync
                        HollowsModule.waypoints.remove(name)
                        source.sendSuccess(
                            Component.translatable(
                                "chat.skylper.hollows.command.waypoint_deleted", structure.displayName
                            )
                        )
                    }
                }
            }
        }
    }
}