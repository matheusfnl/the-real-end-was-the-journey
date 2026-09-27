package net.matheuses.bettereyes.trade;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.matheuses.bettereyes.data.ModAttachments;
import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public final class IglooTrades {
    private IglooTrades() {
    }

    public static void ensureSpecialTrade(
            Villager villager,
            MerchantOffers offers) {

        boolean fromIgloo =
            ((AttachmentTarget) villager).getAttachedOrElse(
                ModAttachments.IGLOO_ORIGIN,
                false
            );

        if (!fromIgloo) {
            return;
        }

        boolean alreadyHasTrade = offers.stream()
            .anyMatch(offer ->
                offer.getResult().is(ModItems.EYE_02)
            );

        if (alreadyHasTrade) {
            return;
        }

        offers.add(new MerchantOffer(
            new ItemCost(Items.EMERALD, 16),
            new ItemStack(ModItems.EYE_02, 1),
            3,
            5,
            0.05F
        ));
    }
}