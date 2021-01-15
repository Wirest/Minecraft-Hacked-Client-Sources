/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityHanging;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ 
/*     */ public abstract class RenderLiving<T extends EntityLiving> extends RendererLivingEntity<T>
/*     */ {
/*     */   public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
/*     */   {
/*  18 */     super(rendermanagerIn, modelbaseIn, shadowsizeIn);
/*     */   }
/*     */   
/*     */   protected boolean canRenderName(T entity)
/*     */   {
/*  23 */     return (super.canRenderName(entity)) && ((entity.getAlwaysRenderNameTagForRender()) || ((entity.hasCustomName()) && (entity == this.renderManager.pointedEntity)));
/*     */   }
/*     */   
/*     */   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ)
/*     */   {
/*  28 */     if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
/*     */     {
/*  30 */       return true;
/*     */     }
/*  32 */     if ((livingEntity.getLeashed()) && (livingEntity.getLeashedToEntity() != null))
/*     */     {
/*  34 */       Entity entity = livingEntity.getLeashedToEntity();
/*  35 */       return camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
/*     */     }
/*     */     
/*     */ 
/*  39 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  51 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*  52 */     renderLeash(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */   
/*     */   public void func_177105_a(T entityLivingIn, float partialTicks)
/*     */   {
/*  57 */     int i = entityLivingIn.getBrightnessForRender(partialTicks);
/*  58 */     int j = i % 65536;
/*  59 */     int k = i / 65536;
/*  60 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private double interpolateValue(double start, double end, double pct)
/*     */   {
/*  68 */     return start + (end - start) * pct;
/*     */   }
/*     */   
/*     */   protected void renderLeash(T entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  73 */     Entity entity = entityLivingIn.getLeashedToEntity();
/*     */     
/*  75 */     if (entity != null)
/*     */     {
/*  77 */       y -= (1.6D - entityLivingIn.height) * 0.5D;
/*  78 */       Tessellator tessellator = Tessellator.getInstance();
/*  79 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  80 */       double d0 = interpolateValue(entity.prevRotationYaw, entity.rotationYaw, partialTicks * 0.5F) * 0.01745329238474369D;
/*  81 */       double d1 = interpolateValue(entity.prevRotationPitch, entity.rotationPitch, partialTicks * 0.5F) * 0.01745329238474369D;
/*  82 */       double d2 = Math.cos(d0);
/*  83 */       double d3 = Math.sin(d0);
/*  84 */       double d4 = Math.sin(d1);
/*     */       
/*  86 */       if ((entity instanceof EntityHanging))
/*     */       {
/*  88 */         d2 = 0.0D;
/*  89 */         d3 = 0.0D;
/*  90 */         d4 = -1.0D;
/*     */       }
/*     */       
/*  93 */       double d5 = Math.cos(d1);
/*  94 */       double d6 = interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
/*  95 */       double d7 = interpolateValue(entity.prevPosY + entity.getEyeHeight() * 0.7D, entity.posY + entity.getEyeHeight() * 0.7D, partialTicks) - d4 * 0.5D - 0.25D;
/*  96 */       double d8 = interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
/*  97 */       double d9 = interpolateValue(entityLivingIn.prevRenderYawOffset, entityLivingIn.renderYawOffset, partialTicks) * 0.01745329238474369D + 1.5707963267948966D;
/*  98 */       d2 = Math.cos(d9) * entityLivingIn.width * 0.4D;
/*  99 */       d3 = Math.sin(d9) * entityLivingIn.width * 0.4D;
/* 100 */       double d10 = interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, partialTicks) + d2;
/* 101 */       double d11 = interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, partialTicks);
/* 102 */       double d12 = interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, partialTicks) + d3;
/* 103 */       x += d2;
/* 104 */       z += d3;
/* 105 */       double d13 = (float)(d6 - d10);
/* 106 */       double d14 = (float)(d7 - d11);
/* 107 */       double d15 = (float)(d8 - d12);
/* 108 */       GlStateManager.disableTexture2D();
/* 109 */       GlStateManager.disableLighting();
/* 110 */       GlStateManager.disableCull();
/* 111 */       int i = 24;
/* 112 */       double d16 = 0.025D;
/* 113 */       worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*     */       
/* 115 */       for (int j = 0; j <= 24; j++)
/*     */       {
/* 117 */         float f = 0.5F;
/* 118 */         float f1 = 0.4F;
/* 119 */         float f2 = 0.3F;
/*     */         
/* 121 */         if (j % 2 == 0)
/*     */         {
/* 123 */           f *= 0.7F;
/* 124 */           f1 *= 0.7F;
/* 125 */           f2 *= 0.7F;
/*     */         }
/*     */         
/* 128 */         float f3 = j / 24.0F;
/* 129 */         worldrenderer.pos(x + d13 * f3 + 0.0D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F), z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
/* 130 */         worldrenderer.pos(x + d13 * f3 + 0.025D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F) + 0.025D, z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
/*     */       }
/*     */       
/* 133 */       tessellator.draw();
/* 134 */       worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*     */       
/* 136 */       for (int k = 0; k <= 24; k++)
/*     */       {
/* 138 */         float f4 = 0.5F;
/* 139 */         float f5 = 0.4F;
/* 140 */         float f6 = 0.3F;
/*     */         
/* 142 */         if (k % 2 == 0)
/*     */         {
/* 144 */           f4 *= 0.7F;
/* 145 */           f5 *= 0.7F;
/* 146 */           f6 *= 0.7F;
/*     */         }
/*     */         
/* 149 */         float f7 = k / 24.0F;
/* 150 */         worldrenderer.pos(x + d13 * f7 + 0.0D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F) + 0.025D, z + d15 * f7).color(f4, f5, f6, 1.0F).endVertex();
/* 151 */         worldrenderer.pos(x + d13 * f7 + 0.025D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F), z + d15 * f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
/*     */       }
/*     */       
/* 154 */       tessellator.draw();
/* 155 */       GlStateManager.enableLighting();
/* 156 */       GlStateManager.enableTexture2D();
/* 157 */       GlStateManager.enableCull();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */