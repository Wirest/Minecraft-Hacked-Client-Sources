package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState {
    /**
     * Get the names of all properties defined for this BlockState
     */
    Collection getPropertyNames();

    /**
     * Get the value of the given Property for this BlockState
     */
    Comparable getValue(IProperty var1);

    /**
     * Get a version of this BlockState with the given Property now set to the given value
     */
    IBlockState withProperty(IProperty var1, Comparable var2);

    /**
     * Create a version of this BlockState with the given property cycled to the next value in order. If the property
     * was at the highest possible value, it is set to the lowest one instead.
     */
    IBlockState cycleProperty(IProperty var1);

    /**
     * Get all properties of this BlockState. The returned Map maps from properties (IProperty) to the respective
     * current value.
     */
    ImmutableMap getProperties();

    Block getBlock();
}
