package net.matheuses.bettereyes.mixin;

import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.world.item.trading.MerchantOffer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantOffer.class)
public abstract class VillagerOfferMixin {

    @Inject(method = "resetUses", at = @At("HEAD"), cancellable = true)
    private void bettereyes$keepEyeTradeSoldOut(CallbackInfo ci) {
        MerchantOffer offer = (MerchantOffer) (Object) this;

        if (offer.getResult().is(ModItems.EYE_02)) {
            ci.cancel();
        }
    }
}