package dev.nyon.skylper.skyblock.hollows.render

import de.hysky.skyblocker.utils.render.RenderHelper
import de.hysky.skyblocker.utils.waypoint.Waypoint
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.extensions.math.blockPos
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3


class HollowsStructureWaypoint(private val wPos: Vec3, private val structure: HollowsStructure) : Waypoint(
    wPos.blockPos, { Type.WAYPOINT }, structure.waypointColor.color.getRGBComponents(null), true
) {
    override fun render(context: WorldRenderContext?) {
        super.render(context)

        val posUp = wPos.add(0.0, 1.0, 0.0)
        RenderHelper.renderText(context, Component.literal(structure.displayName), posUp, true)
        val distance = context!!.camera().position.distanceTo(wPos)
        RenderHelper.renderText(
            context,
            Component.literal(Math.round(distance).toString() + "m"),
            posUp,
            1f,
            (minecraft.font.lineHeight + 1).toFloat(),
            true
        )
    }
}