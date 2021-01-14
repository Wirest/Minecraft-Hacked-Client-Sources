package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
   List getFaceQuads(EnumFacing var1);

   List getGeneralQuads();

   boolean isAmbientOcclusion();

   boolean isGui3d();

   boolean isBuiltInRenderer();

   TextureAtlasSprite getParticleTexture();

   ItemCameraTransforms getItemCameraTransforms();
}
