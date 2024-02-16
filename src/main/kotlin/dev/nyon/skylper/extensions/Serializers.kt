package dev.nyon.skylper.extensions

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.StringTagVisitor
import net.minecraft.nbt.TagParser
import net.minecraft.resources.ResourceLocation
import java.awt.Color

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("color", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeInt(), true)
    }

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeInt(value.rgb)
    }
}

object ResourceLocationSerializer : KSerializer<ResourceLocation> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("resource_location", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): ResourceLocation {
        return ResourceLocation(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ResourceLocation) {
        encoder.encodeString(value.toString())
    }
}

object CompoundTagSerializer : KSerializer<CompoundTag> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("nbt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CompoundTag {
        return TagParser.parseTag(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: CompoundTag) {
        encoder.encodeString(value.toString())
    }
}