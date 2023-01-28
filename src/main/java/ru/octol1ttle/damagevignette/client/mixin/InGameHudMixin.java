package ru.octol1ttle.damagevignette.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.octol1ttle.damagevignette.client.DamageVignette;
import ru.octol1ttle.damagevignette.client.DamageVignetteConfig;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @SuppressWarnings("DataFlowIssue")
    @ModifyConstant(method = "renderVignetteOverlay", constant = @Constant(floatValue = 0.0f, ordinal = 0))
    private float healthVignetteOverlay(float f) {
        LivingEntity entity = (LivingEntity) client.getCameraEntity();
        float hpScaled = entity.getHealth() / entity.getMaxHealth();
        float confMin = MathHelper.clamp(DamageVignetteConfig.CONFIG.minThreshold / 100.0f, 0.0f, DamageVignetteConfig.CONFIG.maxThreshold / 100.0f);
        float confMax = MathHelper.clamp(DamageVignetteConfig.CONFIG.maxThreshold / 100.0f, confMin, 1.0f);
        DamageVignette.curOpacity = 1.0f - (hpScaled * (confMax - confMin) + confMin);
        return MathHelper.clamp(DamageVignette.curOpacity, 0.0f, 1.0f);
    }
}
