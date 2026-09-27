package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.matheuses.bettereyes.access.EvokerOriginAccess;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLootMixin {
    private static final Identifier KILL_DRAGON = Identifier.withDefaultNamespace("end/kill_dragon");

    @Inject(method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/" +
            "world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;)V", at = @At("HEAD"), cancellable = true)
    private void betterEyes$filterEvokerTotem(
            ServerLevel level,
            DamageSource damageSource,
            boolean causedByPlayer,
            ResourceKey<LootTable> lootTable,
            CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Evoker evoker)) {
            return;
        }

        boolean fromMansion = ((EvokerOriginAccess) evoker).betterEyes$spawnedInMansion();
        Player lastPlayer = evoker.getLastHurtByPlayer();

        boolean playerFinishedGame = lastPlayer instanceof ServerPlayer serverPlayer
                && betterEyes$killedDragon(serverPlayer, level);

        if (fromMansion || playerFinishedGame) {
            return; // Mantém o loot vanilla.
        }

        entity.dropFromLootTable(
                level,
                damageSource,
                causedByPlayer,
                lootTable,
                stack -> {
                    if (!stack.is(Items.TOTEM_OF_UNDYING)) {
                        entity.spawnAtLocation(level, stack);
                    }
                });

        callback.cancel();
    }

    @Unique
    private static boolean betterEyes$killedDragon(
            ServerPlayer player,
            ServerLevel level) {
        AdvancementHolder advancement = level.getServer().getAdvancements().get(KILL_DRAGON);

        return advancement != null
                && player.getAdvancements()
                        .getOrStartProgress(advancement)
                        .isDone();
    }
}
