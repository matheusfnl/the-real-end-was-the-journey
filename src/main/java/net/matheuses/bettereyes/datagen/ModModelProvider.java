package net.matheuses.bettereyes.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
  public ModModelProvider(FabricPackOutput output) {
    super(output);
  }

  @Override
  public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerators) {
  }

  @Override
  public void generateItemModels(ItemModelGenerators itemModelGenerators) {
    itemModelGenerators.generateFlatItem(ModItems.EYE_01, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_02, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_03, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_04, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_05, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_06, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_07, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_08, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_09, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_10, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_11, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.EYE_12, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.ICE_APPLE, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.CONCENTRATED_BROWN_MUSHROOM_SOUP, ModelTemplates.FLAT_ITEM);
    itemModelGenerators.generateFlatItem(ModItems.CONCENTRATED_RED_MUSHROOM_SOUP, ModelTemplates.FLAT_ITEM);
  }
}
