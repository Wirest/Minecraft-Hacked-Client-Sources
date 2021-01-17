package me.slowly.client.ui.tabgui.draw;

import java.awt.Color;
import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

public class UITabSlot {
   public static void drawTabGuiSlot(float x, float y, float width, float height) {
      int darkGray = -15066598;
      float borderSize = ((Double)CustomHUDTabGui.borderSize.getValueState()).floatValue();
      Color col1 = (Color)CustomHUDTabGui.colorPickerBackground.getValueState();
      Color col = new Color((float)col1.getRed() / 255.0F, (float)col1.getGreen() / 255.0F, (float)col1.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaBackground.getValueState()).floatValue());
      if (((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue()) {
         drawBorder(x - borderSize, y - borderSize, x + width + borderSize, y + height + borderSize, borderSize, ((Color)CustomHUDTabGui.borderColor.getValueState()).getRGB(), col.getRGB());
      } else {
         Gui.drawRect(x, y, x + width, y + height, col);
      }

   }

   private static void drawBorder(float x, float y, float x2, float y2, float l1, int col1, int col2) {
      if (((Boolean)CustomHUDTabGui.smoothBorder.getValueState()).booleanValue()) {
         RenderHelper.drawBorderedRect(x, y, x2, y2, l1, col1, col2);
      } else {
         Gui.drawBorderedRect(x, y, x2, y2, l1, col1, col2);
      }

   }

   private static void drawSliderBorder(float x, float y, float x2, float y2, int startColor, int endColor, float borderSize, int borderColor) {
      Color c = new Color(borderColor);
      int color = (new Color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, ((Double)CustomHUDTabGui.sliderAlphaBorder.getValueState()).floatValue())).getRGB();
      if (((Boolean)CustomHUDTabGui.sliderSmoothBorder.getValueState()).booleanValue()) {
         RenderHelper.drawGradientRect(x, y, x2, y2, startColor, endColor, borderSize, color);
      } else {
         Minecraft.getMinecraft().ingameGUI.drawGradientBorderedRect(x, y, x2, y2, startColor, endColor, borderSize, color);
      }

   }

   public static void drawTabGuiSelector(float x, float y, float x2, float y2) {
      Color c = (Color)CustomHUDTabGui.colorPickerSlider.getValueState();
      Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
      Color newRainbow = new Color((float)rainbow.getRed() / 255.0F, (float)rainbow.getGreen() / 255.0F, (float)rainbow.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaSlider.getValueState()).floatValue());
      int color = ((Boolean)CustomHUDTabGui.rainbow.getValueState()).booleanValue() ? newRainbow.getRGB() : c.getRGB();
      Color gradientColor = new Color(((Boolean)CustomHUDTabGui.gradient.getValueState()).booleanValue() ? (new Color(color)).darker().darker().getRGB() : color);
      if (((Boolean)CustomHUDTabGui.sliderEnableBorder.getValueState()).booleanValue()) {
         drawSliderBorder(x, y, x2, y2, color, (new Color((float)gradientColor.getRed() / 255.0F, (float)gradientColor.getGreen() / 255.0F, (float)gradientColor.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaSlider.getValueState()).floatValue())).getRGB(), ((Double)CustomHUDTabGui.sliderBorderSize.getValueState()).floatValue(), ((Color)CustomHUDTabGui.sliderBorderColor.getValueState()).getRGB());
      } else {
         Minecraft.getMinecraft().ingameGUI.drawGradientRect(x, y, x2, y2, color, (new Color((float)gradientColor.getRed() / 255.0F, (float)gradientColor.getGreen() / 255.0F, (float)gradientColor.getBlue() / 255.0F, ((Double)CustomHUDTabGui.alphaSlider.getValueState()).floatValue())).getRGB());
      }

   }

   public static void drawString(String text, float x, float y, int color, boolean selected) {
      FontRenderer font = getCurrentFont();
      y += (float)(font.FONT_HEIGHT / 16);
      if (font != null) {
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         if (((Boolean)CustomHUDTabGui.useShadow.getValueState()).booleanValue()) {
            font.drawString(text, x + ((Double)CustomHUDTabGui.xShadow.getValueState()).floatValue(), y + ((Double)CustomHUDTabGui.yShadow.getValueState()).floatValue(), Colors.BLACK.c);
         }

         font.drawString(text, x, y, selected && ((Boolean)CustomHUDTabGui.changeSelectedFontColor.getValueState()).booleanValue() ? ((Color)CustomHUDTabGui.selectedFontColor.getValueState()).getRGB() : color);
      }

   }

   public static FontRenderer getCurrentFont() {
      FontRenderer font = null;
      String fontName = CustomHUDTabGui.getFontName(CustomHUDTabGui.usedFont.getModeAt(CustomHUDTabGui.usedFont.getCurrentMode()));
      if (fontName.equalsIgnoreCase("Minecraft")) {
         font = Minecraft.getMinecraft().fontRendererObj;
      } else if (fontName.equalsIgnoreCase("simpleton")) {
         font = Client.getInstance().getFontManager().getFont(fontName, ((Double)CustomHUDTabGui.fontSize.getValueState()).floatValue(), true);
      } else {
         font = Client.getInstance().getFontManager().getFont(fontName, ((Double)CustomHUDTabGui.fontSize.getValueState()).floatValue());
      }

      return (FontRenderer)font;
   }
}
