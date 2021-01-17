// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty<T extends Comparable<T>>
{
    String getName();
    
    Collection<T> getAllowedValues();
    
    Class<T> getValueClass();
    
    String getName(final T p0);
}
