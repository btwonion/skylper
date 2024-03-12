package dev.nyon.skylper.extensions.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class ClientEntityAnchorArgument : ArgumentType<ClientEntityAnchorArgument.Anchor> {
    override fun parse(reader: StringReader): Anchor {
        val i = reader.cursor
        val string = reader.readUnquotedString()
        val anchor = Anchor.entries.find { it.identifier == string }
        if (anchor == null) {
            reader.cursor = i
            throw ERROR_INVALID.createWithContext(reader, string)
        } else {
            return anchor
        }
    }

    override fun <S> listSuggestions(
        commandContext: CommandContext<S>,
        suggestionsBuilder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(Anchor.entries.map { it.identifier }, suggestionsBuilder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }

    enum class Anchor(val identifier: String, val transform: (Vec3, Entity) -> Vec3) {
        FEET("feet", { vec3: Vec3, _: Entity? -> vec3 }),
        EYES("eyes", { vec3: Vec3, entity: Entity ->
            Vec3(vec3.x, vec3.y + entity.eyeHeight.toDouble(), vec3.z)
        });

        /**
         * Gets the coordinate based on the given command source's position. If the source is not an entity, no offsetting occurs.
         */
        fun apply(source: FabricClientCommandSource): Vec3 {
            val entity = source.entity
            return if (entity == null) source.position else transform(source.position, entity)
        }
    }

    companion object {
        private val EXAMPLES: Collection<String> = mutableListOf("eyes", "feet")
        private val ERROR_INVALID =
            DynamicCommandExceptionType { `object`: Any? ->
                Component.translatableEscape(
                    "argument.anchor.invalid",
                    `object`
                )
            }
    }
}
