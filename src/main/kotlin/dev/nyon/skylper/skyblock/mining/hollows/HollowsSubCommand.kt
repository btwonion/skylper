package dev.nyon.skylper.skyblock.mining.hollows

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.nyon.skylper.extensions.command.arg
import dev.nyon.skylper.extensions.command.arguments.ClientBlockPosArgument
import dev.nyon.skylper.extensions.command.executeAsync
import dev.nyon.skylper.extensions.command.sub
import dev.nyon.skylper.skyblock.mining.hollows.locations.CustomHollowsLocationSpecific
import dev.nyon.skylper.skyblock.mining.hollows.locations.HollowsLocation
import dev.nyon.skylper.skyblock.mining.hollows.locations.PreDefinedHollowsLocationSpecific
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component

fun LiteralArgumentBuilder<FabricClientCommandSource>.appendCrystalHollowsSubCommand() {
    sub("hollows") { hollows ->
        hollows.sub("waypoints") { waypoints ->
            waypoints.sub("set") { set ->
                set.arg(
                    "key",
                    StringArgumentType.word(),
                    PreDefinedHollowsLocationSpecific.entries.map { it.key }) { nameArg ->
                    nameArg.arg("location", ClientBlockPosArgument.blockPos()) { locationArg ->
                        locationArg.executeAsync { context ->
                            val key = StringArgumentType.getString(context, "key")
                            val loc = ClientBlockPosArgument.getBlockPos(context, "location").center

                            val specific = PreDefinedHollowsLocationSpecific.entries.find { it.key == key }
                                ?: CustomHollowsLocationSpecific(key)

                            val location = HollowsLocation(loc, specific)
                            if (HollowsModule.waypoints.none { it.specific == specific } || specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO) {
                                HollowsModule.waypoints.add(location)
                            } else {
                                HollowsModule.waypoints.find { it.specific == specific }?.pos = loc
                            }

                            context.source.sendFeedback(
                                Component.translatable(
                                    "chat.skylper.hollows.command.waypoint_created", specific.displayName
                                )
                            )
                        }
                    }
                }
            }

            waypoints.sub("remove") { remove ->
                remove.arg(
                    "key",
                    StringArgumentType.word(),
                    PreDefinedHollowsLocationSpecific.entries.map { it.key }) { nameArg ->
                    nameArg.executeAsync { context ->
                        val key = StringArgumentType.getString(context, "key")
                        val specific = PreDefinedHollowsLocationSpecific.entries.find { it.key == key }
                            ?: CustomHollowsLocationSpecific(key)

                        HollowsModule.waypoints.removeAll { it.specific == specific }
                        context.source.sendFeedback(
                            Component.translatable(
                                "chat.skylper.hollows.command.waypoint_deleted", specific.displayName
                            )
                        )
                    }
                }
            }
        }
    }
}
