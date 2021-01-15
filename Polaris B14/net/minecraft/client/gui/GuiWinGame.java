/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.MusicTicker;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Session;
/*     */ import org.apache.commons.io.Charsets;
/*     */ 
/*     */ public class GuiWinGame extends GuiScreen
/*     */ {
/*  24 */   private static final org.apache.logging.log4j.Logger logger = ;
/*  25 */   private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
/*  26 */   private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
/*     */   private int field_146581_h;
/*     */   private List<String> field_146582_i;
/*     */   private int field_146579_r;
/*  30 */   private float field_146578_s = 0.5F;
/*     */   
/*     */   public void updateScreen() {
/*  33 */     MusicTicker musicticker = this.mc.func_181535_r();
/*  34 */     SoundHandler soundhandler = this.mc.getSoundHandler();
/*  35 */     if (this.field_146581_h == 0) {
/*  36 */       musicticker.func_181557_a();
/*  37 */       musicticker.func_181558_a(net.minecraft.client.audio.MusicTicker.MusicType.CREDITS);
/*  38 */       soundhandler.resumeSounds();
/*     */     }
/*     */     
/*  41 */     soundhandler.update();
/*  42 */     this.field_146581_h += 1;
/*  43 */     float f = (this.field_146579_r + height + height + 24) / this.field_146578_s;
/*  44 */     if (this.field_146581_h > f) {
/*  45 */       sendRespawnPacket();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException {
/*  50 */     if (keyCode == 1) {
/*  51 */       sendRespawnPacket();
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendRespawnPacket() {
/*  56 */     this.mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C16PacketClientStatus(net.minecraft.network.play.client.C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
/*  57 */     this.mc.displayGuiScreen(null);
/*     */   }
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  65 */     if (this.field_146582_i == null) {
/*  66 */       this.field_146582_i = com.google.common.collect.Lists.newArrayList();
/*     */       try
/*     */       {
/*  69 */         String s = "";
/*  70 */         String s1 = EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
/*  71 */         int i = 274;
/*  72 */         InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
/*  73 */         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
/*  74 */         Random random = new Random(8124371L);
/*     */         
/*  76 */         while ((s = bufferedreader.readLine()) != null) {
/*     */           String s2;
/*     */           String s3;
/*  79 */           for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
/*  80 */             int j = s.indexOf(s1);
/*  81 */             s2 = s.substring(0, j);
/*  82 */             s3 = s.substring(j + s1.length());
/*     */           }
/*     */           
/*  85 */           this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
/*  86 */           this.field_146582_i.add("");
/*     */         }
/*     */         
/*  89 */         inputstream.close();
/*     */         
/*  91 */         for (int k = 0; k < 8; k++) {
/*  92 */           this.field_146582_i.add("");
/*     */         }
/*     */         
/*  95 */         inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
/*  96 */         bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
/*     */         
/*  98 */         while ((s = bufferedreader.readLine()) != null) {
/*  99 */           s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
/* 100 */           s = s.replaceAll("\t", "    ");
/* 101 */           this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
/* 102 */           this.field_146582_i.add("");
/*     */         }
/*     */         
/* 105 */         inputstream.close();
/* 106 */         this.field_146579_r = (this.field_146582_i.size() * 12);
/*     */       } catch (Exception exception) {
/* 108 */         logger.error("Couldn't load credits", exception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
/* 114 */     Tessellator tessellator = Tessellator.getInstance();
/* 115 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 116 */     this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 117 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 118 */     int i = width;
/* 119 */     float f = 0.0F - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
/* 120 */     float f1 = height - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
/* 121 */     float f2 = 0.015625F;
/* 122 */     float f3 = (this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
/* 123 */     float f4 = (this.field_146579_r + height + height + 24) / this.field_146578_s;
/* 124 */     float f5 = (f4 - 20.0F - (this.field_146581_h + p_146575_3_)) * 0.005F;
/* 125 */     if (f5 < f3) {
/* 126 */       f3 = f5;
/*     */     }
/*     */     
/* 129 */     if (f3 > 1.0F) {
/* 130 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 133 */     f3 *= f3;
/* 134 */     f3 = f3 * 96.0F / 255.0F;
/* 135 */     worldrenderer.pos(0.0D, height, this.zLevel).tex(0.0D, f * f2).color(f3, f3, f3, 1.0F).endVertex();
/* 136 */     worldrenderer.pos(i, height, this.zLevel).tex(i * f2, f * f2).color(f3, f3, f3, 1.0F).endVertex();
/* 137 */     worldrenderer.pos(i, 0.0D, this.zLevel).tex(i * f2, f1 * f2).color(f3, f3, f3, 1.0F).endVertex();
/* 138 */     worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, f1 * f2).color(f3, f3, f3, 1.0F).endVertex();
/* 139 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 143 */     drawWinGameScreen(mouseX, mouseY, partialTicks);
/* 144 */     Tessellator tessellator = Tessellator.getInstance();
/* 145 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 146 */     int i = 274;
/* 147 */     int j = width / 2 - i / 2;
/* 148 */     int k = height + 50;
/* 149 */     float f = -(this.field_146581_h + partialTicks) * this.field_146578_s;
/* 150 */     GlStateManager.pushMatrix();
/* 151 */     GlStateManager.translate(0.0F, f, 0.0F);
/* 152 */     this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
/* 153 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 154 */     drawTexturedModalRect(j, k, 0, 0, 155, 44);
/* 155 */     drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
/* 156 */     int l = k + 200;
/*     */     
/* 158 */     for (int i1 = 0; i1 < this.field_146582_i.size(); i1++) {
/* 159 */       if (i1 == this.field_146582_i.size() - 1) {
/* 160 */         float f1 = l + f - (height / 2 - 6);
/* 161 */         if (f1 < 0.0F) {
/* 162 */           GlStateManager.translate(0.0F, -f1, 0.0F);
/*     */         }
/*     */       }
/*     */       
/* 166 */       if ((l + f + 12.0F + 8.0F > 0.0F) && (l + f < height)) {
/* 167 */         String s = (String)this.field_146582_i.get(i1);
/* 168 */         if (s.startsWith("[C]")) {
/* 169 */           this.fontRendererObj.drawStringWithShadow(s.substring(3), j + (i - this.fontRendererObj.getStringWidth(s.substring(3))) / 2, l, 16777215);
/*     */         } else {
/* 171 */           this.fontRendererObj.fontRandom.setSeed(i1 * 4238972211L + this.field_146581_h / 4);
/* 172 */           this.fontRendererObj.drawStringWithShadow(s, j, l, 16777215);
/*     */         }
/*     */       }
/*     */       
/* 176 */       l += 12;
/*     */     }
/*     */     
/* 179 */     GlStateManager.popMatrix();
/* 180 */     this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
/* 181 */     GlStateManager.enableBlend();
/* 182 */     GlStateManager.blendFunc(0, 769);
/* 183 */     int j1 = width;
/* 184 */     int k1 = height;
/* 185 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 186 */     worldrenderer.pos(0.0D, k1, this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 187 */     worldrenderer.pos(j1, k1, this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 188 */     worldrenderer.pos(j1, 0.0D, this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 189 */     worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 190 */     tessellator.draw();
/* 191 */     GlStateManager.disableBlend();
/* 192 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiWinGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */