/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelVillager;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderVillager extends RenderLiving<EntityVillager>
/*    */ {
/* 11 */   private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");
/* 12 */   private static final ResourceLocation farmerVillagerTextures = new ResourceLocation("textures/entity/villager/farmer.png");
/* 13 */   private static final ResourceLocation librarianVillagerTextures = new ResourceLocation("textures/entity/villager/librarian.png");
/* 14 */   private static final ResourceLocation priestVillagerTextures = new ResourceLocation("textures/entity/villager/priest.png");
/* 15 */   private static final ResourceLocation smithVillagerTextures = new ResourceLocation("textures/entity/villager/smith.png");
/* 16 */   private static final ResourceLocation butcherVillagerTextures = new ResourceLocation("textures/entity/villager/butcher.png");
/*    */   
/*    */   public RenderVillager(RenderManager renderManagerIn)
/*    */   {
/* 20 */     super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
/* 21 */     addLayer(new LayerCustomHead(getMainModel().villagerHead));
/*    */   }
/*    */   
/*    */   public ModelVillager getMainModel()
/*    */   {
/* 26 */     return (ModelVillager)super.getMainModel();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityVillager entity)
/*    */   {
/* 34 */     switch (entity.getProfession())
/*    */     {
/*    */     case 0: 
/* 37 */       return farmerVillagerTextures;
/*    */     
/*    */     case 1: 
/* 40 */       return librarianVillagerTextures;
/*    */     
/*    */     case 2: 
/* 43 */       return priestVillagerTextures;
/*    */     
/*    */     case 3: 
/* 46 */       return smithVillagerTextures;
/*    */     
/*    */     case 4: 
/* 49 */       return butcherVillagerTextures;
/*    */     }
/*    */     
/* 52 */     return villagerTextures;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime)
/*    */   {
/* 62 */     float f = 0.9375F;
/*    */     
/* 64 */     if (entitylivingbaseIn.getGrowingAge() < 0)
/*    */     {
/* 66 */       f = (float)(f * 0.5D);
/* 67 */       this.shadowSize = 0.25F;
/*    */     }
/*    */     else
/*    */     {
/* 71 */       this.shadowSize = 0.5F;
/*    */     }
/*    */     
/* 74 */     GlStateManager.scale(f, f, f);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */