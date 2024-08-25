package dev.nyon.skylper.skyblock

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.createYaclScreen
import dev.nyon.skylper.extensions.chatTranslatable
import dev.nyon.skylper.extensions.command.arg
import dev.nyon.skylper.extensions.command.arguments.ClientBlockPosArgument
import dev.nyon.skylper.extensions.command.executeAsync
import dev.nyon.skylper.extensions.command.sub
import dev.nyon.skylper.extensions.skylperPrefix
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.data.online.OnlineData
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CreationReason
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CustomHollowsLocationSpecific
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.HollowsLocation
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.PreDefinedHollowsLocationSpecific
import kotlinx.coroutines.delay
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.milliseconds

fun registerRootCommand() {
    ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
        dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("skylper").also { root ->
            root.openConfigGui()

            root.sub("hollows") { hollows ->
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

                                    val location = HollowsLocation(loc, CreationReason.MANUAL, specific)
                                    if (CrystalHollowsLocationApi.waypoints.none { it.specific == specific } || specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO) {
                                        CrystalHollowsLocationApi.waypoints.add(location)
                                    } else {
                                        CrystalHollowsLocationApi.waypoints.find { it.specific == specific }?.pos = loc
                                    }

                                    context.source.sendFeedback(
                                        chatTranslatable(
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

                                CrystalHollowsLocationApi.waypoints.removeAll { it.specific == specific }
                                context.source.sendFeedback(
                                    chatTranslatable(
                                        "chat.skylper.hollows.command.waypoint_deleted", specific.displayName
                                    )
                                )
                            }
                        }
                    }
                }
            }

            root.sub("gui") { gui ->
                gui.openConfigGui()
            }

            root.sub("data") { data ->
                data.sub("reload") { reload ->
                    reload.executeAsync {
                        OnlineData.data.forEach { it.refresh() }
                    }
                }

                data.sub("save") { save ->
                    save.executeAsync {
                        saveConfig(config)
                        saveConfig(playerData)
                    }
                }
            }

            root.sub("debug") { debug ->
                debug.sub("prefix") { prefix ->
                    prefix.executeAsync {
                        it.source.sendFeedback(skylperPrefix)
                    }
                }
            }
        })
    })
}

private fun LiteralArgumentBuilder<FabricClientCommandSource>.openConfigGui() {
    executeAsync {
        delay(100.milliseconds)
        val screen = createYaclScreen(null)
        minecraft.setScreen(screen)
    }
}
