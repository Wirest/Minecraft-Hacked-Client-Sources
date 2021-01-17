package me.slowly.client.ui.scriptmenu.elements;

import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class UIElementNumeric extends UIElement {
   public float x;
   public float y;
   public float WIDTH = 75.0F;
   public float HEIGHT = 11.0F;
   private double min;
   private double max;
   private double current;
   private double step;
   private ResourceLocation ARROW_DOWN;
   private ResourceLocation ARROW_UP;
   private UnicodeFontRenderer font;
   private boolean changed;
   private MouseInputHandler handler;

   public UIElementNumeric(double current, double min, double max, double step) {
      this.current = current;
      this.min = min;
      this.max = max;
      this.step = step;
      this.ARROW_DOWN = new ResourceLocation("slowly/icon/numeric-down.png");
      this.ARROW_UP = new ResourceLocation("slowly/icon/numeric-up.png");
      this.font = Client.getInstance().getFontManager().consolas14;
      this.handler = new MouseInputHandler(0);
   }

   public void draw(float x, float y, int mouseX, int mouseY) {
      this.changed = false;
      int color = Colors.DARKGREY.c;
      int hoverColor = Colors.WHITE.c;
      Gui.drawRect(x, y, x + this.WIDTH, y + this.HEIGHT, color);
      Gui.drawRect(x, y, x + this.WIDTH, y + this.HEIGHT, ClientUtil.reAlpha(hoverColor, 0.1F));
      boolean imgSize = true;
      boolean hoverAll = (float)mouseX >= x && (float)mouseX <= x + this.WIDTH && (float)mouseY >= y && (float)mouseY <= y + this.HEIGHT - 1.0F;
      boolean hoverUp = (float)mouseX >= x + this.WIDTH - 12.0F && (float)mouseX <= x + this.WIDTH && (float)mouseY >= y && (float)mouseY < y + 5.0F;
      boolean hoverDown = (float)mouseX >= x + this.WIDTH - 12.0F && (float)mouseX <= x + this.WIDTH && (float)mouseY >= y + 5.0F && (float)mouseY <= y + this.HEIGHT - 1.0F;
      if (hoverAll) {
         Gui.drawRect(x + this.WIDTH - 12.0F, y, x + this.WIDTH, y + 5.0F, ClientUtil.reAlpha(-1, 0.1F));
         Gui.drawRect(x + this.WIDTH - 12.0F, y + 5.5F, x + this.WIDTH, y + this.HEIGHT, ClientUtil.reAlpha(-1, 0.1F));
      }

      if (hoverUp) {
         Gui.drawRect(x + this.WIDTH - 12.0F, y, x + this.WIDTH, y + 5.0F, ClientUtil.reAlpha(-1, 0.1F));
      }

      if (hoverDown) {
         Gui.drawRect(x + this.WIDTH - 12.0F, y + 5.5F, x + this.WIDTH, y + this.HEIGHT, ClientUtil.reAlpha(-1, 0.1F));
      }

      this.font.drawString("+", x + this.WIDTH - 8.0F, y - 1.0F, -1);
      this.font.drawString("-", x + this.WIDTH - 8.0F, y + 4.0F, -1);
      this.font.drawString(String.valueOf(this.current), x + 5.0F, y + (this.HEIGHT - (float)this.font.FONT_HEIGHT) / 2.0F, -2894893);
      if (this.handler.canExcecute()) {
         double old = this.current;
         if (hoverUp) {
            this.current += this.step;
         }

         if (hoverDown) {
            this.current -= this.step;
         }

         if (this.current != old) {
            this.changed = true;
         }
      }

      this.current = (double)Math.round(this.current * (1.0D / this.step)) / (1.0D / this.step);
      if (this.current > this.max) {
         this.current = this.max;
      }

      if (this.current < this.min) {
         this.current = this.min;
      }

   }

   public boolean hasChanged() {
      return this.changed;
   }

   public double getCurrent() {
      return this.current;
   }

   public void setCurrent(double current) {
      this.current = current;
   }
}
