package net.matheuses.bettereyes.portal;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
public final class ModPortalFrameProperties {
    public static final IntegerProperty EYE_TYPE
        = IntegerProperty.create("betterEyes$eye", 0, 12);

    private ModPortalFrameProperties() {
    }
}
