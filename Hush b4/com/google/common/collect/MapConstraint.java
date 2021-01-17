// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public interface MapConstraint<K, V>
{
    void checkKeyValue(@Nullable final K p0, @Nullable final V p1);
    
    String toString();
}
