package me.razerboy420.weepcraft.gui.click;

import java.util.ArrayList;
import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.ValuesFile;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

public class Button {

   public Window window;
   public Module mod;
   private int xPos;
   private int yPos;
   public boolean isOverButton;
   public boolean isOpen;
   public ArrayList prop = new ArrayList();


   public Button(Window window, Module mod, int xPos, int yPos) {
      this.window = window;
      this.mod = mod;
      this.xPos = xPos;
      this.yPos = yPos;
   }

   public void draw() {
      Gui.drawString(Wrapper.clientFont(), this.isHovered()?ColorUtil.getColor(Weepcraft.whiteColor) + this.mod.getName():(this.mod.isToggled()?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.whiteColor)) + this.mod.getName(), (float)(this.getX() + this.window.dragX - (this.getX() + 85 + this.window.dragX) + 86 + this.getX() + this.window.dragX), (float)(this.getY() + 4 + this.window.dragY), this.mod.isToggled()?16777215:14540253);
      if(this.isOpen) {
         RenderUtils2D.drawRect((float)(this.getX() + 89 + this.window.dragX), (float)(this.getAmount(this.window, true) + this.window.dragY + 12), (float)(this.getX() + 96 + 89 + this.window.dragX), (float)(this.getAmount(this.window, true) + 14 + this.window.dragY + 13 * this.prop.size()), ColorUtil.getHexColor(Weepcraft.panlColor) & 1895825407);
         RenderUtils2D.drawRect((float)(this.getX() + 89 + this.window.dragX), (float)(this.getAmount(this.window, true) + this.window.dragY), (float)(this.getX() + 96 + 89 + this.window.dragX), (float)(this.getAmount(this.window, true) + 12 + this.window.dragY), ColorUtil.getHexColor(Weepcraft.panlColor) & 1895825407);
         Gui.drawString(Wrapper.clientFont(), ColorUtil.getColor(Weepcraft.enabledColor) + this.mod.getName(), (float)(this.getX() + 91 + this.window.dragX), (float)(this.getAmount(this.window, true) + 3 + this.window.dragY), -1);
      }

      boolean hasmods = false;
      Iterator var3 = Value.modes.iterator();

      while(var3.hasNext()) {
         Value p = (Value)var3.next();
         if(p.name.toLowerCase().startsWith(this.mod.getName().toLowerCase() + "_")) {
            hasmods = true;
            break;
         }
      }

      if(hasmods) {
         Gui.drawString(Wrapper.clientFont(), ColorUtil.getColor(Weepcraft.whiteColor) + (this.isOpen?"-":"+"), (float)(this.getX() + 88 + this.window.dragX - Wrapper.fr().getStringWidth(this.isOpen?"-":"+")), (float)(this.getY() + this.window.dragY + 5), -1);
      }

      var3 = this.prop.iterator();

      while(var3.hasNext()) {
         Properties p1 = (Properties)var3.next();
         p1.render();
      }

   }

   public void mouseClicked(int x, int y, int button) {
      if(this.isHovered()) {
         if(button == 0) {
            Click.sendPanelToFront(this.window);
            this.mod.toggle();
         }

         if(button == 1) {
            this.prop.clear();
            if(this.isOpen) {
               this.isOpen = false;
            } else {
               boolean hasmods = false;
               Iterator v = Value.modes.iterator();

               while(v.hasNext()) {
                  Value count = (Value)v.next();
                  if(count.name.toLowerCase().startsWith(this.mod.getName().toLowerCase() + "_")) {
                     hasmods = true;
                     break;
                  }
               }

               if(hasmods) {
                  Button var8;
                  for(v = this.window.buttons.iterator(); v.hasNext(); var8.isOpen = false) {
                     var8 = (Button)v.next();
                  }

                  this.isOpen = true;
                  int var9 = 0;
                  Iterator var7 = Value.modes.iterator();

                  while(var7.hasNext()) {
                     Value var10 = (Value)var7.next();
                     if(var10.name.toLowerCase().startsWith(this.mod.getName().toLowerCase() + "_")) {
                        this.prop.add(new Properties(var10.name.split("_")[1], (float)(this.window.getX() + this.window.dragX), (float)(this.getAmount(this.window, false) + var9 * 13), var10, this));
                        ++var9;
                     }
                  }
               }
            }
         }
      }

      if(this.isOpen && this.isOpen && this.getHoveredOption() != null && button == 0 && (this.getHoveredOption().value.isaboolean || this.getHoveredOption().value.isamode)) {
         this.getHoveredOption().value.interact();
         Wrapper.mc().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         ValuesFile.save();
      }

   }

   public boolean keyTyped(char key, int keyCode) {
      return false;
   }

   public boolean isHovered() {
      return MouseUtils.getMouseX() >= this.getX() + this.window.dragX && MouseUtils.getMouseY() >= this.getY() + this.window.dragY && MouseUtils.getMouseX() <= this.getX() + 24 + this.window.dragX + 63 && MouseUtils.getMouseY() <= this.getY() + 12 + this.window.dragY && this.window.isOpen() && this.window.isExtended();
   }

   public int getAmount(Window window, boolean kys) {
      return kys?window.getY():window.getY() + 14;
   }

   public int getX() {
      return this.xPos;
   }

   public int getY() {
      return this.yPos;
   }

   public Properties getHoveredOption() {
      Iterator var2 = this.prop.iterator();

      while(var2.hasNext()) {
         Properties prop = (Properties)var2.next();
         if(prop.isHovered()) {
            return prop;
         }
      }

      return null;
   }
}
