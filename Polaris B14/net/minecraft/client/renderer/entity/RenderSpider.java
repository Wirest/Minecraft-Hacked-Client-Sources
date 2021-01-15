/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelSpider;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
/*    */ import net.minecraft.entity.monster.EntitySpider;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSpider<T extends EntitySpider> extends RenderLiving<T>
/*    */ {
/* 10 */   private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
/*    */   
/*    */   public RenderSpider(RenderManager renderManagerIn)
/*    */   {
/* 14 */     super(renderManagerIn, new ModelSpider(), 1.0F);
/* 15 */     addLayer(new LayerSpiderEyes(this));
/*    */   }
/*    */   
/*    */   protected float getDeathMaxRotation(T entityLivingBaseIn)
/*    */   {
/* 20 */     return 180.0F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(T entity)
/*    */   {
/* 28 */     return spiderTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */