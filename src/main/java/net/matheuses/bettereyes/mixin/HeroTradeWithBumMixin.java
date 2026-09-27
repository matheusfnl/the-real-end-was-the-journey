package net.matheuses.bettereyes.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.matheuses.bettereyes.access.HeroEffectCycleAccess;
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
    private final Map<UUID, Long> betterEyes$usedHeroCycles = new HashMap<>();

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void betterEyes$prepareHeroTrade(
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<?> callback) {
        Villager villager = (Villager) (Object) this;

        if (villager.level().isClientSide() ||
                villager.isBaby() ||
                !villager.getVillagerData()
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
                            .nextIntBetweenInclusive(MIN_EMERALD_COST, MAX_EMERALD_COST));

            MerchantOffer heroOffer = new MerchantOffer(
                    new ItemCost(Items.EMERALD, emeraldCost),
                    new ItemStack(ModItems.BUM_ITEM),
                    1,
                    0,
                    0.05F);

            HeroEffectCycleAccess cycleAccess = (HeroEffectCycleAccess) player;

            long currentCycle = cycleAccess.betterEyes$getHeroEffectCycle();

            long usedCycle = betterEyes$usedHeroCycles.getOrDefault(
                    player.getUUID(),
                    -1L);

            if (usedCycle == currentCycle) {
                heroOffer.setToOutOfStock();
            }

            heroOffers.add(heroOffer);
        }

        villager.setOffers(heroOffers);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$saveHeroTradePrices(
            ValueOutput output,
            CallbackInfo callback) {
        ValueOutput.ValueOutputList entries = output.childrenList("betterEyes$heroTradePrices");

        betterEyes$prices.forEach((playerId, price) -> {
            ValueOutput entry = entries.addChild();
            entry.putString("Player", playerId.toString());
            entry.putInt("Price", price);
        });

        ValueOutput.ValueOutputList cycles = output.childrenList("BetterEyesUsedHeroCycles");

        betterEyes$usedHeroCycles.forEach((playerId, cycle) -> {
            ValueOutput entry = cycles.addChild();
            entry.putString("Player", playerId.toString());
            entry.putLong("Cycle", cycle);
        });
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void betterEyes$loadHeroTradePrices(
            ValueInput input,
            CallbackInfo callback) {
        betterEyes$prices.clear();

        for (ValueInput entry : input.childrenListOrEmpty("betterEyes$heroTradePrices")) {
            String playerId = entry.getStringOr("Player", "");
            int price = entry.getIntOr("Price", MIN_EMERALD_COST);

            try {
                betterEyes$prices.put(UUID.fromString(playerId), price);
            } catch (IllegalArgumentException ignored) {
                // Ignora UUIDs inválidos em dados corrompidos.
            }
        }

        betterEyes$usedHeroCycles.clear();

        for (ValueInput entry : input.childrenListOrEmpty("BetterEyesUsedHeroCycles")) {
            String playerId = entry.getStringOr("Player", "");
            long cycle = entry.getLongOr("Cycle", -1L);

            try {
                betterEyes$usedHeroCycles.put(
                        UUID.fromString(playerId),
                        cycle);
            } catch (IllegalArgumentException ignored) {
                // Ignora UUID inválido.
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void betterEyes$closeTradeWithoutHero(CallbackInfo callback) {
        Villager villager = (Villager) (Object) this;

        if (!villager.getVillagerData()
                .profession()
                .is(VillagerProfession.NITWIT)) {
            return;
        }

        if (villager.getTradingPlayer() instanceof ServerPlayer player &&
                !player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            player.closeContainer();
        }
    }

    @Inject(method = "rewardTradeXp", at = @At("TAIL"))
    private void betterEyes$rememberCustomer(
            MerchantOffer offer,
            CallbackInfo callback) {
        Villager villager = (Villager) (Object) this;

        if (!offer.getResult().is(ModItems.BUM_ITEM) ||
                !(villager.getTradingPlayer() instanceof ServerPlayer player)) {
            return;
        }

        HeroEffectCycleAccess cycleAccess = (HeroEffectCycleAccess) player;

        betterEyes$usedHeroCycles.put(
                player.getUUID(),
                cycleAccess.betterEyes$getHeroEffectCycle());
    }
}
