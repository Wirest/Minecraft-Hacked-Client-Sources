/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.BlockModelShapes;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*    */ import net.minecraft.util.IRegistry;
/*    */ 
/*    */ public class ModelManager implements IResourceManagerReloadListener
/*    */ {
/*    */   private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
/*    */   private final TextureMap texMap;
/*    */   private final BlockModelShapes modelProvider;
/*    */   private IBakedModel defaultModel;
/*    */   
/*    */   public ModelManager(TextureMap textures)
/*    */   {
/* 18 */     this.texMap = textures;
/* 19 */     this.modelProvider = new BlockModelShapes(this);
/*    */   }
/*    */   
/*    */   public void onResourceManagerReload(IResourceManager resourceManager)
/*    */   {
/* 24 */     ModelBakery modelbakery = new ModelBakery(resourceManager, this.texMap, this.modelProvider);
/* 25 */     this.modelRegistry = modelbakery.setupModelRegistry();
/* 26 */     this.defaultModel = ((IBakedModel)this.modelRegistry.getObject(ModelBakery.MODEL_MISSING));
/* 27 */     this.modelProvider.reloadModels();
/*    */   }
/*    */   
/*    */   public IBakedModel getModel(ModelResourceLocation modelLocation)
/*    */   {
/* 32 */     if (modelLocation == null)
/*    */     {
/* 34 */       return this.defaultModel;
/*    */     }
/*    */     
/*    */ 
/* 38 */     IBakedModel ibakedmodel = (IBakedModel)this.modelRegistry.getObject(modelLocation);
/* 39 */     return ibakedmodel == null ? this.defaultModel : ibakedmodel;
/*    */   }
/*    */   
/*    */ 
/*    */   public IBakedModel getMissingModel()
/*    */   {
/* 45 */     return this.defaultModel;
/*    */   }
/*    */   
/*    */   public TextureMap getTextureMap()
/*    */   {
/* 50 */     return this.texMap;
/*    */   }
/*    */   
/*    */   public BlockModelShapes getBlockModelShapes()
/*    */   {
/* 55 */     return this.modelProvider;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\ModelManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */