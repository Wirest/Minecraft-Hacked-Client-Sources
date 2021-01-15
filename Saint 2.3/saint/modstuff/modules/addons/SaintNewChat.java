package saint.modstuff.modules.addons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class SaintNewChat extends GuiNewChat {
   private final Minecraft mc;
   private int y1;
   private float width;

   public SaintNewChat(Minecraft mc) {
      super(mc);
      this.mc = mc;
      this.x = 2;
      this.y = 20;
   }

   public void drawChat(int p_146230_1_) {
      if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
         int var2 = this.getLineCount();
         boolean var3 = false;
         int var4 = 0;
         int var5 = this.field_146253_i.size();
         float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
         if (var5 > 0) {
            if (getChatOpen()) {
               var3 = true;
            }

            float var7 = this.getChatScale();
            MathHelper.ceiling_float_int((float)this.getChatWidth() / var7);
            GL11.glPushMatrix();
            GL11.glScalef(var7, var7, 1.0F);

            int var9;
            int var11;
            int var14;
            for(var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
               ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.scrollPos);
               if (var10 != null) {
                  var11 = p_146230_1_ - var10.getUpdatedCounter();
                  if (var11 < 200 || var3) {
                     double var12 = (double)var11 / 200.0D;
                     var12 = 1.0D - var12;
                     var12 *= 10.0D;
                     if (var12 < 0.0D) {
                        var12 = 0.0D;
                     }

                     if (var12 > 1.0D) {
                        var12 = 1.0D;
                     }

                     var12 *= var12;
                     var14 = (int)(255.0D * var12);
                     if (var3) {
                        var14 = 255;
                     }

                     int var10000 = (int)((float)var14 * var6);
                     ++var4;
                  }
               }
            }

            GL11.glTranslated((double)this.x, (double)this.y, 0.0D);
            this.y1 = -var2 * 9;
            GL11.glTranslated(0.0D, -8.0D, 0.0D);
            int color = this.drag ? Integer.MIN_VALUE : 1610612736;
            int bcolor = this.drag ? 1610612736 : 1073741824;
            int var18;
            if (getChatOpen() && this.width > 0.0F) {
               var18 = -var2 * 9;
               float f = (float)(color >> 24 & 255) / 255.0F;
               float f1 = (float)(color >> 16 & 255) / 255.0F;
               float f2 = (float)(color >> 8 & 255) / 255.0F;
               float f3 = (float)(color & 255) / 255.0F;
               GL11.glEnable(3042);
               GL11.glDisable(3553);
               GL11.glBlendFunc(770, 771);
               GL11.glEnable(2848);
               GL11.glPushMatrix();
               GL11.glColor4f(f1, f2, f3, f);
               GL11.glLineWidth(2.0F);
               GL11.glBegin(1);
               GL11.glVertex2d((double)(this.width / 2.0F - 15.0F), (double)(var18 - 11));
               GL11.glVertex2d((double)(this.width / 2.0F - 15.0F), (double)(var18 + 4));
               GL11.glVertex2d((double)(this.width / 2.0F - 15.0F), (double)(var18 + 4));
               GL11.glVertex2d(0.0D, (double)(var18 + 4));
               GL11.glVertex2d(0.0D, (double)(var18 + 4));
               GL11.glVertex2d(0.0D, 9.0D);
               GL11.glVertex2d(0.0D, 9.0D);
               GL11.glVertex2d((double)(this.width + 4.0F), 9.0D);
               GL11.glVertex2d((double)(this.width + 4.0F), 9.0D);
               GL11.glVertex2d((double)(this.width + 4.0F), (double)(var18 + 4));
               GL11.glVertex2d((double)(this.width + 4.0F), (double)(var18 + 4));
               GL11.glVertex2d((double)(this.width / 2.0F + 15.0F), (double)(var18 + 4));
               GL11.glVertex2d((double)(this.width / 2.0F + 15.0F), (double)(var18 + 4));
               GL11.glVertex2d((double)(this.width / 2.0F + 15.0F), (double)(var18 - 11));
               GL11.glVertex2d((double)(this.width / 2.0F + 15.0F), (double)(var18 - 11));
               GL11.glVertex2d((double)(this.width / 2.0F - 15.0F), (double)(var18 - 11));
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glEnable(3553);
               GL11.glDisable(3042);
               GL11.glDisable(2848);
               RenderHelper.drawRect(this.width / 2.0F - 15.0F, (float)(var18 - 11), this.width / 2.0F + 15.0F, (float)var18 + 4.0F, color);
               RenderHelper.getNahrFont().drawString("Chat", this.width / 2.0F - 11.0F, (float)(var18 - 13), NahrFont.FontType.SHADOW_THIN, -1 + ((int)(255.0F * var6) << 24), -16777216 + ((int)(255.0F * var6) << 24));
               RenderHelper.drawRect(0.0F, (float)(var18 + 4), this.width + 4.0F, 9.0F, color);
               this.width = 0.0F;
            } else if (var4 > 0 && this.width > 0.0F) {
               var18 = -var4 * 9;
               RenderHelper.drawBorderedRect(0.0F, (float)(var18 + 4), this.width + 4.0F, 9.0F, 2.0F, bcolor + ((int)(255.0F * var6) << 24), color + ((int)(255.0F * var6) << 24));
               this.width = 0.0F;
            }

            GL11.glLineWidth(1.0F);
            GL11.glTranslated(0.0D, 5.0D, 0.0D);

            for(var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
               ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.scrollPos);
               if (var10 != null) {
                  var11 = p_146230_1_ - var10.getUpdatedCounter();
                  if (var11 < 200 || var3) {
                     double var12 = (double)var11 / 200.0D;
                     var12 = 1.0D - var12;
                     var12 *= 10.0D;
                     if (var12 < 0.0D) {
                        var12 = 0.0D;
                     }

                     if (var12 > 1.0D) {
                        var12 = 1.0D;
                     }

                     var12 *= var12;
                     var14 = (int)(255.0D * var12);
                     if (var3) {
                        var14 = 255;
                     }

                     var14 = (int)((float)var14 * var6);
                     ++var4;
                     if (var14 > 0) {
                        byte var15 = true;
                        int var16 = -var9 * 9;
                        String var17 = Saint.getFriendManager().replaceNames(var10.getChatComponent().getFormattedText(), true);
                        RenderHelper.getNahrFont().drawString(var17, 2.0F, (float)(var16 - 12), NahrFont.FontType.SHADOW_THIN, -1 + (var14 << 24), -16777216 + (var14 << 24));
                        float width = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(var17));
                        if (this.width < width) {
                           this.width = width;
                        }

                        GL11.glDisable(3008);
                     }
                  }
               }
            }

            GL11.glTranslated((double)(-this.x), (double)(-this.y), 0.0D);
            if (var3) {
               var9 = this.mc.fontRendererObj.FONT_HEIGHT;
               GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
               var18 = var5 * var9 + var5;
               var11 = var4 * var9 + var4;
               int var19 = this.scrollPos * var11 / var5;
               int var13 = var11 * var11 / var18;
               if (var18 != var11) {
                  var14 = var19 > 0 ? 170 : 96;
                  int var20 = this.isScrolled ? 13382451 : 3355562;
                  drawRect(0, -var19, 2, -var19 - var13, var20 + (var14 << 24));
                  drawRect(2, -var19, 1, -var19 - var13, 13421772 + (var14 << 24));
               }
            }

            GL11.glPopMatrix();
         }
      }

   }

   private boolean isMouseOverTitle(int par1, int par2) {
      int height = RenderHelper.getScaledRes().getScaledHeight();
      return (float)par1 >= (float)this.x + this.width / 2.0F - 15.0F && par2 >= this.y + this.y1 - 20 + height - 48 && (float)par1 <= (float)this.x + this.width / 2.0F + 15.0F && par2 <= this.y + this.y1 - 6 + height - 48;
   }

   public void mouseClicked(int par1, int par2, int par3) {
      if (this.isMouseOverTitle(par1, par2) && par3 == 0) {
         this.drag = true;
         this.dragX = this.x - par1;
         this.dragY = this.y - par2;
      }

   }

   public void mouseReleased(int par1, int par2, int par3) {
      if (par3 == 0) {
         this.drag = false;
      }

   }

   public void prepareScissorBox(float x, float y, float x2, float y2) {
      int factor = RenderHelper.getScaledRes().getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)RenderHelper.getScaledRes().getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }
}
