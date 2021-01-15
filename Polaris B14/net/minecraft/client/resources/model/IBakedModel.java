package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public abstract interface IBakedModel
{
  public abstract List<BakedQuad> getFaceQuads(EnumFacing paramEnumFacing);
  
  public abstract List<BakedQuad> getGeneralQuads();
  
  public abstract boolean isAmbientOcclusion();
  
  public abstract boolean isGui3d();
  
  public abstract boolean isBuiltInRenderer();
  
  public abstract TextureAtlasSprite getParticleTexture();
  
  public abstract ItemCameraTransforms getItemCameraTransforms();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\IBakedModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */