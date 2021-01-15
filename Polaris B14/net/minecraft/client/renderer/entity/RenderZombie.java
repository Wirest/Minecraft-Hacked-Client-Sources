/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.model.ModelZombie;
/*    */ import net.minecraft.client.model.ModelZombieVillager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
/*    */ import net.minecraft.entity.monster.EntityZombie;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderZombie extends RenderBiped<EntityZombie>
/*    */ {
/* 18 */   private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
/* 19 */   private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
/*    */   private final ModelBiped field_82434_o;
/*    */   private final ModelZombieVillager zombieVillagerModel;
/*    */   private final List<LayerRenderer<EntityZombie>> field_177121_n;
/*    */   private final List<LayerRenderer<EntityZombie>> field_177122_o;
/*    */   
/*    */   public RenderZombie(RenderManager renderManagerIn)
/*    */   {
/* 27 */     super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
/* 28 */     LayerRenderer layerrenderer = (LayerRenderer)this.layerRenderers.get(0);
/* 29 */     this.field_82434_o = this.modelBipedMain;
/* 30 */     this.zombieVillagerModel = new ModelZombieVillager();
/* 31 */     addLayer(new LayerHeldItem(this));
/* 32 */     LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
/*    */     {
/*    */       protected void initArmor()
/*    */       {
/* 36 */         this.field_177189_c = new ModelZombie(0.5F, true);
/* 37 */         this.field_177186_d = new ModelZombie(1.0F, true);
/*    */       }
/* 39 */     };
/* 40 */     addLayer(layerbipedarmor);
/* 41 */     this.field_177122_o = Lists.newArrayList(this.layerRenderers);
/*    */     
/* 43 */     if ((layerrenderer instanceof LayerCustomHead))
/*    */     {
/* 45 */       removeLayer(layerrenderer);
/* 46 */       addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
/*    */     }
/*    */     
/* 49 */     removeLayer(layerbipedarmor);
/* 50 */     addLayer(new LayerVillagerArmor(this));
/* 51 */     this.field_177121_n = Lists.newArrayList(this.layerRenderers);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 62 */     func_82427_a(entity);
/* 63 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityZombie entity)
/*    */   {
/* 71 */     return entity.isVillager() ? zombieVillagerTextures : zombieTextures;
/*    */   }
/*    */   
/*    */   private void func_82427_a(EntityZombie zombie)
/*    */   {
/* 76 */     if (zombie.isVillager())
/*    */     {
/* 78 */       this.mainModel = this.zombieVillagerModel;
/* 79 */       this.layerRenderers = this.field_177121_n;
/*    */     }
/*    */     else
/*    */     {
/* 83 */       this.mainModel = this.field_82434_o;
/* 84 */       this.layerRenderers = this.field_177122_o;
/*    */     }
/*    */     
/* 87 */     this.modelBipedMain = ((ModelBiped)this.mainModel);
/*    */   }
/*    */   
/*    */   protected void rotateCorpse(EntityZombie bat, float p_77043_2_, float p_77043_3_, float partialTicks)
/*    */   {
/* 92 */     if (bat.isConverting())
/*    */     {
/* 94 */       p_77043_3_ += (float)(Math.cos(bat.ticksExisted * 3.25D) * 3.141592653589793D * 0.25D);
/*    */     }
/*    */     
/* 97 */     super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */