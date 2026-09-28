package net.matheuses.bettereyes.item;

import java.util.function.Function;
import java.util.List;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.matheuses.bettereyes.BetterEyes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModItems {
  public static final Item BUM_ITEM = registerItem("bum_item", Item::new);

  public static final Item CONCENTRATED_BROWN_MUSHROOM_SOUP = registerItem("concentrated_brown_mushroom_soup", properties ->
      new Item(properties.stacksTo(1).food(
          new FoodProperties.Builder()
              .nutrition(6)
              .saturationModifier(1.2F)
              .build(),
          Consumables.defaultFood()
              .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                  new MobEffectInstance(MobEffects.REGENERATION, 600, 0),
                  new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0),
                  new MobEffectInstance(MobEffects.RESISTANCE, 300, 0)
              )))
              .build()
      ).usingConvertsTo(Items.BOWL))
  );
  public static final Item CONCENTRATED_RED_MUSHROOM_SOUP = registerItem("concentrated_red_mushroom_soup", properties ->
      new Item(properties.stacksTo(1).food(
          new FoodProperties.Builder()
              .nutrition(6)
              .saturationModifier(1.2F)
              .build(),
          Consumables.defaultFood()
              .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                  new MobEffectInstance(MobEffects.REGENERATION, 600, 0),
                  new MobEffectInstance(MobEffects.SATURATION, 300, 0),
                  new MobEffectInstance(MobEffects.JUMP_BOOST, 300, 0)
              )))
              .build()
      ).usingConvertsTo(Items.BOWL))
  );

  public static final Item ICE_APPLE = registerItem("ice_apple", properties ->
      new Item(properties.food(
          new FoodProperties.Builder()
              .nutrition(4)
              .saturationModifier(1.2F)
              .build(),
          Consumables.defaultFood()
              .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                  new MobEffectInstance(MobEffects.SLOWNESS, 6000, 0),
                  new MobEffectInstance(MobEffects.REGENERATION, 6000, 2),
                  new MobEffectInstance(MobEffects.STRENGTH, 6000, 1)
              )))
              .build()
      ))
  );

  public static final Item EYE_02 = registerItem("eye_02", Item::new);
  public static final Item EYE_04 = registerItem("eye_04", Item::new);
  public static final Item EYE_06 = registerItem("eye_06", Item::new);
  public static final Item EYE_08 = registerItem("eye_08", Item::new);

  public static final Item EYE_01 = registerItem("eye_01", Item::new);
  public static final Item EYE_03 = registerItem("eye_03", Item::new);
  public static final Item EYE_05 = registerItem("eye_05", Item::new);
  public static final Item EYE_07 = registerItem("eye_07", Item::new);
  public static final Item EYE_09 = registerItem("eye_09", Item::new);
  public static final Item EYE_11 = registerItem("eye_11", Item::new);

  private static Item registerItem(String name, Function<Item.Properties, Item> function) {
    Identifier id = BetterEyes.id(name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
    Item item = function.apply(new Item.Properties().setId(key));
    return Registry.register(BuiltInRegistries.ITEM, id, item);
  }

  public static void registerModItems() {
    BetterEyes.LOGGER.info("Registering Mod Items for " + BetterEyes.MOD_ID);

    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
      output.insertBefore(Items.ENDER_EYE, ModItems.BUM_ITEM);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_11);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_09);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_08);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_07);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_06);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_05);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_04);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_03);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_02);
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_01);
    });

    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
      output.insertAfter(Items.ENCHANTED_GOLDEN_APPLE, ModItems.ICE_APPLE);
      output.insertAfter(Items.SUSPICIOUS_STEW, ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP);
      output.insertAfter(Items.SUSPICIOUS_STEW, ModItems.CONCENTRATED_RED_MUSHROOM_SOUP);
    });
  }
}
