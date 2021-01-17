// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer
{
    private int attachTo;
    private float scaleFactor;
    private ModelRenderer modelRenderer;
    
    public PlayerItemRenderer(final int p_i75_1_, final float p_i75_2_, final ModelRenderer p_i75_3_) {
        this.attachTo = 0;
        this.scaleFactor = 0.0f;
        this.modelRenderer = null;
        this.attachTo = p_i75_1_;
        this.scaleFactor = p_i75_2_;
        this.modelRenderer = p_i75_3_;
    }
    
    public ModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }
    
    public void render(final ModelBiped p_render_1_, final float p_render_2_) {
        final ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(p_render_1_, this.attachTo);
        if (modelrenderer != null) {
            modelrenderer.postRender(p_render_2_);
        }
        this.modelRenderer.render(p_render_2_ * this.scaleFactor);
    }
}
