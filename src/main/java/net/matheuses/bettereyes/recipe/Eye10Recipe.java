package net.matheuses.bettereyes.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
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

import net.matheuses.bettereyes.item.ModItems;
import net.matheuses.bettereyes.tag.ModItemTags;

public final class Eye10Recipe extends CustomRecipe {
    public static final Eye10Recipe INSTANCE = new Eye10Recipe();

    public static final MapCodec<Eye10Recipe> CODEC = MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, Eye10Recipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static final RecipeSerializer<Eye10Recipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

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

    private static List<Ingredient> ingredients() {
        Ingredient leaf = Ingredient.of(
                BuiltInRegistries.ITEM.getOrThrow(ItemTags.LEAVES));

        return List.of(
                Ingredient.of(Items.ENDER_PEARL),
                leaf,
                leaf,
                leaf,
                leaf,
                leaf,
                leaf,
                leaf,
                leaf);
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
        return PlacementInfo.create(ingredients());
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                List.of(
                        new SlotDisplay.ItemSlotDisplay(Items.ENDER_PEARL),
                        new SlotDisplay.ItemSlotDisplay(Items.OAK_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.SPRUCE_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.BIRCH_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.JUNGLE_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.ACACIA_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.DARK_OAK_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.MANGROVE_LEAVES),
                        new SlotDisplay.ItemSlotDisplay(Items.CHERRY_LEAVES)),
                new SlotDisplay.ItemStackSlotDisplay(
                        new ItemStackTemplate(ModItems.EYE_10)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
