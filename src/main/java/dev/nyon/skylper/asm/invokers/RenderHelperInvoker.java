package dev.nyon.skylper.asm.invokers;

import de.hysky.skyblocker.utils.render.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.core.BlockPos;
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
}
