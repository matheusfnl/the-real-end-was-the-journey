package net.matheuses.bettereyes.data;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.matheuses.bettereyes.BetterEyes;

public class ModAttachments {
    public static final AttachmentType<Boolean> IGLOO_ORIGIN =
        AttachmentRegistry.<Boolean>builder()
            .persistent(Codec.BOOL)
            .copyOnDeath()
            .buildAndRegister(BetterEyes.id("igloo_origin"));

    public static final AttachmentType<Boolean> EYE_TRADE_USED =
        AttachmentRegistry.<Boolean>builder()
            .persistent(Codec.BOOL)
            .copyOnDeath()
            .buildAndRegister(BetterEyes.id("eye_trade_used"));

    public static void initialize() {
    }
}
