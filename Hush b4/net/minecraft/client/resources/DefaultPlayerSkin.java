// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin
{
    private static final ResourceLocation TEXTURE_STEVE;
    private static final ResourceLocation TEXTURE_ALEX;
    
    static {
        TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
        TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    }
    
    public static ResourceLocation getDefaultSkinLegacy() {
        return DefaultPlayerSkin.TEXTURE_STEVE;
    }
    
    public static ResourceLocation getDefaultSkin(final UUID playerUUID) {
        return isSlimSkin(playerUUID) ? DefaultPlayerSkin.TEXTURE_ALEX : DefaultPlayerSkin.TEXTURE_STEVE;
    }
    
    public static String getSkinType(final UUID playerUUID) {
        return isSlimSkin(playerUUID) ? "slim" : "default";
    }
    
    private static boolean isSlimSkin(final UUID playerUUID) {
        return (playerUUID.hashCode() & 0x1) == 0x1;
    }
}
