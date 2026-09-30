package net.matheuses.bettereyes.mixin;

import net.matheuses.bettereyes.BetterEyes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GiveGiftToHero.class)
public abstract class GiveGiftToHeroMixin {
    @Unique
    private static final ResourceKey<LootTable> BETTER_EYES$NITWIT_GIFT = ResourceKey.create(
            Registries.LOOT_TABLE,
            BetterEyes.id("gameplay/hero_of_the_village/nitwit_gift"));

    @Inject(method = "getLootTableToThrow", at = @At("HEAD"), cancellable = true)
    private static void betterEyes$useNitwitGift(
            Villager villager,
            CallbackInfoReturnable<ResourceKey<LootTable>> callback) {
        if (villager.getVillagerData()
                .profession()
                .is(VillagerProfession.NITWIT)) {
            callback.setReturnValue(BETTER_EYES$NITWIT_GIFT);
        }
    }
}