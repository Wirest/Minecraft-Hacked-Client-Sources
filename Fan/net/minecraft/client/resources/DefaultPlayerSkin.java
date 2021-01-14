package net.minecraft.client.resources;

import java.util.UUID;

import cedo.altmanager.BetterAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");

    /** The default skin for the Alex model. */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

    /**
     * Returns the default skind for versions prior to 1.8, which is always the Steve texture.
     */
    public static ResourceLocation getDefaultSkinLegacy()
    {
        return TEXTURE_STEVE;
    }

    /**
     * Retrieves the default skin for this player. Depending on the model used this will be Alex or Steve.
     */
    public static ResourceLocation getDefaultSkin(UUID playerUUID)
    {
        return Minecraft.getMinecraft().currentScreen instanceof BetterAltManager ? TEXTURE_STEVE:isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
    }

    /**
     * Retrieves the type of skin that a player is using. The Alex model is slim while the Steve model is default.
     */
    public static String getSkinType(UUID playerUUID)
    {
        return Minecraft.getMinecraft().currentScreen instanceof BetterAltManager ? "default":isSlimSkin(playerUUID) ? "slim" : "default";
    }

    /**
     * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
     */
    private static boolean isSlimSkin(UUID playerUUID)
    {
        return (playerUUID.hashCode() & 1) == 1;
    }
}
