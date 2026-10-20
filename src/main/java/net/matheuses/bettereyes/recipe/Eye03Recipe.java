package net.matheuses.bettereyes.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

public class Eye03Recipe extends CustomRecipe {
    public static final Eye03Recipe INSTANCE = new Eye03Recipe();
    public static final MapCodec<Eye03Recipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Eye03Recipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<Eye03Recipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static final List<Ingredient> INGREDIENTS = List.of(
            Ingredient.of(Items.ENDER_PEARL),
            Ingredient.of(Items.NETHER_STAR),
            Ingredient.of(Items.ECHO_SHARD));

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != INGREDIENTS.size()) {
            return false;
        }

        boolean enderPearl = false;
        boolean netherStar = false;
        boolean echoShard = false;
        for (ItemStack stack : input.items()) {
            if (stack.is(Items.ENDER_PEARL) && !enderPearl) {
                enderPearl = true;
            } else if (stack.is(Items.NETHER_STAR) && !netherStar) {
                netherStar = true;
            } else if (stack.is(Items.ECHO_SHARD) && !echoShard) {
                echoShard = true;
            } else {
                return false;
            }
        }
        return enderPearl && netherStar && echoShard;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return new ItemStack(ModItems.EYE_03);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            if (input.getItem(i).is(Items.NETHER_STAR)) {
                remaining.set(i, input.getItem(i).copy());
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(INGREDIENTS);
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                INGREDIENTS.stream().map(Ingredient::display).toList(),
                new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(ModItems.EYE_03)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
