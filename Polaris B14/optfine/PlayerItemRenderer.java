/*    */ package optfine;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ 
/*    */ public class PlayerItemRenderer
/*    */ {
/*  8 */   private int attachTo = 0;
/*  9 */   private float scaleFactor = 0.0F;
/* 10 */   private ModelRenderer modelRenderer = null;
/*    */   
/*    */   public PlayerItemRenderer(int p_i50_1_, float p_i50_2_, ModelRenderer p_i50_3_)
/*    */   {
/* 14 */     this.attachTo = p_i50_1_;
/* 15 */     this.scaleFactor = p_i50_2_;
/* 16 */     this.modelRenderer = p_i50_3_;
/*    */   }
/*    */   
/*    */   public ModelRenderer getModelRenderer()
/*    */   {
/* 21 */     return this.modelRenderer;
/*    */   }
/*    */   
/*    */   public void render(ModelBiped p_render_1_, float p_render_2_)
/*    */   {
/* 26 */     ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(p_render_1_, this.attachTo);
/*    */     
/* 28 */     if (modelrenderer != null)
/*    */     {
/* 30 */       modelrenderer.postRender(p_render_2_);
/*    */     }
/*    */     
/* 33 */     this.modelRenderer.render(p_render_2_ * this.scaleFactor);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */