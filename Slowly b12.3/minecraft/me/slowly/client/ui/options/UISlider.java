package me.slowly.client.ui.options;

import java.awt.Color;
import me.slowly.client.Client;
import me.slowly.client.util.Colors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;

public class UISlider {
   private int height = 1;
   public int width = 100;
   public boolean drag;
   private Color color;
   private float lastMouseX = -1.0F;
   private int x2;
   private int y2;
   private String name;
   private double min;
   private double max;
   private double step;

   public UISlider(String valueName, double min, double max, double step) {
      this.name = valueName;
      this.min = min;
      this.max = max;
      this.step = step;
      this.lastMouseX = -1.0F;
   }

   public UISlider(String valueName, double min, double max, double step, int width) {
      this.name = valueName;
      this.width = width;
      this.min = min;
      this.max = max;
      this.step = step;
      this.lastMouseX = -1.0F;
   }

   public double draw(float value, int mouseX, int mouseY, int x, int y) {
      this.height = 2;
      UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton12;
      String strValue = String.valueOf(this.name + " " + value);
      float strWidth = (float)font.getStringWidth(this.name);
      float strHeight = (float)font.getStringHeight(this.name);
      float vWidth = (float)font.getStringWidth(strValue);
      float vheight = (float)font.getStringHeight(strValue);
      Gui.drawRect(x, y, x + this.width, y + this.height, -14076362);
      font.drawString(strValue, (float)(x + this.width / 2) - vWidth / 2.0F + 0.5F, (float)(y + this.height) + 2.5F, Colors.BLACK.c);
      font.drawString(strValue, (float)(x + this.width / 2) - vWidth / 2.0F, (float)(y + this.height + 2), -1);
      this.x2 = x;
      this.y2 = y;
      return this.changeValue(value, mouseX, mouseY, x, y);
   }

   public boolean mouseClick(int mouseX, int mouseY) {
      if (this.isHovering(mouseX, mouseY)) {
         this.drag = true;
      }

      return this.drag;
   }

   public void mouseRelease() {
      this.drag = false;
   }

   private double changeValue(float value, int mouseX, int mouseY, int x, int y) {
      double valAbs = (double)(mouseX - x);
      double perc = valAbs / (double)this.width;
      perc = Math.min(Math.max(0.0D, perc), 1.0D);
      double valRel = (this.max - this.min) * perc;
      double valuu = this.min + valRel;
      double percSlider = ((double)value - this.min) / (this.max - this.min);
      double val = (double)x + (double)this.width * percSlider;
      Gui.drawRect((float)x, (float)y, this.lastMouseX == -1.0F ? (float)((int)val) : (float)x + (float)this.width * this.lastMouseX, (float)(y + this.height), me.slowly.client.mod.mods.other.Gui.mode.isCurrentMode("Slowly") ? (new Color(41, 128, 185)).getRGB() : -13350562);
      Gui.drawRect(this.lastMouseX == -1.0F ? (float)((int)val) : (float)x + (float)this.width * this.lastMouseX - 1.0F, (float)y, this.lastMouseX == -1.0F ? (float)((int)val + 2) : (float)x + (float)this.width * this.lastMouseX + 1.0F, (float)(y + this.height), (new Color(41, 128, 185)).brighter());
      if (this.drag) {
         this.lastMouseX = ((float)Math.min(Math.max(x, mouseX), x + this.width) - (float)x) / (float)this.width;
         valuu = (double)Math.round(valuu * (1.0D / this.step)) / (1.0D / this.step);
         return valuu;
      } else {
         return (double)Math.round((double)value * (1.0D / this.step)) / (1.0D / this.step);
      }
   }

   public boolean isHovering(int mouseX, int mouseY) {
      return mouseX >= this.x2 && mouseY >= this.y2 && mouseX <= this.x2 + this.width && mouseY < this.y2 + this.height;
   }
}
