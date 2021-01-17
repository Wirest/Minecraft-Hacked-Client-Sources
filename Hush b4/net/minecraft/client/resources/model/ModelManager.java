// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IRegistry;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class ModelManager implements IResourceManagerReloadListener
{
    private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    private final TextureMap texMap;
    private final BlockModelShapes modelProvider;
    private IBakedModel defaultModel;
    
    public ModelManager(final TextureMap textures) {
        this.texMap = textures;
        this.modelProvider = new BlockModelShapes(this);
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        final ModelBakery modelbakery = new ModelBakery(resourceManager, this.texMap, this.modelProvider);
        this.modelRegistry = modelbakery.setupModelRegistry();
        this.defaultModel = this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
        this.modelProvider.reloadModels();
    }
    
    public IBakedModel getModel(final ModelResourceLocation modelLocation) {
        if (modelLocation == null) {
            return this.defaultModel;
        }
        final IBakedModel ibakedmodel = this.modelRegistry.getObject(modelLocation);
        return (ibakedmodel == null) ? this.defaultModel : ibakedmodel;
    }
    
    public IBakedModel getMissingModel() {
        return this.defaultModel;
    }
    
    public TextureMap getTextureMap() {
        return this.texMap;
    }
    
    public BlockModelShapes getBlockModelShapes() {
        return this.modelProvider;
    }
}
