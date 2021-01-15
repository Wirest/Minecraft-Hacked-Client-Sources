/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.monster.EntityCaveSpider;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderCaveSpider extends RenderSpider<EntityCaveSpider>
/*    */ {
/*  9 */   private static final ResourceLocation caveSpiderTextures = new ResourceLocation("textures/entity/spider/cave_spider.png");
/*    */   
/*    */   public RenderCaveSpider(RenderManager renderManagerIn)
/*    */   {
/* 13 */     super(renderManagerIn);
/* 14 */     this.shadowSize *= 0.7F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 23 */     GlStateManager.scale(0.7F, 0.7F, 0.7F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityCaveSpider entity)
/*    */   {
/* 31 */     return caveSpiderTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */