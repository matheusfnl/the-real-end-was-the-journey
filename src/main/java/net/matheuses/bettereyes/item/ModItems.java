package net.matheuses.bettereyes.item;

import net.matheuses.bettereyes.BetterEyes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
  public static final Item BUM_ITEM = registerItem("bum_item", Item::new);

  private static Item registerItem(String name, Function<Item.Properties, Item> function) {
    Identifier id = BetterEyes.id(name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
    Item item = function.apply(new Item.Properties().setId(key));
    return Registry.register(BuiltInRegistries.ITEM, id, item);
  }

  public static void registerModItems() {
    BetterEyes.LOGGER.info("Registering Mod Items for " + BetterEyes.MOD_ID);
  }
}
