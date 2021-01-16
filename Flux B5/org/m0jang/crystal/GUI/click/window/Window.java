package org.m0jang.crystal.GUI.click.window;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.component.Component;
import org.m0jang.crystal.Utils.AnimationTimer;
import org.m0jang.crystal.Utils.MathUtils;

public class Window {
   public String title;
   public int id;
   public boolean isOpened;
   public boolean isEnabled;
   public int x;
   public int y;
   public int width;
   public int height;
   protected int grabX;
   protected int grabY;
   protected boolean isDragging;
   public int scrollY;
   public float scrollAmount;
   public boolean scrollbarEnabled;
   protected int componentsHeight;
   private boolean isScrolling;
   private int toScrollY;
   AnimationTimer closeAnim = new AnimationTimer(20);
   public boolean openHover;
   protected boolean wasMousePressed;
   public List children = new ArrayList();

   public Window(String title, int x, int y) {
      this.x = x;
      this.y = y;
      this.title = title;
      this.id = (new SecureRandom()).nextInt() & Integer.MAX_VALUE;
      this.width = WolframGui.defaultWidth;
      this.height = WolframGui.defaultHeight;
   }

   public void repositionComponents() {
      int maxWidth = 0;
      int y = WolframGui.defaultHeight;

      Component c;
      Iterator var4;
      for(var4 = this.children.iterator(); var4.hasNext(); maxWidth = Math.max(maxWidth, c.width)) {
         c = (Component)var4.next();
         c.offY = y;
         y += c.height;
      }

      this.width = Math.max(WolframGui.defaultWidth, maxWidth);

      for(var4 = this.children.iterator(); var4.hasNext(); c.width = this.width) {
         c = (Component)var4.next();
      }

      this.height = Math.min(WolframGui.maxWindowHeight, y);
      this.componentsHeight = y - WolframGui.defaultHeight;
   }

   public void update(int mouseX, int mouseY) {
      if (this.isDragging) {
         this.x = mouseX - this.grabX;
         this.y = mouseY - this.grabY;
      }

      this.scrollbarEnabled = this.componentsHeight + WolframGui.defaultHeight > WolframGui.maxWindowHeight;
      Iterator var4 = this.children.iterator();

      while(var4.hasNext()) {
         Component c = (Component)var4.next();
         c.update(mouseX, mouseY);
      }

      this.closeAnim.update(this.isOpened);
      if (this.scrollbarEnabled && this.isScrolling) {
         this.scrollAmount = MathUtils.map((float)(mouseY - this.y), (float)(WolframGui.defaultHeight + 3) + (float)WolframGui.scrollbarHeight / 2.0F, (float)(WolframGui.maxWindowHeight - 3) - (float)WolframGui.scrollbarHeight / 2.0F, 0.0F, 1.0F);
         if (this.scrollAmount > 1.0F) {
            this.scrollAmount = 1.0F;
         }

         if (this.scrollAmount < 0.0F) {
            this.scrollAmount = 0.0F;
         }

         this.scrollY = (int)(this.scrollAmount * (float)(this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight)));
         this.toScrollY = this.scrollY;
      }

      if (Math.abs(this.toScrollY - this.scrollY) < 4) {
         this.scrollY = this.toScrollY;
         this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight));
      } else if (this.scrollY > this.toScrollY) {
         this.scrollY -= 4;
         this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight));
      } else if (this.scrollY < this.toScrollY) {
         this.scrollY += 4;
         this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight));
      }

   }

   public void render(int mouseX, int mouseY) {
      int height = WolframGui.defaultHeight;
      RenderUtils.drawRect((float)this.x, (float)this.y, (float)this.width, (float)height, WolframGui.mainColor);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableAlpha();
      if (Crystal.INSTANCE.getGuiManager().gui.isOpened) {
         float edgeFix = (float)this.closeAnim.getValue() * 2.0F - 1.0F;
         float c = (float)(this.y + height / 2) + edgeFix * 2.0F;
         float pointCenterY = (float)(this.y + height / 2) - edgeFix * 2.0F;
         float pointRightY = (float)(this.y + height / 2) + edgeFix * 2.0F;
         RenderUtils.drawLine2D((double)(this.x + this.width - height + 5), (double)c, (double)((float)(this.x + this.width) - (float)height / 2.0F), (double)pointCenterY, 1.5F, 16777215);
         RenderUtils.drawLine2D((double)((float)(this.x + this.width) - (float)height / 2.0F), (double)pointCenterY, (double)(this.x + this.width - 5), (double)pointRightY, 1.5F, 16777215);
      }

      if (this.openHover) {
         RenderUtils.drawRect((float)(this.x + this.width - height), (float)this.y, (float)height, (float)height, 553648127);
      }

      Fonts.segoe16.drawString(this.title, (float)(this.x + 2), (float)(this.y + height / 2 - Fonts.segoe16.getHeight() / 2), 16777215, false);
      GlStateManager.disableAlpha();
      int edgeFix1 = this.closeAnim.getValue() == 1.0D ? 0 : (this.closeAnim.getValue() == 0.0D ? 0 : 1);
      RenderUtils.beginCrop((float)this.x, (float)this.y + (float)((double)(this.height - WolframGui.defaultHeight) * this.closeAnim.getValue()) + (float)WolframGui.defaultHeight, (float)this.width, (float)(this.height - WolframGui.defaultHeight) * (float)this.closeAnim.getValue() + (float)edgeFix1);
      Component c1;
      Iterator pointCenterY1;
      if (this.closeAnim.getValue() > 0.0D) {
         pointCenterY1 = this.children.iterator();

         while(pointCenterY1.hasNext()) {
            c1 = (Component)pointCenterY1.next();
            if (c1.isEnabled && !c1.type.equalsIgnoreCase("TabGui")) {
               c1.render(mouseX, mouseY);
            }
         }
      }

      if (this.scrollbarEnabled && this.closeAnim.getValue() > 0.0D) {
         RenderUtils.drawRect((float)(this.x + this.width - WolframGui.scrollbarWidth), (float)(this.y + WolframGui.defaultHeight), (float)WolframGui.scrollbarWidth, (float)(WolframGui.maxWindowHeight - WolframGui.defaultHeight), -803727336);
         int c2 = (int)MathUtils.map(this.scrollAmount, 0.0F, 1.0F, 3.0F, (float)(WolframGui.maxWindowHeight - WolframGui.defaultHeight - WolframGui.scrollbarHeight - 3)) + this.y + WolframGui.defaultHeight;
         RenderUtils.drawRect((float)(this.x + this.width - WolframGui.scrollbarWidth), (float)c2, (float)WolframGui.scrollbarWidth, (float)WolframGui.scrollbarHeight, GuiManager.getHexMainColor());
      }

      RenderUtils.endCrop();
      if (this.closeAnim.getValue() > 0.0D) {
         pointCenterY1 = this.children.iterator();

         while(pointCenterY1.hasNext()) {
            c1 = (Component)pointCenterY1.next();
            if (c1.isEnabled && c1.type.equalsIgnoreCase("TabGui")) {
               c1.render(mouseX, mouseY);
            }
         }
      }

   }

   public void action(Component component) {
   }

   public void handleMouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      if (isPressed && !this.wasMousePressed && this.overScrollbar(mouseX, mouseY)) {
         this.isScrolling = true;
      }

      if (!isPressed) {
         this.isScrolling = false;
      }

      if (this.mouseOver(mouseX, mouseY)) {
         int barHeight = WolframGui.defaultHeight;
         this.openHover = MathUtils.contains((float)mouseX, (float)mouseY, (float)(this.x + this.width - barHeight), (float)this.y, (float)(this.x + this.width), (float)(this.y + barHeight));
         if (!this.wasMousePressed && isPressed) {
            this.bringToFront();
         }

         if (this.openHover && !this.wasMousePressed && isPressed) {
            this.isOpened = !this.isOpened;
         }

         boolean overBar = MathUtils.contains((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + barHeight));
         if (!this.openHover && overBar && !this.wasMousePressed && isPressed) {
            this.isDragging = true;
            this.grabX = mouseX - this.x;
            this.grabY = mouseY - this.y;
         } else if (!isPressed) {
            this.isDragging = false;
         }

         Component c;
         Iterator var7;
         if (this.isOpened && !overBar) {
            var7 = this.children.iterator();

            while(var7.hasNext()) {
               c = (Component)var7.next();
               if (c.isEnabled) {
                  c.mouseUpdates(mouseX, mouseY, isPressed);
               }
            }
         } else if (this.isOpened) {
            var7 = this.children.iterator();

            while(var7.hasNext()) {
               c = (Component)var7.next();
               if (c.isEnabled) {
                  c.noMouseUpdates();
               }
            }
         }
      } else if (this.isDragging) {
         if (!isPressed) {
            this.isDragging = false;
         }
      } else {
         this.noMouseUpdates();
      }

      this.wasMousePressed = isPressed;
   }

   public void bringToFront() {
      ArrayList copy = new ArrayList();
      boolean isStatic = Crystal.INSTANCE.guiManager.gui.staticWindows.contains(this);
      copy.addAll(isStatic ? Crystal.INSTANCE.guiManager.gui.staticWindows : Crystal.INSTANCE.guiManager.gui.windows);
      copy.remove(this);
      copy.add(0, this);
      if (isStatic) {
         Crystal.INSTANCE.guiManager.gui.staticWindows = copy;
      } else {
         Crystal.INSTANCE.guiManager.gui.windows = copy;
      }

   }

   public void handleWheelUpdates(int mouseX, int mouseY, boolean b) {
      if (this.mouseOver(mouseX, mouseY)) {
         int wheelEvent = Mouse.getEventDWheel();
         if (wheelEvent != 0) {
            if (wheelEvent > 0) {
               wheelEvent = -1;
            } else if (wheelEvent < 0) {
               wheelEvent = 1;
            }

            this.toScrollY += wheelEvent * WolframGui.defaultHeight;
            if (this.toScrollY > this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight)) {
               this.toScrollY = this.componentsHeight - (WolframGui.maxWindowHeight - WolframGui.defaultHeight);
            }

            if (this.toScrollY < 0) {
               this.toScrollY = 0;
            }
         }
      }

   }

   public void noWheelUpdates() {
   }

   public boolean contains(int mouseX, int mouseY) {
      return MathUtils.contains((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + (this.isOpened ? this.height : WolframGui.defaultHeight)));
   }

   public boolean mouseOver(int mouseX, int mouseY) {
      Iterator var4 = Crystal.INSTANCE.guiManager.gui.staticWindows.iterator();

      Window window;
      do {
         if (!var4.hasNext()) {
            var4 = Crystal.INSTANCE.guiManager.gui.windows.iterator();

            do {
               if (!var4.hasNext()) {
                  return false;
               }

               window = (Window)var4.next();
            } while(!window.isEnabled || !window.contains(mouseX, mouseY));

            if (window == this) {
               return true;
            }

            return false;
         }

         window = (Window)var4.next();
      } while(!window.isEnabled || !window.contains(mouseX, mouseY));

      if (window == this) {
         return true;
      } else {
         return false;
      }
   }

   protected void keepInBounds() {
      this.x = Math.max(this.x, 0);
      this.y = Math.max(this.y, 0);
      this.x = Math.min(this.x, RenderUtils.getDisplayWidth() - WolframGui.defaultWidth);
      this.y = Math.min(this.y, RenderUtils.getDisplayHeight() - WolframGui.defaultHeight);
   }

   public void noMouseUpdates() {
      this.openHover = false;
      this.isDragging = false;
      if (this.isOpened) {
         Iterator var2 = this.children.iterator();

         while(var2.hasNext()) {
            Component c = (Component)var2.next();
            if (c.isEnabled) {
               c.noMouseUpdates();
            }
         }
      }

      if (!Mouse.isButtonDown(0)) {
         this.isScrolling = false;
      }

   }

   public boolean overScrollbar(int mouseX, int mouseY) {
      return this.scrollbarEnabled && this.mouseOver(mouseX, mouseY) && MathUtils.contains((float)mouseX, (float)mouseY, (float)(this.x + this.width - WolframGui.scrollbarWidth), (float)(this.y + WolframGui.defaultHeight), (float)(this.x + this.width), (float)(this.y + this.height));
   }
}
