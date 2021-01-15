/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSheep extends RenderLiving<EntitySheep>
/*    */ {
/* 10 */   private static final ResourceLocation shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
/*    */   
/*    */   public RenderSheep(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
/*    */   {
/* 14 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/* 15 */     addLayer(new LayerSheepWool(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntitySheep entity)
/*    */   {
/* 23 */     return shearedSheepTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */