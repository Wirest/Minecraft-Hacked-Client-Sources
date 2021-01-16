package org.m0jang.crystal.GUI.HUD.Themes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.GUI.TabGUI;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Render.Hud;
import org.m0jang.crystal.Utils.RenderUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class OldFlux implements HudTheme {
   public int delay = 500;
   List rainbow = new ArrayList();
   int rainbowindex = 0;
   TimeHelper rainbowTimer = new TimeHelper();
   TimeHelper logoTimer = new TimeHelper();
   private static int currentRainbow;
   private static int hue = 104;
   private boolean down;

   public OldFlux() {
      int b;
      for(b = 0; b < 100; ++b) {
         this.rainbow.add(new Color(b * 255 / 100, 255, 0));
      }

      for(b = 100; b > 0; --b) {
         this.rainbow.add(new Color(255, b * 255 / 100, 0));
      }

      for(b = 0; b < 100; ++b) {
         this.rainbow.add(new Color(255, 0, b * 255 / 100));
      }

      for(b = 100; b > 0; --b) {
         this.rainbow.add(new Color(b * 255 / 100, 0, 255));
      }

      for(b = 0; b < 100; ++b) {
         this.rainbow.add(new Color(0, b * 255 / 100, 255));
      }

      for(b = 100; b > 0; --b) {
         this.rainbow.add(new Color(0, 255, b * 255 / 100));
      }

   }

   public String getName() {
      return "OldFlux";
   }

   public void render() {
      if (Crystal.INSTANCE.getMods().get(Hud.class).isEnabled()) {
         GL11.glPushMatrix();
         int yCount = 2;
         GL11.glPushMatrix();
         GL11.glScalef(1.5F, 1.5F, 1.5F);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("F", 2.0F, 2.0F, (new Color(235, 0, 255)).getRGB());
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("lux", (float)(2 + Minecraft.getMinecraft().fontRendererObj.getStringWidth("F")), 2.0F, (new Color(0, 87, 255)).getRGB());
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("\247aName: \247c" + Minecraft.thePlayer.getName(), 3.0F, 16.0F, 10066329);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("\247aServer: \247c" + Wrapper.mc.getCurrentServerData().serverIP, 3.0F, 25.0F, 10066329);
         GL11.glPopMatrix();
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("X: " + (int)Minecraft.thePlayer.posX, (float)(Minecraft.displayWidth / 2 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth("X: " + (int)Minecraft.thePlayer.posX) + 1)), (float)(Minecraft.displayHeight / 2 - 29), -1);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Y: " + (int)Minecraft.thePlayer.posY, (float)(Minecraft.displayWidth / 2 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth("Y: " + (int)Minecraft.thePlayer.posY) + 1)), (float)(Minecraft.displayHeight / 2 - 19), -1);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Z: " + (int)Minecraft.thePlayer.posZ, (float)(Minecraft.displayWidth / 2 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth("Z: " + (int)Minecraft.thePlayer.posZ) + 1)), (float)(Minecraft.displayHeight / 2 - 9), -1);
         new ScaledResolution(Minecraft.getMinecraft(), Minecraft.displayWidth, Minecraft.displayHeight);
         List sortex = new ArrayList();
         Iterator var5 = Crystal.INSTANCE.getMods().getModList().iterator();

         while(var5.hasNext()) {
            Module mod = (Module)var5.next();
            sortex.add(mod);
         }

         Collections.sort(sortex, new Comparator<Module>() {
            public int compare(Module mod1, Module mod2) {
               String s1 = String.valueOf(mod1.getRenderName());
               String s2 = String.valueOf(mod2.getRenderName());
               int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
               return cmp != 0 ? cmp : s2.compareTo(s1);
            }
         });
         if (this.rainbowTimer.hasPassed(5.0D)) {
            ++this.rainbowindex;
            if (this.rainbowindex > this.rainbow.size() - 1) {
               this.rainbowindex = 0;
            }

            this.rainbowTimer.reset();
            currentRainbow = ((Color)this.rainbow.get(this.rainbowindex)).getRGB();
         }

         int differenceColor = 0;
         Iterator var6 = sortex.iterator();

         while(var6.hasNext()) {
            Module mod2 = (Module)var6.next();
            if (mod2.isEnabled() && mod2.getName() != "Hud" && mod2.getName() != "Commands") {
               int writeColor = this.rainbowindex + differenceColor;
               if (writeColor > this.rainbow.size() - 1) {
                  writeColor -= this.rainbow.size();
               }

               differenceColor += 5;
               Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(mod2.getRenderName(), (float)(Minecraft.displayWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod2.getRenderName()) - 2), (float)yCount, ((Color)this.rainbow.get(writeColor)).getRGB());
               yCount += 10;
            }
         }

         GL11.glPopMatrix();
      }

   }

   public void renderTab() {
      Color c = new Color(235, 0, 255);
      TabGUI.updateBars();
      int highestWidth = 50;
      TabGUI.baseCategoryWidth = highestWidth + 25;
      TabGUI.baseCategoryHeight = Category.values().length * 14 + 2;
      TabGUI.ymod = 16;
      RenderUtils.drawBorderedRect(1, 19 + TabGUI.ymod, 2 + TabGUI.baseCategoryWidth, 21 + TabGUI.baseCategoryHeight - 3 + TabGUI.ymod - 11 + 2 + 1, 1, new Color(30, 30, 30, 255), new Color(0, 0, 0, 255));
      RenderUtils.drawRects(2.0D, (double)(TabGUI.categoryPosition - 2 + TabGUI.ymod) + 0.5D, (double)((float)(2 + TabGUI.baseCategoryWidth - 1) + 0.5F), (double)(TabGUI.categoryPosition + 14 - 2 + TabGUI.ymod - 1) - 0.5D, c);
      int yPos = 22;
      int yPosBottom = 29;

      int i;
      String name;
      for(i = 0; i < Category.values().length; ++i) {
         Category category = Category.values()[i];
         name = Character.toUpperCase(category.name().toLowerCase().charAt(0)) + category.name().toLowerCase().substring(1);
         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, 4.0F, (float)(yPos + 3 - 2 + TabGUI.ymod) - 0.5F, -2105377);
         yPos += 12;
         yPosBottom += 14;
      }

      if (TabGUI.section == TabGUI.Section.MODS) {
         RenderUtils.drawBorderedRect(TabGUI.baseCategoryWidth + 3, TabGUI.categoryPosition - 2 + TabGUI.ymod - 2 + 1, TabGUI.baseCategoryWidth + 2 + TabGUI.baseModWidth - 13, TabGUI.categoryPosition + TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).size() * 12 + 1 + TabGUI.ymod - 2 + 1, 1, new Color(30, 30, 30, 255), new Color(0, 0, 0, 255));
         RenderUtils.drawRects((double)(TabGUI.baseCategoryWidth + 4), (double)(TabGUI.modPosition + TabGUI.ymod - 2) + 0.5D, (double)(TabGUI.baseCategoryWidth + TabGUI.baseModWidth - 12), (double)(TabGUI.modPosition + 14 + TabGUI.ymod - 2 - 1) - 0.5D, c);
         yPos = TabGUI.categoryPosition;
         yPosBottom = TabGUI.categoryPosition + 14;

         for(i = 0; i < TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).size(); ++i) {
            Module mod = (Module)TabGUI.getModsInCategory(Category.values()[TabGUI.categoryTab]).get(i);
            name = mod.getName();
            float var10002 = (float)(TabGUI.baseCategoryWidth + 7);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, var10002, (float)(yPos + 3 - 2 + TabGUI.ymod) - 0.5F, mod.isEnabled() ? 10066329 : -2105377);
            yPos += 12;
            yPosBottom += 14;
         }
      }

   }

   public static int getCurrentRainbow() {
      return currentRainbow;
   }
}
