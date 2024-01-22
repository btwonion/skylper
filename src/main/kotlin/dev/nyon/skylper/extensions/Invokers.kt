package dev.nyon.skylper.extensions

import dev.nyon.skylper.mixins.FrustumInvoker
import net.minecraft.client.renderer.culling.Frustum


fun Frustum.isCubeInFrustum(
    minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double
): Boolean {
    return (this as FrustumInvoker).invokeCubeInFrustum(minX, minY, minZ, maxX, maxY, maxZ)
}