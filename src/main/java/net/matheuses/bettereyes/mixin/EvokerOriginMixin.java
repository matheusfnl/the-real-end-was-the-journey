package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.matheuses.bettereyes.access.EvokerOriginAccess;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Raider.class)
public abstract class EvokerOriginMixin  implements EvokerOriginAccess {
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
}
