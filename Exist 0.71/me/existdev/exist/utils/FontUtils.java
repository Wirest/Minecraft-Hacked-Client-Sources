package me.existdev.exist.utils;

import me.existdev.exist.Exist;
import me.existdev.exist.ttf.CustomFontManager;
import net.minecraft.util.StringUtils;

public class FontUtils {
   // $FF: synthetic field
   private static CustomFontManager fontRenderer;

   // $FF: synthetic method
   public static void setupFontUtils() {
      fontRenderer = Exist.fontManager;
   }

   // $FF: synthetic method
   public static int getStringWidth(String text) {
      return CustomFontManager.fontClickGUI.getStringWidth(StringUtils.stripControlCodes(text));
   }

   // $FF: synthetic method
   public static int getFontHeight() {
      return CustomFontManager.fontClickGUI.FONT_HEIGHT;
   }

   // $FF: synthetic method
   public static void drawStringWithShadow(String text, double x, double y, int color) {
      CustomFontManager.fontClickGUI.drawStringWithShadow(text, (float)x, (float)y, color);
   }

   // $FF: synthetic method
   public static void drawCenteredString(String text, double x, double y, int color) {
      drawStringWithShadow(text, x - (double)(CustomFontManager.fontClickGUI.getStringWidth(text) / 2), y, color);
   }

   // $FF: synthetic method
   public static void drawCenteredStringWithShadow(String text, double x, double y, int color) {
      drawStringWithShadow(text, x - (double)(CustomFontManager.fontClickGUI.getStringWidth(text) / 2), y, color);
   }

   // $FF: synthetic method
   public static void drawTotalCenteredString(String text, double x, double y, int color) {
      drawStringWithShadow(text, x - (double)(CustomFontManager.fontClickGUI.getStringWidth(text) / 2), y - (double)(CustomFontManager.fontClickGUI.FONT_HEIGHT / 2), color);
   }

   // $FF: synthetic method
   public static void drawTotalCenteredStringWithShadow(String text, double x, double y, int color) {
      drawStringWithShadow(text, x - (double)(CustomFontManager.fontClickGUI.getStringWidth(text) / 2), y - (double)((float)CustomFontManager.fontClickGUI.FONT_HEIGHT / 2.0F), color);
   }
}
