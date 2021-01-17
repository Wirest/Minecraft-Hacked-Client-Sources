// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockCompressedPowered extends Block
{
    public BlockCompressedPowered(final Material p_i46386_1_, final MapColor p_i46386_2_) {
        super(p_i46386_1_, p_i46386_2_);
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return 15;
    }
}
