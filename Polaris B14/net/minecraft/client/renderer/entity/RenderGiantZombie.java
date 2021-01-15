/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelZombie;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*    */ import net.minecraft.entity.monster.EntityGiantZombie;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderGiantZombie extends RenderLiving<EntityGiantZombie>
/*    */ {
/* 13 */   private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
/*    */   
/*    */   private float scale;
/*    */   
/*    */ 
/*    */   public RenderGiantZombie(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn, float scaleIn)
/*    */   {
/* 20 */     super(renderManagerIn, modelBaseIn, shadowSizeIn * scaleIn);
/* 21 */     this.scale = scaleIn;
/* 22 */     addLayer(new LayerHeldItem(this));
/* 23 */     addLayer(new LayerBipedArmor(this)
/*    */     {
/*    */       protected void initArmor()
/*    */       {
/* 27 */         this.field_177189_c = new ModelZombie(0.5F, true);
/* 28 */         this.field_177186_d = new ModelZombie(1.0F, true);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public void transformHeldFull3DItemLayer()
/*    */   {
/* 35 */     GlStateManager.translate(0.0F, 0.1875F, 0.0F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 44 */     GlStateManager.scale(this.scale, this.scale, this.scale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityGiantZombie entity)
/*    */   {
/* 52 */     return zombieTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderGiantZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */