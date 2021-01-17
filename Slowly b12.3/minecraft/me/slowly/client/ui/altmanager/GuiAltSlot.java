package me.slowly.client.ui.altmanager;

import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiAltSlot {
   private String username;
   private String password;
   public int x;
   public int y;
   public int WIDTH;
   private ScaledResolution res;
   public static final int HEIGHT = 25;
   private boolean clicked;
   public boolean selected;
   public float opacity;
   public int MIN_HEIGHT;
   public int MAX_HEIGHT;
   private float animation_one;
   private float animation_two;
   private float animation_three;
   private MouseInputHandler handler = new MouseInputHandler(0);

   public GuiAltSlot(String username, String password) {
      this.username = username;
      this.password = password;
      this.x = 12;
   }

   private void update() {
      this.res = new ScaledResolution(Minecraft.getMinecraft());
   }

   public void drawScreen(int mouseX, int mouseY) {
      if (this.y <= this.MAX_HEIGHT - 25) {
         if (this.y >= this.MIN_HEIGHT) {
            UnicodeFontRenderer font = Client.getInstance().getFontManager().robotobold12;
            this.update();
            int lightGray = -15066598;
            int stripeWidth = 2;
            Gui.drawRect(this.x, this.y + 1, this.WIDTH - 2, this.y + 25 - 1, ClientUtil.reAlpha(251658240, 0.025F * this.opacity));
            Gui.drawRect(this.x - stripeWidth, this.y + 1, this.x, this.y - 1 + 25, FlatColors.BLUE.c);
            if (this.isHovering(mouseX, mouseY)) {
               Gui.drawRect(this.x, this.y + 1, this.WIDTH - 2, this.y + 25 - 1, ClientUtil.reAlpha(251658240, 0.2F * this.opacity));
            }

            String text = this.username;
            font.drawCenteredString(text, (float)((10 + this.WIDTH) / 2), (float)(this.y + 12 - font.getStringHeight(text) / 2), ClientUtil.reAlpha(Colors.DARKGREY.c, this.opacity));
            int boxX = this.x + this.WIDTH - 10;
            int boxY = this.y + 1;
            int boxSize = 25;
            int size = 5;
            RenderUtil.drawRoundedRect((float)boxX, (float)boxY, (float)(boxX + boxSize - 2), (float)(boxY + boxSize - 1), 1.0F, FlatColors.RED.c);
            RenderUtil.drawImage(new ResourceLocation("slowly/icon/letter-x.png"), boxX + (boxSize - size) / 2, boxY + (boxSize - size) / 2, size, size);
            boolean hoveringCross = mouseX >= boxX && mouseX <= boxX + boxSize && mouseY >= boxY && mouseY <= boxY + boxSize && mouseY < this.res.getScaledHeight() - 35;
            this.animation_one = (float)RenderUtil.getAnimationState((double)this.animation_one, (double)(hoveringCross ? 1 : 0), 5.0D);
            Gui.drawRect(boxX, boxY, boxX + boxSize - 2, boxY + boxSize - 1, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F * this.animation_one));
            boxX += boxSize + 1;
            size = 14;
            RenderUtil.drawRoundedRect((float)boxX, (float)boxY, (float)(boxX + boxSize - 2), (float)(boxY + boxSize - 1), 1.0F, FlatColors.GREY.c);
            RenderUtil.drawImage(new ResourceLocation("slowly/icon/edit.png"), boxX + (boxSize - size) / 2, boxY + (boxSize - size) / 2, size, size);
            boolean hoveringEdit = mouseX >= boxX && mouseX <= boxX + boxSize && mouseY >= boxY && mouseY <= boxY + boxSize;
            this.animation_two = (float)RenderUtil.getAnimationState((double)this.animation_two, (double)(hoveringEdit ? 1 : 0), 5.0D);
            Gui.drawRect(boxX, boxY, boxX + boxSize - 2, boxY + boxSize - 1, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F * this.animation_two));
            boxX += boxSize + 1;
            size = 7;
            RenderUtil.drawRoundedRect((float)boxX, (float)boxY, (float)(boxX + boxSize - 2), (float)(boxY + boxSize - 1), 1.0F, FlatColors.GREEN.c);
            RenderUtil.drawImage(new ResourceLocation("slowly/icon/checked.png"), boxX + (boxSize - size) / 2, boxY + (boxSize - size) / 2, size, size);
            boolean hoveringLogin = mouseX >= boxX && mouseX <= boxX + boxSize && mouseY >= boxY && mouseY <= boxY + boxSize;
            this.animation_three = (float)RenderUtil.getAnimationState((double)this.animation_three, (double)(hoveringLogin ? 1 : 0), 5.0D);
            Gui.drawRect(boxX, boxY, boxX + boxSize - 2, boxY + boxSize - 1, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F * this.animation_three));
            if (this.handler.canExcecute()) {
               if (hoveringCross) {
                  GuiAltManager.toDelete.add(new GuiAltSlot(this.username, this.password));
               } else if (hoveringLogin) {
                  AltLogin.login(this.username, this.password);
               }
            }

         }
      }
   }

   public boolean isHovering(int mouseX, int mouseY) {
      if (this.y > this.MAX_HEIGHT - 25) {
         return false;
      } else {
         return mouseY > this.y && mouseY <= this.y + 25 && mouseX >= this.x && mouseX <= this.WIDTH && mouseY <= this.MAX_HEIGHT - 25 && mouseY >= this.MIN_HEIGHT + 25;
      }
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
