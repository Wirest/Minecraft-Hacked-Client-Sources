/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelGhast;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.monster.EntityGhast;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderGhast extends RenderLiving<EntityGhast>
/*    */ {
/* 10 */   private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
/* 11 */   private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
/*    */   
/*    */   public RenderGhast(RenderManager renderManagerIn)
/*    */   {
/* 15 */     super(renderManagerIn, new ModelGhast(), 0.5F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityGhast entity)
/*    */   {
/* 23 */     return entity.isAttacking() ? ghastShootingTextures : ghastTextures;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 32 */     float f = 1.0F;
/* 33 */     float f1 = (8.0F + f) / 2.0F;
/* 34 */     float f2 = (8.0F + 1.0F / f) / 2.0F;
/* 35 */     GlStateManager.scale(f2, f1, f2);
/* 36 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */