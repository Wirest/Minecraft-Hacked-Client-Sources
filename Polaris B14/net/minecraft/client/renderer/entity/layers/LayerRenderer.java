package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;

public abstract interface LayerRenderer<E extends EntityLivingBase>
{
  public abstract void doRenderLayer(E paramE, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
  
  public abstract boolean shouldCombineTextures();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */