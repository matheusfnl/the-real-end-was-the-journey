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

    private static List<Ingredient> ingredients() {
        return List.of(
                Ingredient.of(Items.ENDER_PEARL),
                Ingredient.of(Items.ARMADILLO_SCUTE),
                Ingredient.of(Items.GOAT_HORN),
                Ingredient.of(Items.CREAKING_HEART),
                Ingredient.of(Items.TOTEM_OF_UNDYING),
                Ingredient.of(Items.TURTLE_SCUTE));
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
                        new ItemStackTemplate(ModItems.EYE_05)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}