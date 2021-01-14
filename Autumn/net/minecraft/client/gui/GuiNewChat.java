package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.visuals.StreamerMod;

public class GuiNewChat extends Gui {
   private static final Logger logger = LogManager.getLogger();
   private final Minecraft mc;
   private final List sentMessages = Lists.newArrayList();
   private final List chatLines = Lists.newArrayList();
   private final List field_146253_i = Lists.newArrayList();
   private int scrollPos;
   private boolean isScrolled;

   public GuiNewChat(Minecraft mcIn) {
      this.mc = mcIn;
   }

   public static int calculateChatboxWidth(float p_146233_0_) {
      int i = 320;
      int j = 40;
      return MathHelper.floor_float(p_146233_0_ * (float)(i - j) + (float)j);
   }

   public static int calculateChatboxHeight(float p_146243_0_) {
      int i = 180;
      int j = 20;
      return MathHelper.floor_float(p_146243_0_ * (float)(i - j) + (float)j);
   }

   public void drawChat(int p_146230_1_) {
      StreamerMod instance = (StreamerMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(StreamerMod.class);
      if (instance == null || !instance.shouldHideChat()) {
         if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.field_146253_i.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (k > 0) {
               if (this.getChatOpen()) {
                  flag = true;
               }

               float f1 = this.getChatScale();
               int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
               GlStateManager.pushMatrix();
               GlStateManager.translate(2.0F, 20.0F, 0.0F);
               GlStateManager.scale(f1, f1, 1.0F);

               int j1;
               int l1;
               for(int i1 = 0; i1 + this.scrollPos < this.field_146253_i.size() && i1 < i; ++i1) {
                  ChatLine chatline = (ChatLine)this.field_146253_i.get(i1 + this.scrollPos);
                  if (chatline != null) {
                     j1 = p_146230_1_ - chatline.getUpdatedCounter();
                     if (j1 < 200 || flag) {
                        double d0 = (double)j1 / 200.0D;
                        d0 = 1.0D - d0;
                        d0 *= 10.0D;
                        d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                        d0 *= d0;
                        l1 = (int)(255.0D * d0);
                        if (flag) {
                           l1 = 255;
                        }

                        l1 = (int)((float)l1 * f);
                        ++j;
                        if (l1 > 3) {
                           int i2 = 0;
                           int j2 = -i1 * 9;
                           drawRect((double)i2, (double)(j2 - 9), (double)(i2 + l + 4), (double)j2, l1 / 2 << 24);
                           String s = chatline.getChatComponent().getFormattedText();
                           GlStateManager.enableBlend();
                           this.mc.fontRendererObj.drawStringWithShadow(s, (float)i2, (float)(j2 - 8), 16777215 + (l1 << 24));
                           GlStateManager.disableAlpha();
                           GlStateManager.disableBlend();
                        }
                     }
                  }
               }

               if (flag) {
                  FontRenderer var10000 = this.mc.fontRendererObj;
                  int k2 = 9;
                  GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                  int l2 = k * k2 + k;
                  j1 = j * k2 + j;
                  int j3 = this.scrollPos * j1 / k;
                  int k1 = j1 * j1 / l2;
                  if (l2 != j1) {
                     l1 = j3 > 0 ? 170 : 96;
                     int l3 = this.isScrolled ? 13382451 : 3355562;
                     drawRect(0.0D, (double)(-j3), 2.0D, (double)(-j3 - k1), l3 + (l1 << 24));
                     drawRect(2.0D, (double)(-j3), 1.0D, (double)(-j3 - k1), 13421772 + (l1 << 24));
                  }
               }

               GlStateManager.popMatrix();
            }
         }

      }
   }

   public void clearChatMessages() {
      this.field_146253_i.clear();
      this.chatLines.clear();
      this.sentMessages.clear();
   }

   public void printChatMessage(IChatComponent p_146227_1_) {
      this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
   }

   public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_) {
      this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
      logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
   }

   private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
      if (p_146237_2_ != 0) {
         this.deleteChatLine(p_146237_2_);
      }

      int i = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
      List list = GuiUtilRenderComponents.func_178908_a(p_146237_1_, i, this.mc.fontRendererObj, false, false);
      boolean flag = this.getChatOpen();

      IChatComponent ichatcomponent;
      for(Iterator var8 = list.iterator(); var8.hasNext(); this.field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, p_146237_2_))) {
         ichatcomponent = (IChatComponent)var8.next();
         if (flag && this.scrollPos > 0) {
            this.isScrolled = true;
            this.scroll(1);
         }
      }

      while(this.field_146253_i.size() > 100) {
         this.field_146253_i.remove(this.field_146253_i.size() - 1);
      }

      if (!p_146237_4_) {
         this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

         while(this.chatLines.size() > 100) {
            this.chatLines.remove(this.chatLines.size() - 1);
         }
      }

   }

   public void refreshChat() {
      this.field_146253_i.clear();
      this.resetScroll();

      for(int i = this.chatLines.size() - 1; i >= 0; --i) {
         ChatLine chatline = (ChatLine)this.chatLines.get(i);
         this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
      }

   }

   public List getSentMessages() {
      return this.sentMessages;
   }

   public void addToSentMessages(String p_146239_1_) {
      if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_)) {
         this.sentMessages.add(p_146239_1_);
      }

   }

   public void resetScroll() {
      this.scrollPos = 0;
      this.isScrolled = false;
   }

   public void scroll(int p_146229_1_) {
      this.scrollPos += p_146229_1_;
      int i = this.field_146253_i.size();
      if (this.scrollPos > i - this.getLineCount()) {
         this.scrollPos = i - this.getLineCount();
      }

      if (this.scrollPos <= 0) {
         this.scrollPos = 0;
         this.isScrolled = false;
      }

   }

   public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
      if (!this.getChatOpen()) {
         return null;
      } else {
         ScaledResolution scaledresolution = new ScaledResolution(this.mc);
         int i = scaledresolution.getScaleFactor();
         float f = this.getChatScale();
         int j = p_146236_1_ / i - 3;
         int k = p_146236_2_ / i - 27;
         j = MathHelper.floor_float((float)j / f);
         k = MathHelper.floor_float((float)k / f);
         if (j >= 0 && k >= 0) {
            int l = Math.min(this.getLineCount(), this.field_146253_i.size());
            if (j <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale())) {
               FontRenderer var10001 = this.mc.fontRendererObj;
               if (k < 9 * l + l) {
                  var10001 = this.mc.fontRendererObj;
                  int i1 = k / 9 + this.scrollPos;
                  if (i1 >= 0 && i1 < this.field_146253_i.size()) {
                     ChatLine chatline = (ChatLine)this.field_146253_i.get(i1);
                     int j1 = 0;
                     Iterator var12 = chatline.getChatComponent().iterator();

                     while(var12.hasNext()) {
                        IChatComponent ichatcomponent = (IChatComponent)var12.next();
                        if (ichatcomponent instanceof ChatComponentText) {
                           j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
                           if (j1 > j) {
                              return ichatcomponent;
                           }
                        }
                     }
                  }

                  return null;
               }
            }

            return null;
         } else {
            return null;
         }
      }
   }

   public boolean getChatOpen() {
      return this.mc.currentScreen instanceof GuiChat;
   }

   public void deleteChatLine(int p_146242_1_) {
      Iterator iterator = this.field_146253_i.iterator();

      ChatLine chatline1;
      while(iterator.hasNext()) {
         chatline1 = (ChatLine)iterator.next();
         if (chatline1.getChatLineID() == p_146242_1_) {
            iterator.remove();
         }
      }

      iterator = this.chatLines.iterator();

      while(iterator.hasNext()) {
         chatline1 = (ChatLine)iterator.next();
         if (chatline1.getChatLineID() == p_146242_1_) {
            iterator.remove();
            break;
         }
      }

   }

   public int getChatWidth() {
      return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
   }

   public int getChatHeight() {
      return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
   }

   public float getChatScale() {
      return this.mc.gameSettings.chatScale;
   }

   public int getLineCount() {
      return this.getChatHeight() / 9;
   }
}
