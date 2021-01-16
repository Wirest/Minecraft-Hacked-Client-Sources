package me.razerboy420.weepcraft.gui.click;

import java.util.ArrayList;
import java.util.Iterator;

import me.razerboy420.weepcraft.gui.click.items.ClickItem;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.RenderUtils2D;

public class ClickHub {

   public int x;
   public int y;
   public int width;
   public int height;
   public boolean open;
   public ArrayList clickitems = new ArrayList();


   public ClickHub(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {
      if(this.open) {
         RenderUtils2D.drawRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), 1879048192);
         RenderUtils2D.drawRect((float)(this.x + this.width - 6), (float)(this.y + 1), (float)(this.x + this.width - 1), (float)(this.y + this.height - 1), this.isHovered()?1881482533:1879048192);
         Iterator var2 = this.clickitems.iterator();

         while(var2.hasNext()) {
            ClickItem c = (ClickItem)var2.next();
            c.draw();
         }
      } else {
         RenderUtils2D.drawRect((float)this.x, (float)this.y, (float)(this.x + 10), (float)(this.y + this.height), 1879048192);
         RenderUtils2D.drawRect((float)(this.x + 3), (float)(this.y + 1), (float)(this.x + 8), (float)(this.y + this.height - 1), this.isHovered()?1881482533:1879048192);
      }

   }

   public boolean isHovered() {
      return this.open?this.x + this.width - 6 < MouseUtils.getMouseX() && this.x + this.width - 1 > MouseUtils.getMouseX() && this.y + 1 < MouseUtils.getMouseY() && this.y + this.height - 1 > MouseUtils.getMouseY():this.x + 3 < MouseUtils.getMouseX() && this.x + 8 > MouseUtils.getMouseX() && this.y + 1 < MouseUtils.getMouseY() && this.y + this.height - 1 > MouseUtils.getMouseY();
   }

   public void mouseClicked() {
      if(this.open) {
         Iterator var2 = this.clickitems.iterator();

         while(var2.hasNext()) {
            ClickItem c = (ClickItem)var2.next();
            if(c.isHovered()) {
               c.window.setOpen(!c.window.isOpen());
            }
         }
      }

      if(this.isHovered()) {
         this.open = !this.open;
      }

   }
}
