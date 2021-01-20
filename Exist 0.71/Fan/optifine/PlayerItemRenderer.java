package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer
{
    private int attachTo = 0;
    private float scaleFactor = 0.0F;
    private ModelRenderer modelRenderer = null;

    public PlayerItemRenderer(int p_i75_1_, float p_i75_2_, ModelRenderer p_i75_3_)
    {
        this.attachTo = p_i75_1_;
        this.scaleFactor = p_i75_2_;
        this.modelRenderer = p_i75_3_;
    }

    public ModelRenderer getModelRenderer()
    {
        return this.modelRenderer;
    }

    public void render(ModelBiped p_render_1_, float p_render_2_)
    {
        ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(p_render_1_, this.attachTo);

        if (modelrenderer != null)
        {
            modelrenderer.postRender(p_render_2_);
        }

        this.modelRenderer.render(p_render_2_ * this.scaleFactor);
    }
}
