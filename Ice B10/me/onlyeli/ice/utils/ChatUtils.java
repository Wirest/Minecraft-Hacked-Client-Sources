package me.onlyeli.ice.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
	public static void sendMessageToPlayer(String msg) {
		Minecraft.getMinecraft().thePlayer
				.addChatMessage(new ChatComponentText(ChatUtils.format("&8[&bIce&8]:&r " + msg)));
	}

	public static void sendMessageFromPlayer(String msg) {
		Minecraft.getMinecraft().thePlayer.sendChatMessage(msg);
	}

	public static String format(String message) {
		String ret = message;
		String format = "§";
		String[] codes = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l",
				"m", "n", "o", "r" };
		for (int i = 0; i < codes.length; i++) {
			if (ret.contains("&" + codes[i])) {
				ret = ret.replaceAll("&" + codes[i], format + codes[i]);
			}
		}
		return ret;
	}
}