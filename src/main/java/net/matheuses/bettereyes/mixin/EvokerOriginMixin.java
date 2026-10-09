package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.matheuses.bettereyes.access.EvokerOriginAccess;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Raider.class)
public abstract class EvokerOriginMixin implements EvokerOriginAccess {
    @Unique
    boolean betterEyes$spawnedInMansion = false;

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void betterEyes$rememberOrigin(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            EntitySpawnReason spawnReason,
            SpawnGroupData spawnGroupData,
            CallbackInfoReturnable<SpawnGroupData> callback) {
        betterEyes$spawnedInMansion = spawnReason == EntitySpawnReason.STRUCTURE;
    }

    @Override
    public boolean betterEyes$spawnedInMansion() {
        return betterEyes$spawnedInMansion;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$saveOrigin(ValueOutput output, CallbackInfo callback) {
        output.putBoolean("BetterEyes$spawnedInMansion",
                betterEyes$spawnedInMansion);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$loadOrigin(ValueInput input, CallbackInfo callback) {
        betterEyes$spawnedInMansion = input.getBooleanOr(
                "BetterEyes$spawnedInMansion", false);
    }
}
