/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelSilverfish;
/*    */ import net.minecraft.entity.monster.EntitySilverfish;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSilverfish extends RenderLiving<EntitySilverfish>
/*    */ {
/*  9 */   private static final ResourceLocation silverfishTextures = new ResourceLocation("textures/entity/silverfish.png");
/*    */   
/*    */   public RenderSilverfish(RenderManager renderManagerIn)
/*    */   {
/* 13 */     super(renderManagerIn, new ModelSilverfish(), 0.3F);
/*    */   }
/*    */   
/*    */   protected float getDeathMaxRotation(EntitySilverfish entityLivingBaseIn)
/*    */   {
/* 18 */     return 180.0F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntitySilverfish entity)
/*    */   {
/* 26 */     return silverfishTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */