package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.ItemCost;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

@Mixin(Villager.class)
public abstract class HeroTradeWithBumMixin {
    private static final int MAX_EMERALD_COST = 8;
    private static final int MIN_EMERALD_COST = 16;

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void betterEyes$prepareHeroTrade(
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<?> callback
    ) {
        Villager villager = (Villager) (Object) this;

        if (
            villager.level().isClientSide() ||
            villager.isBaby() ||
            ! villager.getVillagerData()
            .profession()
            .is(VillagerProfession.NITWIT)) {
            return;
        }

        MerchantOffers offers = new MerchantOffers();

        if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            int emeraldCost = villager.getRandom().nextIntBetweenInclusive(MIN_EMERALD_COST, MAX_EMERALD_COST);

            offers.add(new MerchantOffer(
                new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(ModItems.BUM_ITEM),
                1,
                0,
                0.0F
            ));
        }

        villager.overrideOffers(offers);
    }
}
