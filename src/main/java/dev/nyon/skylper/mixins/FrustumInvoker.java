package dev.nyon.skylper.mixins;

import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Frustum.class)
public interface FrustumInvoker {

    @Invoker
    boolean invokeCubeInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);
}
