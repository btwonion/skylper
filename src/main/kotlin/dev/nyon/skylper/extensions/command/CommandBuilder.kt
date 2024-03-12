package dev.nyon.skylper.extensions.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.nyon.skylper.mcScope
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

val String.commandLiteral: LiteralArgumentBuilder<FabricClientCommandSource>
    get() {
        return LiteralArgumentBuilder.literal(this)
    }

fun LiteralArgumentBuilder<FabricClientCommandSource>.executeAsync(
    callback: suspend (context: CommandContext<FabricClientCommandSource>) -> Unit
): LiteralArgumentBuilder<FabricClientCommandSource> {
    return executes {
        mcScope.launch {
            callback(it)
        }
        1
    }
}

fun RequiredArgumentBuilder<FabricClientCommandSource, *>.executeAsync(
    callback: suspend (context: CommandContext<FabricClientCommandSource>) -> Unit
) {
    executes {
        mcScope.launch {
            callback(it)
        }
        1
    }
}

fun LiteralArgumentBuilder<FabricClientCommandSource>.sub(
    name: String,
    callback: (LiteralArgumentBuilder<FabricClientCommandSource>) -> Unit = {}
): LiteralArgumentBuilder<FabricClientCommandSource> {
    return then(name.commandLiteral.apply(callback))
}

fun <A> LiteralArgumentBuilder<FabricClientCommandSource>.arg(
    name: String,
    type: ArgumentType<A>,
    suggestions: List<Any>,
    callback: (RequiredArgumentBuilder<FabricClientCommandSource, A>) -> Unit = {}
): LiteralArgumentBuilder<FabricClientCommandSource> {
    return then(
        RequiredArgumentBuilder.argument<FabricClientCommandSource, A>(name, type).suggests { _, builder ->
            val input = builder.input.takeLastWhile { it != ' ' }
            suggestions.map { it.toString() }.filter { it.contains(input) }.forEach { builder.suggest(it) }
            builder.buildFuture()
        }.apply(callback)
    )
}

fun <A> LiteralArgumentBuilder<FabricClientCommandSource>.arg(
    name: String,
    type: ArgumentType<A>,
    callback: (RequiredArgumentBuilder<FabricClientCommandSource, A>) -> Unit = {}
): LiteralArgumentBuilder<FabricClientCommandSource> {
    return then(RequiredArgumentBuilder.argument<FabricClientCommandSource, A>(name, type).apply(callback))
}

fun <A> RequiredArgumentBuilder<FabricClientCommandSource, *>.arg(
    name: String,
    type: ArgumentType<A>,
    suggestions: List<Any>,
    callback: (RequiredArgumentBuilder<FabricClientCommandSource, A>) -> Unit = {}
): RequiredArgumentBuilder<FabricClientCommandSource, *> {
    return then(
        RequiredArgumentBuilder.argument<FabricClientCommandSource, A>(name, type).suggests { _, builder ->
            suggestions.map { it.toString() }.filter { it.contains(builder.input) }.forEach { builder.suggest(it) }
            builder.buildFuture()
        }.apply(callback)
    )
}

fun <A> RequiredArgumentBuilder<FabricClientCommandSource, *>.arg(
    name: String,
    type: ArgumentType<A>,
    callback: (RequiredArgumentBuilder<FabricClientCommandSource, A>) -> Unit = {}
): RequiredArgumentBuilder<FabricClientCommandSource, *> {
    return then(RequiredArgumentBuilder.argument<FabricClientCommandSource, A>(name, type).apply(callback))
}
