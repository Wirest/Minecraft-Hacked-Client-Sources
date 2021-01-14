package net.minecraft.client.resources.model;

import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
    List func_177551_a(EnumFacing var1);

    List func_177550_a();

    boolean isGui3d();

    boolean isAmbientOcclusionEnabled();

    boolean isBuiltInRenderer();

    TextureAtlasSprite getTexture();

    ItemCameraTransforms getItemCameraTransforms();
}
