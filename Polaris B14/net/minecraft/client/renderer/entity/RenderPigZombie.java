/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelZombie;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*    */ import net.minecraft.entity.monster.EntityPigZombie;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderPigZombie extends RenderBiped<EntityPigZombie>
/*    */ {
/* 11 */   private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");
/*    */   
/*    */   public RenderPigZombie(RenderManager renderManagerIn)
/*    */   {
/* 15 */     super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
/* 16 */     addLayer(new LayerHeldItem(this));
/* 17 */     addLayer(new LayerBipedArmor(this)
/*    */     {
/*    */       protected void initArmor()
/*    */       {
/* 21 */         this.field_177189_c = new ModelZombie(0.5F, true);
/* 22 */         this.field_177186_d = new ModelZombie(1.0F, true);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityPigZombie entity)
/*    */   {
/* 32 */     return ZOMBIE_PIGMAN_TEXTURE;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderPigZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */