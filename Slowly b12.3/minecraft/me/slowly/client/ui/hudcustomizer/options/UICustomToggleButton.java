package me.slowly.client.ui.hudcustomizer.options;

import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;

public class UICustomToggleButton {
   private CustomValue value;
   private MouseInputHandler handler;
   public int width;
   private int height;
   private int lastX;
   private float animationX = 2.14748365E9F;

   public UICustomToggleButton(CustomValue value, MouseInputHandler handler) {
      this.value = value;
      this.handler = handler;
   }

   public void draw(int mouseX, int mouseY, int x, int y, int width, int height) {
      this.height = height;
      UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton13;
      this.width = 25 + font.getStringWidth(this.value.getValueName()) + 5;
      int radius = 4;
      String strValue = this.value.getValueName();
      boolean enabled = ((Boolean)this.value.getValueState()).booleanValue();
      int color = enabled ? Colors.GREEN.c : Colors.RED.c;
      this.animate(x, mouseY, radius, enabled);
      this.toggle(mouseX, mouseY, x, y, radius);
      this.drawToggleButton(x, y, radius, color, enabled);
      font.drawString(strValue, (float)(x + 5) + 0.5F, (float)y + (float)(height - font.FONT_HEIGHT) / 2.0F + 0.5F, Colors.BLACK.c);
      font.drawString(strValue, (float)(x + 5), (float)y + (float)(height - font.FONT_HEIGHT) / 2.0F, enabled ? -1 : FlatColors.GREY.c);
      this.lastX = x;
   }

   private void drawToggleButton(int x, int y, int radius, int color, boolean enabled) {
      float xMid = (float)(x + this.width - radius * 2 - 3);
      float yMid = (float)y + (float)(this.height - radius) / 2.0F + 2.0F;
      Gui.drawRect(xMid - (float)radius - 0.5F, yMid - (float)radius - 0.5F, xMid + (float)radius + 0.5F, yMid + (float)radius + 0.5F, color);
      Gui.circle(xMid - (float)radius, yMid, (float)radius, color);
      Gui.circle(xMid + (float)radius, yMid, (float)radius, color);
      Gui.circle(this.animationX, yMid, (float)radius - 0.5F, -1);
   }

   private void animate(int x, int y, int radius, boolean enabled) {
      float xMid = (float)(x + this.width - radius * 2 - 3);
      float yMid = (float)y + (float)(this.height - radius) / 2.0F - 3.0F;
      float xEnabled = !enabled ? xMid - (float)radius + 0.25F : xMid + (float)radius - 0.25F;
      if (this.lastX != x) {
         this.animationX = xEnabled;
      }

      if (this.animationX == 2.14748365E9F) {
         this.animationX = xEnabled;
      } else {
         this.animationX = (float)RenderUtil.getAnimationState((double)this.animationX, (double)xEnabled, 100.0D);
      }

   }

   private void toggle(int mouseX, int mouseY, int x, int y, int radius) {
      if (this.isHovering(mouseX, mouseY, x, y, radius) && this.handler.canExcecute()) {
         this.value.setValueState(!((Boolean)this.value.getValueState()).booleanValue());
      }

   }

   public boolean isHovering(int mouseX, int mouseY, int x, int y, int radius) {
      float xMid = (float)x + (float)(this.width - radius) / 2.0F;
      float yMid = (float)y + (float)(this.height - radius) / 2.0F;
      return mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height;
   }
}
