/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelCreeper;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderCreeper;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerCreeperCharge implements LayerRenderer<EntityCreeper>
/*    */ {
/* 11 */   private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
/*    */   private final RenderCreeper creeperRenderer;
/* 13 */   private final ModelCreeper creeperModel = new ModelCreeper(2.0F);
/*    */   
/*    */   public LayerCreeperCharge(RenderCreeper creeperRendererIn)
/*    */   {
/* 17 */     this.creeperRenderer = creeperRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityCreeper entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 22 */     if (entitylivingbaseIn.getPowered())
/*    */     {
/* 24 */       boolean flag = entitylivingbaseIn.isInvisible();
/* 25 */       GlStateManager.depthMask(!flag);
/* 26 */       this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
/* 27 */       GlStateManager.matrixMode(5890);
/* 28 */       GlStateManager.loadIdentity();
/* 29 */       float f = entitylivingbaseIn.ticksExisted + partialTicks;
/* 30 */       GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
/* 31 */       GlStateManager.matrixMode(5888);
/* 32 */       GlStateManager.enableBlend();
/* 33 */       float f1 = 0.5F;
/* 34 */       GlStateManager.color(f1, f1, f1, 1.0F);
/* 35 */       GlStateManager.disableLighting();
/* 36 */       GlStateManager.blendFunc(1, 1);
/* 37 */       this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
/* 38 */       this.creeperModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/* 39 */       GlStateManager.matrixMode(5890);
/* 40 */       GlStateManager.loadIdentity();
/* 41 */       GlStateManager.matrixMode(5888);
/* 42 */       GlStateManager.enableLighting();
/* 43 */       GlStateManager.disableBlend();
/* 44 */       GlStateManager.depthMask(flag);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerCreeperCharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */