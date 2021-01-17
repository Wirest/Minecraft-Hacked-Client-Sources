package me.slowly.client.ui.hudcustomizer.customs;

import java.awt.Color;
import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.CustomHUDOptions;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class CustomHUDModList {
   public static ArrayList optionList = new ArrayList();
   public static CustomHUDOptions fontTab;
   public static CustomValue usedFont = new CustomValue("Font");
   public static CustomValue fontColorMode = new CustomValue("Color");
   public static CustomValue fontSize = new CustomValue("Font Size", 16.0D, 8.0D, 22.0D, 1.0D, "px");
   public static CustomValue yAdd = new CustomValue("Y Add", 0.0D, -5.0D, 1.0D, 0.5D, "px");
   public static CustomValue selectedFontColor = new CustomValue("Font Color", new Color(255, 255, 255), new CustomValue("alpha", 1.0D));
   public static CustomValue useShadow = new CustomValue("Shadow", false);
   public static CustomValue sortedMode = new CustomValue("Sorted", true);
   public static CustomValue xShadow = new CustomValue("Shadow-X", 0.5D, 0.0D, 2.0D, 0.25D, "px");
   public static CustomValue yShadow = new CustomValue("Shadow-Y", 0.5D, 0.0D, 2.0D, 0.25D, "px");
   public static CustomValue backgroundAlpha = new CustomValue("Background Alpha", 0.5D, 0.0D, 1.0D, 0.05D, "");
   public static CustomHUDOptions borderTab;
   public static CustomValue alphaBorder = new CustomValue("Alpha", 1.0D, 0.0D, 1.0D, 0.05D, "");
   public static CustomValue enableBorder = new CustomValue("Enable Border", false);
   public static CustomValue borderSize = new CustomValue("Border Size", 0.5D, 0.5D, 2.0D, 0.5D, "px");
   public static CustomValue borderColor;
   public static CustomValue borderMode;
   public static CustomValue borderColorMode;

   static {
      borderColor = new CustomValue("Border Color", new Color(0, 0, 0, 100), alphaBorder);
      borderMode = new CustomValue("Border Mode");
      borderColorMode = new CustomValue("Border Color");
   }

   public CustomHUDModList() {
      this.addFontTab();
      this.addBorderTab();
      optionList.add(fontTab);
      optionList.add(borderTab);
   }

   private void addBorderTab() {
      borderTab = new CustomHUDOptions("Border");
      borderTab.addValue(alphaBorder);
      borderTab.addValue(enableBorder);
      borderTab.addValue(borderSize);
      borderTab.addValue(borderColor);
      borderMode.mode.add("Mode 1");
      borderMode.mode.add("Mode 2");
      borderMode.mode.add("Mode 3");
      borderTab.addValue(borderMode);
      borderColorMode.mode.add("Mod Color");
      borderColorMode.mode.add("Own Color");
      borderColorMode.mode.add("Rainbow");
      borderTab.addValue(borderColorMode);
   }

   private void addColorModes() {
      fontColorMode.mode.add("Mod Color");
      fontColorMode.mode.add("Color");
      fontColorMode.mode.add("Rainbow");
   }

   private void addFonts() {
      usedFont.mode.add("Minecraft");
      usedFont.mode.add("Comfortaa");
      usedFont.mode.add("Simpleton");
      usedFont.mode.add("Payback");
      usedFont.mode.add("Verdana");
      usedFont.mode.add("Arial Bold");
      usedFont.mode.add("Roboto Bold");
   }

   private static String getFontName(String value) {
      switch(value.hashCode()) {
      case -120391103:
         if (value.equals("Simpleton")) {
            return "simpleton";
         }
         break;
      case 317902540:
         if (value.equals("Comfortaa")) {
            return "comfortaa";
         }
         break;
      case 562931328:
         if (value.equals("Roboto Bold")) {
            return "robotobold";
         }
         break;
      case 877640047:
         if (value.equals("Payback")) {
            return "payback";
         }
         break;
      case 1843725378:
         if (value.equals("Arial Bold")) {
            return "arialBold";
         }
         break;
      case 2015806707:
         if (value.equals("Verdana")) {
            return "VERDANA";
         }
      }

      return "minecraft";
   }

   public static FontRenderer getCurrentFont() {
      FontRenderer font = null;
      String fontName = getFontName(usedFont.getModeAt(usedFont.getCurrentMode()));
      if (fontName.equalsIgnoreCase("Minecraft")) {
         font = Minecraft.getMinecraft().fontRendererObj;
      } else if (fontName.equalsIgnoreCase("simpleton")) {
         font = Client.getInstance().getFontManager().getFont(fontName, ((Double)fontSize.getValueState()).floatValue(), true);
      } else {
         font = Client.getInstance().getFontManager().getFont(fontName, ((Double)fontSize.getValueState()).floatValue());
      }

      return (FontRenderer)font;
   }

   private void addFontTab() {
      this.addColorModes();
      this.addFonts();
      fontTab = new CustomHUDOptions("Font");
      fontTab.addValue(yAdd);
      fontTab.addValue(usedFont);
      fontTab.addValue(fontColorMode);
      fontTab.addValue(selectedFontColor);
      fontTab.addValue(fontSize);
      fontTab.addValue(useShadow);
      fontTab.addValue(xShadow);
      fontTab.addValue(yShadow);
      fontTab.addValue(sortedMode);
      fontTab.addValue(backgroundAlpha);
   }
}
