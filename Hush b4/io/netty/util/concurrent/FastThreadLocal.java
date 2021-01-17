// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadLocal<V>
{
    private static final int variablesToRemoveIndex;
    private final int index;
    
    public static void removeAll() {
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return;
        }
        try {
            final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
            if (v != null && v != InternalThreadLocalMap.UNSET) {
                final Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
                final FastThreadLocal[] arr$;
                final FastThreadLocal<?>[] variablesToRemoveArray = (FastThreadLocal<?>[])(arr$ = variablesToRemove.toArray(new FastThreadLocal[variablesToRemove.size()]));
                for (final FastThreadLocal<?> tlv : arr$) {
                    tlv.remove(threadLocalMap);
                }
            }
        }
        finally {
            InternalThreadLocalMap.remove();
        }
    }
    
    public static int size() {
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return 0;
        }
        return threadLocalMap.size();
    }
    
    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }
    
    private static void addToVariablesToRemove(final InternalThreadLocalMap threadLocalMap, final FastThreadLocal<?> variable) {
        final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
        Set<FastThreadLocal<?>> variablesToRemove;
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            variablesToRemove = Collections.newSetFromMap(new IdentityHashMap<FastThreadLocal<?>, Boolean>());
            threadLocalMap.setIndexedVariable(FastThreadLocal.variablesToRemoveIndex, variablesToRemove);
        }
        else {
            variablesToRemove = (Set<FastThreadLocal<?>>)v;
        }
        variablesToRemove.add(variable);
    }
    
    private static void removeFromVariablesToRemove(final InternalThreadLocalMap threadLocalMap, final FastThreadLocal<?> variable) {
        final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            return;
        }
        final Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
        variablesToRemove.remove(variable);
    }
    
    public FastThreadLocal() {
        this.index = InternalThreadLocalMap.nextVariableIndex();
    }
    
    public final V get() {
        return this.get(InternalThreadLocalMap.get());
    }
    
    public final V get(final InternalThreadLocalMap threadLocalMap) {
        final Object v = threadLocalMap.indexedVariable(this.index);
        if (v != InternalThreadLocalMap.UNSET) {
            return (V)v;
        }
        return this.initialize(threadLocalMap);
    }
    
    private V initialize(final InternalThreadLocalMap threadLocalMap) {
        V v = null;
        try {
            v = this.initialValue();
        }
        catch (Exception e) {
            PlatformDependent.throwException(e);
        }
        threadLocalMap.setIndexedVariable(this.index, v);
        addToVariablesToRemove(threadLocalMap, this);
        return v;
    }
    
    public final void set(final V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            this.set(InternalThreadLocalMap.get(), value);
        }
        else {
            this.remove();
        }
    }
    
    public final void set(final InternalThreadLocalMap threadLocalMap, final V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            if (threadLocalMap.setIndexedVariable(this.index, value)) {
                addToVariablesToRemove(threadLocalMap, this);
            }
        }
        else {
            this.remove(threadLocalMap);
        }
    }
    
    public final boolean isSet() {
        return this.isSet(InternalThreadLocalMap.getIfSet());
    }
    
    public final boolean isSet(final InternalThreadLocalMap threadLocalMap) {
        return threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index);
    }
    
    public final void remove() {
        this.remove(InternalThreadLocalMap.getIfSet());
    }
    
    public final void remove(final InternalThreadLocalMap threadLocalMap) {
        if (threadLocalMap == null) {
            return;
        }
        final Object v = threadLocalMap.removeIndexedVariable(this.index);
        removeFromVariablesToRemove(threadLocalMap, this);
        if (v != InternalThreadLocalMap.UNSET) {
            try {
                this.onRemoval(v);
            }
            catch (Exception e) {
                PlatformDependent.throwException(e);
            }
        }
    }
    
    protected V initialValue() throws Exception {
        return null;
    }
    
    protected void onRemoval(final V value) throws Exception {
    }
    
    static {
        variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
    }
}
