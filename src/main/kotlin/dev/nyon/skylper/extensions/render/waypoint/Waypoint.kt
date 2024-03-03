package dev.nyon.skylper.extensions.render.waypoint

import dev.nyon.skylper.extensions.math.blockPos
import dev.nyon.skylper.extensions.render.renderBeaconBeam
import dev.nyon.skylper.extensions.render.renderOutline
import dev.nyon.skylper.extensions.render.renderText
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

/**
 * Renders a waypoint in the world.
 *
 * @param name The name of the waypoint
 * @param pos The pos the waypoint is located
 * @param type The waypoint [dev.nyon.skylper.extensions.render.waypoint.WaypointType]
 * @param color The color of the waypoint
 * @param shouldRenderDistance Decides whether the distance to the waypoint should be rendered
 * @param shouldRenderName Decides whether the name of the waypoint should be rendered
 * @param minBeaconBeamY The Y coordinate where the beacon beam should start
 */
open class Waypoint(
    val name: Component,
    val pos: Vec3,
    private val type: WaypointType,
    private val color: Int,
    private val shouldRenderDistance: Boolean = true,
    private val shouldRenderName: Boolean = true,
    private val minBeaconBeamY: Int = 0
) {
    companion object {
        private const val BACKGROUND_COLOR = 0x383838
    }

    fun render(context: WorldRenderContext) {
        val blockBox = AABB(pos.x - 0.5, pos.y - 0.5, pos.z - 0.5, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
        val beaconPos = pos.blockPos.atY(minBeaconBeamY).center
        when (type) {
            WaypointType.BEAM -> context.renderBeaconBeam(beaconPos, color)
            WaypointType.OUTLINE -> context.renderOutline(blockBox, color, 5f, true)
            WaypointType.OUTLINE_WITH_BEAM -> {
                context.renderBeaconBeam(beaconPos, color)
                context.renderOutline(blockBox, color, 15f, true)
            }
        }

        if (shouldRenderDistance) {
            val distance = context.camera().position.distanceTo(pos)

            context.renderText(
                Component.literal(Math.round(distance).toString() + "m"),
                pos.subtract(0.0, 4.0, 0.0),
                10f,
                BACKGROUND_COLOR
            )
        }

        if (shouldRenderName) context.renderText(
            name.copy().withStyle { it.withBold(true) }, pos, 15f, BACKGROUND_COLOR
        )
    }
}