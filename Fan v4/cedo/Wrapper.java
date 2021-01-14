package cedo;

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
    public static boolean authorized;
    public static double getFuckedPrinceKin;

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
