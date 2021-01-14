package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
   private static final int BUTTON_COLOR = -1945564919;
   private static final Color BUTTON_OUTLINE = new Color(131, 194, 239);
   public int xPosition;
   public int yPosition;
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected int width;
   protected int height;
   protected boolean hovered;

   public GuiButton(int buttonId, int x, int y, String buttonText) {
      this(buttonId, x, y, 200, 20, buttonText);
   }

   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.width = widthIn;
      this.height = heightIn;
      this.displayString = buttonText;
   }

   protected int getHoverState(boolean mouseOver) {
      int i = 1;
      if (!this.enabled) {
         i = 0;
      } else if (mouseOver) {
         i = 2;
      }

      return i;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
         FontRenderer fontrenderer = mc.fontRendererObj;
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int i = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         Gui.drawRect((double)this.xPosition, (double)this.yPosition, (double)(this.xPosition + this.width), (double)(this.yPosition + this.height), -1945564919);
         Gui.drawBorder((double)this.xPosition, (double)this.yPosition, (double)this.width, (double)this.height, 0.5F, this.hovered && this.enabled ? BUTTON_OUTLINE.getRGB() : BUTTON_OUTLINE.darker().getRGB());
         this.mouseDragged(mc, mouseX, mouseY);
         int j = -1;
         if (!this.enabled) {
            j = 10526880;
         } else if (this.hovered) {
            j = BUTTON_OUTLINE.getRGB();
         }

         this.drawCenteredString(fontrenderer, (!this.enabled ? "§O" : "") + this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
      }

   }

   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
   }

   public void mouseReleased(int mouseX, int mouseY) {
   }

   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   public void drawButtonForegroundLayer(int mouseX, int mouseY) {
   }

   public void playPressSound(SoundHandler soundHandlerIn) {
      soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }
}
