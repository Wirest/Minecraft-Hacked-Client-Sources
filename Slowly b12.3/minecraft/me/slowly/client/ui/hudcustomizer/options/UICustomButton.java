package me.slowly.client.ui.hudcustomizer.options;

import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;

public class UICustomButton {
   public static boolean draw(String text, int x, int y, int mouseX, int mouseY, int color) {
      int width = 150;
      int height = 25;
      RenderUtil.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), color);
      UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton15;
      font.drawString(text, (float)(x + (width - font.getStringWidth(text)) / 2), (float)(y + (height - font.FONT_HEIGHT) / 2), -1);
      boolean hover = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
      if (hover) {
         RenderUtil.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), ClientUtil.reAlpha(Colors.BLACK.c, 0.15F));
      }

      return hover;
   }
}
