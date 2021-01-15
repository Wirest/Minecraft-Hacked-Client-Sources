/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerSaddle;
/*    */ import net.minecraft.entity.passive.EntityPig;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderPig extends RenderLiving<EntityPig>
/*    */ {
/* 10 */   private static final ResourceLocation pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
/*    */   
/*    */   public RenderPig(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
/*    */   {
/* 14 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/* 15 */     addLayer(new LayerSaddle(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityPig entity)
/*    */   {
/* 23 */     return pigTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */