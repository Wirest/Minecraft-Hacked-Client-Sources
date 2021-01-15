/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelArmorStand;
/*    */ import net.minecraft.client.model.ModelArmorStandArmor;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArmorStandRenderer
/*    */   extends RendererLivingEntity<EntityArmorStand>
/*    */ {
/* 17 */   public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");
/*    */   
/*    */   public ArmorStandRenderer(RenderManager p_i46195_1_)
/*    */   {
/* 21 */     super(p_i46195_1_, new ModelArmorStand(), 0.0F);
/* 22 */     LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
/*    */     {
/*    */       protected void initArmor()
/*    */       {
/* 26 */         this.field_177189_c = new ModelArmorStandArmor(0.5F);
/* 27 */         this.field_177186_d = new ModelArmorStandArmor(1.0F);
/*    */       }
/* 29 */     };
/* 30 */     addLayer(layerbipedarmor);
/* 31 */     addLayer(new LayerHeldItem(this));
/* 32 */     addLayer(new LayerCustomHead(getMainModel().bipedHead));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityArmorStand entity)
/*    */   {
/* 40 */     return TEXTURE_ARMOR_STAND;
/*    */   }
/*    */   
/*    */   public ModelArmorStand getMainModel()
/*    */   {
/* 45 */     return (ModelArmorStand)super.getMainModel();
/*    */   }
/*    */   
/*    */   protected void rotateCorpse(EntityArmorStand bat, float p_77043_2_, float p_77043_3_, float partialTicks)
/*    */   {
/* 50 */     GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */   
/*    */   protected boolean canRenderName(EntityArmorStand entity)
/*    */   {
/* 55 */     return entity.getAlwaysRenderNameTag();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\ArmorStandRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */