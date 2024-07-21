@file:OptIn(ExperimentalSerializationApi::class)

package dev.nyon.skylper.extensions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.world.phys.Vec3
import java.awt.Color

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("color", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeInt(), true)
    }

    override fun serialize(
        encoder: Encoder, value: Color
    ) {
        encoder.encodeInt(value.rgb)
    }
}

object RegexSerializer : KSerializer<Regex> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("regex", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Regex {
        return decoder.decodeString().toRegex()
    }

    override fun serialize(encoder: Encoder, value: Regex) {
        encoder.encodeString(value.toString())
    }
}

object Vec3Serializer : KSerializer<Vec3> {
    private val delegateSerializer = DoubleArraySerializer()
    override val descriptor: SerialDescriptor = SerialDescriptor("Vec3", delegateSerializer.descriptor)

    override fun deserialize(decoder: Decoder): Vec3 {
        val array = decoder.decodeSerializableValue(delegateSerializer)
        return Vec3(array[0], array[1], array[2])
    }

    override fun serialize(encoder: Encoder, value: Vec3) {
        val data = doubleArrayOf(value.x, value.y, value.z)
        encoder.encodeSerializableValue(delegateSerializer, data)
    }
}