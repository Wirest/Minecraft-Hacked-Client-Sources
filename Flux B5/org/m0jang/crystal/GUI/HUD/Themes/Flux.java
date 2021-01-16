package org.m0jang.crystal.GUI.HUD.Themes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Wrapper;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.TabGUI;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Render.Hud;
import org.m0jang.crystal.Utils.RenderUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class Flux implements HudTheme {
   public int delay = 500;
   List rainbow = new ArrayList();
   int rainbowindex = 0;
   TimeHelper rainbowTimer = new TimeHelper();
   TimeHelper logoTimer = new TimeHelper();
   private static int currentRainbow;
   private static int hue = 104;
   private boolean down;
   private static Color backgroundColor = new Color(30, 30, 30, 130);
   private static Color borderColor = new Color(15, 15, 15, 170);
   private static Color overrayColor = new Color(186, 70, 200, 200);

   public String getName() {
      return "Flux";
   }

   public void render() {
      if (Crystal.INSTANCE.getMods().get(Hud.class).isEnabled()) {
         int yCount = 2;
         if (this.down && hue < 1) {
            this.down = false;
         }

         if (!this.down && hue > 150) {
            this.down = true;
         }

         if (this.logoTimer.hasPassed(1.0D)) {
            if (this.down) {
               --hue;
            } else {
               ++hue;
            }

            this.logoTimer.reset();
         }

         if (Hud.watermark.getBooleanValue()) {
            Fonts.segoe22.drawStringWithShadow("Flux", 2.0F, 2.0F, (new Color(186, hue, 200)).getRGB());
         }

         ScaledResolution scaledResolution = new ScaledResolution(Wrapper.mc, Minecraft.displayWidth, Minecraft.displayHeight);
         if (Hud.coords.getBooleanValue()) {
            Fonts.segoe22.drawStringWithShadow("X: " + (int)Minecraft.thePlayer.posX, (float)(Minecraft.displayWidth / 2 - (Fonts.segoe22.getStringWidth("X: " + (int)Minecraft.thePlayer.posX) + 1)), (float)(Minecraft.displayHeight / 2 - 31), -1);
            Fonts.segoe22.drawStringWithShadow("Y: " + (int)Minecraft.thePlayer.posY, (float)(Minecraft.displayWidth / 2 - (Fonts.segoe22.getStringWidth("Y: " + (int)Minecraft.thePlayer.posY) + 1)), (float)(Minecraft.displayHeight / 2 - 21), -1);
            Fonts.segoe22.drawStringWithShadow("Z: " + (int)Minecraft.thePlayer.posZ, (float)(Minecraft.displayWidth / 2 - (Fonts.segoe22.getStringWidth("Z: " + (int)Minecraft.thePlayer.posZ) + 1)), (float)(Minecraft.displayHeight / 2 - 11), -1);
         }

         List sortex = Crystal.INSTANCE.getMods().getModList();
         if (Hud.array.getBooleanValue()) {
            Collections.sort(sortex, new Comparator<Module>() {
               public int compare(Module mod1, Module mod2) {
                  String s1 = mod1.getRenderName();
                  if (mod1.getRenderName().equals(mod1.getName()) && mod1.isHasSubModule()) {
                     s1 = mod1.getName() + " \247f" + mod1.getCurrnetSubModule();
                  }

                  String s2 = mod2.getRenderName();
                  if (mod2.getRenderName().equals(mod2.getName()) && mod2.isHasSubModule()) {
                     s2 = mod2.getName() + " \247f" + mod2.getCurrnetSubModule();
                  }

                  return Fonts.segoe18.getStringWidth(s2) - Fonts.segoe18.getStringWidth(s1);
               }
            });
            final int differenceColor = 0;
            Iterator var6 = sortex.iterator();

            while(var6.hasNext()) {
               Module mod2 = (Module)var6.next();
               if (mod2.isEnabled() && mod2.getName() != "Hud" && mod2.getName() != "Commands") {
                  String show = mod2.getRenderName();
                  if (mod2.getRenderName().equals(mod2.getName()) && mod2.isHasSubModule()) {
                     show = mod2.getName() + " \247f" + mod2.getCurrnetSubModule();
                  }

                  Fonts.segoe18.drawStringWithShadow(show, (float)(scaledResolution.getScaledWidth() - Fonts.segoe18.getStringWidth(show) - 2), (float)yCount, (new Color(186, 60, 200)).getRGB());
                  yCount += Fonts.segoe18.getStringHeight("FUCK ME") + 1;
               }
            }

            GL11.glPopMatrix();
         }
      }

   }

   public void renderTab() {
      if (Crystal.INSTANCE.mods.get(Hud.class).isEnabled() && Hud.tabgui.getBooleanValue()) {
         GL11.glPushMatrix();
         TabGUI.updateBars();
         int highestWidth = 50;
         TabGUI.baseCategoryWidth = highestWidth + 25;
         TabGUI.baseCategoryHeight = Category.values().length * 14 + 2;
         TabGUI.ymod = 0;
         RenderUtils.drawBorderedRect(1, 19 + TabGUI.ymod + 1, 2 + TabGUI.baseCategoryWidth + 1, 21 + TabGUI.baseCategoryHeight - 3 + TabGUI.ymod - 11 + 2 - 1 + 1, 1, backgroundColor, borderColor);
         RenderUtils.drawRects(2.0D, (double)(TabGUI.categoryPosition - 1 + TabGUI.ymod), (double)(2 + TabGUI.baseCategoryWidth), (double)(TabGUI.categoryPosition + 14 + TabGUI.ymod - 4), overrayColor);
         int yPos = 22;

         int i;
         String name;
         for(i = 0; i < Category.values().length; ++i) {
            Category category = Category.values()[i];
            name = Character.toUpperCase(category.name().toLowerCase().charAt(0)) + category.name().toLowerCase().substring(1);
            Fonts.segoe18.drawStringWithShadow(name, 4.0F, (float)(yPos + TabGUI.ymod), -2105377);
            yPos += 12;
         }

         if (TabGUI.section == TabGUI.Section.MODS) {
            RenderUtils.drawBorderedRect(TabGUI.baseCategoryWidth + 2, TabGUI.categoryPosition - 2 + TabGUI.ymod - 2 + 1 + 1, TabGUI.baseCategoryWidth + 2 + TabGUI.baseModWidth - 13, TabGUI.categoryPosition + TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).size() * 12 + 1 + TabGUI.ymod - 2 - 1 + 1, 1, backgroundColor, borderColor);
            RenderUtils.drawRects((double)(TabGUI.baseCategoryWidth + 3), (double)(TabGUI.modPosition - 1 + TabGUI.ymod), (double)(TabGUI.baseCategoryWidth + TabGUI.baseModWidth - 12), (double)(TabGUI.modPosition + 14 + TabGUI.ymod - 4), overrayColor);
            yPos = TabGUI.categoryPosition;

            for(i = 0; i < TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).size(); ++i) {
               Module mod = (Module)TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).get(i);
               name = mod.getName();
               Fonts.segoe18.drawStringWithShadow(name, (float)(TabGUI.baseCategoryWidth + 7), (float)(yPos + TabGUI.ymod), mod.isEnabled() ? 10066329 : -2105377);
               yPos += 12;
            }
         }

         GL11.glPopMatrix();
      }

   }

   public static int getCurrentRainbow() {
      return currentRainbow;
   }
}
