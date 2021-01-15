/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelWither;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
/*    */ import net.minecraft.entity.boss.BossStatus;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderWither extends RenderLiving<EntityWither>
/*    */ {
/* 12 */   private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
/* 13 */   private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
/*    */   
/*    */   public RenderWither(RenderManager renderManagerIn)
/*    */   {
/* 17 */     super(renderManagerIn, new ModelWither(0.0F), 1.0F);
/* 18 */     addLayer(new LayerWitherAura(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityWither entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 29 */     BossStatus.setBossStatus(entity, true);
/* 30 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityWither entity)
/*    */   {
/* 38 */     int i = entity.getInvulTime();
/* 39 */     return (i > 0) && ((i > 80) || (i / 5 % 2 != 1)) ? invulnerableWitherTextures : witherTextures;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityWither entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 48 */     float f = 2.0F;
/* 49 */     int i = entitylivingbaseIn.getInvulTime();
/*    */     
/* 51 */     if (i > 0)
/*    */     {
/* 53 */       f -= (i - partialTickTime) / 220.0F * 0.5F;
/*    */     }
/*    */     
/* 56 */     GlStateManager.scale(f, f, f);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */