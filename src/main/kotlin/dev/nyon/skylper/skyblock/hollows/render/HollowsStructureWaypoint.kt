package dev.nyon.skylper.skyblock.hollows.render

import de.hysky.skyblocker.utils.render.RenderHelper
import de.hysky.skyblocker.utils.waypoint.Waypoint
import dev.nyon.skylper.asm.invokers.RenderHelperInvoker
import dev.nyon.skylper.extensions.math.blockPos
import dev.nyon.skylper.minecraft
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import java.awt.Color

class HollowsStructureWaypoint(
    private val wPos: Vec3, private val displayName: String, private val descY: Int, color: Color
) : Waypoint(
    wPos.blockPos, { Type.WAYPOINT }, color.getRGBComponents(null), true
) {
    override fun render(context: WorldRenderContext?) {
        RenderHelperInvoker.invokeRenderBeaconBeam(context, pos.atY(0), colorComponents)

        // In jungle temple the Y is way above the crystal --> the title should be on the entrance Y
        RenderHelper.renderText(context, Component.literal(displayName), pos.atY(descY).center, 10f, true)
        val distance = context!!.camera().position.distanceTo(wPos)
        RenderHelper.renderText(
            context,
            Component.literal(Math.round(distance).toString() + "m"),
            pos.atY(descY - 1).center,
            6f,
            (minecraft.font.lineHeight + 1).toFloat(),
            true
        )
    }
}