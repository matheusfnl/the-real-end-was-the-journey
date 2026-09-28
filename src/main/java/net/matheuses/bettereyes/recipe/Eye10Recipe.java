package net.matheuses.bettereyes.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import net.matheuses.bettereyes.item.ModItems;

public final class Eye10Recipe extends CustomRecipe {
    public static final Eye10Recipe INSTANCE = new Eye10Recipe();

    public static final MapCodec<Eye10Recipe> CODEC = MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, Eye10Recipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    public static final RecipeSerializer<Eye10Recipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 9) {
            return false;
        }

        boolean hasEnderPearl = false;
        List<ItemStack> leaves = new ArrayList<>(8);

        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(Items.ENDER_PEARL) && !hasEnderPearl) {
                hasEnderPearl = true;
            } else if (stack.is(ItemTags.LEAVES) && leaves.size() < 8) {
                leaves.add(stack);
            } else {
                return false;
            }
        }

        if (!hasEnderPearl || leaves.size() != 8) {
            return false;
        }

        for (int i = 0; i < leaves.size(); i++) {
            for (int j = i + 1; j < leaves.size(); j++) {
                if (leaves.get(i).getItem() == leaves.get(j).getItem()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return new ItemStack(ModItems.EYE_10);
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
