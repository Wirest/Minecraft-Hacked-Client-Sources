package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public abstract interface RemovalListener<K, V> {
    public abstract void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}




