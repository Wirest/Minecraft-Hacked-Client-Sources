package net.minecraft.client.resources.model;

import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BuiltInModel implements IBakedModel
{
    private ItemCameraTransforms cameraTransforms;

    public BuiltInModel(ItemCameraTransforms p_i46086_1_)
    {
        this.cameraTransforms = p_i46086_1_;
    }

    @Override
	public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return null;
    }

    @Override
	public List getGeneralQuads()
    {
        return null;
    }

    @Override
	public boolean isAmbientOcclusion()
    {
        return false;
    }

    @Override
	public boolean isGui3d()
    {
        return true;
    }

    @Override
	public boolean isBuiltInRenderer()
    {
        return true;
    }

    @Override
	public TextureAtlasSprite getTexture()
    {
        return null;
    }

    @Override
	public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }
}
