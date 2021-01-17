// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockRotatedPillar extends Block
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS;
    
    static {
        AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    }
    
    protected BlockRotatedPillar(final Material materialIn) {
        super(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockRotatedPillar(final Material p_i46385_1_, final MapColor p_i46385_2_) {
        super(p_i46385_1_, p_i46385_2_);
    }
}
