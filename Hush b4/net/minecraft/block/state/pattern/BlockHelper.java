// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class BlockHelper implements Predicate<IBlockState>
{
    private final Block block;
    
    private BlockHelper(final Block blockType) {
        this.block = blockType;
    }
    
    public static BlockHelper forBlock(final Block blockType) {
        return new BlockHelper(blockType);
    }
    
    @Override
    public boolean apply(final IBlockState p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getBlock() == this.block;
    }
}
