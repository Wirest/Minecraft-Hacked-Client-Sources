package me.razerboy420.weepcraft.gui.click;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.gui.Gui;

public class Properties {

   public String name;
   public float x;
   public float y;
   public int width = 88;
   public int height = 13;
   public Button button;
   public Value value;
   public boolean isEditing;
   int count = 10;


   public Properties(String name, float x, float y, Value value, Button button) {
      this.name = name;
      this.x = x;
      this.y = y;
      this.value = value;
      this.button = button;
   }

   public void render() {
      this.x = (float)(this.button.window.getX() + 91);
      if(this.count > 0) {
         --this.count;
      }

      if(this.count == 0) {
         this.count = 40;
      }

      if(this.button.isOpen) {
         this.width = 94;
         int color = -13619152;
         String h = this.isHovered()?ColorUtil.getColor(Weepcraft.normalColor):(this.value.isaboolean?(this.value.boolvalue?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.whiteColor)):ColorUtil.getColor(Weepcraft.whiteColor));
         Gui.drawString(Wrapper.clientFont(), h + (this.value.isamode?this.name + ": " + this.value.stringvalue:(this.value.iseditable?this.name + ": " + this.value.editvalue + (this.isEditing && this.count > 20?"_":""):this.name)), this.x + (float)this.button.window.dragX + 1.0F, this.y + (float)this.button.window.dragY + 2.0F, -1);
         if(this.value.isafloat) {
            Gui.drawRect(this.x + (float)this.button.window.dragX, this.y + (float)this.button.window.dragY + 11.0F, this.x + (float)this.button.window.dragX + this.getPosition(), this.y + (float)this.button.window.dragY + 12.0F, ColorUtil.getHexColor(Weepcraft.normalColor));
            Gui.drawString(Wrapper.clientFont(), ColorUtil.getColor(Weepcraft.whiteColor) + this.value.value, this.x + (float)this.button.window.dragX + (float)this.width - 2.0F - (float)Wrapper.clientFont().getStringWidth("" + this.value.value), this.y + (float)this.button.window.dragY + 2.0F, -1);
            if(Mouse.isButtonDown(0) && this.isHovered()) {
               float position = (float)MouseUtils.getMouseX() - (this.x + (float)this.button.window.dragX);
               float percent = (float)MathUtils.round((double)(position / (float)this.width * 100.0F), 0);
               float increment = this.value.increment.floatValue();
               if(percent > 100.0F) {
                  percent = 100.0F;
               }

               if(percent < 0.0F) {
                  percent = 0.0F;
               }

               float value = percent / 100.0F * this.value.max.floatValue();
               if(value > this.value.max.floatValue()) {
                  value = this.value.max.floatValue();
               }

               if(value < this.value.min.floatValue()) {
                  value = this.value.min.floatValue();
               }

               this.value.value = Float.valueOf(value);
               this.value.value = Float.valueOf((float)((double)((float)Math.round((double)value * (1.0D / (double)increment))) / (1.0D / (double)increment)));
               this.value.value = Float.valueOf((float)MathUtils.round((double)this.value.value.floatValue(), 2));
            }
         }

      }
   }

   public float getPosition() {
      return this.value.value.floatValue() / this.value.max.floatValue() * (float)this.width;
   }

   public boolean isHovered() {
      return this.x + (float)this.button.window.dragX <= (float)MouseUtils.getMouseX() && this.x + (float)this.button.window.dragX + (float)this.width >= (float)MouseUtils.getMouseX() && this.y + (float)this.button.window.dragY <= (float)MouseUtils.getMouseY() && this.y + (float)this.button.window.dragY + 12.0F >= (float)MouseUtils.getMouseY();
   }
}
