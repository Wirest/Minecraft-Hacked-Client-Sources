
package me.memewaredevs.client.util;

import me.memewaredevs.client.util.misc.MinecraftUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;

public class ChatUtil implements MinecraftUtil {

    public static void printChat(final String text) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("\u00a78[\u00a79Memeware\u00a78]\u00a7f " + text));
    }

    public static void printChatFlag(final String text) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText("\u00a78[\u00a79MAC\u00a78]\u00a7f " + text));
    }

    public static void sendChat(final String text) {
        mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(text));
    }

}
