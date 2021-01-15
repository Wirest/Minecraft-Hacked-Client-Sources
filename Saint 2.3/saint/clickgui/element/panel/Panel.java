package saint.clickgui.element.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;
import saint.clickgui.element.Element;
import saint.modstuff.ModManager;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class Panel {
   private ModManager.Category category;
   private int x;
   private int y;
   private int x2;
   private int y2;
   private int width;
   private int height;
   private int scroll;
   private int dragged;
   private boolean open;
   private boolean drag;
   private final List elements = new ArrayList();
   private boolean scrollbar = false;

   public Panel(ModManager.Category category, int x, int y, int width, int height, boolean open) {
      this.category = category;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.open = open;
      this.setupItems();
   }

   public void setupItems() {
   }

   public int getElementsHeight() {
      int height = 0;
      int count = 0;
      Iterator var4 = this.elements.iterator();

      while(var4.hasNext()) {
         Element element = (Element)var4.next();
         if (count < 10) {
            height += element.getHeight() + 1;
            ++count;
         }
      }

      return height;
   }

   public boolean isHovering(int mouseX, int mouseY) {
      float textWidth = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(this.category.getName() + " [§8" + this.elements.size() + "§f]")) - 100.0F;
      return (float)mouseX >= (float)this.x - textWidth / 2.0F - 4.0F && (float)mouseX <= (float)this.x - textWidth / 2.0F + RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(this.category.getName() + " [§8" + this.elements.size() + "§f]")) + 4.0F && mouseY >= this.y && mouseY <= this.y + this.height - (this.open ? 2 : 0);
   }

   public void drag(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = this.x2 + mouseX;
         this.y = this.y2 + mouseY;
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
         this.x2 = this.x - mouseX;
         this.y2 = this.y - mouseY;
         this.drag = true;
      } else if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
         this.open = !this.open;
      } else if (this.open) {
         Iterator var5 = this.elements.iterator();

         while(var5.hasNext()) {
            Element element = (Element)var5.next();
            element.mouseClicked(mouseX, mouseY, mouseButton);
         }

      }
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (state == 0) {
         this.drag = false;
      }

      if (this.open) {
         Iterator var5 = this.elements.iterator();

         while(var5.hasNext()) {
            Element element = (Element)var5.next();
            element.mouseReleased(mouseX, mouseY, state);
         }

      }
   }

   public void drawScreen(int mouseX, int mouseY, float button) {
      this.drag(mouseX, mouseY);
      float elementsHeight = (float)(this.open ? this.getElementsHeight() - 1 : 0);
      float textWidth = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(this.category.getName() + " [§8" + this.elements.size() + "§f]"));
      boolean scrollbar = this.elements.size() >= 10;
      if (this.scrollbar != scrollbar) {
         this.scrollbar = scrollbar;
      }

      RenderHelper.drawBorderedRect((float)this.getX() - (textWidth - 100.0F) / 2.0F - 4.0F, (float)this.getY(), (float)this.getX() - (textWidth - 100.0F) / 2.0F + textWidth + 4.0F, (float)(this.getY() + 16), 1.0F, -16777216, this.open ? -2142711348 : Integer.MIN_VALUE);
      RenderHelper.getNahrFont().drawString(this.getTitle(), (float)this.getX() - (textWidth - 100.0F) / 2.0F, (float)this.getY(), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      int y;
      if (Mouse.hasWheel() && mouseX >= this.getX() && mouseX <= this.getX() + 100 && mouseY >= this.getY() && (float)mouseY <= (float)(this.getY() + 19) + elementsHeight) {
         y = Mouse.getDWheel();
         if (y < 0 && this.scroll < this.elements.size() - 10) {
            ++this.scroll;
            if (this.scroll < 0) {
               this.scroll = 0;
            }
         } else if (y > 0) {
            --this.scroll;
            if (this.scroll < 0) {
               this.scroll = 0;
            }
         }

         if (y < 0) {
            if (this.dragged < this.elements.size() - 10) {
               ++this.dragged;
            }
         } else if (y > 0 && this.dragged >= 1) {
            --this.dragged;
         }
      }

      if (this.open) {
         RenderHelper.drawBorderedRect((float)(this.getX() - (scrollbar ? 4 : 0)), (float)(this.getY() + 18), (float)(this.getX() + 100), (float)(this.getY() + 19) + elementsHeight, 1.5F, -587202560, Integer.MIN_VALUE);
         if (scrollbar) {
            RenderHelper.drawBorderedRect((float)(this.getX() - 2), (float)(this.getY() + 21), (float)this.getX(), (float)(this.getY() + 16) + elementsHeight, 1.5F, -587202560, -2142711348);
            RenderHelper.drawRect((float)(this.getX() - 2), (float)(this.getY() + 30) + (elementsHeight - 24.0F) / (float)(this.elements.size() - 10) * (float)this.dragged - 10.0F, (float)this.getX(), (float)(this.getY() + 40) + (elementsHeight - 24.0F) / (float)(this.elements.size() - 10) * (float)this.dragged, -587202560);
         }

         y = this.y + this.height - 2;
         int count = 0;
         this.elements.size();
         Iterator var10 = this.elements.iterator();

         while(var10.hasNext()) {
            Element element = (Element)var10.next();
            ++count;
            if (count > this.scroll && count < this.scroll + 11 && this.scroll < this.elements.size()) {
               element.setLocation(this.x + 2, y);
               element.setWidth(this.getWidth() - 4);
               element.drawScreen(mouseX, mouseY, button);
               y += element.getHeight() + 1;
            }
         }
      }

   }

   public boolean getScrollbar() {
      return this.scrollbar;
   }

   public ModManager.Category getCategory() {
      return this.category;
   }

   public int getX() {
      return this.x;
   }

   public String getTitle() {
      return this.category.getName() + " [§8" + this.elements.size() + "§f]";
   }

   public int getY() {
      return this.y;
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean getOpen() {
      return this.open;
   }

   public List getElements() {
      return this.elements;
   }

   public void setX(int dragX) {
      this.x = dragX;
   }

   public void setY(int dragY) {
      this.y = dragY;
   }
}
