/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ 
/*     */ public class GuiNewChat extends Gui
/*     */ {
/*  20 */   private static final Logger logger = ;
/*     */   private final Minecraft mc;
/*  22 */   private final List<String> sentMessages = Lists.newArrayList();
/*  23 */   private final List<ChatLine> chatLines = Lists.newArrayList();
/*  24 */   private final List<ChatLine> field_146253_i = Lists.newArrayList();
/*     */   private int scrollPos;
/*     */   private boolean isScrolled;
/*     */   
/*     */   public GuiNewChat(Minecraft mcIn) {
/*  29 */     this.mc = mcIn;
/*     */   }
/*     */   
/*     */   public void drawChat(int p_146230_1_) {
/*  33 */     CFontRenderer font = FontLoaders.vardana12;
/*  34 */     if (this.mc.gameSettings.chatVisibility != net.minecraft.entity.player.EntityPlayer.EnumChatVisibility.HIDDEN) {
/*  35 */       int i = getLineCount();
/*  36 */       boolean flag = false;
/*  37 */       int j = 0;
/*  38 */       int k = this.field_146253_i.size();
/*  39 */       float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
/*     */       
/*  41 */       if (k > 0) {
/*  42 */         if (getChatOpen()) {
/*  43 */           flag = true;
/*     */         }
/*     */         
/*  46 */         float f1 = getChatScale();
/*  47 */         int l = MathHelper.ceiling_float_int(getChatWidth() / f1);
/*  48 */         GlStateManager.pushMatrix();
/*  49 */         GlStateManager.translate(2.0F, 20.0F, 0.0F);
/*  50 */         GlStateManager.scale(f1, f1, 1.0F);
/*     */         
/*  52 */         for (int i1 = 0; (i1 + this.scrollPos < this.field_146253_i.size()) && (i1 < i); i1++) {
/*  53 */           ChatLine chatline = (ChatLine)this.field_146253_i.get(i1 + this.scrollPos);
/*     */           
/*  55 */           if (chatline != null) {
/*  56 */             int j1 = p_146230_1_ - chatline.getUpdatedCounter();
/*     */             
/*  58 */             if ((j1 < 200) || (flag)) {
/*  59 */               double d0 = j1 / 200.0D;
/*  60 */               d0 = 1.0D - d0;
/*  61 */               d0 *= 10.0D;
/*  62 */               d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
/*  63 */               d0 *= d0;
/*  64 */               int l1 = (int)(255.0D * d0);
/*     */               
/*  66 */               if (flag) {
/*  67 */                 l1 = 255;
/*     */               }
/*     */               
/*  70 */               l1 = (int)(l1 * f);
/*  71 */               j++;
/*     */               
/*  73 */               if (l1 > 3) {
/*  74 */                 int i2 = 0;
/*  75 */                 int j2 = -i1 * 9;
/*  76 */                 drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
/*  77 */                 if (Polaris.instance.settingsManager.getSettingByName("TTF Chat").getValBoolean()) {
/*  78 */                   String s = chatline.getChatComponent().getFormattedText();
/*  79 */                   GlStateManager.enableBlend();
/*  80 */                   font.drawStringWithShadow(s, i2, (float)(j2 - 7.5D) + 1.0F, 
/*  81 */                     16777215 + (l1 << 24));
/*  82 */                   GlStateManager.disableAlpha();
/*  83 */                   GlStateManager.disableBlend();
/*     */                 } else {
/*  85 */                   String s = chatline.getChatComponent().getFormattedText();
/*  86 */                   GlStateManager.enableBlend();
/*  87 */                   this.mc.fontRendererObj.drawStringWithShadow(s, i2, j2 - 9 + 1.0F, 
/*  88 */                     16777215 + (l1 << 24));
/*  89 */                   GlStateManager.disableAlpha();
/*  90 */                   GlStateManager.disableBlend();
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  97 */         if (flag) {
/*  98 */           if (Polaris.instance.settingsManager.getSettingByName("TTF Chat").getValBoolean()) {
/*  99 */             int k2 = font.getHeight();
/* 100 */             GlStateManager.translate(-3.0F, 0.0F, 0.0F);
/* 101 */             int l2 = k * k2 + k;
/* 102 */             int i3 = j * k2 + j;
/* 103 */             int j3 = this.scrollPos * i3 / k;
/* 104 */             int k1 = i3 * i3 / l2;
/*     */             
/* 106 */             if (l2 != i3) {
/* 107 */               int k3 = j3 > 0 ? 170 : 96;
/* 108 */               int l3 = this.isScrolled ? 13382451 : 3355562;
/* 109 */               drawRect(0.0D, -j3, 2.0D, -j3 - k1, l3 + (k3 << 24));
/* 110 */               drawRect(2.0D, -j3, 1.0D, -j3 - k1, 13421772 + (k3 << 24));
/*     */             }
/*     */           } else {
/* 113 */             int k2 = this.mc.fontRendererObj.getHeight();
/* 114 */             GlStateManager.translate(-3.0F, 0.0F, 0.0F);
/* 115 */             int l2 = k * k2 + k;
/* 116 */             int i3 = j * k2 + j;
/* 117 */             int j3 = this.scrollPos * i3 / k;
/* 118 */             int k1 = i3 * i3 / l2;
/*     */             
/* 120 */             if (l2 != i3) {
/* 121 */               int k3 = j3 > 0 ? 170 : 96;
/* 122 */               int l3 = this.isScrolled ? 13382451 : 3355562;
/* 123 */               drawRect(0.0D, -j3, 2.0D, -j3 - k1, l3 + (k3 << 24));
/* 124 */               drawRect(2.0D, -j3, 1.0D, -j3 - k1, 13421772 + (k3 << 24));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 129 */         GlStateManager.popMatrix();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearChatMessages()
/*     */   {
/* 138 */     this.field_146253_i.clear();
/* 139 */     this.chatLines.clear();
/* 140 */     this.sentMessages.clear();
/*     */   }
/*     */   
/*     */   public void printChatMessage(IChatComponent p_146227_1_) {
/* 144 */     printChatMessageWithOptionalDeletion(p_146227_1_, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_)
/*     */   {
/* 152 */     setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
/* 153 */     logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
/*     */   }
/*     */   
/*     */   private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
/* 157 */     if (p_146237_2_ != 0) {
/* 158 */       deleteChatLine(p_146237_2_);
/*     */     }
/*     */     
/* 161 */     int i = MathHelper.floor_float(getChatWidth() / getChatScale());
/* 162 */     List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(p_146237_1_, i, this.mc.fontRendererObj, 
/* 163 */       false, false);
/* 164 */     boolean flag = getChatOpen();
/*     */     
/* 166 */     for (IChatComponent ichatcomponent : list) {
/* 167 */       if ((flag) && (this.scrollPos > 0)) {
/* 168 */         this.isScrolled = true;
/* 169 */         scroll(1);
/*     */       }
/*     */       
/* 172 */       this.field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, p_146237_2_));
/*     */     }
/*     */     
/* 175 */     while (this.field_146253_i.size() > 100) {
/* 176 */       this.field_146253_i.remove(this.field_146253_i.size() - 1);
/*     */     }
/*     */     
/* 179 */     if (!p_146237_4_) {
/* 180 */       this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));
/*     */       
/* 182 */       while (this.chatLines.size() > 100) {
/* 183 */         this.chatLines.remove(this.chatLines.size() - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void refreshChat() {
/* 189 */     this.field_146253_i.clear();
/* 190 */     resetScroll();
/*     */     
/* 192 */     for (int i = this.chatLines.size() - 1; i >= 0; i--) {
/* 193 */       ChatLine chatline = (ChatLine)this.chatLines.get(i);
/* 194 */       setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getSentMessages() {
/* 199 */     return this.sentMessages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addToSentMessages(String p_146239_1_)
/*     */   {
/* 207 */     if ((this.sentMessages.isEmpty()) || 
/* 208 */       (!((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_))) {
/* 209 */       this.sentMessages.add(p_146239_1_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetScroll()
/*     */   {
/* 217 */     this.scrollPos = 0;
/* 218 */     this.isScrolled = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void scroll(int p_146229_1_)
/*     */   {
/* 225 */     this.scrollPos += p_146229_1_;
/* 226 */     int i = this.field_146253_i.size();
/*     */     
/* 228 */     if (this.scrollPos > i - getLineCount()) {
/* 229 */       this.scrollPos = (i - getLineCount());
/*     */     }
/*     */     
/* 232 */     if (this.scrollPos <= 0) {
/* 233 */       this.scrollPos = 0;
/* 234 */       this.isScrolled = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_)
/*     */   {
/* 242 */     CFontRenderer font = FontLoaders.vardana12;
/* 243 */     if (!getChatOpen()) {
/* 244 */       return null;
/*     */     }
/* 246 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, 
/* 247 */       this.mc.displayHeight);
/* 248 */     int i = scaledresolution.getScaleFactor();
/* 249 */     float f = getChatScale();
/* 250 */     int j = p_146236_1_ / i - 3;
/* 251 */     int k = p_146236_2_ / i - 27;
/* 252 */     j = MathHelper.floor_float(j / f);
/* 253 */     k = MathHelper.floor_float(k / f);
/*     */     
/* 255 */     if ((j >= 0) && (k >= 0)) {
/* 256 */       int l = Math.min(getLineCount(), this.field_146253_i.size());
/*     */       
/* 258 */       if ((j <= MathHelper.floor_float(getChatWidth() / getChatScale())) && 
/* 259 */         (k < font.getHeight() * l + l)) {
/* 260 */         int i1 = k / font.getHeight() + this.scrollPos;
/*     */         
/* 262 */         if ((i1 >= 0) && (i1 < this.field_146253_i.size())) {
/* 263 */           ChatLine chatline = (ChatLine)this.field_146253_i.get(i1);
/* 264 */           int j1 = 0;
/*     */           
/* 266 */           for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
/* 267 */             if ((ichatcomponent instanceof ChatComponentText)) {
/* 268 */               j1 += font.getStringWidth(GuiUtilRenderComponents.func_178909_a(
/* 269 */                 ((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
/*     */               
/* 271 */               if (j1 > j) {
/* 272 */                 return ichatcomponent;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 278 */         return null;
/*     */       }
/* 280 */       return null;
/*     */     }
/*     */     
/* 283 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getChatOpen()
/*     */   {
/* 292 */     return this.mc.currentScreen instanceof GuiChat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void deleteChatLine(int p_146242_1_)
/*     */   {
/* 299 */     Iterator<ChatLine> iterator = this.field_146253_i.iterator();
/*     */     
/* 301 */     while (iterator.hasNext()) {
/* 302 */       ChatLine chatline = (ChatLine)iterator.next();
/*     */       
/* 304 */       if (chatline.getChatLineID() == p_146242_1_) {
/* 305 */         iterator.remove();
/*     */       }
/*     */     }
/*     */     
/* 309 */     iterator = this.chatLines.iterator();
/*     */     
/* 311 */     while (iterator.hasNext()) {
/* 312 */       ChatLine chatline1 = (ChatLine)iterator.next();
/*     */       
/* 314 */       if (chatline1.getChatLineID() == p_146242_1_) {
/* 315 */         iterator.remove();
/* 316 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getChatWidth() {
/* 322 */     return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
/*     */   }
/*     */   
/*     */   public int getChatHeight() {
/* 326 */     return calculateChatboxHeight(
/* 327 */       getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float getChatScale()
/*     */   {
/* 334 */     return this.mc.gameSettings.chatScale;
/*     */   }
/*     */   
/*     */   public static int calculateChatboxWidth(float p_146233_0_) {
/* 338 */     int i = 320;
/* 339 */     int j = 40;
/* 340 */     return MathHelper.floor_float(p_146233_0_ * (i - j) + j);
/*     */   }
/*     */   
/*     */   public static int calculateChatboxHeight(float p_146243_0_) {
/* 344 */     int i = 180;
/* 345 */     int j = 20;
/* 346 */     return MathHelper.floor_float(p_146243_0_ * (i - j) + j);
/*     */   }
/*     */   
/*     */   public int getLineCount() {
/* 350 */     return getChatHeight() / 9;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiNewChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */