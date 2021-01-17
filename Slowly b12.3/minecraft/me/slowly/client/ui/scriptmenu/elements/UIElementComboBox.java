package me.slowly.client.ui.scriptmenu.elements;

import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UIElementComboBox extends UIElement {
   public float x;
   public float y;
   public float WIDTH = 75.0F;
   public float HEIGHT = 10.0F;
   public boolean open;
   public boolean hover;
   public boolean disabled;
   private ArrayList optionList;
   private double animationY;
   private double hoverOpacity;
   public int selected;
   public int hoveringOption;
   private ResourceLocation ARROW_DOWN;
   private UnicodeFontRenderer font;
   private boolean changed;

   public UIElementComboBox(ArrayList optionList) {
      this.optionList = optionList;
      this.selected = 0;
      this.ARROW_DOWN = new ResourceLocation("slowly/icon/arrow_down.png");
      this.font = Client.getInstance().getFontManager().consolas14;
   }

   public void draw(float xPosition, float yPosition, int mouseX, int mouseY) {
      this.WIDTH = 75.0F;
      this.HEIGHT = 11.0F;
      this.changed = false;
      this.font = Client.getInstance().getFontManager().consolas13;
      this.x = xPosition;
      this.y = yPosition;
      this.hover = !this.disabled && (float)mouseX >= this.x && (float)mouseX <= this.x + this.WIDTH && (float)mouseY >= this.y && (float)mouseY <= this.y + this.HEIGHT;
      this.hoveringOption = -1;
      this.animationY = RenderUtil.getAnimationState(this.animationY, (double)(this.open ? 0.0F : (float)this.optionList.size() * this.HEIGHT), 500.0D);
      this.hoverOpacity = RenderUtil.getAnimationState(this.hoverOpacity, this.hover ? 0.15D : 0.1D, 1.0D);
      if (this.open && this.animationY > (double)((float)this.optionList.size() / 2.0F * this.HEIGHT)) {
         this.animationY = (double)((float)this.optionList.size() / 2.0F * this.HEIGHT);
      }

      if (!this.open && this.animationY > (double)((float)this.optionList.size() / 1.25F * this.HEIGHT)) {
         this.animationY = (double)((float)this.optionList.size() * this.HEIGHT);
      }

      int color = Colors.DARKGREY.c;
      int hoverColor = Colors.WHITE.c;
      Gui.drawRect(this.x, this.y, this.x + this.WIDTH, this.y + this.HEIGHT, color);
      Gui.drawRect(this.x, this.y, this.x + this.WIDTH, this.y + this.HEIGHT, ClientUtil.reAlpha(hoverColor, (float)this.hoverOpacity));
      int imgSize = 4;
      this.font.drawString(this.getSelected(), this.x + 5.0F, this.y + (this.HEIGHT - (float)this.font.FONT_HEIGHT) / 2.0F, -2894893);
      RenderUtil.drawImage(this.ARROW_DOWN, (int)this.x + (int)this.WIDTH - 8, (int)(this.y + (this.HEIGHT - (float)imgSize) / 2.0F), imgSize, imgSize);
   }

   public void drawbox(int mouseX, int mouseY) {
      int color = Colors.DARKGREY.c;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)this.y + (int)this.HEIGHT, (int)this.x + (int)this.WIDTH, (int)this.y + (int)this.HEIGHT + (int)((float)this.optionList.size() * this.HEIGHT));
      float yAxis = this.y + this.HEIGHT - (float)((int)this.animationY);
      Gui.drawRect(this.x, yAxis, this.x + this.WIDTH, yAxis + this.HEIGHT * (float)this.optionList.size(), color);
      Gui.drawBorderedRect(this.x, yAxis, this.x + this.WIDTH, yAxis + this.HEIGHT * (float)this.optionList.size(), 0.5F, FlatColors.BLUE.c, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F));

      for(int i = 0; i < this.optionList.size(); ++i) {
         boolean hover = this.animationY == 0.0D && (float)mouseX >= this.x && (float)mouseX <= this.x + this.WIDTH && (float)mouseY > yAxis && (float)mouseY <= yAxis + this.HEIGHT;
         if (hover) {
            this.hoveringOption = i;
         }

         Gui.drawRect(this.x + 0.5F, yAxis, this.x + this.WIDTH - 0.5F, yAxis + this.HEIGHT, ClientUtil.reAlpha(FlatColors.BLUE.c, hover ? 1.0F : 0.0F));
         this.font.drawString((String)this.optionList.get(i), this.x + 5.0F, yAxis + (this.HEIGHT - (float)this.font.FONT_HEIGHT) / 2.0F, -2894893);
         yAxis += this.HEIGHT;
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
   }

   public void drawbox(int mouseX, int mouseY, int yAdd) {
      int color = Colors.DARKGREY.c;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x, (int)this.y + yAdd + (int)this.HEIGHT, (int)this.x + (int)this.WIDTH, (int)this.y + (int)this.HEIGHT + (int)((float)this.optionList.size() * this.HEIGHT));
      float yAxis = this.y + (float)yAdd + this.HEIGHT - (float)((int)this.animationY);
      Gui.drawRect(this.x, yAxis, this.x + this.WIDTH, yAxis + this.HEIGHT * (float)this.optionList.size(), color);
      Gui.drawBorderedRect(this.x, yAxis, this.x + this.WIDTH, yAxis + this.HEIGHT * (float)this.optionList.size(), 0.5F, FlatColors.BLUE.c, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F));

      for(int i = 0; i < this.optionList.size(); ++i) {
         boolean hover = this.animationY == 0.0D && (float)mouseX >= this.x && (float)mouseX <= this.x + this.WIDTH && (float)mouseY > yAxis && (float)mouseY <= yAxis + this.HEIGHT;
         if (hover) {
            this.hoveringOption = i;
         }

         Gui.drawRect(this.x + 0.5F, yAxis, this.x + this.WIDTH - 0.5F, yAxis + this.HEIGHT, ClientUtil.reAlpha(FlatColors.BLUE.c, hover ? 1.0F : 0.0F));
         this.font.drawString((String)this.optionList.get(i), this.x + 5.0F, yAxis + (this.HEIGHT - (float)this.font.FONT_HEIGHT) / 2.0F, -2894893);
         yAxis += this.HEIGHT;
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
   }

   public String getSelected() {
      return (String)this.optionList.get(this.selected);
   }

   public boolean hasChanged() {
      return this.changed;
   }

   public void mouseClick(int mouseX, int mouseY) {
      if (this.hover) {
         this.open = !this.open;
      } else {
         this.open = false;
      }

      if (this.hoveringOption != -1 && this.animationY == 0.0D) {
         this.selected = this.hoveringOption;
         this.changed = true;
      }

   }
}
