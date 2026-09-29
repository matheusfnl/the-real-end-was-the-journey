package net.matheuses.bettereyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;

@Mixin(StrongholdPieces.PortalRoom.class)
public abstract class StrongholdPortalRoomMixin {
    @ModifyConstant(method = "postProcess", constant = @Constant(floatValue = 0.9F))
    private float betterEyes$disableGeneratedEyes(float original) {
        return 1.0F;
    }
}
