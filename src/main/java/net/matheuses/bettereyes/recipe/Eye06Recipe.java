package net.matheuses.bettereyes.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.core.component.DataComponents;
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

    private static List<Ingredient> ingredients() {
        return List.of(
                Ingredient.of(Items.ENDER_PEARL),
                Ingredient.of(ModItems.CONCENTRATED_RED_MUSHROOM_SOUP),
                Ingredient.of(ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP),
                Ingredient.of(Items.MUSHROOM_STEW),
                Ingredient.of(Items.RABBIT_STEW),
                Ingredient.of(Items.BEETROOT_SOUP),
                Ingredient.of(Items.SUSPICIOUS_STEW));
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
                ingredients().stream()
                        .map(Ingredient::display)
                        .toList(),
                new SlotDisplay.ItemStackSlotDisplay(
                        new ItemStackTemplate(ModItems.EYE_06)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
