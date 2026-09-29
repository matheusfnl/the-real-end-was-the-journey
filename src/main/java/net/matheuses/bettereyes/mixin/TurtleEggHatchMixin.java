package net.matheuses.bettereyes.mixin;

import net.matheuses.bettereyes.BetterEyes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggHatchMixin {
    private static final Identifier HUNT_PATH =
            BetterEyes.id("exploration/paths/hunt");
    private static final Identifier HATCH_ADVANCEMENT =
            BetterEyes.id("exploration/hunt/hatch_turtle_egg");

    @Inject(method = "randomTick", at = @At("TAIL"))
    private void bettereyes$awardTurtleEggHatch(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random,
            CallbackInfo callback) {
        if (!state.is(Blocks.TURTLE_EGG)
                || state.getValue(TurtleEggBlock.HATCH)
                    != TurtleEggBlock.MAX_HATCH_LEVEL
                || level.getBlockState(pos).is(Blocks.TURTLE_EGG)) {
            return;
        }

        AdvancementHolder path =
                level.getServer().getAdvancements().get(HUNT_PATH);
        AdvancementHolder advancement =
                level.getServer().getAdvancements().get(HATCH_ADVANCEMENT);

        if (path == null || advancement == null) {
            return;
        }

        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5) > 256.0) {
                continue;
            }

            if (!player.getAdvancements()
                    .getOrStartProgress(path)
                    .isDone()) {
                continue;
            }

            player.getAdvancements().award(
                    advancement,
                    "hatch_turtle_egg");
        }
    }
}
