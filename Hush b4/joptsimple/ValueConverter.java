// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

public interface ValueConverter<V>
{
    V convert(final String p0);
    
    Class<V> valueType();
    
    String valuePattern();
}
