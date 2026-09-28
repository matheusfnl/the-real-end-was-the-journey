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

public final class Eye05Recipe extends CustomRecipe {
    public static final Eye05Recipe INSTANCE = new Eye05Recipe();

    public static final MapCodec<Eye05Recipe> CODEC = MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, Eye05Recipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static final RecipeSerializer<Eye05Recipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 6) {
            return false;
        }

        boolean hasEnderPearl = false;
        boolean hasArmadilloScute = false;
        boolean hasGoatHorn = false;
        boolean hasCreakingHeart = false;
        boolean hasTotem = false;
        boolean hasTurtleScute = false;

        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(Items.ENDER_PEARL) && !hasEnderPearl) {
                hasEnderPearl = true;
            } else if (stack.is(Items.ARMADILLO_SCUTE) && !hasArmadilloScute) {
                hasArmadilloScute = true;
            } else if (stack.is(Items.GOAT_HORN)
                    && !hasGoatHorn
                    && stack.has(DataComponents.INSTRUMENT)) {
                hasGoatHorn = true;
            } else if (stack.is(Items.CREAKING_HEART) && !hasCreakingHeart) {
                hasCreakingHeart = true;
            } else if (stack.is(Items.TOTEM_OF_UNDYING) && !hasTotem) {
                hasTotem = true;
            } else if (stack.is(Items.TURTLE_SCUTE) && !hasTurtleScute) {
                hasTurtleScute = true;
            } else {
                return false;
            }
        }

        return hasEnderPearl
                && hasArmadilloScute
                && hasGoatHorn
                && hasCreakingHeart
                && hasTotem
                && hasTurtleScute;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (stack.is(Items.GOAT_HORN)
                    && stack.has(DataComponents.INSTRUMENT)) {
                ItemStack result = new ItemStack(ModItems.EYE_05);

                result.copyFrom(DataComponents.INSTRUMENT, stack);

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