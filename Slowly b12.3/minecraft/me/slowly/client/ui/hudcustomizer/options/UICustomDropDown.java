package me.slowly.client.ui.hudcustomizer.options;

import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UICustomDropDown {
   private CustomValue value;
   private int x;
   private int y;
   private int width;
   private int height;
   public boolean open;

   public UICustomDropDown(CustomValue value) {
      this.value = value;
   }

   public void draw(int mouseX, int mouseY, int x, int y) {
      UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton13;
      this.x = x;
      this.y = y;
      this.width = 55;
      this.height = 13;
      Gui.drawRect(x, y, x + this.width, y + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.35F));
      if (this.isHovering(mouseX, mouseY, x, y)) {
         Gui.drawRect(x, y, x + this.width, y + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.15F));
      }

      font.drawString(this.value.getModeAt(this.value.getCurrentMode()), (float)(x + 2), (float)(y + (this.height - font.FONT_HEIGHT) / 2), -1);
      int iconSize = 4;
      RenderUtil.drawImage(new ResourceLocation("slowly/icon/sort-down.png"), x + this.width - iconSize - 2, y + (this.height - iconSize) / 2, iconSize, iconSize);
      int yAxis = y + this.height + 1;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      if (this.open) {
         Gui.drawRect(x, yAxis - 1, x + this.width, yAxis, FlatColors.ORANGE.c);

         for(int i = 0; i < this.value.mode.size(); ++i) {
            String mode = (String)this.value.mode.get(i);
            Gui.drawRect(x, yAxis, x + this.width, yAxis + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.35F));
            if (this.isHovering(mouseX, mouseY, x, yAxis)) {
               Gui.drawRect(x, yAxis, x + this.width, yAxis + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.15F));
            }

            font.drawString(this.value.getModeAt(i), (float)(x + 2), (float)(yAxis + (this.height - font.FONT_HEIGHT) / 2), -1);
            yAxis += this.height;
         }
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
   }

   public void mouseClick(int mouseX, int mouseY) {
      if (this.isHovering(mouseX, mouseY, this.x, this.y)) {
         this.open = !this.open;
      } else if (this.open) {
         int yAxis = this.y + this.height;

         for(int i = 0; i < this.value.mode.size(); ++i) {
            String mode = (String)this.value.mode.get(i);
            if (this.isHovering(mouseX, mouseY, this.x, yAxis)) {
               this.value.setCurrentMode(i);
            }

            yAxis += this.height;
         }

         this.open = false;
      }

   }

   public void mouseRelease() {
   }

   public boolean isHovering(int mouseX, int mouseY, int x, int y) {
      return mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height - 1;
   }
}
