package me.slowly.client.ui.hudcustomizer.customs;

import java.awt.Color;
import java.util.ArrayList;
import me.slowly.client.ui.hudcustomizer.CustomHUDOptions;
import me.slowly.client.ui.hudcustomizer.CustomValue;

public class CustomHUDHotbar {
   public static ArrayList optionList = new ArrayList();
   public static CustomHUDOptions otherTab;
   public static CustomValue flatStyle = new CustomValue("Flat PlayerStats", false);
   public static CustomValue enable = new CustomValue("Enable", false);
   public static CustomHUDOptions dateTab;
   public static CustomValue dateEnabled = new CustomValue("Enable", true);
   public static CustomValue dateFont = new CustomValue("Font");
   public static CustomValue datePosition = new CustomValue("Position");
   public static CustomValue dateFontSize = new CustomValue("Font Size", 15.0D, 10.0D, 20.0D, 1.0D, "");
   public static CustomValue dateUseShadow = new CustomValue("Use Shadow", true);
   public static CustomValue dateShadowSize = new CustomValue("Shadow Size", 1.0D, 0.0D, 1.0D, 0.5D, "px");
   public static CustomValue dateFontColor = new CustomValue("Font Color", new Color(255, 255, 255), new CustomValue("alpha", 1.0D));
   public static CustomHUDOptions timeTab;
   public static CustomValue timeFont = new CustomValue("Font");
   public static CustomValue timeEnabled = new CustomValue("Enable", true);
   public static CustomValue timePosition = new CustomValue("Position");
   public static CustomValue timeFontSize = new CustomValue("Font Size", 15.0D, 10.0D, 20.0D, 1.0D, "");
   public static CustomValue timeUseShadow = new CustomValue("Use Shadow", true);
   public static CustomValue timeShadowSize = new CustomValue("Shadow Size", 1.0D, 0.0D, 1.0D, 0.5D, "px");
   public static CustomValue timeFontColor = new CustomValue("Font Color", new Color(255, 255, 255), new CustomValue("alpha", 1.0D));
   public static CustomHUDOptions pingTab;
   public static CustomValue pingEnabled = new CustomValue("Enable", true);
   public static CustomValue pingFont = new CustomValue("Font");
   public static CustomValue pingPosition = new CustomValue("Position");
   public static CustomValue pingFontSize = new CustomValue("Font Size", 15.0D, 10.0D, 20.0D, 1.0D, "");
   public static CustomValue pingUseShadow = new CustomValue("Use Shadow", true);
   public static CustomValue pingShadowSize = new CustomValue("Shadow Size", 1.0D, 0.0D, 1.0D, 0.5D, "px");
   public static CustomValue pingFontColor = new CustomValue("Font Color", new Color(255, 255, 255), new CustomValue("alpha", 1.0D));
   public static CustomHUDOptions positionTab;
   public static CustomValue positionEnabled = new CustomValue("Enable", true);
   public static CustomValue positionFont = new CustomValue("Font");
   public static CustomValue positionPosition = new CustomValue("Position");
   public static CustomValue positionFontSize = new CustomValue("Font Size", 15.0D, 10.0D, 20.0D, 1.0D, "");
   public static CustomValue positionUseShadow = new CustomValue("Use Shadow", true);
   public static CustomValue positionShadowSize = new CustomValue("Shadow Size", 1.0D, 0.0D, 1.0D, 0.5D, "px");
   public static CustomValue positionFontColor = new CustomValue("Font Color", new Color(255, 255, 255), new CustomValue("alpha", 1.0D));

   private CustomHUDOptions addItemTab(String name, CustomValue enabled, CustomValue position, CustomValue font, CustomValue fontSize, CustomValue useShadow, CustomValue shadowSize, CustomValue colorPicker) {
      CustomHUDOptions tab = new CustomHUDOptions(name);
      tab.addValue(enabled);
      position.mode.add("Left");
      position.mode.add("Right");
      tab.addValue(position);
      tab.addValue(fontSize);

      for(int i = 0; i < getFonts().size(); ++i) {
         font.mode.add((String)getFonts().get(i));
      }

      tab.addValue(font);
      tab.addValue(useShadow);
      tab.addValue(shadowSize);
      tab.addValue(colorPicker);
      return tab;
   }

   private static ArrayList getFonts() {
      ArrayList value = new ArrayList();
      value.add("Minecraft");
      value.add("Comfortaa");
      value.add("Simpleton");
      value.add("Payback");
      value.add("Verdana");
      value.add("Arial Bold");
      value.add("Roboto Bold");
      return value;
   }

   public void addOtherTab() {
      otherTab = new CustomHUDOptions("Other");
      otherTab.addValue(enable);
      otherTab.addValue(flatStyle);
   }

   public CustomHUDHotbar() {
      this.addOtherTab();
      dateTab = this.addItemTab("Date", dateEnabled, datePosition, dateFont, dateFontSize, dateUseShadow, dateShadowSize, dateFontColor);
      timeTab = this.addItemTab("Time", timeEnabled, timePosition, timeFont, timeFontSize, timeUseShadow, timeShadowSize, timeFontColor);
      pingTab = this.addItemTab("Ping", pingEnabled, pingPosition, pingFont, pingFontSize, pingUseShadow, pingShadowSize, pingFontColor);
      positionTab = this.addItemTab("Location", positionEnabled, positionPosition, positionFont, positionFontSize, positionUseShadow, positionShadowSize, positionFontColor);
      optionList.add(dateTab);
      optionList.add(timeTab);
      optionList.add(pingTab);
      optionList.add(positionTab);
      optionList.add(otherTab);
   }

   public static String getFontName(String value) {
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
}
