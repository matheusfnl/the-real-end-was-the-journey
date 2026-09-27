package net.matheuses.bettereyes.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MushroomCow.class)
public abstract class MooshroomSoupMixin {

    @ModifyExpressionValue(
        method = "mobInteract",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;MUSHROOM_STEW:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item bettereyes$useConcentratedSoup(Item original) {
        MushroomCow cow = (MushroomCow) (Object) this;

        if (cow.getVariant() == MushroomCow.Variant.BROWN) {
            return ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP;
        }

        return ModItems.CONCENTRATED_RED_MUSHROOM_SOUP;
    }
}