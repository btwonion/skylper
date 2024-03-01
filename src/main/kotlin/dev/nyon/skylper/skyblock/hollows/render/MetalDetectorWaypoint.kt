package dev.nyon.skylper.skyblock.hollows.render

import de.hysky.skyblocker.utils.render.RenderHelper
import de.hysky.skyblocker.utils.waypoint.Waypoint
import dev.nyon.skylper.extensions.color
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

class MetalDetectorWaypoint(pos: BlockPos) : Waypoint(
    pos, Type.WAYPOINT, ChatFormatting.RED.color!!.color.getRGBComponents(null)
) {
    override fun render(context: WorldRenderContext?) {
        super.render(context)

        val vec = pos.center

        RenderHelper.renderText(
            context, Component.literal("Treasure").withColor(ChatFormatting.RED.color!!), vec.add(
                Vec3(0.0, 4.0, 0.0)
            ), 20f, true
        )
        val distance = context!!.camera().position.distanceTo(vec)
        RenderHelper.renderText(
            context, Component.literal(Math.round(distance).toString() + "m"), vec.add(Vec3(0.0, 0.3, 0.0)), 13f, true
        )
    }
}