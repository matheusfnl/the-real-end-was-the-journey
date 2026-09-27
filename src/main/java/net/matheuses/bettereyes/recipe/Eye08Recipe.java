package net.matheuses.bettereyes.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import net.matheuses.bettereyes.item.ModItems;
import net.matheuses.bettereyes.tag.ModItemTags;

public final class Eye08Recipe extends CustomRecipe {
    public static final Eye08Recipe INSTANCE = new Eye08Recipe();

    public static final MapCodec<Eye08Recipe> CODEC =
            MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, Eye08Recipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    public static final RecipeSerializer<Eye08Recipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.items().size() != 9) {
            return false;
        }

        ItemStack enderPearl = ItemStack.EMPTY;
        List<ItemStack> discs = new ArrayList<>(8);

        for (ItemStack stack : input.items()) {
            if (stack.is(Items.ENDER_PEARL)) {
                if (!enderPearl.isEmpty()) {
                    return false;
                }

                enderPearl = stack;
            } else if (stack.is(ModItemTags.MUSIC_DISCS)) {
                if (discs.size() >= 8) {
                    return false;
                }

                discs.add(stack);
            } else {
                return false;
            }
        }

        if (enderPearl.isEmpty() || discs.size() != 8) {
            return false;
        }

        for (int i = 0; i < discs.size(); i++) {
            for (int j = i + 1; j < discs.size(); j++) {
                if (discs.get(i).getItem() == discs.get(j).getItem()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return new ItemStack(ModItems.EYE_08);
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}