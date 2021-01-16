package me.razerboy420.weepcraft.gui.click;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class Window {

   private String title;
   private int xPos;
   private int yPos;
   private boolean isOpen;
   private boolean isExtended;
   private boolean isPinned;
   public int dragX;
   public int dragY;
   public int lastDragX;
   public int lastDragY;
   protected boolean dragging;
   public ArrayList buttons = new ArrayList();
   private int buttonCount = 0;
   private int sliderCount = 0;


   public Window(String title, int x, int y) {
      this.title = title;
      this.xPos = x;
      this.yPos = y;
      Click.windows.add(this);
      Click.unFocusedWindows.add(this);
   }

   public void windowDragged(int x, int y) {
      int differencex = x - this.lastDragX - this.dragX;
      int differencey = y - this.lastDragY - this.dragY;
      if(differencey < 50 && differencex < 50) {
         this.dragX = x - this.lastDragX;
         this.dragY = y - this.lastDragY;
      }

   }

   public void addButton(Module mod) {
      this.buttons.add(new Button(this, mod, this.xPos + 1, this.yPos + 13 * this.buttons.size() + 11));
   }

   public void draw(int x, int y) {
      int border = ColorUtil.getHexColor(Weepcraft.borderColor);
      int panel = ColorUtil.getHexColor(Weepcraft.panlColor);
      if(this.isOpen) {
         if(this.dragging) {
            this.windowDragged(x, y);
         }

         RenderUtils2D.drawRect((float)(this.xPos + this.dragX), (float)(this.yPos + this.dragY), (float)(this.xPos + 90 + this.dragX), (float)(this.yPos + 12 + this.dragY), panel & 1895825407);
         RenderUtils2D.drawRect((float)(this.xPos + 90 + this.dragX - 10), (float)(this.yPos + this.dragY + 2), (float)(this.xPos + 90 + this.dragX - 2), (float)(this.yPos + 12 + this.dragY - 2), this.isExtended?panel & 1895825407:1882206256);
         if(!this.title.equalsIgnoreCase("Mod Hub") && !this.title.equalsIgnoreCase("Radar")) {
            RenderUtils2D.drawRect((float)(this.xPos + 90 + this.dragX - 10 - 9), (float)(this.yPos + this.dragY + 2), (float)(this.xPos + 90 + this.dragX - 2 - 9), (float)(this.yPos + 12 + this.dragY - 2), this.isPinned?panel & 1895825407:1882206256);
         }

         Gui.drawString(Wrapper.clientFont(), ColorUtil.getColor(Weepcraft.whiteColor) + this.title, (float)(this.xPos + this.dragX + 2), (float)(this.yPos + this.dragY + 3), -1);
         if(this.isExtended) {
            if(!this.buttons.isEmpty()) {
               RenderUtils2D.drawRect((float)(this.xPos + this.dragX), (float)(this.yPos + 12 + this.dragY), (float)(this.xPos + 90 + this.dragX), (float)(this.yPos + 12 + this.dragY + 1 + 13 * this.buttons.size()), panel & 1895825407);
            }

            Iterator var6 = this.buttons.iterator();

            while(var6.hasNext()) {
               Button startx = (Button)var6.next();
               startx.draw();
               if(x >= startx.getX() + this.dragX && y >= startx.getY() + 1 + this.dragY && x <= startx.getX() + 23 + this.dragX + 63 && y <= startx.getY() + 12 + this.dragY) {
                  startx.isOverButton = true;
               } else {
                  startx.isOverButton = false;
               }
            }
         }

         if(this.getTitle().equalsIgnoreCase("Mod Hub") && this.isExtended() && this.isOpen()) {
            Weepcraft.getClick().tabgui.draw();
         }

         if(this.getTitle().equalsIgnoreCase("Radar")) {
            Weepcraft.radar.draw(this.getX() + this.dragX, this.getY() + this.dragY + 12);
         }
      }

      if(Mouse.isButtonDown(0) && x >= this.xPos + this.dragX && y >= this.yPos + this.dragY && x <= this.xPos + 90 + this.dragX - 18 && y <= this.yPos + 12 + this.dragY) {
         this.dragging = true;
      } else {
         this.dragging = false;
      }

      if(Click.getFocusedPanel() != this) {
         this.dragging = false;
      }

      if(Wrapper.mc().currentScreen == Weepcraft.getClick() && this.getHoveredButton() != null) {
         GL11.glPushMatrix();
         GL11.glScaled(0.8D, 0.8D, 1.0D);
         float startx1 = (float)((double)(MouseUtils.getMouseX() + 7 + Wrapper.fr().getStringWidth(this.getHoveredButton().mod.getDescription()) + 1) * 0.8D > (double)RenderUtils2D.newScaledResolution().getScaledWidth()?(double)(MouseUtils.getMouseX() - 7 - Wrapper.fr().getStringWidth(this.getHoveredButton().mod.getDescription()) - 1) / 0.8D:(double)(MouseUtils.getMouseX() + 7) / 0.8D);
         Gui.drawRect(startx1, (float)(MouseUtils.getMouseY() + 7) / 0.8F, startx1 + (float)Wrapper.fr().getStringWidth(this.getHoveredButton().mod.getDescription()) + 1.0F, (float)(MouseUtils.getMouseY() + 7 + Wrapper.fr().FONT_HEIGHT + 1) / 0.8F, Integer.MIN_VALUE);
         Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + this.getHoveredButton().mod.getDescription(), startx1 + 1.0F, (float)(MouseUtils.getMouseY() + 9) / 0.8F, -1);
         GL11.glPopMatrix();
      }

      if(Keyboard.isKeyDown(29)) {
         this.undragall();
      }

   }

   public Button getHoveredButton() {
      Iterator var2 = this.buttons.iterator();

      while(var2.hasNext()) {
         Button b = (Button)var2.next();
         if(b.isHovered()) {
            return b;
         }
      }

      return null;
   }

   public boolean othersaredragging() {
      Iterator var2 = Click.windows.iterator();

      while(var2.hasNext()) {
         Window w = (Window)var2.next();
         if(w.dragging) {
            return true;
         }
      }

      return false;
   }

   public void undragall() {
      Window w;
      for(Iterator var2 = Click.windows.iterator(); var2.hasNext(); w.dragging = false) {
         w = (Window)var2.next();
      }

   }

   public void mouseClicked(int x, int y, int button) {
      Iterator var5 = this.buttons.iterator();

      while(var5.hasNext()) {
         Button xButton = (Button)var5.next();
         xButton.mouseClicked(x, y, button);
      }

      if(x >= this.xPos + this.dragX && y >= this.yPos + this.dragY && x <= this.xPos + 69 + this.dragX && y <= this.yPos + 12 + this.dragY) {
         Click.sendPanelToFront(this);
      }

      if(x >= this.xPos + 90 + this.dragX - 10 && y >= this.yPos + this.dragY + 2 && x <= this.xPos + 90 + this.dragX - 2 && y <= this.yPos + 12 + this.dragY - 2) {
         this.isExtended = !this.isExtended;
      }

      if(x >= this.xPos + 90 + this.dragX - 10 - 9 && y >= this.yPos + this.dragY + 2 && x <= this.xPos + 90 + this.dragX - 2 - 9 && y <= this.yPos + 12 + this.dragY - 2) {
         this.isPinned = !this.isPinned;
      }

      if(x >= this.xPos + this.dragX && y >= this.yPos + this.dragY && x <= this.xPos + 69 + this.dragX && y <= this.yPos + 12 + this.dragY) {
         this.lastDragX = MouseUtils.getMouseX() - this.dragX;
         this.lastDragY = MouseUtils.getMouseY() - this.dragY;
      }

   }

   public boolean keyTyped(char key, int keyCode) {
      Iterator var4 = this.buttons.iterator();

      while(var4.hasNext()) {
         Button button = (Button)var4.next();
         if(button.keyTyped(key, keyCode)) {
            return true;
         }
      }

      return false;
   }

   public void mouseMovedOrUp(int x, int y, int b) {
      if(b == 0) {
         this.dragging = false;
      }

   }

   public final String getTitle() {
      return this.title;
   }

   public final int getX() {
      return this.xPos;
   }

   public final int getY() {
      return this.yPos;
   }

   public boolean isExtended() {
      return this.isExtended;
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public boolean isPinned() {
      return this.isPinned;
   }

   public void setOpen(boolean flag) {
      this.isOpen = flag;
   }

   public void setExtended(boolean flag) {
      this.isExtended = flag;
   }

   public void setPinned(boolean flag) {
      this.isPinned = flag;
   }

   public static Window get(String name) {
      Iterator var2 = Click.windows.iterator();

      while(var2.hasNext()) {
         Window w = (Window)var2.next();
         if(w.title.equalsIgnoreCase(name)) {
            return w;
         }
      }

      return null;
   }
}
