// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public abstract class WorldGenerator
{
    private final boolean doBlockNotify;
    
    public WorldGenerator() {
        this(false);
    }
    
    public WorldGenerator(final boolean notify) {
        this.doBlockNotify = notify;
    }
    
    public abstract boolean generate(final World p0, final Random p1, final BlockPos p2);
    
    public void func_175904_e() {
    }
    
    protected void setBlockAndNotifyAdequately(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.doBlockNotify) {
            worldIn.setBlockState(pos, state, 3);
        }
        else {
            worldIn.setBlockState(pos, state, 2);
        }
    }
}
