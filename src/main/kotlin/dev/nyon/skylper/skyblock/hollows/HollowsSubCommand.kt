package dev.nyon.skylper.skyblock.hollows

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.nyon.skylper.extensions.command.arguments.ClientBlockPosArgument
import dev.nyon.skylper.extensions.command.arg
import dev.nyon.skylper.extensions.command.executeAsync
import dev.nyon.skylper.extensions.command.sub
import dev.nyon.skylper.extensions.math.blockPos
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component

fun LiteralArgumentBuilder<FabricClientCommandSource>.appendCrystalHollowsSubCommand() {
    sub("hollows") { hollows ->
        hollows.sub("waypoints") { waypoints ->
            waypoints.sub("set") { set ->
                set.arg("internal_name",
                    StringArgumentType.word(),
                    HollowsStructure.entries.map { it.internalWaypointName }) { nameArg ->
                    nameArg.arg("location", ClientBlockPosArgument.blockPos()) { locationArg ->
                        locationArg.executeAsync { context ->
                            val name = StringArgumentType.getString(context, "internal_name")
                            val loc = ClientBlockPosArgument.getBlockPos(context, "location").center

                            val structure = HollowsStructure.entries.find { it.internalWaypointName == name }
                            if (structure == null) {
                                context.source.sendError(Component.translatable("chat.skylper.hollows.command.no_internal_waypoint_name"))
                                return@executeAsync
                            }

                            HollowsModule.waypoints[structure.internalWaypointName] = Waypoint(
                                Component.literal(structure.displayName),
                                loc.blockPos.atY(if (structure == HollowsStructure.JUNGLE_TEMPLE) 115 else (structure.maxY + structure.minY) / 2).center,
                                WaypointType.BEAM,
                                structure.waypointColor
                            )
                            context.source.sendFeedback(
                                Component.translatable(
                                    "chat.skylper.hollows.command.waypoint_created", structure.displayName
                                )
                            )
                        }
                    }
                }
            }

            waypoints.sub("remove") { remove ->
                remove.arg("internal_name",
                    StringArgumentType.word(),
                    HollowsStructure.entries.map { it.internalWaypointName }) { nameArg ->
                    nameArg.executeAsync { context ->
                        val name = StringArgumentType.getString(context, "internal_name")
                        val structure =
                            HollowsStructure.entries.find { it.internalWaypointName == name } ?: return@executeAsync
                        HollowsModule.waypoints.remove(name)
                        context.source.sendFeedback(
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