// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state;

import net.minecraft.block.Block;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;
import java.util.Collection;

public interface IBlockState
{
    Collection<IProperty> getPropertyNames();
    
     <T extends Comparable<T>> T getValue(final IProperty<T> p0);
    
     <T extends Comparable<T>, V extends T> IBlockState withProperty(final IProperty<T> p0, final V p1);
    
     <T extends Comparable<T>> IBlockState cycleProperty(final IProperty<T> p0);
    
    ImmutableMap<IProperty, Comparable> getProperties();
    
    Block getBlock();
}
