/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.model.ModelBox;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderHelper;
/*    */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.projectile.EntityArrow;
/*    */ 
/*    */ public class LayerArrow implements LayerRenderer<EntityLivingBase>
/*    */ {
/*    */   private final RendererLivingEntity field_177168_a;
/*    */   
/*    */   public LayerArrow(RendererLivingEntity p_i46124_1_)
/*    */   {
/* 20 */     this.field_177168_a = p_i46124_1_;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 25 */     int i = entitylivingbaseIn.getArrowCountInEntity();
/*    */     
/* 27 */     if (i > 0)
/*    */     {
/* 29 */       Entity entity = new EntityArrow(entitylivingbaseIn.worldObj, entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
/* 30 */       Random random = new Random(entitylivingbaseIn.getEntityId());
/* 31 */       RenderHelper.disableStandardItemLighting();
/*    */       
/* 33 */       for (int j = 0; j < i; j++)
/*    */       {
/* 35 */         GlStateManager.pushMatrix();
/* 36 */         ModelRenderer modelrenderer = this.field_177168_a.getMainModel().getRandomModelBox(random);
/* 37 */         ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
/* 38 */         modelrenderer.postRender(0.0625F);
/* 39 */         float f = random.nextFloat();
/* 40 */         float f1 = random.nextFloat();
/* 41 */         float f2 = random.nextFloat();
/* 42 */         float f3 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16.0F;
/* 43 */         float f4 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f1) / 16.0F;
/* 44 */         float f5 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f2) / 16.0F;
/* 45 */         GlStateManager.translate(f3, f4, f5);
/* 46 */         f = f * 2.0F - 1.0F;
/* 47 */         f1 = f1 * 2.0F - 1.0F;
/* 48 */         f2 = f2 * 2.0F - 1.0F;
/* 49 */         f *= -1.0F;
/* 50 */         f1 *= -1.0F;
/* 51 */         f2 *= -1.0F;
/* 52 */         float f6 = net.minecraft.util.MathHelper.sqrt_float(f * f + f2 * f2);
/* 53 */         entity.prevRotationYaw = (entity.rotationYaw = (float)(Math.atan2(f, f2) * 180.0D / 3.141592653589793D));
/* 54 */         entity.prevRotationPitch = (entity.rotationPitch = (float)(Math.atan2(f1, f6) * 180.0D / 3.141592653589793D));
/* 55 */         double d0 = 0.0D;
/* 56 */         double d1 = 0.0D;
/* 57 */         double d2 = 0.0D;
/* 58 */         this.field_177168_a.getRenderManager().renderEntityWithPosYaw(entity, d0, d1, d2, 0.0F, partialTicks);
/* 59 */         GlStateManager.popMatrix();
/*    */       }
/*    */       
/* 62 */       RenderHelper.enableStandardItemLighting();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */