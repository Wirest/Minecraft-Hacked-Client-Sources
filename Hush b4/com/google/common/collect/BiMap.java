// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V>
{
    V put(@Nullable final K p0, @Nullable final V p1);
    
    V forcePut(@Nullable final K p0, @Nullable final V p1);
    
    void putAll(final Map<? extends K, ? extends V> p0);
    
    Set<V> values();
    
    BiMap<V, K> inverse();
}
