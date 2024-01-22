package dev.nyon.skylper.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.ParticleSpawnEvent;
import dev.nyon.skylper.extensions.math.Vec3Comparable;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
@SuppressWarnings("SpellCheckingInspection")
public class LevelRendererMixin {

    @Inject(
        method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V",
        at = @At("TAIL")
    )
    private void triggerParticleSpawnEvent(
        ParticleOptions options,
        boolean force,
        boolean decreased,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        CallbackInfo ci
    ) {
        EventHandler.INSTANCE.invokeEvent(new ParticleSpawnEvent(options, new Vec3Comparable(x, y, z)));
    }
}
