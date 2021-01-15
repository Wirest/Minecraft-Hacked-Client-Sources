/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelSheep1;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderSheep;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerSheepWool implements LayerRenderer<EntitySheep>
/*    */ {
/* 12 */   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
/*    */   private final RenderSheep sheepRenderer;
/* 14 */   private final ModelSheep1 sheepModel = new ModelSheep1();
/*    */   
/*    */   public LayerSheepWool(RenderSheep sheepRendererIn)
/*    */   {
/* 18 */     this.sheepRenderer = sheepRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntitySheep entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 23 */     if ((!entitylivingbaseIn.getSheared()) && (!entitylivingbaseIn.isInvisible()))
/*    */     {
/* 25 */       this.sheepRenderer.bindTexture(TEXTURE);
/*    */       
/* 27 */       if ((entitylivingbaseIn.hasCustomName()) && ("jeb_".equals(entitylivingbaseIn.getCustomNameTag())))
/*    */       {
/* 29 */         int i1 = 25;
/* 30 */         int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
/* 31 */         int j = EnumDyeColor.values().length;
/* 32 */         int k = i % j;
/* 33 */         int l = (i + 1) % j;
/* 34 */         float f = (entitylivingbaseIn.ticksExisted % 25 + partialTicks) / 25.0F;
/* 35 */         float[] afloat1 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(k));
/* 36 */         float[] afloat2 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(l));
/* 37 */         GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
/*    */       }
/*    */       else
/*    */       {
/* 41 */         float[] afloat = EntitySheep.func_175513_a(entitylivingbaseIn.getFleeceColor());
/* 42 */         GlStateManager.color(afloat[0], afloat[1], afloat[2]);
/*    */       }
/*    */       
/* 45 */       this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
/* 46 */       this.sheepModel.setLivingAnimations(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
/* 47 */       this.sheepModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerSheepWool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */