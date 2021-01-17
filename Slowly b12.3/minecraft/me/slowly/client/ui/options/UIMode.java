package me.slowly.client.ui.options;

import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import me.slowly.client.value.Value;
import net.minecraft.client.gui.Gui;

public class UIMode {
   private int height;
   public int width;
   private Value value;
   private MouseInputHandler handler;

   public UIMode(Value value, MouseInputHandler handler, int width, int height) {
      this.value = value;
      this.handler = handler;
      this.width = width;
      this.height = height;
   }

   public void draw(int mouseX, int mouseY, int x, int y) {
      this.setNextMode(mouseX, mouseY, x, y);
      UnicodeFontRenderer font = Client.getInstance().getFontManager().VERDANA12;
      String displayText = this.value.getModeTitle() + " " + this.value.getModeAt(this.value.getCurrentMode());
      String modeCountText = this.value.getCurrentMode() + 1 + "/" + this.value.mode.size();
      if (this.isHovering(mouseX, mouseY, x, y)) {
         Gui.drawRect(x, y, x + this.width, y + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.35F));
      }

      font.drawString(displayText, (float)x + (float)(this.width - font.getStringWidth(displayText)) / 2.0F + 0.5F, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0F + 0.5F, Colors.BLACK.c);
      font.drawString(displayText, (float)x + (float)(this.width - font.getStringWidth(displayText)) / 2.0F, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0F, -1);
      font.drawString(modeCountText, (float)(x + this.width - font.getStringWidth(modeCountText) - 4) + 0.5F, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0F + 0.5F, Colors.BLACK.c);
      font.drawString(modeCountText, (float)(x + this.width - font.getStringWidth(modeCountText) - 4), (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0F, -1);
   }

   private void setNextMode(int mouseX, int mouseY, int x, int y) {
      if (this.isHovering(mouseX, mouseY, x, y) && this.handler.canExcecute()) {
         if (this.value.getCurrentMode() < this.value.mode.size() - 1) {
            this.value.setCurrentMode(this.value.getCurrentMode() + 1);
         } else {
            this.value.setCurrentMode(0);
         }
      }

   }

   public boolean isHovering(int mouseX, int mouseY, int x, int y) {
      return mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height;
   }
}
