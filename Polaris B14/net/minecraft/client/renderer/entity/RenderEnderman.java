/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.model.ModelEnderman;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
/*    */ import net.minecraft.entity.monster.EntityEnderman;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderEnderman extends RenderLiving<EntityEnderman>
/*    */ {
/* 13 */   private static final ResourceLocation endermanTextures = new ResourceLocation("textures/entity/enderman/enderman.png");
/*    */   
/*    */   private ModelEnderman endermanModel;
/*    */   
/* 17 */   private Random rnd = new Random();
/*    */   
/*    */   public RenderEnderman(RenderManager renderManagerIn)
/*    */   {
/* 21 */     super(renderManagerIn, new ModelEnderman(0.0F), 0.5F);
/* 22 */     this.endermanModel = ((ModelEnderman)this.mainModel);
/* 23 */     addLayer(new LayerEndermanEyes(this));
/* 24 */     addLayer(new LayerHeldBlock(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 35 */     this.endermanModel.isCarrying = (entity.getHeldBlockState().getBlock().getMaterial() != net.minecraft.block.material.Material.air);
/* 36 */     this.endermanModel.isAttacking = entity.isScreaming();
/*    */     
/* 38 */     if (entity.isScreaming())
/*    */     {
/* 40 */       double d0 = 0.02D;
/* 41 */       x += this.rnd.nextGaussian() * d0;
/* 42 */       z += this.rnd.nextGaussian() * d0;
/*    */     }
/*    */     
/* 45 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityEnderman entity)
/*    */   {
/* 53 */     return endermanTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */