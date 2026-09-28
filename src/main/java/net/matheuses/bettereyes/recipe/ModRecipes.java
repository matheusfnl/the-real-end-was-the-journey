package net.matheuses.bettereyes.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import net.matheuses.bettereyes.BetterEyes;

public final class ModRecipes {
    private ModRecipes() {
    }

    public static void registerModRecipes() {
        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                BetterEyes.id("eye_08_different_discs"),
                Eye08Recipe.SERIALIZER);
        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                BetterEyes.id("eye_05_with_horn"),
                Eye05Recipe.SERIALIZER);
    }
}