/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 */
package me.aristhena.lucid.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatUtils {
    public static void sendClientMessage(String message) {
        String prefix = "\u00a7b[Lucid]: \u00a7r";
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(String.valueOf(prefix) + message));
    }

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
    }
}

