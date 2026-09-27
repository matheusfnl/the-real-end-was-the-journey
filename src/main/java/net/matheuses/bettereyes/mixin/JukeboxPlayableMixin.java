package net.matheuses.bettereyes.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.item.component.CustomData;

import net.matheuses.bettereyes.item.ModItems;

@Mixin(JukeboxPlayable.class)
public class JukeboxPlayableMixin {
    @Inject(method = "tryInsertIntoJukebox", at = @At("HEAD"))
    private static void bettereyes$chooseEye08Song(
            Level level,
            BlockPos pos,
            ItemStack toInsert,
            Player player,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!toInsert.is(ModItems.EYE_08)) {
            return;
        }

        var state = level.getBlockState(pos);
        if (!state.is(Blocks.JUKEBOX)
                || state.getValue(JukeboxBlock.HAS_RECORD)) {
            return;
        }

        CustomData customData =
                toInsert.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        ListTag discIds =
                customData.copyTag().getListOrEmpty("eye_08_discs");

        List<JukeboxPlayable> songs = new ArrayList<>();

        for (int i = 0; i < discIds.size(); i++) {
            Identifier id = Identifier.tryParse(discIds.getStringOr(i, ""));
            if (id == null) {
                continue;
            }

            Item item = BuiltInRegistries.ITEM.getValue(id);
            if (item == null || item == Items.AIR) {
                continue;
            }

            JukeboxPlayable song =
                    new ItemStack(item).get(DataComponents.JUKEBOX_PLAYABLE);

            if (song != null) {
                songs.add(song);
            }
        }

        if (!songs.isEmpty()) {
            JukeboxPlayable chosen =
                    songs.get(RandomSource.create().nextInt(songs.size()));

            toInsert.set(DataComponents.JUKEBOX_PLAYABLE, chosen);
        }
    }
}