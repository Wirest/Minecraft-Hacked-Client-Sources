package me.slowly.client.ui.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class TabGui {
   private int POS_X = 0;
   private int POS_Y = 12;
   private int TAB_WIDTH = 65;
   private int TAB_HEIGHT = 12;
   private ArrayList tabs = new ArrayList();
   private TabSelected currentTab = null;
   private int tabCount = 0;
   private float animateY;
   public static FontRenderer font;
   private KeyInputHandler arrowUp = new KeyInputHandler(200);
   private KeyInputHandler arrowDown = new KeyInputHandler(208);
   private KeyInputHandler arrowLeft = new KeyInputHandler(203);
   private KeyInputHandler arrowRight = new KeyInputHandler(205);
   private int lastTab = -1;

   static {
      font = Client.getInstance().getFontManager().simpleton20;
   }

   public TabGui() {
      this.tabs.clear();
      int yAxis = this.POS_Y;
      Mod.Category[] var5;
      int var4 = (var5 = Mod.Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Mod.Category c = var5[var3];
         if (c != Mod.Category.NONE && c != Mod.Category.SETTINGS) {
            this.tabs.add(new TabSelected(c, this.TAB_WIDTH + 1, yAxis, this.TAB_WIDTH, this.TAB_HEIGHT, this.arrowUp, this.arrowDown, this.arrowLeft, this.arrowRight));
            yAxis += this.TAB_HEIGHT;
         }
      }

   }

   private void prepare() {
      this.POS_X = ((Double)CustomHUDTabGui.xAdd.getValueState()).intValue();
      this.POS_Y = ((Double)CustomHUDTabGui.yAdd.getValueState()).intValue();
      this.TAB_WIDTH = this.longestCategoryName() + ((Double)CustomHUDTabGui.scaleX.getValueState()).intValue();
      this.TAB_HEIGHT = UITabSlot.getCurrentFont().FONT_HEIGHT + ((Double)CustomHUDTabGui.scaleY.getValueState()).intValue();
      font = UITabSlot.getCurrentFont();
      if (this.tabCount < 0) {
         this.tabCount = 0;
      }

      if (this.tabCount > this.tabs.size() - 1) {
         this.tabCount = this.tabs.size() - 1;
      }

      this.currentTab = (TabSelected)this.tabs.get(this.tabCount);
      int yAxis = this.POS_Y;

      for(Iterator var3 = this.tabs.iterator(); var3.hasNext(); yAxis += this.TAB_HEIGHT) {
         TabSelected selected = (TabSelected)var3.next();
         selected.y = yAxis;
         selected.height = this.TAB_HEIGHT;
         selected.width = 70;
      }

   }

   private int longestCategoryName() {
      int length = -1;
      Mod.Category[] var5;
      int var4 = (var5 = Mod.Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Mod.Category c = var5[var3];
         int curLength = UITabSlot.getCurrentFont().getStringWidth(c.name());
         if (curLength > length) {
            length = curLength;
         }
      }

      return length;
   }

   private void handleKeyInput() {
      if (Minecraft.getMinecraft().currentScreen == null) {
         if (this.currentTab.open) {
            if (this.arrowLeft.canExcecute()) {
               if (this.currentTab != null && this.currentTab.openValues == null) {
                  this.currentTab.open = false;
                  this.currentTab.animationX = 0.0F;
                  this.currentTab.activated = false;
                  this.currentTab.openValues.activated = false;
               } else if (this.currentTab != null && this.currentTab.openValues != null && this.currentTab.openValues.openOption == null) {
                  this.currentTab.openValues.animationX = 0.0F;
                  this.currentTab.openValues = null;
                  this.currentTab.activated = false;
                  this.currentTab.openValues.activated = false;
                  this.currentTab.openValues.openOption = null;
               } else {
                  this.currentTab.openValues.openOption.animationX = 0.0F;
                  this.currentTab.openValues.openOption = null;
                  this.currentTab.openValues.activated = false;
               }
            }
         } else {
            if (this.arrowUp.canExcecute()) {
               this.tabCount -= this.tabCount == 0 ? -this.tabs.size() + 1 : 1;
               if (this.tabCount == this.tabs.size() - 1) {
                  this.animateY = (float)this.currentTab.getY();
               }
            }

            if (this.arrowDown.canExcecute()) {
               this.tabCount += this.tabCount == this.tabs.size() - 1 ? -this.tabs.size() + 1 : 1;
            }

            if (this.arrowRight.canExcecute()) {
               this.currentTab.open = true;
            }
         }

      }
   }

   private void drawRectWithAnimation() {
      float add = 50.0F * RenderUtil.delta;
      boolean speedUp = this.tabCount == 0 && this.animateY > (float)this.currentTab.getY() && this.lastTab != 1;
      boolean speedDown = this.tabCount == this.tabs.size() - 1 && this.animateY < (float)this.currentTab.getY() && this.lastTab != this.tabs.size() - 2;
      if (this.animateY - add > (float)this.currentTab.getY()) {
         this.animateY -= add * (float)(speedUp ? 5 : 1);
      } else if (this.animateY > (float)this.currentTab.getY()) {
         this.animateY = (float)this.currentTab.getY();
         this.lastTab = this.tabCount;
      }

      if (this.animateY + add < (float)this.currentTab.getY()) {
         this.animateY += add * (float)(speedDown ? 5 : 1);
      } else if (this.animateY < (float)this.currentTab.getY()) {
         this.animateY = (float)this.currentTab.getY();
         this.lastTab = this.tabCount;
      }

      UITabSlot.drawTabGuiSelector((float)this.POS_X, this.animateY, (float)(this.POS_X + this.TAB_WIDTH - 1), this.animateY + (float)this.TAB_HEIGHT);
   }

   public void draw() {
      this.prepare();
      this.handleKeyInput();
      UITabSlot.drawTabGuiSlot((float)this.POS_X, (float)this.POS_Y, (float)(this.TAB_WIDTH - 1), (float)(this.TAB_HEIGHT * this.tabs.size()));
      this.drawRectWithAnimation();

      for(int i = 0; i < this.tabs.size(); ++i) {
         int y = this.POS_Y + this.TAB_HEIGHT * i;
         String text = ((TabSelected)this.tabs.get(i)).getCategory().name();
         text = text.substring(0, 1) + text.substring(1, text.length()).toLowerCase();
         UITabSlot.drawString(text, (float)(this.POS_X + 2), (float)(y + (this.TAB_HEIGHT - font.FONT_HEIGHT) / 2), -1, this.tabCount == i);
      }

      if (this.currentTab.open) {
         this.currentTab.draw(this.TAB_WIDTH - 65 + this.POS_X + (((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue() ? ((Double)CustomHUDTabGui.borderSize.getValueState()).intValue() * 2 + 2 : 0));
      }

   }
}
