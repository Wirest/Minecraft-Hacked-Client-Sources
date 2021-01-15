package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BuiltInModel implements IBakedModel
{
    private ItemCameraTransforms field_177557_a;
    private static final String __OBFID = "CL_00002392";

    public BuiltInModel(ItemCameraTransforms p_i46086_1_)
    {
        this.field_177557_a = p_i46086_1_;
    }

    public List func_177551_a(EnumFacing p_177551_1_)
    {
        return null;
    }

    public List func_177550_a()
    {
        return null;
    }

    public boolean isGui3d()
    {
        return false;
    }

    public boolean isAmbientOcclusionEnabled()
    {
        return true;
    }

    public boolean isBuiltInRenderer()
    {
        return true;
    }

    public TextureAtlasSprite getTexture()
    {
        return null;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.field_177557_a;
    }
}
