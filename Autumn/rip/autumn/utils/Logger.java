package rip.autumn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import rip.autumn.core.Autumn;

public final class Logger {
   protected static final Minecraft mc = Minecraft.getMinecraft();

   public static void log(String msg) {
      if (mc.thePlayer != null && mc.theWorld != null) {
         StringBuilder tempMsg = new StringBuilder();
         String[] var2 = msg.split("\n");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            tempMsg.append(line).append("§7");
         }

         mc.thePlayer.addChatMessage(new ChatComponentText("§c[" + Autumn.INSTANCE.getName() + "]§7: " + tempMsg.toString()));
      }

   }
}
