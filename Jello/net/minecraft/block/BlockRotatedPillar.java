package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public abstract class BlockRotatedPillar extends Block
{
    public static final PropertyEnum field_176298_M = PropertyEnum.create("axis", EnumFacing.Axis.class);
    

    protected BlockRotatedPillar(Material p_i45425_1_)
    {
        super(p_i45425_1_);
    }
}
