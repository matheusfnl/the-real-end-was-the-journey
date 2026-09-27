package net.matheuses.bettereyes.mixin;

import java.util.Optional;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.matheuses.bettereyes.data.ModAttachments;
import net.matheuses.bettereyes.structure.IglooTemplateAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin implements IglooTemplateAccess {

    @Unique
    private boolean bettereyes$iglooBasement;

    @Override
    public boolean bettereyes$isIglooBasement() {
        return this.bettereyes$iglooBasement;
    }

    @Override
    public void bettereyes$setIglooBasement(boolean value) {
        this.bettereyes$iglooBasement = value;
    }

    @ModifyExpressionValue(
        method = "placeEntities",
        at = @At(
            value = "INVOKE",
            target =
                "Lnet/minecraft/world/level/levelgen/structure/"
                + "templatesystem/StructureTemplate;"
                + "createEntityIgnoreException("
                + "Lnet/minecraft/util/ProblemReporter;"
                + "Lnet/minecraft/world/level/ServerLevelAccessor;"
                + "Lnet/minecraft/nbt/CompoundTag;"
                + ")Ljava/util/Optional;"
        )
    )
    private Optional<Entity> bettereyes$markIglooZombie(
            Optional<Entity> original) {

        if (this.bettereyes$iglooBasement) {
            original.ifPresent(entity -> {
                if (entity instanceof ZombieVillager) {
                    ((AttachmentTarget) entity).setAttached(
                        ModAttachments.IGLOO_ORIGIN,
                        true
                    );
                }
            });
        }

        return original;
    }
}
