package me.existdev.exist.gui.clickgui.elements;

import java.util.Iterator;
import me.existdev.exist.gui.clickgui.ClickGUI;
import me.existdev.exist.gui.clickgui.elements.ModuleButton;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.FontUtils;

public class Element {
   // $FF: synthetic field
   public ClickGUI clickgui;
   // $FF: synthetic field
   public ModuleButton parent;
   // $FF: synthetic field
   public Setting set;
   // $FF: synthetic field
   public double offset;
   // $FF: synthetic field
   public double x;
   // $FF: synthetic field
   public double y;
   // $FF: synthetic field
   public double width;
   // $FF: synthetic field
   public double height;
   // $FF: synthetic field
   public String setstrg;
   // $FF: synthetic field
   public boolean comboextended;

   // $FF: synthetic method
   public void setup() {
      this.clickgui = this.parent.parent.clickgui;
   }

   // $FF: synthetic method
   public void update() {
      this.x = this.parent.x + this.parent.width + 2.0D;
      this.y = this.parent.y + this.offset;
      this.width = this.parent.width + 10.0D;
      this.height = 15.0D;
      String sname = this.set.getName();
      if(this.set.isBoolean()) {
         this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
         double displayval = this.x + this.width - (double)FontUtils.getStringWidth(this.setstrg);
         if(displayval < this.x + 13.0D) {
            this.width += this.x + 13.0D - displayval + 1.0D;
         }
      } else {
         String displaymax;
         if(this.set.isModes()) {
            this.height = (double)(this.comboextended?this.set.getOptions().size() * (FontUtils.getFontHeight() + 2) + 15:15);
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            int displayval1 = FontUtils.getStringWidth(this.setstrg);
            Iterator textx = this.set.getOptions().iterator();

            while(textx.hasNext()) {
               displaymax = (String)textx.next();
               int temp = FontUtils.getStringWidth(displaymax);
               if(temp > displayval1) {
                  displayval1 = temp;
               }
            }

            double displaymax1 = this.x + this.width - (double)displayval1;
            if(displaymax1 < this.x) {
               this.width += this.x - displaymax1 + 1.0D;
            }
         } else if(this.set.isDigit()) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            String displayval2 = "" + (double)Math.round(this.set.getCurrentValue() * 100.0D) / 100.0D;
            displaymax = "" + (double)Math.round(this.set.getMaxValue() * 100.0D) / 100.0D;
            double textx1 = this.x + this.width - (double)FontUtils.getStringWidth(this.setstrg) - (double)FontUtils.getStringWidth(displaymax) - 4.0D;
            if(textx1 < this.x) {
               this.width += this.x - textx1 + 1.0D;
            }
         }
      }

   }

   // $FF: synthetic method
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
   }

   // $FF: synthetic method
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
      return this.isHovered(mouseX, mouseY);
   }

   // $FF: synthetic method
   public void mouseReleased(int mouseX, int mouseY, int state) {
   }

   // $FF: synthetic method
   public boolean isHovered(int mouseX, int mouseY) {
      return (double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= this.y && (double)mouseY <= this.y + this.height;
   }
}
