package dev.nyon.skylper.asm.invokers;

import de.hysky.skyblocker.utils.render.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderHelper.class)
public interface RenderHelperInvoker {

    @Invoker("renderBeaconBeam")
    static void invokeRenderBeaconBeam(
        WorldRenderContext context,
        BlockPos pos,
        float[] colorComponents
    ) {
        throw new AssertionError();
    }

    @Invoker("renderFilled")
    static void invokeRenderFilled(
        WorldRenderContext context,
        Vec3 pos,
        Vec3 dimensions,
        float[] colorComponents,
        float alpha,
        boolean throughWalls
    ) {
        throw new AssertionError();
    }
}
