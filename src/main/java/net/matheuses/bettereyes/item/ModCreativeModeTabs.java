package net.matheuses.bettereyes.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.matheuses.bettereyes.BetterEyes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> BETTER_EYES_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            BetterEyes.id("better_eyes"));

    public static void registerModCreativeModeTabs() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                BETTER_EYES_TAB,
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.better-eyes"))
                        .icon(() -> new ItemStack(ModItems.EYE_01))
                        .displayItems((context, output) -> {
                            output.accept(ModItems.EYE_01);
                            output.accept(ModItems.EYE_02);
                            output.accept(ModItems.EYE_03);
                            output.accept(ModItems.EYE_04);
                            output.accept(ModItems.EYE_05);
                            output.accept(ModItems.EYE_06);
                            output.accept(ModItems.EYE_07);
                            output.accept(ModItems.EYE_08);
                            output.accept(ModItems.EYE_09);
                            output.accept(ModItems.EYE_11);
                            output.accept(ModItems.ICE_APPLE);
                            output.accept(ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP);
                            output.accept(ModItems.CONCENTRATED_RED_MUSHROOM_SOUP);
                            output.accept(ModItems.BUM_ITEM);
                        })
                        .build());
    }
}