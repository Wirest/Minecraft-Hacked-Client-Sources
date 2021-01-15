/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelPlayer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class LayerCape implements LayerRenderer
/*    */ {
/*    */   private final RenderPlayer playerRenderer;
/*    */   private static final String __OBFID = "CL_00002425";
/*    */   
/*    */   public LayerCape(RenderPlayer playerRendererIn)
/*    */   {
/* 17 */     this.playerRenderer = playerRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 22 */     if ((entitylivingbaseIn.hasPlayerInfo()) && (!entitylivingbaseIn.isInvisible()) && (entitylivingbaseIn.isWearing(net.minecraft.entity.player.EnumPlayerModelParts.CAPE)) && (entitylivingbaseIn.getLocationCape() != null))
/*    */     {
/* 24 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 25 */       this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
/* 26 */       GlStateManager.pushMatrix();
/* 27 */       GlStateManager.translate(0.0F, 0.0F, 0.125F);
/* 28 */       double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
/* 29 */       double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
/* 30 */       double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
/* 31 */       float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
/* 32 */       double d3 = MathHelper.sin(f * 3.1415927F / 180.0F);
/* 33 */       double d4 = -MathHelper.cos(f * 3.1415927F / 180.0F);
/* 34 */       float f1 = (float)d1 * 10.0F;
/* 35 */       f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
/* 36 */       float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
/* 37 */       float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
/*    */       
/* 39 */       if (f2 < 0.0F)
/*    */       {
/* 41 */         f2 = 0.0F;
/*    */       }
/*    */       
/* 44 */       if (f2 > 165.0F)
/*    */       {
/* 46 */         f2 = 165.0F;
/*    */       }
/*    */       
/* 49 */       float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
/* 50 */       f1 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
/*    */       
/* 52 */       if (entitylivingbaseIn.isSneaking())
/*    */       {
/* 54 */         f1 += 25.0F;
/* 55 */         GlStateManager.translate(0.0F, 0.142F, -0.0178F);
/*    */       }
/*    */       
/* 58 */       GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
/* 59 */       GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
/* 60 */       GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
/* 61 */       GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/* 62 */       this.playerRenderer.getMainModel().renderCape(0.0625F);
/* 63 */       GlStateManager.popMatrix();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 74 */     doRenderLayer((AbstractClientPlayer)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerCape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */