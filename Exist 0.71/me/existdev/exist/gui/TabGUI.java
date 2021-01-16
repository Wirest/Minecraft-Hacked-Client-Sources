package me.existdev.exist.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.ttf.CustomFontManager;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;

public class TabGUI {
   // $FF: synthetic field
   private static final int NO_COLOR = 0;
   // $FF: synthetic field
   private static final int INSIDE_COLOR = -1610612736;
   // $FF: synthetic field
   private static final int BORDER_COLOR = 2013265920;
   // $FF: synthetic field
   private static final int COMPONENT_HEIGHT = 14;
   // $FF: synthetic field
   static int baseCategoryWidth;
   // $FF: synthetic field
   private static int baseCategoryHeight;
   // $FF: synthetic field
   private static int baseModWidth;
   // $FF: synthetic field
   private static int baseModHeight;
   // $FF: synthetic field
   private static TabGUI.Section section;
   // $FF: synthetic field
   private static int categoryTab;
   // $FF: synthetic field
   private static int modTab;
   // $FF: synthetic field
   private static int categoryPosition;
   // $FF: synthetic field
   private static int categoryTargetPosition;
   // $FF: synthetic field
   private static int modPosition;
   // $FF: synthetic field
   private static int modTargetPosition;
   // $FF: synthetic field
   private static boolean transitionQuickly;
   // $FF: synthetic field
   private static long lastUpdateTime;
   // $FF: synthetic field
   private static int backColor;
   // $FF: synthetic field
   private static int selectedBoxColor;
   // $FF: synthetic field
   private static int A;

   static {
      section = TabGUI.Section.CATEGORY;
      categoryTab = 0;
      modTab = 0;
      categoryPosition = 21;
      categoryTargetPosition = 21;
      modPosition = 15;
      modTargetPosition = 15;
   }

   // $FF: synthetic method
   public static void init() {
      int highestWidth = 0;
      Module.Category[] values;
      int length = (values = Module.Category.values()).length - 2;

      for(int i = 0; i < length; ++i) {
         Module.Category category = values[i];
         String name = category.name();
         int stringWidth = CustomFontManager.fontTabGUI.getStringWidth(name) + 5;
         highestWidth = Math.max(stringWidth, highestWidth);
      }

      baseCategoryWidth = highestWidth + 15;
      baseCategoryHeight = (Module.Category.values().length - 1) * 15 + 1;
   }

   // $FF: synthetic method
   public static void renderTabGUI() {
      updateBars();
      Color clientColor = ColorUtils.getClientColor();
      backColor = (new Color(0, 0, 0, ColorUtils.SmoothUpAlpha(3, A))).hashCode();
      if(!Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Icon").getBooleanValue()) {
         RenderHelper.drawRect(1.0F, 30.0F, (float)(2 + baseCategoryWidth), (float)(26 + baseCategoryHeight), -1879048192);
         RenderHelper.drawRect(3.0F, (float)(10 + categoryPosition), (float)(2 + baseCategoryWidth - 1), (float)(categoryPosition + 24), (new Color(clientColor.getRed(), clientColor.getGreen(), clientColor.getBlue(), 255)).hashCode());
         RenderHelper.drawRect(0.0F, 30.0F, 2.0F, (float)(26 + baseCategoryHeight), (new Color(clientColor.getRed(), clientColor.getGreen(), clientColor.getBlue(), 255)).hashCode());
      } else {
         RenderHelper.drawRect(4.0F, 60.0F, (float)(5 + baseCategoryWidth), (float)(56 + baseCategoryHeight), -1879048192);
         RenderHelper.drawRect(5.0F, (float)(40 + categoryPosition), (float)(4 + baseCategoryWidth), (float)(categoryPosition + 54), (new Color(clientColor.getRed(), clientColor.getGreen(), clientColor.getBlue(), 255)).hashCode());
      }

      int yPos = 20;
      int yPosBottom = 29;

      int i;
      String name;
      for(i = 0; i < Module.Category.values().length - 1; ++i) {
         Module.Category mod = Module.Category.values()[i];
         name = mod.name();
         if(!Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Icon").getBooleanValue()) {
            CustomFontManager.fontTabGUI.drawStringWithShadow(name, 6.0F, (float)(yPos + 12), -1);
         } else {
            CustomFontManager.fontTabGUI.drawStringWithShadow(name, 9.0F, (float)(yPos + 42), -1);
         }

         yPos += 14;
         yPosBottom += 14;
      }

      if(section == TabGUI.Section.MODS) {
         if(!Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Icon").getBooleanValue()) {
            RenderHelper.drawRect((float)(baseCategoryWidth + 3), (float)(categoryPosition + 9), (float)(baseCategoryWidth + baseModWidth + 5), (float)(categoryPosition + getModsInCategory(Module.Category.values()[categoryTab]).size() * 14 + 11), backColor);
            RenderHelper.drawRect((float)(baseCategoryWidth + 4), (float)(modPosition + 10), (float)(baseCategoryWidth + baseModWidth + 4), (float)(modPosition + 24), clientColor.hashCode());
         } else {
            RenderHelper.drawRect((float)(baseCategoryWidth + 6), (float)(categoryPosition + 39), (float)(baseCategoryWidth + baseModWidth + 8), (float)(categoryPosition + getModsInCategory(Module.Category.values()[categoryTab]).size() * 14 + 41), backColor);
            RenderHelper.drawRect((float)(baseCategoryWidth + 7), (float)(modPosition + 40), (float)(baseCategoryWidth + baseModWidth + 7), (float)(modPosition + 54), clientColor.hashCode());
         }

         yPos = categoryPosition;
         yPosBottom = categoryPosition + 14;

         for(i = 0; i < getModsInCategory(Module.Category.values()[categoryTab]).size(); ++i) {
            Module var6 = (Module)getModsInCategory(Module.Category.values()[categoryTab]).get(i);
            name = var6.getName();
            if(!Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Icon").getBooleanValue()) {
               CustomFontManager.fontTabGUI.drawStringWithShadow(name, (float)(baseCategoryWidth + 8), (float)(yPos + 12), var6.isToggled()?16777215:-7303024);
            } else {
               CustomFontManager.fontTabGUI.drawStringWithShadow(name, (float)(baseCategoryWidth + 10), (float)(yPos + 42), var6.isToggled()?16777215:-7303024);
            }

            yPos += 14;
            yPosBottom += 14;
         }
      }

   }

   // $FF: synthetic method
   public static void keyPress(int key) {
      if(section == TabGUI.Section.CATEGORY) {
         switch(key) {
         case 200:
            --categoryTab;
            categoryTargetPosition -= 14;
            if(categoryTab < 0) {
               transitionQuickly = true;
               categoryTargetPosition = 21 + (Module.Category.values().length - 2) * 14;
               categoryTab = Module.Category.values().length - 2;
            }
            break;
         case 205:
            int mod = 0;

            int stringWidth;
            for(Iterator var3 = getModsInCategory(Module.Category.values()[categoryTab]).iterator(); var3.hasNext(); mod = Math.max(stringWidth, mod)) {
               Module module = (Module)var3.next();
               String name = module.getName();
               stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
            }

            baseModWidth = mod + 6;
            baseModHeight = getModsInCategory(Module.Category.values()[categoryTab]).size() * 14 + 2;
            modTargetPosition = modPosition = categoryTargetPosition;
            modTab = 0;
            A = 130;
            section = TabGUI.Section.MODS;
            break;
         case 208:
            ++categoryTab;
            categoryTargetPosition += 14;
            if(categoryTab > Module.Category.values().length - 2) {
               transitionQuickly = true;
               categoryTargetPosition = 21;
               categoryTab = 0;
            }
         }
      } else if(section == TabGUI.Section.MODS) {
         switch(key) {
         case 200:
            --modTab;
            modTargetPosition -= 14;
            if(modTab < 0) {
               transitionQuickly = true;
               modTargetPosition = categoryTargetPosition + (getModsInCategory(Module.Category.values()[categoryTab]).size() - 1) * 14;
               modTab = getModsInCategory(Module.Category.values()[categoryTab]).size() - 1;
            }
         case 201:
         case 202:
         case 204:
         case 206:
         case 207:
         default:
            break;
         case 203:
            section = TabGUI.Section.CATEGORY;
            A = 10;
            break;
         case 205:
            Module mod1 = (Module)getModsInCategory(Module.Category.values()[categoryTab]).get(modTab);
            mod1.toggle();
            break;
         case 208:
            ++modTab;
            modTargetPosition += 14;
            if(modTab > getModsInCategory(Module.Category.values()[categoryTab]).size() - 1) {
               transitionQuickly = true;
               modTargetPosition = categoryTargetPosition;
               modTab = 0;
            }
         }
      }

   }

   // $FF: synthetic method
   private static void updateBars() {
      long timeDifference = System.currentTimeMillis() - lastUpdateTime;
      lastUpdateTime = System.currentTimeMillis();
      int increment = transitionQuickly?100:20;
      increment = Math.max(1, Math.round((float)((long)increment * timeDifference / 100L)));
      if(categoryPosition < categoryTargetPosition) {
         categoryPosition += increment;
         if(categoryPosition >= categoryTargetPosition) {
            categoryPosition = categoryTargetPosition;
            transitionQuickly = false;
         }
      } else if(categoryPosition > categoryTargetPosition) {
         categoryPosition -= increment;
         if(categoryPosition <= categoryTargetPosition) {
            categoryPosition = categoryTargetPosition;
            transitionQuickly = false;
         }
      }

      if(modPosition < modTargetPosition) {
         modPosition += increment;
         if(modPosition >= modTargetPosition) {
            modPosition = modTargetPosition;
            transitionQuickly = false;
         }
      } else if(modPosition > modTargetPosition) {
         modPosition -= increment;
         if(modPosition <= modTargetPosition) {
            modPosition = modTargetPosition;
            transitionQuickly = false;
         }
      }

   }

   // $FF: synthetic method
   private static List getModsInCategory(Module.Category Category) {
      ArrayList modList = new ArrayList();
      Iterator var3 = getSortedModuleArray().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if(mod.getCategory() == Category) {
            modList.add(mod);
         }
      }

      return modList;
   }

   // $FF: synthetic method
   private static List getSortedModuleArray() {
      ArrayList list = new ArrayList();
      Iterator var2 = Exist.moduleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module mod = (Module)var2.next();
         list.add(mod);
      }

      list.sort(new Comparator() {
         public int compare(Module m1, Module m2) {
            String s1 = m1.getName();
            String s2 = m2.getName();
            int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
            return cmp != 0?cmp:s2.compareTo(s1);
         }
      });
      return list;
   }

   // $FF: synthetic method
   public static int baseCategoryHeight() {
      return baseCategoryHeight;
   }

   // $FF: synthetic method
   public static int baseCategoryWidth() {
      return baseCategoryWidth;
   }

   private static enum Section {
      CATEGORY("CATEGORY", 0),
      MODS("MODS", 1);

      // $FF: synthetic method
      private Section(String s, int n) {
      }
   }
}
