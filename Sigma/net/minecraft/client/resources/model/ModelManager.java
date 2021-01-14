package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IRegistry;

public class ModelManager implements IResourceManagerReloadListener {
    private IRegistry modelRegistry;
    private final TextureMap field_174956_b;
    private final BlockModelShapes field_174957_c;
    private IBakedModel defaultModel;
    private static final String __OBFID = "CL_00002388";

    public ModelManager(TextureMap p_i46082_1_) {
        this.field_174956_b = p_i46082_1_;
        this.field_174957_c = new BlockModelShapes(this);
    }

    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        ModelBakery var2 = new ModelBakery(p_110549_1_, this.field_174956_b, this.field_174957_c);
        this.modelRegistry = var2.setupModelRegistry();
        this.defaultModel = (IBakedModel) this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
        this.field_174957_c.func_178124_c();
    }

    public IBakedModel getModel(ModelResourceLocation p_174953_1_) {
        if (p_174953_1_ == null) {
            return this.defaultModel;
        } else {
            IBakedModel var2 = (IBakedModel) this.modelRegistry.getObject(p_174953_1_);
            return var2 == null ? this.defaultModel : var2;
        }
    }

    public IBakedModel getMissingModel() {
        return this.defaultModel;
    }

    public TextureMap func_174952_b() {
        return this.field_174956_b;
    }

    public BlockModelShapes getBlockModelShapes() {
        return this.field_174957_c;
    }
}
