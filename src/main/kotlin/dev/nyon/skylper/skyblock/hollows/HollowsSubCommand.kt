package dev.nyon.skylper.skyblock.hollows

import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.coordinates.Coordinates
import net.minecraft.network.chat.Component
import net.silkmc.silk.commands.LiteralCommandBuilder

fun LiteralCommandBuilder<CommandSourceStack>.appendCrystalHollowsSubCommand() {
    literal("hollows") {
        literal("waypoints") {
            literal("set") {
                argument<String>("internal_name") { structureName ->
                    suggestList { HollowsStructure.entries.map { it.internalWaypointName } }
                    argument<Coordinates>("location") { location ->
                        runsAsync {
                            val name = structureName()
                            val loc = location().getPosition(source)

                            val structure = HollowsStructure.entries.find { it.internalWaypointName == name }
                            if (structure == null) {
                                source.sendFailure(Component.translatable("chat.skylper.hollows.command.no_internal_waypoint_name"))
                                return@runsAsync
                            }

                            HollowsModule.waypoints[structure.internalWaypointName] = HollowsStructureWaypoint(
                                loc, structure
                            )
                            source.sendSuccess({
                                Component.translatable(
                                    "chat.skylper.hollows.command.waypoint_created", structure.displayName
                                )
                            }, true)
                        }
                    }
                }
            }

            literal("remove") {
                argument<String>("internal_name") { structureName ->
                    suggestList { HollowsStructure.entries.map { it.internalWaypointName } }
                    runsAsync {
                        val name = structureName()

                        val structure = HollowsStructure.entries.find { it.internalWaypointName == name } ?: return@runsAsync
                        HollowsModule.waypoints.remove(name)
                        source.sendSuccess({
                            Component.translatable(
                                "chat.skylper.hollows.command.waypoint_deleted", structure.displayName
                            )
                        }, true)
                    }
                }
            }
        }
    }
}