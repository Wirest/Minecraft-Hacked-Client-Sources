// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.utils;

import me.aristhena.event.events.MoveEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.network.Packet;
import net.minecraft.util.MovementInput;
import net.minecraft.client.settings.GameSettings;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import me.aristhena.utils.minecraft.FontRenderer;

public final class ClientUtils
{
    public static FontRenderer clientFont;
    
    public static void loadClientFont() {
        ClientUtils.clientFont = new FontRenderer(mc().gameSettings, new ResourceLocation("client/font/ascii.png"), mc().renderEngine, false);
        if (mc().gameSettings.language != null) {
            mc().fontRendererObj.setUnicodeFlag(mc().isUnicode());
            mc().fontRendererObj.setBidiFlag(mc().mcLanguageManager.isCurrentLanguageBidirectional());
        }
        mc().mcResourceManager.registerReloadListener(ClientUtils.clientFont);
    }
    
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP player() {
        return mc().thePlayer;
    }
    
    public static PlayerControllerMP playerController() {
        return mc().playerController;
    }
    
    public static WorldClient world() {
        return mc().theWorld;
    }
    
    public static List<Entity> loadedEntityList() {
        final List<Entity> loadedList = new ArrayList<Entity>(world().loadedEntityList);
        loadedList.remove(player());
        return loadedList;
    }
    
    public static GameSettings gamesettings() {
        return mc().gameSettings;
    }
    
    public static MovementInput movementInput() {
        return player().movementInput;
    }
    
    public static double x() {
        return player().posX;
    }
    
    public static void x(final double x) {
        player().posX = x;
    }
    
    public static double y() {
        return player().posY;
    }
    
    public static void y(final double y) {
        player().posY = y;
    }
    
    public static double z() {
        return player().posZ;
    }
    
    public static void z(final double z) {
        player().posZ = z;
    }
    
    public static float yaw() {
        return player().rotationYaw;
    }
    
    public static void yaw(final float yaw) {
        player().rotationYaw = yaw;
    }
    
    public static float pitch() {
        return player().rotationPitch;
    }
    
    public static void pitch(final float pitch) {
        player().rotationPitch = pitch;
    }
    
    public static FontRenderer clientFont() {
        return ClientUtils.clientFont;
    }
    
    public static void packet(final Packet packet) {
        mc().getNetHandler().addToSendQueue(packet);
    }
    
    public static void sendMessage(final String message) {
        new ChatMessage.ChatMessageBuilder(true, true).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }
    
    public static void sendMessage(final String message, final boolean prefix) {
        new ChatMessage.ChatMessageBuilder(prefix, true).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }
    
    public static void setMoveSpeed(final MoveEvent event, final double speed) {
        double forward = movementInput().moveForward;
        double strafe = movementInput().moveStrafe;
        float yaw = yaw();
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static void offsetPosition(final double speed) {
        double forward = movementInput().moveForward;
        double strafe = movementInput().moveStrafe;
        float yaw = yaw();
        if (forward == 0.0 && strafe == 0.0) {
            return;
        }
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        player().setPositionAndUpdate(x() + (forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f))), y(), z() + (forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f))));
    }
}
