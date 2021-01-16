package org.m0jang.crystal.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
   public static void sendMessageToPlayer(String msg) {
      Minecraft.getMinecraft();
      Minecraft.thePlayer.addChatMessage(new ChatComponentText("\2478[\2475F\2479lux\2478]:\247r " + msg));
   }

   public static void sendMessageFromPlayer(String msg) {
      Minecraft.getMinecraft();
      Minecraft.thePlayer.sendChatMessage(msg);
   }
}
