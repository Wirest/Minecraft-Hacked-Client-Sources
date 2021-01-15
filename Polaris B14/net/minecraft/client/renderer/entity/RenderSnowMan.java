/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelSnowMan;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
/*    */ import net.minecraft.entity.monster.EntitySnowman;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSnowMan extends RenderLiving<EntitySnowman>
/*    */ {
/* 10 */   private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
/*    */   
/*    */   public RenderSnowMan(RenderManager renderManagerIn)
/*    */   {
/* 14 */     super(renderManagerIn, new ModelSnowMan(), 0.5F);
/* 15 */     addLayer(new LayerSnowmanHead(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntitySnowman entity)
/*    */   {
/* 23 */     return snowManTextures;
/*    */   }
/*    */   
/*    */   public ModelSnowMan getMainModel()
/*    */   {
/* 28 */     return (ModelSnowMan)super.getMainModel();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSnowMan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */