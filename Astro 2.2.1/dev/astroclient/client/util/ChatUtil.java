package dev.astroclient.client.util;

import dev.astroclient.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {

    public static void tellPlayer(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247C[\2474" + Client.INSTANCE.NAME + "\247C]\247F " + msg));
    }

    public static void logIRC(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247C[\2474" + "IRC" + "\247C]\247F " + msg));
    }
}
