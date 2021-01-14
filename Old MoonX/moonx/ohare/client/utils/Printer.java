package moonx.ohare.client.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

		import net.minecraft.client.Minecraft;
		import net.minecraft.util.ChatComponentTranslation;

public class Printer {

	public static void print(final String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(
				new ChatComponentTranslation(
						ChatFormatting.GRAY + "[" + ChatFormatting.DARK_RED + "Moon X"
								+ ChatFormatting.GRAY + "]" + ChatFormatting.WHITE + " > "+ message, new Object[0]));
	}
}