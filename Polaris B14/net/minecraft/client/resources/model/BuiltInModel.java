/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*    */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public class BuiltInModel implements IBakedModel
/*    */ {
/*    */   private ItemCameraTransforms cameraTransforms;
/*    */   
/*    */   public BuiltInModel(ItemCameraTransforms p_i46086_1_)
/*    */   {
/* 15 */     this.cameraTransforms = p_i46086_1_;
/*    */   }
/*    */   
/*    */   public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_)
/*    */   {
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public List<BakedQuad> getGeneralQuads()
/*    */   {
/* 25 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isAmbientOcclusion()
/*    */   {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGui3d()
/*    */   {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isBuiltInRenderer()
/*    */   {
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite getParticleTexture()
/*    */   {
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public ItemCameraTransforms getItemCameraTransforms()
/*    */   {
/* 50 */     return this.cameraTransforms;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\BuiltInModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */