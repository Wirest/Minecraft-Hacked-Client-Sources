package me.existdev.exist.gui.clickgui.elements.menu;

import java.awt.Color;
import java.util.Iterator;
import me.existdev.exist.gui.clickgui.elements.Element;
import me.existdev.exist.gui.clickgui.elements.ModuleButton;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.FontUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class ElementComboBox extends Element {
   public ElementComboBox(ModuleButton iparent, Setting iset) {
      this.parent = iparent;
      this.set = iset;
      super.setup();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      Color temp = ColorUtils.getClientColor();
      int color = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150)).getRGB();
      RenderHelper.drawRect(this.x + 1.0D, this.y - 1.0D, this.x + this.width, this.y + 13.0D, -1325400064);
      FontUtils.drawTotalCenteredString(this.setstrg, this.x + this.width / 2.0D, this.y + 6.0D, -1);
      int clr1 = color;
      int clr2 = temp.getRGB();
      if(this.comboextended) {
         RenderHelper.drawRect(this.x + 1.0D, this.y + 14.0D, this.x + this.width, this.y + this.height, -1441656302);
         double ay = this.y + 15.0D;

         for(Iterator var11 = this.set.getOptions().iterator(); var11.hasNext(); ay += (double)(FontUtils.getFontHeight() + 2)) {
            String sld = (String)var11.next();
            String elementtitle = sld.substring(0, 1).toUpperCase() + sld.substring(1, sld.length());
            FontUtils.drawCenteredString(elementtitle, this.x + this.width / 2.0D, ay, -1);
            if(sld.equalsIgnoreCase(this.set.getCurrentOption())) {
               RenderHelper.drawRect(this.x + 1.0D, ay - 1.0D, this.x + 2.0D, ay + (double)FontUtils.getFontHeight() + 2.0D, clr1);
            }

            if((double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= ay && (double)mouseY < ay + (double)FontUtils.getFontHeight() + 2.0D) {
               RenderHelper.drawRect(this.x + this.width - 1.2D, ay - 1.0D, this.x + this.width, ay + (double)FontUtils.getFontHeight() + 2.0D, clr2);
            }
         }
      }

   }

   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if(mouseButton == 0) {
         if(this.isButtonHovered(mouseX, mouseY)) {
            this.comboextended = !this.comboextended;
            return true;
         }

         if(!this.comboextended) {
            return false;
         }

         double ay = this.y + 15.0D;

         for(Iterator var7 = this.set.getOptions().iterator(); var7.hasNext(); ay += (double)(FontUtils.getFontHeight() + 2)) {
            String slcd = (String)var7.next();
            if((double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= ay && (double)mouseY <= ay + (double)FontUtils.getFontHeight() + 2.0D) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
               if(this.clickgui != null && this.clickgui.setmgr != null) {
                  this.clickgui.setmgr.getSetting(this.set.getModule(), this.set.getName()).setCurrentOption(slcd.toLowerCase());
               }

               return true;
            }
         }
      }

      return super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public boolean isButtonHovered(int mouseX, int mouseY) {
      return (double)mouseX >= this.x && (double)mouseX <= this.x + this.width && (double)mouseY >= this.y && (double)mouseY <= this.y + 15.0D;
   }
}
