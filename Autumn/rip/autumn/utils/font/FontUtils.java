package rip.autumn.utils.font;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public final class FontUtils {
   public static Font getFontFromTTF(ResourceLocation loc, float fontSize, int fontType) {
      try {
         Font output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream());
         output = output.deriveFont(fontSize);
         return output;
      } catch (Exception var5) {
         return null;
      }
   }
}
