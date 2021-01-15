/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.RenderWolf;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ import net.minecraft.entity.passive.EntityWolf;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class LayerWolfCollar implements LayerRenderer<EntityWolf>
/*    */ {
/* 12 */   private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
/*    */   private final RenderWolf wolfRenderer;
/*    */   
/*    */   public LayerWolfCollar(RenderWolf wolfRendererIn)
/*    */   {
/* 17 */     this.wolfRenderer = wolfRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityWolf entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 22 */     if ((entitylivingbaseIn.isTamed()) && (!entitylivingbaseIn.isInvisible()))
/*    */     {
/* 24 */       this.wolfRenderer.bindTexture(WOLF_COLLAR);
/* 25 */       EnumDyeColor enumdyecolor = EnumDyeColor.byMetadata(entitylivingbaseIn.getCollarColor().getMetadata());
/* 26 */       float[] afloat = EntitySheep.func_175513_a(enumdyecolor);
/* 27 */       net.minecraft.client.renderer.GlStateManager.color(afloat[0], afloat[1], afloat[2]);
/* 28 */       this.wolfRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerWolfCollar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */