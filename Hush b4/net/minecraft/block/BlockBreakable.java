// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBreakable extends Block
{
    private boolean ignoreSimilarity;
    
    protected BlockBreakable(final Material materialIn, final boolean ignoreSimilarityIn) {
        this(materialIn, ignoreSimilarityIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockBreakable(final Material p_i46393_1_, final boolean p_i46393_2_, final MapColor p_i46393_3_) {
        super(p_i46393_1_, p_i46393_3_);
        this.ignoreSimilarity = p_i46393_2_;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (this == Blocks.glass || this == Blocks.stained_glass) {
            if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate) {
                return true;
            }
            if (block == this) {
                return false;
            }
        }
        return (this.ignoreSimilarity || block != this) && super.shouldSideBeRendered(worldIn, pos, side);
    }
}
