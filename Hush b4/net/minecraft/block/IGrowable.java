// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IGrowable
{
    boolean canGrow(final World p0, final BlockPos p1, final IBlockState p2, final boolean p3);
    
    boolean canUseBonemeal(final World p0, final Random p1, final BlockPos p2, final IBlockState p3);
    
    void grow(final World p0, final Random p1, final BlockPos p2, final IBlockState p3);
}
