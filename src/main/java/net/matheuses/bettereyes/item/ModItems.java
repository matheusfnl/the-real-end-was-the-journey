package net.matheuses.bettereyes.item;

import java.util.function.Function;

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

public class ModItems {
  public static final Item BUM_ITEM = registerItem("bum_item", Item::new);
  public static final Item EYE_02 = registerItem("eye_02", Item::new);

  private static Item registerItem(String name, Function<Item.Properties, Item> function) {
    Identifier id = BetterEyes.id(name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
    Item item = function.apply(new Item.Properties().setId(key));
    return Registry.register(BuiltInRegistries.ITEM, id, item);
  }

  public static void registerModItems() {
    BetterEyes.LOGGER.info("Registering Mod Items for " + BetterEyes.MOD_ID);

    CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
      output.insertAfter(Items.ENDER_EYE, ModItems.EYE_02);
    });
  }
}
