/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class ModelSprite
/*     */ {
/*  13 */   private ModelRenderer modelRenderer = null;
/*  14 */   private int textureOffsetX = 0;
/*  15 */   private int textureOffsetY = 0;
/*  16 */   private float posX = 0.0F;
/*  17 */   private float posY = 0.0F;
/*  18 */   private float posZ = 0.0F;
/*  19 */   private int sizeX = 0;
/*  20 */   private int sizeY = 0;
/*  21 */   private int sizeZ = 0;
/*  22 */   private float sizeAdd = 0.0F;
/*  23 */   private float minU = 0.0F;
/*  24 */   private float minV = 0.0F;
/*  25 */   private float maxU = 0.0F;
/*  26 */   private float maxV = 0.0F;
/*     */   
/*     */   public ModelSprite(ModelRenderer p_i43_1_, int p_i43_2_, int p_i43_3_, float p_i43_4_, float p_i43_5_, float p_i43_6_, int p_i43_7_, int p_i43_8_, int p_i43_9_, float p_i43_10_)
/*     */   {
/*  30 */     this.modelRenderer = p_i43_1_;
/*  31 */     this.textureOffsetX = p_i43_2_;
/*  32 */     this.textureOffsetY = p_i43_3_;
/*  33 */     this.posX = p_i43_4_;
/*  34 */     this.posY = p_i43_5_;
/*  35 */     this.posZ = p_i43_6_;
/*  36 */     this.sizeX = p_i43_7_;
/*  37 */     this.sizeY = p_i43_8_;
/*  38 */     this.sizeZ = p_i43_9_;
/*  39 */     this.sizeAdd = p_i43_10_;
/*  40 */     this.minU = (p_i43_2_ / p_i43_1_.textureWidth);
/*  41 */     this.minV = (p_i43_3_ / p_i43_1_.textureHeight);
/*  42 */     this.maxU = ((p_i43_2_ + p_i43_7_) / p_i43_1_.textureWidth);
/*  43 */     this.maxV = ((p_i43_3_ + p_i43_8_) / p_i43_1_.textureHeight);
/*     */   }
/*     */   
/*     */   public void render(Tessellator p_render_1_, float p_render_2_)
/*     */   {
/*  48 */     GlStateManager.translate(this.posX * p_render_2_, this.posY * p_render_2_, this.posZ * p_render_2_);
/*  49 */     float f = this.minU;
/*  50 */     float f1 = this.maxU;
/*  51 */     float f2 = this.minV;
/*  52 */     float f3 = this.maxV;
/*     */     
/*  54 */     if (this.modelRenderer.mirror)
/*     */     {
/*  56 */       f = this.maxU;
/*  57 */       f1 = this.minU;
/*     */     }
/*     */     
/*  60 */     if (this.modelRenderer.mirrorV)
/*     */     {
/*  62 */       f2 = this.maxV;
/*  63 */       f3 = this.minV;
/*     */     }
/*     */     
/*  66 */     renderItemIn2D(p_render_1_, f, f2, f1, f3, this.sizeX, this.sizeY, p_render_2_ * this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
/*  67 */     GlStateManager.translate(-this.posX * p_render_2_, -this.posY * p_render_2_, -this.posZ * p_render_2_);
/*     */   }
/*     */   
/*     */   public static void renderItemIn2D(Tessellator p_renderItemIn2D_0_, float p_renderItemIn2D_1_, float p_renderItemIn2D_2_, float p_renderItemIn2D_3_, float p_renderItemIn2D_4_, int p_renderItemIn2D_5_, int p_renderItemIn2D_6_, float p_renderItemIn2D_7_, float p_renderItemIn2D_8_, float p_renderItemIn2D_9_)
/*     */   {
/*  72 */     if (p_renderItemIn2D_7_ < 6.25E-4F)
/*     */     {
/*  74 */       p_renderItemIn2D_7_ = 6.25E-4F;
/*     */     }
/*     */     
/*  77 */     float f = p_renderItemIn2D_3_ - p_renderItemIn2D_1_;
/*  78 */     float f1 = p_renderItemIn2D_4_ - p_renderItemIn2D_2_;
/*  79 */     double d0 = MathHelper.abs(f) * (p_renderItemIn2D_8_ / 16.0F);
/*  80 */     double d1 = MathHelper.abs(f1) * (p_renderItemIn2D_9_ / 16.0F);
/*  81 */     WorldRenderer worldrenderer = p_renderItemIn2D_0_.getWorldRenderer();
/*  82 */     GL11.glNormal3f(0.0F, 0.0F, -1.0F);
/*  83 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  84 */     worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
/*  85 */     worldrenderer.pos(d0, 0.0D, 0.0D).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
/*  86 */     worldrenderer.pos(d0, d1, 0.0D).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
/*  87 */     worldrenderer.pos(0.0D, d1, 0.0D).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
/*  88 */     p_renderItemIn2D_0_.draw();
/*  89 */     GL11.glNormal3f(0.0F, 0.0F, 1.0F);
/*  90 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  91 */     worldrenderer.pos(0.0D, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
/*  92 */     worldrenderer.pos(d0, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
/*  93 */     worldrenderer.pos(d0, 0.0D, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
/*  94 */     worldrenderer.pos(0.0D, 0.0D, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
/*  95 */     p_renderItemIn2D_0_.draw();
/*  96 */     float f2 = 0.5F * f / p_renderItemIn2D_5_;
/*  97 */     float f3 = 0.5F * f1 / p_renderItemIn2D_6_;
/*  98 */     GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
/*  99 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 101 */     for (int i = 0; i < p_renderItemIn2D_5_; i++)
/*     */     {
/* 103 */       float f4 = i / p_renderItemIn2D_5_;
/* 104 */       float f5 = p_renderItemIn2D_1_ + f * f4 + f2;
/* 105 */       worldrenderer.pos(f4 * d0, 0.0D, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_2_).endVertex();
/* 106 */       worldrenderer.pos(f4 * d0, 0.0D, 0.0D).tex(f5, p_renderItemIn2D_2_).endVertex();
/* 107 */       worldrenderer.pos(f4 * d0, d1, 0.0D).tex(f5, p_renderItemIn2D_4_).endVertex();
/* 108 */       worldrenderer.pos(f4 * d0, d1, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_4_).endVertex();
/*     */     }
/*     */     
/* 111 */     p_renderItemIn2D_0_.draw();
/* 112 */     GL11.glNormal3f(1.0F, 0.0F, 0.0F);
/* 113 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 115 */     for (int j = 0; j < p_renderItemIn2D_5_; j++)
/*     */     {
/* 117 */       float f7 = j / p_renderItemIn2D_5_;
/* 118 */       float f10 = p_renderItemIn2D_1_ + f * f7 + f2;
/* 119 */       float f6 = f7 + 1.0F / p_renderItemIn2D_5_;
/* 120 */       worldrenderer.pos(f6 * d0, d1, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_4_).endVertex();
/* 121 */       worldrenderer.pos(f6 * d0, d1, 0.0D).tex(f10, p_renderItemIn2D_4_).endVertex();
/* 122 */       worldrenderer.pos(f6 * d0, 0.0D, 0.0D).tex(f10, p_renderItemIn2D_2_).endVertex();
/* 123 */       worldrenderer.pos(f6 * d0, 0.0D, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_2_).endVertex();
/*     */     }
/*     */     
/* 126 */     p_renderItemIn2D_0_.draw();
/* 127 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 128 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 130 */     for (int k = 0; k < p_renderItemIn2D_6_; k++)
/*     */     {
/* 132 */       float f8 = k / p_renderItemIn2D_6_;
/* 133 */       float f11 = p_renderItemIn2D_2_ + f1 * f8 + f3;
/* 134 */       float f13 = f8 + 1.0F / p_renderItemIn2D_6_;
/* 135 */       worldrenderer.pos(0.0D, f13 * d1, 0.0D).tex(p_renderItemIn2D_1_, f11).endVertex();
/* 136 */       worldrenderer.pos(d0, f13 * d1, 0.0D).tex(p_renderItemIn2D_3_, f11).endVertex();
/* 137 */       worldrenderer.pos(d0, f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f11).endVertex();
/* 138 */       worldrenderer.pos(0.0D, f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f11).endVertex();
/*     */     }
/*     */     
/* 141 */     p_renderItemIn2D_0_.draw();
/* 142 */     GL11.glNormal3f(0.0F, -1.0F, 0.0F);
/* 143 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 145 */     for (int l = 0; l < p_renderItemIn2D_6_; l++)
/*     */     {
/* 147 */       float f9 = l / p_renderItemIn2D_6_;
/* 148 */       float f12 = p_renderItemIn2D_2_ + f1 * f9 + f3;
/* 149 */       worldrenderer.pos(d0, f9 * d1, 0.0D).tex(p_renderItemIn2D_3_, f12).endVertex();
/* 150 */       worldrenderer.pos(0.0D, f9 * d1, 0.0D).tex(p_renderItemIn2D_1_, f12).endVertex();
/* 151 */       worldrenderer.pos(0.0D, f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f12).endVertex();
/* 152 */       worldrenderer.pos(d0, f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f12).endVertex();
/*     */     }
/*     */     
/* 155 */     p_renderItemIn2D_0_.draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ModelSprite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */