/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelBoat;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.item.EntityBoat;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderBoat extends Render<EntityBoat>
/*    */ {
/* 12 */   private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");
/*    */   
/*    */ 
/* 15 */   protected ModelBase modelBoat = new ModelBoat();
/*    */   
/*    */   public RenderBoat(RenderManager renderManagerIn)
/*    */   {
/* 19 */     super(renderManagerIn);
/* 20 */     this.shadowSize = 0.5F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 31 */     GlStateManager.pushMatrix();
/* 32 */     GlStateManager.translate((float)x, (float)y + 0.25F, (float)z);
/* 33 */     GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
/* 34 */     float f = entity.getTimeSinceHit() - partialTicks;
/* 35 */     float f1 = entity.getDamageTaken() - partialTicks;
/*    */     
/* 37 */     if (f1 < 0.0F)
/*    */     {
/* 39 */       f1 = 0.0F;
/*    */     }
/*    */     
/* 42 */     if (f > 0.0F)
/*    */     {
/* 44 */       GlStateManager.rotate(MathHelper.sin(f) * f * f1 / 10.0F * entity.getForwardDirection(), 1.0F, 0.0F, 0.0F);
/*    */     }
/*    */     
/* 47 */     float f2 = 0.75F;
/* 48 */     GlStateManager.scale(f2, f2, f2);
/* 49 */     GlStateManager.scale(1.0F / f2, 1.0F / f2, 1.0F / f2);
/* 50 */     bindEntityTexture(entity);
/* 51 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 52 */     this.modelBoat.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 53 */     GlStateManager.popMatrix();
/* 54 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityBoat entity)
/*    */   {
/* 62 */     return boatTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */