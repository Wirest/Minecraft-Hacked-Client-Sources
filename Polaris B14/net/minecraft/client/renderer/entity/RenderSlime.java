/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
/*    */ import net.minecraft.entity.monster.EntitySlime;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSlime extends RenderLiving<EntitySlime>
/*    */ {
/* 11 */   private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
/*    */   
/*    */   public RenderSlime(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
/*    */   {
/* 15 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/* 16 */     addLayer(new LayerSlimeGel(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 27 */     this.shadowSize = (0.25F * entity.getSlimeSize());
/* 28 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntitySlime entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 37 */     float f = entitylivingbaseIn.getSlimeSize();
/* 38 */     float f1 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f * 0.5F + 1.0F);
/* 39 */     float f2 = 1.0F / (f1 + 1.0F);
/* 40 */     GlStateManager.scale(f2 * f, 1.0F / f2 * f, f2 * f);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntitySlime entity)
/*    */   {
/* 48 */     return slimeTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */