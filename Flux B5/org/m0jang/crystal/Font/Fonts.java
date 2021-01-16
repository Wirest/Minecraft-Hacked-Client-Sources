package org.m0jang.crystal.Font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
   public static FluxFontRenderer segoe22 = new FluxFontRenderer(getFont(22), true, 8);
   public static FluxFontRenderer segoe18 = new FluxFontRenderer(getFont(18), true, 8);
   public static FluxFontRenderer segoe16 = new FluxFontRenderer(getFont(16), true, 8);

   private static Font getFont(int size) {
      Font font = null;

      try {
         InputStream ex = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("wolfram/URWGothicLBook.ttf")).getInputStream();
         font = Font.createFont(0, ex);
         font = font.deriveFont(0, (float)size);
      } catch (Exception var3) {
         var3.printStackTrace();
         System.err.println("Font not loaded.  Using serif font.");
         font = new Font("default", 0, size);
      }

      return font;
   }
}
