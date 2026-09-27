package net.matheuses.bettereyes.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.matheuses.bettereyes.data.ModAttachments;
import net.matheuses.bettereyes.item.ModItems;
import net.matheuses.bettereyes.trade.IglooTrades;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class VillagerTradesMixin {
    @Inject(
        method = "notifyTrade",
        at = @At("RETURN")
    )
    private void bettereyes$removeIglooTradeAfterPurchase(
            MerchantOffer offer,
            CallbackInfo ci) {
        if (!offer.getResult().is(ModItems.EYE_02)
                || !((Object) this instanceof Villager villager)) {
            return;
        }

        AttachmentTarget attachments = (AttachmentTarget) villager;
        if (!attachments.getAttachedOrElse(ModAttachments.IGLOO_ORIGIN, false)) {
            return;
        }

        attachments.setAttached(ModAttachments.EYE_TRADE_USED, true);
        villager.getOffers().remove(offer);
    }

    @Inject(
        method = "getOffers",
        at = @At("RETURN")
    )
    private void bettereyes$addIglooTrade(
            CallbackInfoReturnable<MerchantOffers> cir) {

        if ((Object) this instanceof Villager villager) {
            IglooTrades.ensureSpecialTrade(
                villager,
                cir.getReturnValue()
            );
        }
    }
}
