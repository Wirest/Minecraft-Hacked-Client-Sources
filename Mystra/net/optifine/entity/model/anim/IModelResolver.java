package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public interface IModelResolver extends IExpressionResolver
{
    ModelRenderer getModelRenderer(String var1);

    ModelVariableFloat getModelVariable(String var1);
}
