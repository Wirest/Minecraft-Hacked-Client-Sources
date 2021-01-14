package com.etb.client.utils;

import com.etb.client.Client;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Printer {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void print(final String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", ChatFormatting.RED + Client.INSTANCE.clientName + " > "+ChatFormatting.GRAY , message)));
    }

    public static void printWithoutPrefix(final String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }
}