/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderLightningBolt extends Render<EntityLightningBolt>
/*     */ {
/*     */   public RenderLightningBolt(RenderManager renderManagerIn)
/*     */   {
/*  15 */     super(renderManagerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  26 */     Tessellator tessellator = Tessellator.getInstance();
/*  27 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  28 */     GlStateManager.disableTexture2D();
/*  29 */     GlStateManager.disableLighting();
/*  30 */     GlStateManager.enableBlend();
/*  31 */     GlStateManager.blendFunc(770, 1);
/*  32 */     double[] adouble = new double[8];
/*  33 */     double[] adouble1 = new double[8];
/*  34 */     double d0 = 0.0D;
/*  35 */     double d1 = 0.0D;
/*  36 */     Random random = new Random(entity.boltVertex);
/*     */     
/*  38 */     for (int i = 7; i >= 0; i--)
/*     */     {
/*  40 */       adouble[i] = d0;
/*  41 */       adouble1[i] = d1;
/*  42 */       d0 += random.nextInt(11) - 5;
/*  43 */       d1 += random.nextInt(11) - 5;
/*     */     }
/*     */     
/*  46 */     for (int k1 = 0; k1 < 4; k1++)
/*     */     {
/*  48 */       Random random1 = new Random(entity.boltVertex);
/*     */       
/*  50 */       for (int j = 0; j < 3; j++)
/*     */       {
/*  52 */         int k = 7;
/*  53 */         int l = 0;
/*     */         
/*  55 */         if (j > 0)
/*     */         {
/*  57 */           k = 7 - j;
/*     */         }
/*     */         
/*  60 */         if (j > 0)
/*     */         {
/*  62 */           l = k - 2;
/*     */         }
/*     */         
/*  65 */         double d2 = adouble[k] - d0;
/*  66 */         double d3 = adouble1[k] - d1;
/*     */         
/*  68 */         for (int i1 = k; i1 >= l; i1--)
/*     */         {
/*  70 */           double d4 = d2;
/*  71 */           double d5 = d3;
/*     */           
/*  73 */           if (j == 0)
/*     */           {
/*  75 */             d2 += random1.nextInt(11) - 5;
/*  76 */             d3 += random1.nextInt(11) - 5;
/*     */           }
/*     */           else
/*     */           {
/*  80 */             d2 += random1.nextInt(31) - 15;
/*  81 */             d3 += random1.nextInt(31) - 15;
/*     */           }
/*     */           
/*  84 */           worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*  85 */           float f = 0.5F;
/*  86 */           float f1 = 0.45F;
/*  87 */           float f2 = 0.45F;
/*  88 */           float f3 = 0.5F;
/*  89 */           double d6 = 0.1D + k1 * 0.2D;
/*     */           
/*  91 */           if (j == 0)
/*     */           {
/*  93 */             d6 *= (i1 * 0.1D + 1.0D);
/*     */           }
/*     */           
/*  96 */           double d7 = 0.1D + k1 * 0.2D;
/*     */           
/*  98 */           if (j == 0)
/*     */           {
/* 100 */             d7 *= ((i1 - 1) * 0.1D + 1.0D);
/*     */           }
/*     */           
/* 103 */           for (int j1 = 0; j1 < 5; j1++)
/*     */           {
/* 105 */             double d8 = x + 0.5D - d6;
/* 106 */             double d9 = z + 0.5D - d6;
/*     */             
/* 108 */             if ((j1 == 1) || (j1 == 2))
/*     */             {
/* 110 */               d8 += d6 * 2.0D;
/*     */             }
/*     */             
/* 113 */             if ((j1 == 2) || (j1 == 3))
/*     */             {
/* 115 */               d9 += d6 * 2.0D;
/*     */             }
/*     */             
/* 118 */             double d10 = x + 0.5D - d7;
/* 119 */             double d11 = z + 0.5D - d7;
/*     */             
/* 121 */             if ((j1 == 1) || (j1 == 2))
/*     */             {
/* 123 */               d10 += d7 * 2.0D;
/*     */             }
/*     */             
/* 126 */             if ((j1 == 2) || (j1 == 3))
/*     */             {
/* 128 */               d11 += d7 * 2.0D;
/*     */             }
/*     */             
/* 131 */             worldrenderer.pos(d10 + d2, y + i1 * 16, d11 + d3).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
/* 132 */             worldrenderer.pos(d8 + d4, y + (i1 + 1) * 16, d9 + d5).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
/*     */           }
/*     */           
/* 135 */           tessellator.draw();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 140 */     GlStateManager.disableBlend();
/* 141 */     GlStateManager.enableLighting();
/* 142 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceLocation getEntityTexture(EntityLightningBolt entity)
/*     */   {
/* 150 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderLightningBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */