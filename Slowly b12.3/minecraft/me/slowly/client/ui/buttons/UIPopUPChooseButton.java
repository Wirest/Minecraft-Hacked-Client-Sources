package me.slowly.client.ui.buttons;

import me.slowly.client.Client;
import me.slowly.client.ui.designsettings.UIDesignSettings;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UIPopUPChooseButton {
   public float x;
   public float y;
   public float currentRadius;
   public float minRadius;
   public float maxRadius;
   private boolean open;
   private boolean animateUp;
   private boolean animateDown;
   private MouseInputHandler mouseClickedPopUpMenu = new MouseInputHandler(0);
   private double animationScale;
   public String textureLocation;
   public String name;

   public UIPopUPChooseButton(String name, int x, int y, String textureLocation) {
      this.name = name;
      this.maxRadius = 15.0F;
      this.textureLocation = textureLocation;
      this.x = (float)x;
      this.y = (float)y;
   }

   public void draw(int mouseX, int mouseY) {
      this.maxRadius = 12.0F;
      float add = RenderUtil.delta * 100.0F;
      if (this.currentRadius + add < this.maxRadius) {
         this.currentRadius += add;
      } else {
         this.currentRadius = this.maxRadius;
      }

      this.animationScale = RenderUtil.getAnimationState(this.animationScale, this.isHovering(mouseX, mouseY) ? 1.05D : 1.0D, 1.0D);
      if (this.animationScale < 1.0D) {
         this.animationScale = 1.0D;
      }

      float xMid = this.x + this.maxRadius / 2.0F;
      float yMid = this.y + this.maxRadius / 2.0F;
      GL11.glPushMatrix();
      GL11.glTranslated((double)xMid, (double)yMid, 0.0D);
      if (this.isHovering(mouseX, mouseY)) {
         GL11.glScaled(this.animationScale, this.animationScale, 0.0D);
      }

      GL11.glTranslated((double)(-xMid), (double)(-yMid), 0.0D);
      if (this.currentRadius > 1.0F) {
         Gui.circle(this.x, this.y, this.currentRadius, UIDesignSettings.getColor());
         if (this.isHovering(mouseX, mouseY)) {
            Gui.drawFilledCircle(this.x, this.y, this.currentRadius + 0.5F, ClientUtil.reAlpha(Colors.BLACK.c, 0.1F));
         }

         RenderUtil.drawImage(new ResourceLocation(this.textureLocation), (int)(this.x - this.currentRadius / 2.0F), (int)(this.y - this.currentRadius / 2.0F), (int)this.currentRadius, (int)this.currentRadius);
      }

      GL11.glPopMatrix();
      if (this.currentRadius == this.maxRadius && this.isHovering(mouseX, mouseY)) {
         UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton17;
         int xFont = (int)(this.x + this.maxRadius + 5.0F);
         int yFont = (int)(this.y - this.maxRadius / 4.0F - 1.0F);
         RenderUtil.drawRoundedRect((float)(xFont - 2), (float)(yFont - 1), (float)(xFont + font.getStringWidth(this.name) + 2), (float)(yFont + font.FONT_HEIGHT + 1), 1.0F, FlatColors.ASPHALT.c);
         font.drawString(this.name, (float)xFont, (float)yFont, FlatColors.GREY.c);
      }

   }

   public boolean clicked(int mouseX, int mouseY) {
      return this.currentRadius == this.maxRadius && this.mouseClickedPopUpMenu.canExcecute() && this.isHovering(mouseX, mouseY);
   }

   private boolean isHovering(int mouseX, int mouseY) {
      return (float)mouseX >= this.x - this.currentRadius && (float)mouseX <= this.x + this.currentRadius && (float)mouseY >= this.y - this.currentRadius && (float)mouseY <= this.y + this.currentRadius;
   }

   public void setY(float y) {
      this.y = y;
   }
}
