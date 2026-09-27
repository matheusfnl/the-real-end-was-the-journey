package net.matheuses.bettereyes.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import net.matheuses.bettereyes.BetterEyes;

public final class ModItemTags {
    public static final TagKey<Item> MUSIC_DISCS =
            TagKey.create(Registries.ITEM, BetterEyes.id("music_discs"));

    private ModItemTags() {
    }
}