package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import saint.comandstuff.commands.Ghost;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;

public class GuiButton extends Gui {
   private final TimeHelper time;
   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
   protected int width;
   protected int height;
   public int xPosition;
   public int yPosition;
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected boolean hovered;
   private static final String __OBFID = "CL_00000668";

   public GuiButton(int buttonId, int x, int y, String buttonText) {
      this(buttonId, x, y, 200, 20, buttonText);
   }

   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      this.time = new TimeHelper();
      this.width = 200;
      this.height = 20;
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
      byte var2 = 1;
      if (!this.enabled) {
         var2 = 0;
      } else if (mouseOver) {
         var2 = 2;
      }

      return var2;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
         FontRenderer var4 = mc.fontRendererObj;
         mc.getTextureManager().bindTexture(buttonTextures);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int var5 = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         if (!Ghost.shouldGhost) {
            float pos = 0.0F;
            if (!this.hovered) {
               this.time.reset();
            }

            if (this.time.hasReached(25L)) {
               pos = 0.1F;
            }

            if (this.time.hasReached(50L)) {
               pos = 0.2F;
            }

            if (this.time.hasReached(75L)) {
               pos = 0.3F;
            }

            if (this.time.hasReached(100L)) {
               pos = 0.4F;
            }

            if (this.time.hasReached(125L)) {
               pos = 0.5F;
            }

            if (this.time.hasReached(150L)) {
               pos = 0.6F;
            }

            if (this.time.hasReached(175L)) {
               pos = 0.7F;
            }

            if (this.time.hasReached(200L)) {
               pos = 0.8F;
            }

            if (this.time.hasReached(225L)) {
               pos = 0.9F;
            }

            if (this.time.hasReached(250L)) {
               pos = 1.0F;
            }

            RenderHelper.drawRect((float)this.xPosition - (this.hovered ? pos : 0.0F), (float)this.yPosition - (this.hovered ? pos : 0.0F), (float)(this.xPosition + this.width) + (this.hovered ? pos : 0.0F), (float)(this.yPosition + this.height) + (this.hovered ? pos : 0.0F), Integer.MIN_VALUE);
            if (this.hovered) {
               RenderHelper.drawRect((float)this.xPosition - pos, (float)this.yPosition - pos, (float)(this.xPosition + this.width) + pos, (float)(this.yPosition + this.height) + pos, -2130706433);
            }

            GL11.glColor3f(2.55F, 2.55F, 2.55F);
         } else {
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
         }

         this.mouseDragged(mc, mouseX, mouseY);
         GL11.glPushMatrix();
         GL11.glPushAttrib(1048575);
         int var6 = -1;
         if (!this.enabled) {
            var6 = -9868951;
         } else if (this.hovered && !Ghost.shouldGhost) {
            var6 = -23296;
         }

         if (!Ghost.shouldGhost) {
            float textWidth = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(this.displayString));
            RenderHelper.getNahrFont().drawString(this.displayString, (float)(this.xPosition + this.width / 2) - textWidth / 2.0F, (float)(this.yPosition + (this.height - 8) / 2 - 4), NahrFont.FontType.SHADOW_THIN, var6, -16777216);
         } else {
            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
         }

         GL11.glPopAttrib();
         GL11.glPopMatrix();
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
      soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void func_175211_a(int p_175211_1_) {
      this.width = p_175211_1_;
   }
}
