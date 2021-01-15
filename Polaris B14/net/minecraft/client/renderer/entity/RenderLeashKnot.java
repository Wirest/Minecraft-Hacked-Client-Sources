/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelLeashKnot;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.EntityLeashKnot;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderLeashKnot extends Render<EntityLeashKnot>
/*    */ {
/* 10 */   private static final ResourceLocation leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
/* 11 */   private ModelLeashKnot leashKnotModel = new ModelLeashKnot();
/*    */   
/*    */   public RenderLeashKnot(RenderManager renderManagerIn)
/*    */   {
/* 15 */     super(renderManagerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityLeashKnot entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 26 */     GlStateManager.pushMatrix();
/* 27 */     GlStateManager.disableCull();
/* 28 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 29 */     float f = 0.0625F;
/* 30 */     GlStateManager.enableRescaleNormal();
/* 31 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 32 */     GlStateManager.enableAlpha();
/* 33 */     bindEntityTexture(entity);
/* 34 */     this.leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f);
/* 35 */     GlStateManager.popMatrix();
/* 36 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityLeashKnot entity)
/*    */   {
/* 44 */     return leashKnotTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderLeashKnot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */