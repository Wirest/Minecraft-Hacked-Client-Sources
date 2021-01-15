/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelWither;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderWither;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerWitherAura implements LayerRenderer<EntityWither>
/*    */ {
/* 12 */   private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
/*    */   private final RenderWither witherRenderer;
/* 14 */   private final ModelWither witherModel = new ModelWither(0.5F);
/*    */   
/*    */   public LayerWitherAura(RenderWither witherRendererIn)
/*    */   {
/* 18 */     this.witherRenderer = witherRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityWither entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 23 */     if (entitylivingbaseIn.isArmored())
/*    */     {
/* 25 */       GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
/* 26 */       this.witherRenderer.bindTexture(WITHER_ARMOR);
/* 27 */       GlStateManager.matrixMode(5890);
/* 28 */       GlStateManager.loadIdentity();
/* 29 */       float f = entitylivingbaseIn.ticksExisted + partialTicks;
/* 30 */       float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
/* 31 */       float f2 = f * 0.01F;
/* 32 */       GlStateManager.translate(f1, f2, 0.0F);
/* 33 */       GlStateManager.matrixMode(5888);
/* 34 */       GlStateManager.enableBlend();
/* 35 */       float f3 = 0.5F;
/* 36 */       GlStateManager.color(f3, f3, f3, 1.0F);
/* 37 */       GlStateManager.disableLighting();
/* 38 */       GlStateManager.blendFunc(1, 1);
/* 39 */       this.witherModel.setLivingAnimations(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
/* 40 */       this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
/* 41 */       this.witherModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/* 42 */       GlStateManager.matrixMode(5890);
/* 43 */       GlStateManager.loadIdentity();
/* 44 */       GlStateManager.matrixMode(5888);
/* 45 */       GlStateManager.enableLighting();
/* 46 */       GlStateManager.disableBlend();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerWitherAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */