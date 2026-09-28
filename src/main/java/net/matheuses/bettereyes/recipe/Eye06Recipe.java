package net.matheuses.bettereyes.recipe;

import com.mojang.serialization.MapCodec;

import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class Eye06Recipe extends CustomRecipe {
    public static final Eye06Recipe INSTANCE = new Eye06Recipe();

    public static final MapCodec<Eye06Recipe> CODEC = MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, Eye06Recipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static final RecipeSerializer<Eye06Recipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 7) {
            return false;
        }

        boolean hasEnderPearl = false;
        boolean hasRedSoup = false;
        boolean hasBrownSoup = false;
        boolean hasMushroomStew = false;
        boolean hasRabbitStew = false;
        boolean hasBeetrootSoup = false;
        boolean hasSuspiciousStew = false;

        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(Items.ENDER_PEARL) && !hasEnderPearl) {
                hasEnderPearl = true;
            } else if (stack.is(ModItems.CONCENTRATED_RED_MUSHROOM_SOUP)
                    && !hasRedSoup) {
                hasRedSoup = true;
            } else if (stack.is(ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP)
                    && !hasBrownSoup) {
                hasBrownSoup = true;
            } else if (stack.is(Items.MUSHROOM_STEW) && !hasMushroomStew) {
                hasMushroomStew = true;
            } else if (stack.is(Items.RABBIT_STEW) && !hasRabbitStew) {
                hasRabbitStew = true;
            } else if (stack.is(Items.BEETROOT_SOUP) && !hasBeetrootSoup) {
                hasBeetrootSoup = true;
            } else if (stack.is(Items.SUSPICIOUS_STEW)
                    && !hasSuspiciousStew
                    && stack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
                hasSuspiciousStew = true;
            } else {
                return false;
            }
        }

        return hasEnderPearl
                && hasRedSoup
                && hasBrownSoup
                && hasMushroomStew
                && hasRabbitStew
                && hasBeetrootSoup
                && hasSuspiciousStew;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (stack.is(Items.SUSPICIOUS_STEW)
                    && stack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
                ItemStack result = new ItemStack(ModItems.EYE_06);

                result.copyFrom(
                        DataComponents.SUSPICIOUS_STEW_EFFECTS,
                        stack);

                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
