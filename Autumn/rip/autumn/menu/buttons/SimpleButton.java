package rip.autumn.menu.buttons;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import rip.autumn.utils.render.AnimationUtils;

public final class SimpleButton extends GuiButton {
   private int color = 170;
   private double animation = 0.0D;

   public SimpleButton(int buttonId, int x, int y, String buttonText) {
      super(buttonId, x - (int)((double)Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText) / 2.0D), y, Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText), 10, buttonText);
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      this.mouseDragged(mc, mouseX, mouseY);
      if (this.hovered) {
         if (this.color < 255) {
            this.color += 5;
         }

         if (this.animation < (double)this.width / 2.0D) {
            this.animation = AnimationUtils.animate((double)this.width / 2.0D, this.animation, 0.10000000149011612D);
         }
      } else {
         if (this.color > 170) {
            this.color -= 5;
         }

         if (this.animation > 0.0D) {
            this.animation = AnimationUtils.animate(0.0D, this.animation, 0.10000000149011612D);
         }
      }

      drawRect((double)this.xPosition + (double)this.width / 2.0D - this.animation, (double)(this.yPosition + this.height + 2), (double)this.xPosition + (double)this.width / 2.0D + this.animation, (double)(this.yPosition + this.height + 3), (new Color(this.color, this.color, this.color)).getRGB());
      mc.fontRenderer.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, (new Color(this.color, this.color, this.color)).getRGB());
   }
}
