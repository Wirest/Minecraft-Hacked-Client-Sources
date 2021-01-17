package me.slowly.client.ui.hudcustomizer.customs;

import java.awt.Color;
import java.util.ArrayList;
import me.slowly.client.ui.hudcustomizer.CustomHUDOptions;
import me.slowly.client.ui.hudcustomizer.CustomValue;

public class CustomHUDTabGui {
   public static ArrayList optionList = new ArrayList();
   public static CustomHUDOptions colorTab;
   public static CustomValue alphaSlider = new CustomValue("Alpha Slider", 1.0D, 0.0D, 1.0D, 0.05D, "");
   public static CustomValue alphaBackground = new CustomValue("Alpha Background", 1.0D, 0.0D, 1.0D, 0.05D, "");
   public static CustomValue rainbow = new CustomValue("Rainbow", false);
   public static CustomValue gradient = new CustomValue("Gradient", false);
   public static CustomValue colorPickerSlider;
   public static CustomValue colorPickerBackground;
   public static CustomHUDOptions fontTab;
   public static CustomValue changeSelectedFontColor;
   public static CustomValue useShadow;
   public static CustomValue xShadow;
   public static CustomValue yShadow;
   public static CustomValue fontSize;
   public static CustomValue usedFont;
   public static CustomValue fontColor;
   public static CustomValue selectedFontColor;
   public static CustomHUDOptions sizeTab;
   public static CustomValue scaleX;
   public static CustomValue scaleY;
   public static CustomHUDOptions positionTab;
   public static CustomValue xAdd;
   public static CustomValue yAdd;
   public static CustomHUDOptions borderTab;
   public static CustomValue alphaBorder;
   public static CustomValue enableBorder;
   public static CustomValue smoothBorder;
   public static CustomValue borderSize;
   public static CustomValue borderColor;
   public static CustomHUDOptions sliderBorderTab;
   public static CustomValue sliderAlphaBorder;
   public static CustomValue sliderEnableBorder;
   public static CustomValue sliderSmoothBorder;
   public static CustomValue sliderBorderSize;
   public static CustomValue sliderBorderColor;
   public static CustomHUDOptions otherOption;
   public static CustomValue enableTabGui;

   static {
      colorPickerSlider = new CustomValue("Slider Color", new Color(20, 100, 255), alphaSlider);
      colorPickerBackground = new CustomValue("Background Color", new Color(0, 0, 0, 100), alphaBackground);
      changeSelectedFontColor = new CustomValue("Change selected Font Color", false);
      useShadow = new CustomValue("Shadow", false);
      xShadow = new CustomValue("Shadow-X", 0.5D, 0.0D, 2.0D, 0.25D, "px");
      yShadow = new CustomValue("Shadow-Y", 0.5D, 0.0D, 2.0D, 0.25D, "px");
      fontSize = new CustomValue("Font Size", 16.0D, 8.0D, 22.0D, 1.0D, "px");
      usedFont = new CustomValue("Font");
      fontColor = new CustomValue("Font Color", new Color(0, 0, 0, 100), new CustomValue("alpha", 1.0D));
      selectedFontColor = new CustomValue("Selected Font Color", new Color(0, 0, 0, 100), new CustomValue("alpha", 1.0D));
      scaleX = new CustomValue("X-Scale", 0.0D, 0.0D, 50.0D, 0.5D, "%");
      scaleY = new CustomValue("Y-Scale", 0.0D, 0.0D, 15.0D, 0.5D, "%");
      xAdd = new CustomValue("X-Add", 0.0D, 0.0D, 50.0D, 1.0D, "px");
      yAdd = new CustomValue("Y-Add", 12.0D, 12.0D, 50.0D, 1.0D, "px");
      alphaBorder = new CustomValue("Alpha", 1.0D, 0.0D, 1.0D, 0.05D, "");
      enableBorder = new CustomValue("Enable Border", false);
      smoothBorder = new CustomValue("Smooth", false);
      borderSize = new CustomValue("Border Size", 0.5D, 0.5D, 2.0D, 0.5D, "px");
      borderColor = new CustomValue("Border Color", new Color(0, 0, 0, 100), alphaBorder);
      sliderAlphaBorder = new CustomValue("Alpha", 1.0D, 0.0D, 1.0D, 0.05D, "");
      sliderEnableBorder = new CustomValue("Enable Border", false);
      sliderSmoothBorder = new CustomValue("Smooth", false);
      sliderBorderSize = new CustomValue("Border Size", 0.5D, 0.5D, 2.0D, 0.5D, "px");
      sliderBorderColor = new CustomValue("Border Color", new Color(0, 0, 0, 100), alphaBorder);
      enableTabGui = new CustomValue("Enable", true);
   }

   private void addOtherTab() {
      otherOption = new CustomHUDOptions("Other");
      otherOption.addValue(enableTabGui);
   }

   private void addSliderBorderTab() {
      sliderBorderTab = new CustomHUDOptions("Slider Border");
      sliderBorderTab.addValue(sliderAlphaBorder);
      sliderBorderTab.addValue(sliderEnableBorder);
      sliderBorderTab.addValue(sliderBorderSize);
      sliderBorderTab.addValue(sliderBorderColor);
      sliderBorderTab.addValue(sliderSmoothBorder);
   }

   private void addBorderTab() {
      borderTab = new CustomHUDOptions("Border");
      borderTab.addValue(alphaBorder);
      borderTab.addValue(enableBorder);
      borderTab.addValue(borderSize);
      borderTab.addValue(borderColor);
      borderTab.addValue(smoothBorder);
   }

   private void addColorTab() {
      colorTab = new CustomHUDOptions("Rectangle");
      colorTab.addValue(alphaSlider);
      colorTab.addValue(alphaBackground);
      colorTab.addValue(rainbow);
      colorTab.addValue(gradient);
      colorTab.addValue(colorPickerSlider);
      colorTab.addValue(colorPickerBackground);
   }

   private void addSizeTab() {
      sizeTab = new CustomHUDOptions("Size");
      sizeTab.addValue(scaleX);
      sizeTab.addValue(scaleY);
   }

   private void addPositionTab() {
      positionTab = new CustomHUDOptions("Position");
      positionTab.addValue(xAdd);
      positionTab.addValue(yAdd);
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

   private void addFonts() {
      usedFont.mode.add("Minecraft");
      usedFont.mode.add("Comfortaa");
      usedFont.mode.add("Simpleton");
      usedFont.mode.add("Payback");
      usedFont.mode.add("Verdana");
      usedFont.mode.add("Arial Bold");
      usedFont.mode.add("Roboto Bold");
   }

   private void addFontTab() {
      this.addFonts();
      fontTab = new CustomHUDOptions("Font");
      fontTab.addValue(useShadow);
      fontTab.addValue(xShadow);
      fontTab.addValue(changeSelectedFontColor);
      fontTab.addValue(yShadow);
      fontTab.addValue(fontSize);
      fontTab.addValue(usedFont);
      fontTab.addValue(selectedFontColor);
   }

   public CustomHUDTabGui() {
      this.addColorTab();
      this.addSizeTab();
      this.addPositionTab();
      this.addFontTab();
      this.addBorderTab();
      this.addSliderBorderTab();
      this.addOtherTab();
      optionList.add(colorTab);
      optionList.add(sizeTab);
      optionList.add(positionTab);
      optionList.add(fontTab);
      optionList.add(borderTab);
      optionList.add(sliderBorderTab);
      optionList.add(otherOption);
   }
}
