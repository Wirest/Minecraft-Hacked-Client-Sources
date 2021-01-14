package cedo;

import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;

/**
 * Fan Client's Wrapper. Use this as needed.
 */
public class Wrapper {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static MinecraftFontRenderer frregular = FontUtil.regular;
    public static MinecraftFontRenderer frregular2 = FontUtil.regular2;
    public static MinecraftFontRenderer frclean = FontUtil.clean;
    public static MinecraftFontRenderer frcleansmall = FontUtil.cleanSmall;
    public static MinecraftFontRenderer frcleanmedium = FontUtil.cleanmedium;
    public static MinecraftFontRenderer frcleanlarge = FontUtil.cleanlarge;
    public static MinecraftFontRenderer frmedium = FontUtil.mediumfont;
    public static MinecraftFontRenderer frsmall = FontUtil.smallfont;
    public static MinecraftFontRenderer frtommysmall = FontUtil.tommysmallfont;
    public static MinecraftFontRenderer frregularsmall = FontUtil.regularSmall;
    public static MinecraftFontRenderer frlarge = FontUtil.large;
    public static MinecraftFontRenderer expandedfont = FontUtil.expandedfont;
    public static MinecraftFontRenderer tabguimodule = FontUtil.tabguimodule;
    public static boolean authorized;

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }

    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }

    public static FontRenderer getFontRenderer() {
        return getMinecraft().fontRendererObj;
    }

    public static EntityRenderer getRenderer() {
        return getMinecraft().entityRenderer;
    }

    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    public static NetHandlerPlayClient getSendQueue() {
        return getPlayer().sendQueue;
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(mc);
    }

    public static float getTimerSpeed() {
        return mc.timer.timerSpeed;
    }

    public static void setTimerSpeed(float speed) {
        mc.timer.timerSpeed = speed;
    }

    public void jump() {
        getPlayer().jump();
    }
}
