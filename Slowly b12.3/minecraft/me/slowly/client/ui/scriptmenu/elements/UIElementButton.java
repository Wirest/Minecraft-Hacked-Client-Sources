package me.slowly.client.ui.scriptmenu.elements;

import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class UIElementButton extends UIElement {
   private String text;
   private ResourceLocation image;
   private UnicodeFontRenderer font;
   private int x;
   private int y;
   private int width;
   private int height;
   private double hoverOpacity;
   private boolean hover;
   private boolean lastClicked;
   private MouseInputHandler mouseHandler;
   private int IMAGE_SIZE = 6;
   public boolean disabled;

   public UIElementButton(String text) {
      this.text = text;
      this.image = null;
      this.font = Client.getInstance().getFontManager().consolas14;
      this.width = this.font.getStringWidth(text) + 6;
      this.height = this.font.FONT_HEIGHT + 2;
      this.hoverOpacity = 0.0D;
      this.mouseHandler = new MouseInputHandler(0);
   }

   public UIElementButton(ResourceLocation rl) {
      this.text = null;
      this.image = rl;
      this.font = null;
      this.width = this.IMAGE_SIZE * 2;
      this.height = this.IMAGE_SIZE * 2;
      this.hoverOpacity = 0.0D;
      this.mouseHandler = new MouseInputHandler(0);
   }

   public void draw(float x, float y, int mouseX, int mouseY) {
      if (this.font != null) {
         this.font = Client.getInstance().getFontManager().consolas14;
         this.width = this.font.getStringWidth(this.text) + 8;
         this.height = this.font.FONT_HEIGHT + 4;
      }

      this.IMAGE_SIZE = 8;
      this.x = (int)x;
      this.y = (int)y;
      this.hover = !this.disabled && (float)mouseX >= x && (float)mouseX <= x + (float)this.width && (float)mouseY >= y && (float)mouseY <= y + (float)this.height;
      this.hoverOpacity = RenderUtil.getAnimationState(this.hoverOpacity, this.hover ? 0.15D : 0.1D, 1.0D);
      int color = Colors.DARKGREY.c;
      int hoverColor = Colors.WHITE.c;
      Gui.drawRect(x, y, x + (float)this.width, y + (float)this.height, color);
      Gui.drawRect(x, y, x + (float)this.width, y + (float)this.height, ClientUtil.reAlpha(hoverColor, (float)this.hoverOpacity));
      if (this.lastClicked) {
         Gui.drawBorderedRect(x, y, x + (float)this.width, y + (float)this.height, 0.5F, FlatColors.BLUE.c, this.hover && Mouse.isButtonDown(0) ? FlatColors.BLUE.c : 0);
      }

      if (this.text != null) {
         this.font.drawString(this.text, (float)this.x + (float)(this.width - this.font.getStringWidth(this.text)) / 2.0F, (float)this.y + (float)(this.height - this.font.FONT_HEIGHT) / 2.0F, -2894893);
      }

      if (this.image != null) {
         RenderUtil.drawImage(this.image, (int)((float)this.x + (float)(this.width - this.IMAGE_SIZE) / 2.0F), (int)((float)this.y + (float)(this.height - this.IMAGE_SIZE) / 2.0F), this.IMAGE_SIZE, this.IMAGE_SIZE);
      }

      if (this.disabled) {
         Gui.drawRect(x, y, x + (float)this.width, y + (float)this.height, ClientUtil.reAlpha(color, 0.5F));
      }

      this.setBorder(this.hover);
   }

   private void setBorder(boolean hover) {
      if (!hover && Mouse.isButtonDown(0)) {
         this.lastClicked = false;
      }

   }

   public boolean clicked() {
      if (this.mouseHandler.canExcecute() && this.hover) {
         this.lastClicked = true;
         return true;
      } else {
         return false;
      }
   }

   public void setImage(ResourceLocation image) {
      this.image = image;
   }
}
