/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.stream.IStream;
/*    */ 
/*    */ public class GuiStreamIndicator
/*    */ {
/* 11 */   private static final net.minecraft.util.ResourceLocation locationStreamIndicator = new net.minecraft.util.ResourceLocation("textures/gui/stream_indicator.png");
/*    */   private final Minecraft mc;
/* 13 */   private float field_152443_c = 1.0F;
/* 14 */   private int field_152444_d = 1;
/*    */   
/*    */   public GuiStreamIndicator(Minecraft mcIn) {
/* 17 */     this.mc = mcIn;
/*    */   }
/*    */   
/*    */   public void render(int p_152437_1_, int p_152437_2_) {
/* 21 */     if (this.mc.getTwitchStream().isBroadcasting()) {
/* 22 */       GlStateManager.enableBlend();
/* 23 */       int i = this.mc.getTwitchStream().func_152920_A();
/* 24 */       if (i > 0) {
/* 25 */         String s = i;
/* 26 */         int j = this.mc.fontRendererObj.getStringWidth(s);
/* 27 */         int k = 20;
/* 28 */         int l = p_152437_1_ - j - 1;
/* 29 */         int i1 = p_152437_2_ + 20 - 1;
/* 30 */         int j1 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
/* 31 */         GlStateManager.disableTexture2D();
/* 32 */         Tessellator tessellator = Tessellator.getInstance();
/* 33 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 34 */         GlStateManager.color(0.0F, 0.0F, 0.0F, (0.65F + 0.35000002F * this.field_152443_c) / 2.0F);
/* 35 */         worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
/* 36 */         worldrenderer.pos(l, j1, 0.0D).endVertex();
/* 37 */         worldrenderer.pos(p_152437_1_, j1, 0.0D).endVertex();
/* 38 */         worldrenderer.pos(p_152437_1_, i1, 0.0D).endVertex();
/* 39 */         worldrenderer.pos(l, i1, 0.0D).endVertex();
/* 40 */         tessellator.draw();
/* 41 */         GlStateManager.enableTexture2D();
/* 42 */         this.mc.fontRendererObj.drawString(s, p_152437_1_ - j, p_152437_2_ + 20, 16777215);
/*    */       }
/*    */       
/* 45 */       render(p_152437_1_, p_152437_2_, func_152440_b(), 0);
/* 46 */       render(p_152437_1_, p_152437_2_, func_152438_c(), 17);
/*    */     }
/*    */   }
/*    */   
/*    */   private void render(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_) {
/* 51 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * this.field_152443_c);
/* 52 */     this.mc.getTextureManager().bindTexture(locationStreamIndicator);
/* 53 */     float f = 150.0F;
/* 54 */     float f1 = 0.0F;
/* 55 */     float f2 = p_152436_3_ * 0.015625F;
/* 56 */     float f3 = 1.0F;
/* 57 */     float f4 = (p_152436_3_ + 16) * 0.015625F;
/* 58 */     Tessellator tessellator = Tessellator.getInstance();
/* 59 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 60 */     worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX);
/* 61 */     worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, f).tex(f1, f4).endVertex();
/* 62 */     worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, f).tex(f3, f4).endVertex();
/* 63 */     worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, f).tex(f3, f2).endVertex();
/* 64 */     worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0, f).tex(f1, f2).endVertex();
/* 65 */     tessellator.draw();
/* 66 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   private int func_152440_b() {
/* 70 */     return this.mc.getTwitchStream().isPaused() ? 16 : 0;
/*    */   }
/*    */   
/*    */   private int func_152438_c() {
/* 74 */     return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
/*    */   }
/*    */   
/*    */   public void func_152439_a() {
/* 78 */     if (this.mc.getTwitchStream().isBroadcasting()) {
/* 79 */       this.field_152443_c += 0.025F * this.field_152444_d;
/* 80 */       if (this.field_152443_c < 0.0F) {
/* 81 */         this.field_152444_d *= -1;
/* 82 */         this.field_152443_c = 0.0F;
/* 83 */       } else if (this.field_152443_c > 1.0F) {
/* 84 */         this.field_152444_d *= -1;
/* 85 */         this.field_152443_c = 1.0F;
/*    */       }
/*    */     } else {
/* 88 */       this.field_152443_c = 1.0F;
/* 89 */       this.field_152444_d = 1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiStreamIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */