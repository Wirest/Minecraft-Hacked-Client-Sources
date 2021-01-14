package store.shadowclient.client.utils.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class UIFlatButton extends GuiButton {
   private TimeHelper time = new TimeHelper();
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected boolean hovered;
   private int color;
   private float opacity;

   public UIFlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.displayString = buttonText;
      this.color = color;
   }

   protected int getHoverState(boolean mouseOver) {
      byte i = 1;
      if(!this.enabled) {
         i = 0;
      } else if(mouseOver) {
         i = 2;
      }

      return i;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(this.visible) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int var5 = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         if(!this.hovered) {
            this.time.reset();
            this.opacity = 0.0F;
         }

         if(this.hovered) {
            this.opacity += 0.5F;
            if(this.opacity > 1.0F) {
               this.opacity = 1.0F;
            }
         }

         float radius = (float)this.height / 2.0F;
         Gui.drawRect((float)this.xPosition + radius - this.opacity * 0.1F, (float)this.yPosition - this.opacity, (float)(this.xPosition + this.width) - radius + this.opacity * 0.1F, (float)this.yPosition + radius * 2.0F + this.opacity, this.color);
         GL11.glColor3f(2.55F, 2.55F, 2.55F);
         this.mouseDragged(mc, mouseX, mouseY);
         GL11.glPushMatrix();
         GL11.glPushAttrib(1048575);
         GL11.glScaled(1.0D, 1.0D, 1.0D);
         boolean var6 = true;
         Minecraft.getMinecraft().fontRendererObj.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)this.yPosition + (float)(this.height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0F, this.hovered?-1:-3487030);
         GL11.glPopAttrib();
         GL11.glPopMatrix();
      }

   }

   private Color darkerColor(Color c, int step) {
      int red = c.getRed();
      int blue = c.getBlue();
      int green = c.getGreen();
      int var10000;
      if(red >= step) {
         var10000 = red - step;
      }

      if(blue >= step) {
         var10000 = blue - step;
      }

      if(green >= step) {
         var10000 = green - step;
      }

      return c.darker();
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
