/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelEnderMite;
/*    */ import net.minecraft.entity.monster.EntityEndermite;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderEndermite extends RenderLiving<EntityEndermite>
/*    */ {
/*  9 */   private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation("textures/entity/endermite.png");
/*    */   
/*    */   public RenderEndermite(RenderManager renderManagerIn)
/*    */   {
/* 13 */     super(renderManagerIn, new ModelEnderMite(), 0.3F);
/*    */   }
/*    */   
/*    */   protected float getDeathMaxRotation(EntityEndermite entityLivingBaseIn)
/*    */   {
/* 18 */     return 180.0F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityEndermite entity)
/*    */   {
/* 26 */     return ENDERMITE_TEXTURES;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */