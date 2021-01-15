/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelZombieVillager;
/*    */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*    */ 
/*    */ public class LayerVillagerArmor extends LayerBipedArmor
/*    */ {
/*    */   public LayerVillagerArmor(RendererLivingEntity<?> rendererIn)
/*    */   {
/* 10 */     super(rendererIn);
/*    */   }
/*    */   
/*    */   protected void initArmor()
/*    */   {
/* 15 */     this.field_177189_c = new ModelZombieVillager(0.5F, 0.0F, true);
/* 16 */     this.field_177186_d = new ModelZombieVillager(1.0F, 0.0F, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerVillagerArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */