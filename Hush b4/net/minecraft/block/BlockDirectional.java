// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockDirectional extends Block
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockDirectional(final Material materialIn) {
        super(materialIn);
    }
    
    protected BlockDirectional(final Material p_i46398_1_, final MapColor p_i46398_2_) {
        super(p_i46398_1_, p_i46398_2_);
    }
}
