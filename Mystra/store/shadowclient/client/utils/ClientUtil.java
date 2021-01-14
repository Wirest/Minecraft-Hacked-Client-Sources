package store.shadowclient.client.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ClientUtil {
   protected static Minecraft mc = Minecraft.getMinecraft();

   public static int reAlpha(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }
}
