package org.m0jang.crystal.GUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Render.Hud;

public class TabGUI {
   public static int baseCategoryWidth;
   public static int baseCategoryHeight;
   public static int ymod = -2;
   public static int baseModWidth;
   public static TabGUI.Section section;
   public static int categoryTab;
   private static int modTab;
   public static int categoryPosition;
   private static int categoryTargetPosition;
   public static int modPosition;
   private static int modTargetPosition;
   private static long lastUpdateTime;
   private static boolean transitionQuickly;

   static {
      section = TabGUI.Section.CATEGORY;
      categoryTab = 0;
      modTab = 0;
      categoryPosition = 22;
      categoryTargetPosition = 22;
      modPosition = 22;
      modTargetPosition = 22;
   }

   public static void init() {
      int highestWidth = 0;
      Category[] values;
      int length = (values = Category.values()).length;

      for(int i = 0; i < length; ++i) {
         Category category = values[i];
         String name = Character.toUpperCase(category.name().charAt(0)) + category.name().substring(1);
         int stringWidth = Fonts.segoe18.getStringWidth(name);
         highestWidth = Math.max(stringWidth, highestWidth);
      }

      baseCategoryWidth = highestWidth + 25;
      baseCategoryHeight = Category.values().length * 14 + 2;
   }

   public static void keyPress(int key) {
      if (Hud.tabgui.getBooleanValue()) {
         if (section == TabGUI.Section.CATEGORY) {
            switch(key) {
            case 28:
            default:
               break;
            case 200:
               --categoryTab;
               categoryTargetPosition -= 12;
               if (categoryTab < 0) {
                  transitionQuickly = true;
                  categoryTargetPosition = 22 + (Category.values().length - 1) * 12;
                  categoryTab = Category.values().length - 1;
               }
               break;
            case 205:
               int highestWidth = 0;

               int stringWidth;
               for(Iterator var3 = getModsInCategory(Category.values()[categoryTab]).iterator(); var3.hasNext(); highestWidth = Math.max(stringWidth, highestWidth)) {
                  Module module = (Module)var3.next();
                  String name = String.valueOf(String.valueOf(Character.toUpperCase(module.getName().charAt(0)))) + module.getName().substring(1);
                  stringWidth = Fonts.segoe18.getStringWidth(name);
               }

               baseModWidth = highestWidth + 25;
               modTargetPosition = modPosition = categoryTargetPosition;
               modTab = 0;
               section = TabGUI.Section.MODS;
               break;
            case 208:
               ++categoryTab;
               categoryTargetPosition += 12;
               if (categoryTab > Category.values().length - 1) {
                  transitionQuickly = true;
                  categoryTargetPosition = 22;
                  categoryTab = 0;
               }
            }
         } else if (section == TabGUI.Section.MODS) {
            Module mod;
            switch(key) {
            case 28:
               mod = (Module)getModsInCategory(Category.values()[categoryTab]).get(modTab);
               mod.toggle();
               break;
            case 200:
               --modTab;
               modTargetPosition -= 12;
               if (modTab < 0) {
                  transitionQuickly = true;
                  modTargetPosition = categoryTargetPosition + (getModsInCategory(Category.values()[categoryTab]).size() - 1) * 12;
                  modTab = getModsInCategory(Category.values()[categoryTab]).size() - 1;
               }
               break;
            case 203:
               section = TabGUI.Section.CATEGORY;
               break;
            case 205:
               mod = (Module)getModsInCategory(Category.values()[categoryTab]).get(modTab);
               mod.toggle();
               break;
            case 208:
               ++modTab;
               modTargetPosition += 12;
               if (modTab > getModsInCategory(Category.values()[categoryTab]).size() - 1) {
                  transitionQuickly = true;
                  modTargetPosition = categoryTargetPosition;
                  modTab = 0;
               }
            }
         }
      }

   }

   public static void updateBars() {
      if (Hud.tabgui.getBooleanValue()) {
         long timeDifference = System.nanoTime() / 1000000L - lastUpdateTime;
         lastUpdateTime = System.nanoTime() / 1000000L;
         int increment = transitionQuickly ? 100 : 20;
         increment = Math.max(1, Math.round((float)((long)increment * timeDifference / 100L)));
         if (categoryPosition < categoryTargetPosition) {
            categoryPosition += increment;
            if (categoryPosition >= categoryTargetPosition) {
               categoryPosition = categoryTargetPosition;
               transitionQuickly = false;
            }
         } else if (categoryPosition > categoryTargetPosition) {
            categoryPosition -= increment;
            if (categoryPosition <= categoryTargetPosition) {
               categoryPosition = categoryTargetPosition;
               transitionQuickly = false;
            }
         }

         if (modPosition < modTargetPosition) {
            modPosition += increment;
            if (modPosition >= modTargetPosition) {
               modPosition = modTargetPosition;
               transitionQuickly = false;
            }
         } else if (modPosition > modTargetPosition) {
            modPosition -= increment;
            if (modPosition <= modTargetPosition) {
               modPosition = modTargetPosition;
               transitionQuickly = false;
            }
         }
      }

   }

   public static List getModsInCategory(Category category) {
      List modList = new ArrayList();
      Iterator var3 = Crystal.INSTANCE.getMods().getModList().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if (mod.getCategory() == category) {
            modList.add(mod);
         }
      }
      modList.sort(new Comparator<Module>() {
		public int compare(Module o1, Module o2) {
			return o1.getName().compareTo(o2.getName());
		}
      });
      return modList;
   }

   public static enum Section {
      CATEGORY,
      MODS;
   }
}
