package net.minecraft.block.state.pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockStateHelper implements Predicate
{
    private final BlockState blockstate;
    private final Map propertyPredicates = Maps.newHashMap();

    private BlockStateHelper(BlockState blockStateIn)
    {
        this.blockstate = blockStateIn;
    }

    public static BlockStateHelper forBlock(Block blockIn)
    {
        return new BlockStateHelper(blockIn.getBlockState());
    }

    public boolean matchesState(IBlockState testState)
    {
        if (testState != null && testState.getBlock().equals(this.blockstate.getBlock()))
        {
            Iterator var2 = this.propertyPredicates.entrySet().iterator();
            Entry var3;
            Comparable var4;

            do
            {
                if (!var2.hasNext())
                {
                    return true;
                }

                var3 = (Entry)var2.next();
                var4 = testState.getValue((IProperty)var3.getKey());
            }
            while (((Predicate)var3.getValue()).apply(var4));

            return false;
        }
        else
        {
            return false;
        }
    }

    public BlockStateHelper where(IProperty property, Predicate is)
    {
        if (!this.blockstate.getProperties().contains(property))
        {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        else
        {
            this.propertyPredicates.put(property, is);
            return this;
        }
    }

    @Override
	public boolean apply(Object p_apply_1_)
    {
        return this.matchesState((IBlockState)p_apply_1_);
    }
}
