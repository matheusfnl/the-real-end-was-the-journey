package net.matheuses.bettereyes.portal;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.matheuses.bettereyes.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public final class ModPortalHandler {
    private static BlockPattern frameShape;

    private ModPortalHandler() {
    }

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            BlockPos framePos = hitResult.getBlockPos();
            BlockState frameState = level.getBlockState(framePos);

            if (!frameState.is(Blocks.END_PORTAL_FRAME)) {
                return InteractionResult.PASS;
            }

            ItemStack stack = player.getItemInHand(hand);
            Item item = stack.getItem();

            // O olho vanilla nunca pode ser colocado no frame.
            if (item == Items.ENDER_EYE) {
                return InteractionResult.FAIL;
            }

            int eyeType = getEyeType(item);

            // Outros itens continuam com o comportamento normal.
            if (eyeType == 0) {
                return InteractionResult.PASS;
            }

            int currentEyeType = frameState.getValue(ModPortalFrameProperties.EYE_TYPE);

            // Um frame que já possui um olho novo não aceita outro.
            if (frameState.getValue(EndPortalFrameBlock.HAS_EYE)
                    && currentEyeType != 0) {
                return InteractionResult.FAIL;
            }

            BlockPattern.BlockPatternMatch frameMatch = getOrCreateFrameShape().find(level, framePos);

            if (frameMatch != null
                    && containsEyeType(frameMatch, eyeType, framePos)) {
                return InteractionResult.FAIL;
            }

            // No cliente, informa que a interação deve ser enviada ao servidor.
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            BlockState filledState = frameState
                    .setValue(EndPortalFrameBlock.HAS_EYE, true)
                    .setValue(ModPortalFrameProperties.EYE_TYPE, eyeType);

            Block.pushEntitiesUp(frameState, filledState, level, framePos);
            level.setBlock(framePos, filledState, 2);
            level.updateNeighbourForOutputSignal(
                    framePos,
                    Blocks.END_PORTAL_FRAME);

            stack.consume(1, player);
            level.levelEvent(1503, framePos, 0);

            tryActivatePortal(level, framePos);

            return InteractionResult.SUCCESS;
        });
    }

    private static int getEyeType(Item item) {
        if (item == ModItems.EYE_01) {
            return 1;
        }

        if (item == ModItems.EYE_02) {
            return 2;
        }

        if (item == ModItems.EYE_03) {
            return 3;
        }

        if (item == ModItems.EYE_04) {
            return 4;
        }

        if (item == ModItems.EYE_05) {
            return 5;
        }

        if (item == ModItems.EYE_06) {
            return 6;
        }

        if (item == ModItems.EYE_07) {
            return 7;
        }

        if (item == ModItems.EYE_08) {
            return 8;
        }

        if (item == ModItems.EYE_09) {
            return 9;
        }

        if (item == ModItems.EYE_10) {
            return 10;
        }

        if (item == ModItems.EYE_11) {
            return 11;
        }

        if (item == ModItems.EYE_12) {
            return 12;
        }

        return 0;
    }

    private static boolean containsEyeType(
            BlockPattern.BlockPatternMatch match,
            int eyeType,
            BlockPos ignoredPos) {
        for (PatternFrame frame : getPatternFrames(match)) {
            BlockInWorld block = match.getBlock(frame.x(), frame.y(), 0);

            if (block.getPos().equals(ignoredPos)) {
                continue;
            }

            if (block.getState()
                    .getValue(ModPortalFrameProperties.EYE_TYPE) == eyeType) {
                return true;
            }
        }

        return false;
    }

    private static void tryActivatePortal(Level level, BlockPos insertedPos) {
        // Uma nova busca é necessária porque a busca anterior guardou
        // em cache o estado vazio do frame alterado.
        BlockPattern.BlockPatternMatch frameMatch = getOrCreateFrameShape().find(level, insertedPos);

        if (frameMatch == null || !hasEveryEyeExactlyOnce(frameMatch)) {
            return;
        }

        BlockPattern.BlockPatternMatch vanillaMatch = EndPortalFrameBlock.getOrCreatePortalShape()
                .find(level, insertedPos);

        if (vanillaMatch == null) {
            return;
        }

        BlockPos portalCorner = vanillaMatch
                .getFrontTopLeft()
                .offset(-3, 0, -3);

        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                BlockPos portalPos = portalCorner.offset(x, 0, z);

                level.destroyBlock(portalPos, true, null);
                level.setBlock(
                        portalPos,
                        Blocks.END_PORTAL.defaultBlockState(),
                        2);
            }
        }

        level.globalLevelEvent(
                1038,
                portalCorner.offset(1, 0, 1),
                0);
    }

    private static boolean hasEveryEyeExactlyOnce(
            BlockPattern.BlockPatternMatch match) {
        boolean[] found = new boolean[13];

        for (PatternFrame frame : getPatternFrames(match)) {
            BlockState state = match
                    .getBlock(frame.x(), frame.y(), 0)
                    .getState();

            if (!state.getValue(EndPortalFrameBlock.HAS_EYE)) {
                return false;
            }

            int eyeType = state.getValue(ModPortalFrameProperties.EYE_TYPE);

            if (eyeType < 1 || eyeType > 12 || found[eyeType]) {
                return false;
            }

            found[eyeType] = true;
        }

        for (int eyeType = 1; eyeType <= 12; eyeType++) {
            if (!found[eyeType]) {
                return false;
            }
        }

        return true;
    }

    private static List<PatternFrame> getPatternFrames(
            BlockPattern.BlockPatternMatch match) {
        return List.of(
                new PatternFrame(1, 0),
                new PatternFrame(2, 0),
                new PatternFrame(3, 0),

                new PatternFrame(0, 1),
                new PatternFrame(4, 1),
                new PatternFrame(0, 2),
                new PatternFrame(4, 2),
                new PatternFrame(0, 3),
                new PatternFrame(4, 3),

                new PatternFrame(1, 4),
                new PatternFrame(2, 4),
                new PatternFrame(3, 4));
    }

    private static BlockPattern getOrCreateFrameShape() {
        if (frameShape == null) {
            frameShape = BlockPatternBuilder.start()
                    .aisle(
                            "?vvv?",
                            ">???<",
                            ">???<",
                            ">???<",
                            "?^^^?")
                    .where(
                            '?',
                            BlockInWorld.hasState(
                                    BlockStatePredicate.ANY))
                    .where(
                            '^',
                            frameFacing(Direction.SOUTH))
                    .where(
                            '>',
                            frameFacing(Direction.WEST))
                    .where(
                            'v',
                            frameFacing(Direction.NORTH))
                    .where(
                            '<',
                            frameFacing(Direction.EAST))
                    .build();
        }

        return frameShape;
    }

    private static java.util.function.Predicate<BlockInWorld> frameFacing(
            Direction direction) {
        return BlockInWorld.hasState(
                BlockStatePredicate
                        .forBlock(Blocks.END_PORTAL_FRAME)
                        .where(
                                EndPortalFrameBlock.FACING,
                                Predicates.equalTo(direction)));
    }

    private record PatternFrame(int x, int y) {
    }
}
