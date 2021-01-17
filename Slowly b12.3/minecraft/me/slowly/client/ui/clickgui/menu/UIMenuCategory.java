package me.slowly.client.ui.clickgui.menu;

import java.awt.Color;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.mods.other.Gui;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class UIMenuCategory {
   public Mod.Category c;
   UIMenuMods uiMenuMods;
   private MouseInputHandler handler;
   public boolean open;
   public int x;
   public int y;
   public int width;
   public int tab_height;
   public int x2;
   public int y2;
   public boolean drag = true;
   private double arrowAngle = 0.0D;

   public UIMenuCategory(Mod.Category c, int x, int y, int width, int tab_height, MouseInputHandler handler) {
      this.c = c;
      this.x = x;
      this.y = y;
      this.width = width;
      this.tab_height = tab_height;
      this.uiMenuMods = new UIMenuMods(c, handler);
      this.handler = handler;
   }

   public void draw(int mouseX, int mouseY) {
      new ScaledResolution(Minecraft.getMinecraft());
      UnicodeFontRenderer font = Client.getInstance().getFontManager().getFont("simpleton", 14.0F, true);
      RenderUtil.drawRoundedRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.tab_height), 1.5F, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 1.0F) : -8487037);
      String name = "";
      if (Gui.mode.isCurrentMode("Slowly")) {
         name = "Panel " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
      } else {
         name = "     " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length()) + "     ";
      }

      font.drawString(name, (float)(this.x + (this.width - font.getStringWidth(name)) / 2), (float)(this.y + (this.tab_height - font.FONT_HEIGHT) / 2), -1);
      double xMid = (double)(this.x + this.width - 10 + 2);
      double yMid = (double)(this.y + 12 + 2);
      this.arrowAngle = RenderUtil.getAnimationState(this.arrowAngle, (double)(this.uiMenuMods.open ? -90 : 0), 1000.0D);
      GL11.glPushMatrix();
      GL11.glTranslated(xMid, yMid, 0.0D);
      GL11.glRotated(this.arrowAngle, 0.0D, 0.0D, 1.0D);
      GL11.glTranslated(-xMid, -yMid, 0.0D);
      boolean hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
      if (hoverArrow) {
         RenderUtil.drawImage(new ResourceLocation("slowly/icon/arrow-down.png"), this.x + this.width - 10, this.y + 10, 5, 5, new Color(0.7058824F, 0.7058824F, 0.7058824F));
      } else {
         RenderUtil.drawImage(new ResourceLocation("slowly/icon/arrow-down.png"), this.x + this.width - 10, this.y + 10, 5, 5);
      }

      GL11.glPopMatrix();
      if (Gui.mode.isCurrentMode("Slowly")) {
         RenderUtil.drawImage(new ResourceLocation("slowly/icon/menu.png"), this.x + 10, this.y + 9, 8, 8);
      }

      this.upateUIMenuMods();
      this.width = font.getStringWidth(name) + 40;
      this.uiMenuMods.draw(mouseX, mouseY);
      this.move(mouseX, mouseY);
   }

   private void move(int mouseX, int mouseY) {
      boolean hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
      if (!hoverArrow && this.isHovering(mouseX, mouseY) && this.handler.canExcecute()) {
         this.drag = true;
         this.x2 = mouseX - this.x;
         this.y2 = mouseY - this.y;
      }

      if (hoverArrow && this.handler.canExcecute()) {
         this.uiMenuMods.open = !this.uiMenuMods.open;
      }

      if (!Mouse.isButtonDown(0)) {
         this.drag = false;
      }

      if (this.drag) {
         this.x = mouseX - this.x2;
         this.y = mouseY - this.y2;
      }

   }

   private boolean isHovering(int mouseX, int mouseY) {
      return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.tab_height;
   }

   private void upateUIMenuMods() {
      this.uiMenuMods.x = this.x;
      this.uiMenuMods.y = this.y;
      this.uiMenuMods.tab_height = this.tab_height;
      this.uiMenuMods.width = this.width;
   }

   public void mouseClick(int mouseX, int mouseY) {
      this.uiMenuMods.mouseClick(mouseX, mouseY);
   }

   public void mouseRelease(int mouseX, int mouseY) {
      this.uiMenuMods.mouseRelease(mouseX, mouseY);
   }
}
