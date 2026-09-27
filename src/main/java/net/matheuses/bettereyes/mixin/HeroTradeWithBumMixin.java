package net.matheuses.bettereyes.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@Mixin(Villager.class)
public abstract class HeroTradeWithBumMixin {
    private static final int MIN_EMERALD_COST = 8;

    private static final int MAX_EMERALD_COST = 16;

    @Unique
    private final Map<UUID, Integer> betterEyes$prices = new HashMap<>();

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

        MerchantOffers heroOffers = new MerchantOffers();

        if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            int emeraldCost = betterEyes$prices.computeIfAbsent(
                player.getUUID(),
                ignored -> villager.getRandom()
                    .nextIntBetweenInclusive(MIN_EMERALD_COST, MAX_EMERALD_COST)
            );

            heroOffers.add(new MerchantOffer(
                new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(ModItems.BUM_ITEM),
                1,
                0,
                0.05F
            ));
        }

        villager.setOffers(heroOffers);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$saveHeroTradePrices(
        ValueOutput output,
        CallbackInfo callback
    ) {
        ValueOutput.ValueOutputList entries =
            output.childrenList("betterEyes$heroTradePrices");

        betterEyes$prices.forEach((playerId, price) -> {
            ValueOutput entry = entries.addChild();
            entry.putString("Player", playerId.toString());
            entry.putInt("Price", price);
        });
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$loadHeroTradePrices(
      ValueInput input,
      CallbackInfo callback
    ) {
        betterEyes$prices.clear();

        for (
            ValueInput entry :
            input.childrenListOrEmpty("BetterEyesHeroTradePrices")
        ) {
            String playerId = entry.getStringOr("Player", "");
            int price = entry.getIntOr("Price", MIN_EMERALD_COST);

            try {
                betterEyes$prices.put(UUID.fromString(playerId), price);
            } catch (IllegalArgumentException ignored) {
                // Ignora UUIDs inválidos em dados corrompidos.
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void betterEyes$closeTradeWithoutHero(CallbackInfo callback) {
        Villager villager = (Villager) (Object) this;

        if (
            ! villager.getVillagerData()
            .profession()
            .is(VillagerProfession.NITWIT)
        ) {
            return;
        }

        if (
            villager.getTradingPlayer() instanceof ServerPlayer player &&
            !player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)
        ) {
            player.closeContainer();
        }
    }
}
