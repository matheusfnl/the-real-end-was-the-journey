package net.matheuses.bettereyes.mixin;

import net.matheuses.bettereyes.mixin.access.HeroEffectCycleAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class HeroEffectCycleMixin
        implements HeroEffectCycleAccess {

    @Unique
    private long betterEyes$heroEffectCycle;

    @Unique
    private boolean betterEyes$heroEffectActive;

    @Inject(method = "tick", at = @At("TAIL"))
    private void betterEyes$updateHeroEffectCycle(
            CallbackInfo callback) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        boolean active = player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE);

        if (active && !betterEyes$heroEffectActive) {
            betterEyes$heroEffectCycle++;
        }

        betterEyes$heroEffectActive = active;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$saveHeroEffectCycle(
            ValueOutput output,
            CallbackInfo callback) {
        output.putLong(
                "BetterEyes$heroEffectCycle",
                betterEyes$heroEffectCycle);

        output.putBoolean(
                "BetterEyes$heroEffectActive",
                betterEyes$heroEffectActive);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$loadHeroEffectCycle(
            ValueInput input,
            CallbackInfo callback) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        betterEyes$heroEffectCycle = input.getLongOr("BetterEyes$heroEffectCycle", 0L);

        betterEyes$heroEffectActive = input.getBooleanOr(
                "BetterEyes$heroEffectActive",
                player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE));
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void betterEyes$restoreHeroEffectCycle(
            ServerPlayer previousPlayer,
            boolean keepEverything,
            CallbackInfo callback) {
        HeroEffectCycleAccess previous = (HeroEffectCycleAccess) previousPlayer;

        betterEyes$heroEffectCycle = previous.betterEyes$getHeroEffectCycle();

        ServerPlayer player = (ServerPlayer) (Object) this;

        betterEyes$heroEffectActive = player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE);
    }

    @Override
    public long betterEyes$getHeroEffectCycle() {
        return betterEyes$heroEffectCycle;
    }

    @Override
    public boolean betterEyes$isHeroEffectActive() {
        return betterEyes$heroEffectActive;
    }
}