/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelCreeper;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderCreeper extends RenderLiving<EntityCreeper>
/*    */ {
/* 12 */   private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
/*    */   
/*    */   public RenderCreeper(RenderManager renderManagerIn)
/*    */   {
/* 16 */     super(renderManagerIn, new ModelCreeper(), 0.5F);
/* 17 */     addLayer(new LayerCreeperCharge(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 26 */     float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
/* 27 */     float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
/* 28 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 29 */     f *= f;
/* 30 */     f *= f;
/* 31 */     float f2 = (1.0F + f * 0.4F) * f1;
/* 32 */     float f3 = (1.0F + f * 0.1F) / f1;
/* 33 */     GlStateManager.scale(f2, f3, f2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int getColorMultiplier(EntityCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime)
/*    */   {
/* 41 */     float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
/*    */     
/* 43 */     if ((int)(f * 10.0F) % 2 == 0)
/*    */     {
/* 45 */       return 0;
/*    */     }
/*    */     
/*    */ 
/* 49 */     int i = (int)(f * 0.2F * 255.0F);
/* 50 */     i = MathHelper.clamp_int(i, 0, 255);
/* 51 */     return i << 24 | 0xFFFFFF;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityCreeper entity)
/*    */   {
/* 60 */     return creeperTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */