/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.GlStateManager.TexGen;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.tileentity.TileEntityEndPortal;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
/*     */ {
/*  17 */   private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
/*  18 */   private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
/*  19 */   private static final Random field_147527_e = new Random(31100L);
/*  20 */   FloatBuffer field_147528_b = net.minecraft.client.renderer.GLAllocation.createDirectFloatBuffer(16);
/*     */   
/*     */   public void renderTileEntityAt(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/*  24 */     float f = (float)this.rendererDispatcher.entityX;
/*  25 */     float f1 = (float)this.rendererDispatcher.entityY;
/*  26 */     float f2 = (float)this.rendererDispatcher.entityZ;
/*  27 */     GlStateManager.disableLighting();
/*  28 */     field_147527_e.setSeed(31100L);
/*  29 */     float f3 = 0.75F;
/*     */     
/*  31 */     for (int i = 0; i < 16; i++)
/*     */     {
/*  33 */       GlStateManager.pushMatrix();
/*  34 */       float f4 = 16 - i;
/*  35 */       float f5 = 0.0625F;
/*  36 */       float f6 = 1.0F / (f4 + 1.0F);
/*     */       
/*  38 */       if (i == 0)
/*     */       {
/*  40 */         bindTexture(END_SKY_TEXTURE);
/*  41 */         f6 = 0.1F;
/*  42 */         f4 = 65.0F;
/*  43 */         f5 = 0.125F;
/*  44 */         GlStateManager.enableBlend();
/*  45 */         GlStateManager.blendFunc(770, 771);
/*     */       }
/*     */       
/*  48 */       if (i >= 1)
/*     */       {
/*  50 */         bindTexture(END_PORTAL_TEXTURE);
/*     */       }
/*     */       
/*  53 */       if (i == 1)
/*     */       {
/*  55 */         GlStateManager.enableBlend();
/*  56 */         GlStateManager.blendFunc(1, 1);
/*  57 */         f5 = 0.5F;
/*     */       }
/*     */       
/*  60 */       float f7 = (float)-(y + f3);
/*  61 */       float f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
/*  62 */       float f9 = f7 + f4 + (float)ActiveRenderInfo.getPosition().yCoord;
/*  63 */       float f10 = f8 / f9;
/*  64 */       f10 = (float)(y + f3) + f10;
/*  65 */       GlStateManager.translate(f, f10, f2);
/*  66 */       GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
/*  67 */       GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
/*  68 */       GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
/*  69 */       GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
/*  70 */       GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
/*  71 */       GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
/*  72 */       GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
/*  73 */       GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
/*  74 */       GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
/*  75 */       GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
/*  76 */       GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
/*  77 */       GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
/*  78 */       GlStateManager.popMatrix();
/*  79 */       GlStateManager.matrixMode(5890);
/*  80 */       GlStateManager.pushMatrix();
/*  81 */       GlStateManager.loadIdentity();
/*  82 */       GlStateManager.translate(0.0F, (float)(net.minecraft.client.Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
/*  83 */       GlStateManager.scale(f5, f5, f5);
/*  84 */       GlStateManager.translate(0.5F, 0.5F, 0.0F);
/*  85 */       GlStateManager.rotate((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
/*  86 */       GlStateManager.translate(-0.5F, -0.5F, 0.0F);
/*  87 */       GlStateManager.translate(-f, -f2, -f1);
/*  88 */       f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
/*  89 */       GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float)ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
/*  90 */       Tessellator tessellator = Tessellator.getInstance();
/*  91 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  92 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/*  93 */       float f11 = (field_147527_e.nextFloat() * 0.5F + 0.1F) * f6;
/*  94 */       float f12 = (field_147527_e.nextFloat() * 0.5F + 0.4F) * f6;
/*  95 */       float f13 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;
/*     */       
/*  97 */       if (i == 0)
/*     */       {
/*  99 */         f11 = f12 = f13 = 1.0F * f6;
/*     */       }
/*     */       
/* 102 */       worldrenderer.pos(x, y + f3, z).color(f11, f12, f13, 1.0F).endVertex();
/* 103 */       worldrenderer.pos(x, y + f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
/* 104 */       worldrenderer.pos(x + 1.0D, y + f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
/* 105 */       worldrenderer.pos(x + 1.0D, y + f3, z).color(f11, f12, f13, 1.0F).endVertex();
/* 106 */       tessellator.draw();
/* 107 */       GlStateManager.popMatrix();
/* 108 */       GlStateManager.matrixMode(5888);
/* 109 */       bindTexture(END_SKY_TEXTURE);
/*     */     }
/*     */     
/* 112 */     GlStateManager.disableBlend();
/* 113 */     GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
/* 114 */     GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
/* 115 */     GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
/* 116 */     GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
/* 117 */     GlStateManager.enableLighting();
/*     */   }
/*     */   
/*     */   private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
/*     */   {
/* 122 */     this.field_147528_b.clear();
/* 123 */     this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
/* 124 */     this.field_147528_b.flip();
/* 125 */     return this.field_147528_b;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityEndPortalRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */