package net.minecraft.optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer {
    private int attachTo = 0;
    private float scaleFactor = 0.0F;
    private ModelRenderer modelRenderer = null;

    public PlayerItemRenderer(int attachTo, float scaleFactor, ModelRenderer modelRenderer) {
        this.attachTo = attachTo;
        this.scaleFactor = scaleFactor;
        this.modelRenderer = modelRenderer;
    }

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    public void render(ModelBiped modelBiped, float scale) {
        ModelRenderer attachModel = PlayerItemModel.getAttachModel(modelBiped, attachTo);

        if (attachModel != null) {
            attachModel.postRender(scale);
        }

        modelRenderer.render(scale * scaleFactor);
    }
}
