/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*    */ 
/*    */ public class LayerBipedArmor extends LayerArmorBase<ModelBiped>
/*    */ {
/*    */   public LayerBipedArmor(RendererLivingEntity<?> rendererIn)
/*    */   {
/* 10 */     super(rendererIn);
/*    */   }
/*    */   
/*    */   protected void initArmor()
/*    */   {
/* 15 */     this.field_177189_c = new ModelBiped(0.5F);
/* 16 */     this.field_177186_d = new ModelBiped(1.0F);
/*    */   }
/*    */   
/*    */   protected void func_177179_a(ModelBiped p_177179_1_, int p_177179_2_)
/*    */   {
/* 21 */     func_177194_a(p_177179_1_);
/*    */     
/* 23 */     switch (p_177179_2_)
/*    */     {
/*    */     case 1: 
/* 26 */       p_177179_1_.bipedRightLeg.showModel = true;
/* 27 */       p_177179_1_.bipedLeftLeg.showModel = true;
/* 28 */       break;
/*    */     
/*    */     case 2: 
/* 31 */       p_177179_1_.bipedBody.showModel = true;
/* 32 */       p_177179_1_.bipedRightLeg.showModel = true;
/* 33 */       p_177179_1_.bipedLeftLeg.showModel = true;
/* 34 */       break;
/*    */     
/*    */     case 3: 
/* 37 */       p_177179_1_.bipedBody.showModel = true;
/* 38 */       p_177179_1_.bipedRightArm.showModel = true;
/* 39 */       p_177179_1_.bipedLeftArm.showModel = true;
/* 40 */       break;
/*    */     
/*    */     case 4: 
/* 43 */       p_177179_1_.bipedHead.showModel = true;
/* 44 */       p_177179_1_.bipedHeadwear.showModel = true;
/*    */     }
/*    */   }
/*    */   
/*    */   protected void func_177194_a(ModelBiped p_177194_1_)
/*    */   {
/* 50 */     p_177194_1_.setInvisible(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerBipedArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */