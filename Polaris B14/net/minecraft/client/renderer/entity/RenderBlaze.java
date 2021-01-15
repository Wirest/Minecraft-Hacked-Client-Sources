/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBlaze;
/*    */ import net.minecraft.entity.monster.EntityBlaze;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderBlaze extends RenderLiving<EntityBlaze>
/*    */ {
/*  9 */   private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
/*    */   
/*    */   public RenderBlaze(RenderManager renderManagerIn)
/*    */   {
/* 13 */     super(renderManagerIn, new ModelBlaze(), 0.5F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityBlaze entity)
/*    */   {
/* 21 */     return blazeTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */