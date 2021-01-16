package me.razerboy420.weepcraft.gui.clientsettings.crosshair.pieces;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.gui.Gui;

public class Slider {

   public int x;
   public int y;
   public int width;
   public int height;
   public Value value;
   public float slidamount;


   public Slider(int x, int y, int width, int height, Value value) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.value = value;
      this.slidamount = value.value.floatValue();
   }

   public void draw() {
      Gui.drawRect((float)this.x, (float)(this.y + this.height / 2 - 2), (float)(this.x + this.width), (float)(this.y + this.height / 2 + 2), -10000537);
      Gui.drawRect((float)this.x + this.getPosition() - 2.0F, (float)(this.y + this.height / 2 - 4), (float)this.x + this.getPosition() + 2.0F, (float)(this.y + this.height / 2 + 4), -6974059);
      Gui.drawString(Wrapper.fr(), this.value.name.replace("crosshair_", ""), (float)this.x, (float)(this.y - 2), -1);
      Gui.drawString(Wrapper.fr(), "" + this.value.value, (float)(this.x + this.width - Wrapper.fr().getStringWidth("" + this.value.value)), (float)(this.y - 2), -1);
      if(this.isHovered() && Mouse.isButtonDown(0)) {
         float position = (float)(MouseUtils.getMouseX() - this.x);
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

   public boolean isHovered() {
      return this.x - 5 < MouseUtils.getMouseX() && this.x + 5 + this.width > MouseUtils.getMouseX() && this.y < MouseUtils.getMouseY() && this.y + this.height > MouseUtils.getMouseY();
   }

   public float getPosition() {
      return this.value.value.floatValue() / this.value.max.floatValue() * (float)this.width;
   }
}
