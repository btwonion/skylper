package dev.nyon.skylper.extensions.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.SharedSuggestionProvider.TextCoordinates
import net.minecraft.core.BlockPos
import java.util.concurrent.CompletableFuture

class ClientBlockPosArgument : ArgumentType<ClientCoordinates> {
    override fun parse(stringReader: StringReader): ClientCoordinates {
        return if (stringReader.canRead() && stringReader.peek() == '^') {
            ClientLocalCoordinates.parse(stringReader)
        } else {
            ClientWorldCoordinates.parseInt(
                stringReader
            )
        }
    }

    override fun <S> listSuggestions(
        commandContext: CommandContext<S>,
        suggestionsBuilder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        if (commandContext.source !is SharedSuggestionProvider) {
            return Suggestions.empty()
        } else {
            val string = suggestionsBuilder.remaining
            val collection: Collection<TextCoordinates> =
                if (string.isNotEmpty() && string[0] == '^') {
                    setOf(TextCoordinates.DEFAULT_LOCAL)
                } else {
                    (commandContext.source as SharedSuggestionProvider).relevantCoordinates
                }

            return SharedSuggestionProvider.suggestCoordinates(
                string,
                collection,
                suggestionsBuilder,
                Commands.createValidator { stringReader: StringReader ->
                    this.parse(
                        stringReader
                    )
                }
            )
        }
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }

    companion object {
        private val EXAMPLES: Collection<String> = mutableListOf("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5")

        fun blockPos(): ClientBlockPosArgument {
            return ClientBlockPosArgument()
        }

        fun getBlockPos(
            commandContext: CommandContext<FabricClientCommandSource>,
            string: String
        ): BlockPos {
            return commandContext.getArgument(string, ClientCoordinates::class.java).getBlockPos(
                commandContext.source!!
            )
        }
    }
}
