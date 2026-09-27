package net.matheuses.bettereyes.mixin;

import net.matheuses.bettereyes.trade.IglooTrades;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.MerchantOffers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class VillagerTradesMixin {
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
