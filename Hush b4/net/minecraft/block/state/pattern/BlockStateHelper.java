// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import java.util.Iterator;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class BlockStateHelper implements Predicate<IBlockState>
{
    private final BlockState blockstate;
    private final Map<IProperty, Predicate> propertyPredicates;
    
    private BlockStateHelper(final BlockState blockStateIn) {
        this.propertyPredicates = (Map<IProperty, Predicate>)Maps.newHashMap();
        this.blockstate = blockStateIn;
    }
    
    public static BlockStateHelper forBlock(final Block blockIn) {
        return new BlockStateHelper(blockIn.getBlockState());
    }
    
    @Override
    public boolean apply(final IBlockState p_apply_1_) {
        if (p_apply_1_ != null && p_apply_1_.getBlock().equals(this.blockstate.getBlock())) {
            for (final Map.Entry<IProperty, Predicate> entry : this.propertyPredicates.entrySet()) {
                final Object object = p_apply_1_.getValue(entry.getKey());
                if (!entry.getValue().apply(object)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public <V extends Comparable<V>> BlockStateHelper where(final IProperty<V> property, final Predicate<? extends V> is) {
        if (!this.blockstate.getProperties().contains(property)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        this.propertyPredicates.put(property, is);
        return this;
    }
}
