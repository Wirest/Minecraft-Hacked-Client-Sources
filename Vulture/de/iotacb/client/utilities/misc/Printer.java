package de.iotacb.client.utilities.misc;

import de.iotacb.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Printer {
	
	public final void printMessage(String message) {
		final String msg = String.format("[" + Client.INSTANCE.getClientColorCode() + "§l§n%s§f]: %s", Client.INSTANCE.getClientName(), message);
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
	}
	
}
