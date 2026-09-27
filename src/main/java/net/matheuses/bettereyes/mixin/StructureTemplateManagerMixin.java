package net.matheuses.bettereyes.mixin;

import java.util.Optional;

import net.matheuses.bettereyes.structure.IglooTemplateAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplateManager.class)
public abstract class StructureTemplateManagerMixin {
    @Unique
    private static final Identifier BETTEREYES_IGLOO_BOTTOM =
        Identifier.withDefaultNamespace("igloo/bottom");

    @Inject(
        method = "get",
        at = @At("RETURN")
    )
    private void bettereyes$identifyIglooTemplate(
            Identifier id,
            CallbackInfoReturnable<Optional<StructureTemplate>> cir) {

        boolean isIglooBasement =
            BETTEREYES_IGLOO_BOTTOM.equals(id);

        cir.getReturnValue().ifPresent(template -> {
            ((IglooTemplateAccess) template)
                .bettereyes$setIglooBasement(isIglooBasement);
        });
    }
}