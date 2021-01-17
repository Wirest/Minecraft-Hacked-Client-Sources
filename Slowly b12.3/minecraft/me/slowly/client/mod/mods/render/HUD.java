package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.events.EventRender2D;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.hotbaritems.HotbarItem;
import me.slowly.client.ui.hotbaritems.HotbarItemDate;
import me.slowly.client.ui.hotbaritems.HotbarItemPing;
import me.slowly.client.ui.hotbaritems.HotbarItemPosition;
import me.slowly.client.ui.hotbaritems.HotbarItemTime;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDLogo;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDModList;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.util.ClientFont;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUD extends Mod {
   private static int potionEffectY = 0;
   private static ArrayList leftItems = new ArrayList();
   private static ArrayList rightItems = new ArrayList();
   private static HotbarItem itemPing = new HotbarItemPing();
   private static HotbarItem itemPosition = new HotbarItemPosition();
   private static HotbarItem itemDate = new HotbarItemDate();
   private static HotbarItem itemTime = new HotbarItemTime();

   public HUD() {
      super("HUD", Mod.Category.NONE, 0);
      this.set(true, false);
   }

   @EventTarget
   public void onRender(EventRender2D event) {
      if (!this.mc.gameSettings.showDebugInfo) {
         if (CustomHUDLogo.mode.isCurrentMode("Old")) {
            Gui.drawRect(0, 0, this.mc.fontRendererObj.getStringWidth("Slowly b1.2.3") + 12, this.mc.fontRendererObj.FONT_HEIGHT + 3, Integer.MIN_VALUE);
            this.mc.fontRendererObj.drawStringWithShadow("¡ìcCyka Blyat!!!", 1.0F, 2.0F, -1);
         } else if (CustomHUDLogo.mode.isCurrentMode("Style 1")) {
            int size = 64;
            RenderUtil.drawImage(new ResourceLocation("slowly/clientlogo.png"), -(size / 4) + 1, -(size / 4) + 1, size, size);
         } else {
            short size;
            if (CustomHUDLogo.mode.isCurrentMode("Style 2")) {
               size = 128;
               RenderUtil.drawImage(new ResourceLocation("slowly/clientlogo2.png"), -(size / 4) - 1, -(size / 3) - 3, size, size);
            } else if (CustomHUDLogo.mode.isCurrentMode("Style 3")) {
               size = 128;
               RenderUtil.drawImage(new ResourceLocation("slowly/clientlogo3.png"), -(size / 4) + 20, -(size / 3) + 10, size, size);
            } else if (CustomHUDLogo.mode.isCurrentMode("Style 4")) {
               size = 128;
               RenderUtil.drawImage(new ResourceLocation("slowly/clientlogo4.png"), -(size / 4) + 20, -(size / 3) + 2, size, size);
            }
         }

         this.renderPotionEffects();
         this.renderMods();
         GL11.glColor3f(0.0F, 0.0F, 0.0F);
         if (((Boolean)CustomHUDTabGui.enableTabGui.getValueState()).booleanValue()) {
            Client.getInstance().getTabGui().draw();
         }

         UnicodeFontRenderer logoFont = Client.getInstance().getFontManager().arialBold30;
      }
   }

   private ArrayList clientNameChars() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < "Slowly".length(); ++i) {
         String c = Character.toString("Slowly".charAt(i)).toUpperCase();
         list.add(c);
      }

      return list;
   }

   private int getHue(float value) {
       float hue = 1.0f - value / 360.0f;
       int color = (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
       return color;
   }

   public static void drawHotbarItems(float hotBarHeight) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      float leftX = 1.0F;
      float leftY = (float)((double)(res.getScaledHeight() - 1) - ClientUtil.addY);
      float rightX = (float)(res.getScaledWidth() - 1);
      float rightY = (float)((double)(res.getScaledHeight() - 1) - ClientUtil.addY);
      setMap(itemDate);
      setMap(itemTime);
      setMap(itemPing);
      setMap(itemPosition);

      HotbarItem item;
      Iterator var7;
      float fHeight;
      float fWidth;
      float shadowSize;
      for(var7 = leftItems.iterator(); var7.hasNext(); item.getFont().drawString(item.getStr(), leftX, leftY, ((Color)item.fontColor.getValueState()).getRGB())) {
         item = (HotbarItem)var7.next();
         item.updateFont();
         fHeight = (float)item.getFont().FONT_HEIGHT;
         fWidth = (float)item.getFont().getStringWidth(item.getStr());
         if ((double)(leftY - fHeight + 1.0F) <= (double)res.getScaledHeight() - ClientUtil.addY - (double)hotBarHeight) {
            leftY = (float)((double)(res.getScaledHeight() - 1) - ClientUtil.addY);
            leftX += fWidth * 2.0F;
         }

         leftY -= fHeight + 1.0F;
         shadowSize = ((Double)item.shadowSize.getValueState()).floatValue();
         if (((Boolean)item.useShadow.getValueState()).booleanValue()) {
            item.getFont().drawString(item.getStr(), leftX + shadowSize, leftY + shadowSize, Colors.BLACK.c);
         }
      }

      for(var7 = rightItems.iterator(); var7.hasNext(); item.getFont().drawString(item.getStr(), rightX - fWidth, rightY, ((Color)item.fontColor.getValueState()).getRGB())) {
         item = (HotbarItem)var7.next();
         item.updateFont();
         fHeight = (float)item.getFont().FONT_HEIGHT;
         fWidth = (float)item.getFont().getStringWidth(item.getStr());
         if ((double)(rightY - fHeight + 1.0F) <= (double)res.getScaledHeight() - ClientUtil.addY - (double)hotBarHeight) {
            rightY = (float)((double)res.getScaledHeight() - ClientUtil.addY - 1.0D);
            rightX -= fWidth * 2.0F;
         }

         rightY -= fHeight + 1.0F;
         shadowSize = ((Double)item.shadowSize.getValueState()).floatValue();
         if (((Boolean)item.useShadow.getValueState()).booleanValue()) {
            item.getFont().drawString(item.getStr(), rightX - fWidth + shadowSize, rightY + shadowSize, Colors.BLACK.c);
         }
      }

   }

   private static void setMap(HotbarItem item) {
      if (((Boolean)item.enabled.getValueState()).booleanValue()) {
         if (item.position.isCurrentMode("Left")) {
            if (!leftItems.contains(item)) {
               leftItems.add(item);
            }

            rightItems.remove(item);
         } else if (!rightItems.contains(item)) {
            leftItems.remove(item);
            rightItems.add(item);
         }
      } else {
         leftItems.remove(item);
         rightItems.remove(item);
      }

   }

   private void renderPotionEffects() {
      UnicodeFontRenderer font = Client.getInstance().getFontManager().VERDANA18;
      ScaledResolution res = new ScaledResolution(this.mc);
      int y = (int)((double)(res.getScaledHeight() - (font.FONT_HEIGHT + 1) * this.mc.thePlayer.getActivePotionEffects().size()) - ClientUtil.addY) - (((Boolean)CustomHUDHotbar.enable.getValueState()).booleanValue() ? 23 : 0);
      Iterator localIterator1 = this.mc.thePlayer.getActivePotionEffects().iterator();

      for(potionEffectY = 0; localIterator1.hasNext(); potionEffectY += font.FONT_HEIGHT) {
         PotionEffect effect = (PotionEffect)localIterator1.next();
         Potion potion = Potion.potionTypes[effect.getPotionID()];
         String name = I18n.format(potion.getName());
         int color = Integer.MIN_VALUE;
         if (effect.getEffectName() == "potion.weither") {
            color = -16777216;
         } else if (effect.getEffectName() == "potion.weakness") {
            color = -9868951;
         } else if (effect.getEffectName() == "potion.waterBreathing") {
            color = -16728065;
         } else if (effect.getEffectName() == "potion.saturation") {
            color = -11179217;
         } else if (effect.getEffectName() == "potion.resistance") {
            color = -5658199;
         } else if (effect.getEffectName() == "potion.regeneration") {
            color = -1146130;
         } else if (effect.getEffectName() == "potion.poison") {
            color = -14513374;
         } else if (effect.getEffectName() == "potion.nightVision") {
            color = -6737204;
         } else if (effect.getEffectName() == "potion.moveSpeed") {
            color = FlatColors.WHITE.c;
         } else if (effect.getEffectName() == "potion.moveSlowdown") {
            color = Colors.DARKGREY.c;
         } else if (effect.getEffectName() == "potion.jump") {
            color = Colors.WHITE.c;
         } else if (effect.getEffectName() == "potion.invisibility") {
            color = -9404272;
         } else if (effect.getEffectName() == "potion.hunger") {
            color = -16744448;
         } else if (effect.getEffectName() == "potion.heal") {
            color = -65536;
         } else if (effect.getEffectName() == "potion.harm") {
            color = -3730043;
         } else if (effect.getEffectName() == "potion.fireResistance") {
            color = Colors.ORANGE.c;
         } else if (effect.getEffectName() == "potion.healthBoost") {
            color = -40121;
         } else if (effect.getEffectName() == "potion.digSpeed") {
            color = Colors.YELLOW.c;
         } else if (effect.getEffectName() == "potion.digSlowdown") {
            color = Colors.DARKGREY.c;
         } else if (effect.getEffectName() == "potion.damageBoost") {
            color = -7667712;
         } else if (effect.getEffectName() == "potion.confusion") {
            color = Colors.DARKGREEN.c;
         } else if (effect.getEffectName() == "potion.blindness") {
            color = -8355712;
         } else if (effect.getEffectName() == "potion.absorption") {
            color = Colors.YELLOW.c;
         }

         ClientUtil.getClientfont().drawString(name, (float)res.getScaledWidth() - ClientUtil.getClientfont().getStringWidth(ClientUtil.removeColorCode(name) + Potion.getDurationString(effect) + " ") - 2.0F, (float)(y + 3), ClientFont.FontType.SHADOW_THIN, color, Colors.BLACK.c);
         name = Potion.getDurationString(effect);
         font.drawString(name, (float)(res.getScaledWidth() - font.getStringWidth(name) - 2) + 0.5F, (float)y + 0.5F, Colors.DARKGREY.c);
         font.drawString(name, (float)(res.getScaledWidth() - font.getStringWidth(name) - 2), (float)y, Colors.GREY.c);
         y += font.FONT_HEIGHT;
      }

   }

   public static int getPotionEffectY() {
      return potionEffectY;
   }

   private void renderMods() {
       ScaledResolution res = new ScaledResolution(this.mc);
       ModManager.sortedModList.sort(new Comparator<Mod>(){

           @Override
           public int compare(Mod m1, Mod m2) {
               return CustomHUDModList.getCurrentFont().getStringWidth(m2.getNameWithSuffix()) - CustomHUDModList.getCurrentFont().getStringWidth(m1.getNameWithSuffix());
           }
       });
      int yAxis = 0;
      boolean width = false;
      int countMod = 0;
      ArrayList listToUse = ((Boolean)CustomHUDModList.sortedMode.getValueState()).booleanValue() ? ModManager.sortedModList : ModManager.getModList();
      Iterator var7 = listToUse.iterator();

      while(var7.hasNext()) {
         Mod m = (Mod)var7.next();
         if (!m.getCategory().equals(Mod.Category.NONE)) {
            if (m.isEnabled()) {
               if (m.posX > 0.0F) {
                  m.posX -= RenderUtil.delta * Math.max(25.0F, Math.abs(m.posX - 0.0F) * 4.0F);
               }

               if (m.posX < 0.0F) {
                  m.posX = 0.0F;
               }
            } else {
               double max = (double)(CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) + 5);
               if ((double)m.posX < max) {
                  m.posX = (float)((double)m.posX + (double)RenderUtil.delta * Math.max(50.0D, Math.abs((double)m.posX - max) * 4.0D));
               }

               if ((double)m.posX > max) {
                  m.posX = (float)max;
               }
            }

            if (m.posX < (float)(CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) + 5)) {
               int width1 = CustomHUDModList.getCurrentFont().getStringWidth(m.getName());
               ++countMod;
               int rainbowCol = Gui.rainbow(System.nanoTime(), (float)countMod, 1.0F).getRGB();
               float borderSize = 0.0F;
               float borderSize2 = 0.0F;
               float addShadowX;
               if (((Boolean)CustomHUDModList.enableBorder.getValueState()).booleanValue()) {
                  addShadowX = ((Double)CustomHUDModList.alphaBorder.getValueState()).floatValue();
                  int c = CustomHUDModList.borderColorMode.isCurrentMode("Mod Color") ? m.getColor() : (CustomHUDModList.borderColorMode.isCurrentMode("Own Color") ? ((Color)CustomHUDModList.borderColor.getValueState()).getRGB() : rainbowCol);
                  Color col = new Color(c);
                  int color = (new Color((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, addShadowX)).getRGB();
                  float x = (float)(res.getScaledWidth() - CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) - 2) + m.posX;
                  int height = (int)(((Double)CustomHUDModList.fontSize.getValueState()).doubleValue() / 2.0D);
                  float bSize = ((Double)CustomHUDModList.borderSize.getValueState()).floatValue();
                  if (CustomHUDModList.borderMode.isCurrentMode("Mode 1")) {
                     borderSize = bSize;
                     Gui.drawRect((float)res.getScaledWidth() - bSize, (float)yAxis, (float)res.getScaledWidth(), (float)(yAxis + height), color);
                  } else if (CustomHUDModList.borderMode.isCurrentMode("Mode 2")) {
                     borderSize2 = bSize;
                     Gui.drawRect((float)(res.getScaledWidth() - CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) - 2) + m.posX - borderSize - bSize, (float)yAxis, (float)res.getScaledWidth() - borderSize, (float)(yAxis + (int)(((Double)CustomHUDModList.fontSize.getValueState()).doubleValue() / 2.0D)), ClientUtil.reAlpha(Colors.BLACK.c, ((Double)CustomHUDModList.backgroundAlpha.getValueState()).floatValue()));
                     Gui.drawRect(x - bSize, (float)yAxis, x, (float)(yAxis + height), color);
                  }
               }

               if (borderSize2 == 0.0F) {
                  Gui.drawRect((float)(res.getScaledWidth() - CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) - 2) + m.posX - borderSize - borderSize2, (float)yAxis, (float)res.getScaledWidth() - borderSize, (float)(yAxis + (int)(((Double)CustomHUDModList.fontSize.getValueState()).doubleValue() / 2.0D)), ClientUtil.reAlpha(Colors.BLACK.c, ((Double)CustomHUDModList.backgroundAlpha.getValueState()).floatValue()));
               }

               addShadowX = ((Double)CustomHUDModList.xShadow.getValueState()).floatValue();
               float addShadowY = ((Double)CustomHUDModList.yShadow.getValueState()).floatValue();
               if (((Boolean)CustomHUDModList.useShadow.getValueState()).booleanValue()) {
                  CustomHUDModList.getCurrentFont().drawString(m.getName(), (float)res.getScaledWidth() - borderSize - (float)CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) - 2.0F + m.posX + addShadowX + 1.0F, (float)yAxis + addShadowY + (float)((Double)CustomHUDModList.yAdd.getValueState()).byteValue(), Colors.BLACK.c);
               }

               CustomHUDModList.getCurrentFont().drawString(m.getName(), (float)res.getScaledWidth() - borderSize - (float)CustomHUDModList.getCurrentFont().getStringWidth(m.getNameWithSuffix()) - 2.0F + m.posX + 1.0F, (float)(yAxis + ((Double)CustomHUDModList.yAdd.getValueState()).byteValue()), CustomHUDModList.fontColorMode.isCurrentMode("Mod Color") ? m.getColor() : (CustomHUDModList.fontColorMode.isCurrentMode("Color") ? ((Color)CustomHUDModList.selectedFontColor.getValueState()).getRGB() : rainbowCol));
               if (m.showValue != null) {
                  if (((Boolean)CustomHUDModList.useShadow.getValueState()).booleanValue()) {
                     CustomHUDModList.getCurrentFont().drawString(m.getValue(), (float)res.getScaledWidth() - borderSize - (float)CustomHUDModList.getCurrentFont().getStringWidth(m.getValue()) - 3.0F + m.posX + addShadowX + 1.0F, (float)yAxis + addShadowY + (float)((Double)CustomHUDModList.yAdd.getValueState()).byteValue(), Colors.BLACK.c);
                  }

                  CustomHUDModList.getCurrentFont().drawString(m.getValue(), (float)res.getScaledWidth() - borderSize - (float)CustomHUDModList.getCurrentFont().getStringWidth(m.getValue()) - 3.0F + m.posX + 1.0F, (float)(yAxis + ((Double)CustomHUDModList.yAdd.getValueState()).byteValue()), -9868951);
               }

               yAxis = (int)((double)yAxis + ((Double)CustomHUDModList.fontSize.getValueState()).doubleValue() / 2.0D);
            }
         }
      }

   }
}
