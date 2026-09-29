package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.matheuses.bettereyes.portal.ModPortalFrameProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Mixin(EndPortalFrameBlock.class)
public abstract class EndPortalFrameBlockMixin {
    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void betterEyes$addEyeType(
            StateDefinition.Builder<Block, BlockState> builder,
            CallbackInfo ci) {
        builder.add(ModPortalFrameProperties.EYE_TYPE);
    }
}
