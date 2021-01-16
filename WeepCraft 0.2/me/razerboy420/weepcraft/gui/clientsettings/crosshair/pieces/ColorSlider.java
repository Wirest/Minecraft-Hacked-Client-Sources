package me.razerboy420.weepcraft.gui.clientsettings.crosshair.pieces;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.module.modules.graphics.Crosshair;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.RainbowUtil;
import net.minecraft.client.gui.Gui;

public class ColorSlider {

   public int x;
   public int y;
   public int width;
   public int height;
   public float slidamount;


   public ColorSlider(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.slidamount = Crosshair.color.value.floatValue();
   }

   public void draw() {
      for(int position = 0; position < 270; ++position) {
         int percent = this.x + position;
         Gui.drawRect((float)percent, (float)this.y, (float)(percent + 1), (float)(this.y + this.height), RainbowUtil.getColorViaHue((float)position).getRGB());
      }

      Gui.drawRect((float)this.x + this.getPosition() - 1.0F, (float)this.y, (float)this.x + this.getPosition() + 1.0F, (float)(this.y + this.height), -6974059);
      if(this.isHovered() && Mouse.isButtonDown(0)) {
         float var5 = (float)(MouseUtils.getMouseX() - this.x);
         float var6 = (float)MathUtils.round((double)(var5 / (float)this.width * 100.0F), 0);
         float increment = 1.0F;
         if(var6 > 100.0F) {
            var6 = 100.0F;
         }

         if(var6 < 0.0F) {
            var6 = 0.0F;
         }

         float value = var6 / 100.0F * 270.0F;
         if(value > 270.0F) {
            value = 270.0F;
         }

         if(value < 0.0F) {
            value = 0.0F;
         }

         this.slidamount = value;
         this.slidamount = (float)((double)((float)Math.round((double)value * (1.0D / (double)increment))) / (1.0D / (double)increment));
         this.slidamount = (float)MathUtils.round((double)this.slidamount, 2);
         Crosshair.color.value = Float.valueOf(this.slidamount);
      }

   }

   public boolean isHovered() {
      return this.x - 5 < MouseUtils.getMouseX() && this.x + 5 + this.width > MouseUtils.getMouseX() && this.y - 1 < MouseUtils.getMouseY() && this.y + 4 + this.height > MouseUtils.getMouseY();
   }

   public float getPosition() {
      return this.slidamount / 270.0F * (float)this.width;
   }
}
