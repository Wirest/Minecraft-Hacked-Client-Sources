// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Function;
import java.util.concurrent.ConcurrentMap;
import com.google.common.base.Objects;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Equivalence;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Deprecated
@Beta
@GwtCompatible(emulated = true)
abstract class GenericMapMaker<K0, V0>
{
    @GwtIncompatible("To be supported")
    MapMaker.RemovalListener<K0, V0> removalListener;
    
    @GwtIncompatible("To be supported")
    abstract GenericMapMaker<K0, V0> keyEquivalence(final Equivalence<Object> p0);
    
    public abstract GenericMapMaker<K0, V0> initialCapacity(final int p0);
    
    abstract GenericMapMaker<K0, V0> maximumSize(final int p0);
    
    public abstract GenericMapMaker<K0, V0> concurrencyLevel(final int p0);
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public abstract GenericMapMaker<K0, V0> weakKeys();
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public abstract GenericMapMaker<K0, V0> weakValues();
    
    @Deprecated
    @GwtIncompatible("java.lang.ref.SoftReference")
    public abstract GenericMapMaker<K0, V0> softValues();
    
    abstract GenericMapMaker<K0, V0> expireAfterWrite(final long p0, final TimeUnit p1);
    
    @GwtIncompatible("To be supported")
    abstract GenericMapMaker<K0, V0> expireAfterAccess(final long p0, final TimeUnit p1);
    
    @GwtIncompatible("To be supported")
     <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener() {
        return Objects.firstNonNull((MapMaker.RemovalListener<K, V>)this.removalListener, (MapMaker.RemovalListener<K, V>)NullListener.INSTANCE);
    }
    
    public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeMap();
    
    @GwtIncompatible("MapMakerInternalMap")
    abstract <K, V> MapMakerInternalMap<K, V> makeCustomMap();
    
    @Deprecated
    abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeComputingMap(final Function<? super K, ? extends V> p0);
    
    @GwtIncompatible("To be supported")
    enum NullListener implements MapMaker.RemovalListener<Object, Object>
    {
        INSTANCE;
        
        @Override
        public void onRemoval(final MapMaker.RemovalNotification<Object, Object> notification) {
        }
    }
}
