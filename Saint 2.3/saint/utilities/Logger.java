package saint.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Logger {
   public static final String prefix = "§7[§9Saint§7]§r ";

   public static void writeChat(String message) {
      Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§9Saint§7]§r " + message));
   }

   public static void writeConsole(String text) {
      System.out.println("[Saint] " + text);
   }
}
