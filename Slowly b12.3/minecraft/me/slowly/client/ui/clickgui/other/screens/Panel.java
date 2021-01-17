package me.slowly.client.ui.clickgui.other.screens;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.clickgui.other.UIOther;
import me.slowly.client.util.RenderUtil;

public class Panel {
   private long lastFrame;
   private int x;
   private int y;
   private int x2;
   private int y2;
   private int width = 82;
   private int height = 15;
   private Mod.Category cat;
   private boolean isOpen;
   private ModButton modulePanel;
   private int openValueY;
   private int allModules;
   private static final float SPEED = 500.0F;
   private float yAdd;

   public Panel(int x, int y, Mod.Category cat) {
      this.yAdd = (float)(-this.width);
      this.x = x;
      this.y = y;
      this.cat = cat;
      this.modulePanel = new ModButton(cat, x * 2, y, this.width, this.width, y, 0);
      this.allModules = this.getAllModulesSize();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.update(mouseX, mouseY);
      new Color(41, 128, 185, 1);
      this.yAddCalc(this.getDelta());
      if (this.isOpen) {
         this.updateModulePanel(this.x, this.y + this.height + (int)this.yAdd, this.width, this.height, this.y, this.yAdd);
         this.modulePanel.drawScreen(mouseX, mouseY, partialTicks, this.getDelta());
      }

      RenderUtil.drawRoundedRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), 3.0F, -14057287);
      Client.getInstance().getFontManager().simpleton16.drawString(this.cat.name(), (float)this.x + (float)this.width / 2.0F - this.getStringWidth() / 2.0F, (float)this.y + (float)this.height / 2.0F - this.getStringHeight() / 2.0F + 3.0F, -1);
      this.openValueY = (int)this.modulePanel.yAdd;
   }

   private float getDelta() {
      return RenderUtil.delta;
   }

   private int getAllModulesSize() {
      int mod = 0;
      Iterator var3 = ModManager.getModList().iterator();

      while(var3.hasNext()) {
         Mod m = (Mod)var3.next();
         if (this.cat.equals(m.getCategory())) {
            ++mod;
         }
      }

      return mod;
   }

   private void yAddCalc(float delta) {
      if (this.yAdd < 0.0F && this.isOpen) {
         this.yAdd += 100.0F * delta;
      }

      if (this.yAdd > 0.0F) {
         this.yAdd = 0.0F;
      }

   }

   private float getStringWidth() {
      return (float)Client.getInstance().getFontManager().Bebas25.getStringWidth(this.cat.name());
   }

   private float getStringHeight() {
      return (float)Client.getInstance().getFontManager().Bebas25.getStringHeight(this.cat.name());
   }

   private void update(int mouseX, int mouseY) {
      if (UIOther.move == this.cat) {
         this.x = mouseX - this.x2;
         this.y = mouseY - this.y2;
      }

   }

   private void updateModulePanel(int x, int y, int width, int height, int panelY, float yAdd) {
      this.modulePanel.update(x, y, width, height, panelY, yAdd);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (this.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + this.height)) {
         if (mouseButton == 0) {
            UIOther.move = this.cat;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
         }

         if (mouseButton == 1 && !(this.isOpen = !this.isOpen)) {
            this.yAdd = (float)(-this.openValueY);
         }
      }

      if (this.isOpen) {
         this.modulePanel.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   private int categoryModsSize() {
      int cool = 0;
      Iterator var3 = ModManager.getModList().iterator();

      while(var3.hasNext()) {
         Mod m = (Mod)var3.next();
         if (m.getCategory() == this.cat) {
            ++cool;
         }
      }

      return cool;
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.isOpen) {
         this.modulePanel.mouseReleased(mouseX, mouseY, state);
      }

   }

   private boolean isHovering(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yBottom) {
      return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public Mod.Category getCat() {
      return this.cat;
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public void setOpen(boolean isOpen) {
      this.isOpen = isOpen;
   }
}
