/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelIronGolem;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderIronGolem extends RenderLiving<EntityIronGolem>
/*    */ {
/* 11 */   private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");
/*    */   
/*    */   public RenderIronGolem(RenderManager renderManagerIn)
/*    */   {
/* 15 */     super(renderManagerIn, new ModelIronGolem(), 0.5F);
/* 16 */     addLayer(new LayerIronGolemFlower(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityIronGolem entity)
/*    */   {
/* 24 */     return ironGolemTextures;
/*    */   }
/*    */   
/*    */   protected void rotateCorpse(EntityIronGolem bat, float p_77043_2_, float p_77043_3_, float partialTicks)
/*    */   {
/* 29 */     super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*    */     
/* 31 */     if (bat.limbSwingAmount >= 0.01D)
/*    */     {
/* 33 */       float f = 13.0F;
/* 34 */       float f1 = bat.limbSwing - bat.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
/* 35 */       float f2 = (Math.abs(f1 % f - f * 0.5F) - f * 0.25F) / (f * 0.25F);
/* 36 */       GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */