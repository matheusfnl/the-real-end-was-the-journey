package net.matheuses.bettereyes.mixin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    @Unique
    private final Set<UUID> betterEyes$customers = new HashSet<>();

    @Unique
    private final Map<UUID, Long> betterEyes$blockedUntil = new HashMap<>();

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
            UUID playerId = player.getUUID();
            long currentTick = villager.level().getGameTime();
            int emeraldCost = betterEyes$prices.computeIfAbsent(
                player.getUUID(),
                ignored -> villager.getRandom()
                    .nextIntBetweenInclusive(MIN_EMERALD_COST, MAX_EMERALD_COST)
            );

            MerchantOffer heroOffer = new MerchantOffer(
                new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(ModItems.BUM_ITEM),
                1,
                0,
                0.05F
            );

            long blockedUntil =
                betterEyes$blockedUntil.getOrDefault(playerId, 0L);

            if (currentTick < blockedUntil) {
                heroOffer.setToOutOfStock();
            } else {
                betterEyes$blockedUntil.remove(playerId);
            }

            heroOffers.add(heroOffer);
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

        ValueOutput.ValueOutputList customers =
            output.childrenList("betterEyes$heroTradeCustomers");

        betterEyes$customers.forEach(playerId -> {
            ValueOutput entry = customers.addChild();
            entry.putString("Player", playerId.toString());
        });

        ValueOutput.ValueOutputList blocks =
            output.childrenList("BetterEyesHeroTradeBlocks");

        betterEyes$blockedUntil.forEach((playerId, expirationTick) -> {
            ValueOutput entry = blocks.addChild();
            entry.putString("Player", playerId.toString());
            entry.putLong("Until", expirationTick);
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
            input.childrenListOrEmpty("betterEyes$heroTradePrices")
        ) {
            String playerId = entry.getStringOr("Player", "");
            int price = entry.getIntOr("Price", MIN_EMERALD_COST);

            try {
                betterEyes$prices.put(UUID.fromString(playerId), price);
            } catch (IllegalArgumentException ignored) {
                // Ignora UUIDs inválidos em dados corrompidos.
            }
        }

        betterEyes$customers.clear();

        for (
            ValueInput entry :
            input.childrenListOrEmpty("betterEyes$heroTradeCustomers")
        ) {
            String playerId = entry.getStringOr("Player", "");

            try {
                betterEyes$customers.add(UUID.fromString(playerId));
            } catch (IllegalArgumentException ignored) {
                // Ignora UUIDs inválidos.
            }
        }

        betterEyes$blockedUntil.clear();

        for (
            ValueInput entry :
            input.childrenListOrEmpty("BetterEyesHeroTradeBlocks")
        ) {
            String playerId = entry.getStringOr("Player", "");
            long expirationTick = entry.getLongOr("Until", 0L);

            try {
                betterEyes$blockedUntil.put(
                    UUID.fromString(playerId),
                    expirationTick
                );
            } catch (IllegalArgumentException ignored) {
                // Ignora UUID inválido.
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
            betterEyes$blockedUntil.remove(player.getUUID());
            player.closeContainer();
        }
    }

    @Inject(method = "rewardTradeXp", at = @At("TAIL"))
    private void betterEyes$rememberCustomer(
        MerchantOffer offer,
        CallbackInfo callback
    ) {
        Villager villager = (Villager) (Object) this;

        if (
          !offer.getResult().is(ModItems.BUM_ITEM) ||
          !(villager.getTradingPlayer() instanceof ServerPlayer player)
        ) {
            return;
        }

        var heroEffect =
          player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);

        if (heroEffect != null) {
            long expirationTick =
                villager.level().getGameTime() +
                heroEffect.getDuration();

            betterEyes$blockedUntil.put(
                player.getUUID(),
                expirationTick
            );
        }
    }
}
