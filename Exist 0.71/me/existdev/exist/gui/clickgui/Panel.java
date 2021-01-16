package me.existdev.exist.gui.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import me.existdev.exist.gui.clickgui.ClickGUI;
import me.existdev.exist.gui.clickgui.elements.ModuleButton;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.FontUtils;
import me.existdev.exist.utils.helper.RenderHelper;

public class Panel {
   // $FF: synthetic field
   public String title;
   // $FF: synthetic field
   public double x;
   // $FF: synthetic field
   public double y;
   // $FF: synthetic field
   private double x2;
   // $FF: synthetic field
   private double y2;
   // $FF: synthetic field
   public double width;
   // $FF: synthetic field
   public double height;
   // $FF: synthetic field
   public boolean dragging;
   // $FF: synthetic field
   public boolean extended;
   // $FF: synthetic field
   public boolean visible;
   // $FF: synthetic field
   public ArrayList Elements = new ArrayList();
   // $FF: synthetic field
   public ClickGUI clickgui;

   // $FF: synthetic method
   public Panel(String ititle, double ix, double iy, double iwidth, double iheight, boolean iextended, ClickGUI parent) {
      this.title = ititle;
      this.x = ix;
      this.y = iy;
      this.width = iwidth;
      this.height = iheight;
      this.extended = iextended;
      this.dragging = false;
      this.visible = true;
      this.clickgui = parent;
      this.setup();
   }

   // $FF: synthetic method
   public void setup() {
   }

   // $FF: synthetic method
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if(this.visible) {
         if(this.dragging) {
            this.x = this.x2 + (double)mouseX;
            this.y = this.y2 + (double)mouseY;
         }

         Color temp = ColorUtils.getClientColor().darker();
         int outlineColor = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170)).getRGB();
         RenderHelper.drawRect(this.x, this.y - 1.0D, this.x + this.width - 16.0D, this.y + this.height - 1.0D, -1325400064);
         RenderHelper.drawRect(this.x + this.width - 15.0D, this.y - 1.0D, this.x + this.width, this.y + this.height - 1.0D, -1879048192);
         FontUtils.drawStringWithShadow(this.extended?"V":">", this.x + this.width - 9.0D, this.y + this.height / 2.0D - (double)(FontUtils.getFontHeight() / 2) - 2.0D, -1052689);
         FontUtils.drawStringWithShadow(this.title, this.x + this.width / 2.0D - 20.0D, this.y + this.height / 2.0D - (double)(FontUtils.getFontHeight() / 2) - 2.0D, -1052689);
         if(this.extended && !this.Elements.isEmpty()) {
            double startY = this.y + this.height;
            int epanelcolor = -1879048192;

            ModuleButton et;
            for(Iterator var10 = this.Elements.iterator(); var10.hasNext(); startY += et.height + 1.0D) {
               et = (ModuleButton)var10.next();
               RenderHelper.drawRect(this.x, startY, this.x + this.width, startY + et.height + 1.0D, epanelcolor);
               et.x = this.x + 2.0D;
               et.y = startY;
               et.width = this.width - 4.0D;
               et.drawScreen(mouseX, mouseY, partialTicks);
            }

            RenderHelper.drawRect(this.x, startY + 1.0D, this.x + this.width, startY + 1.0D, epanelcolor);
         }

      }
   }

   // $FF: synthetic method
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if(!this.visible) {
         return false;
      } else if(mouseButton == 0 && this.isHovered(mouseX, mouseY)) {
         this.x2 = this.x - (double)mouseX;
         this.y2 = this.y - (double)mouseY;
         this.dragging = true;
         return true;
      } else if(mouseButton == 1 && this.isHovered(mouseX, mouseY)) {
         this.extended = !this.extended;
         return true;
      } else {
         if(this.extended) {
            Iterator var5 = this.Elements.iterator();

            while(var5.hasNext()) {
               ModuleButton et = (ModuleButton)var5.next();
               if(et.mouseClicked(mouseX, mouseY, mouseButton)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   // $FF: synthetic method
   public void mouseReleased(int mouseX, int mouseY, int state) {
      if(this.visible) {
         if(state == 0) {
            this.dragging = false;
         }

      }
   }

   // $FF: synthetic method
   public boolean isHovered(int mouseX, int mouseY) {
      return (double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= this.y && (double)mouseY <= this.y + this.height;
   }
}
