// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.io.Serializable;

public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable
{
    private final ImmutableMap<Class<? extends B>, B> delegate;
    
    public static <B> Builder<B> builder() {
        return new Builder<B>();
    }
    
    public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(final Map<? extends Class<? extends S>, ? extends S> map) {
        if (map instanceof ImmutableClassToInstanceMap) {
            final ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap<B>)(ImmutableClassToInstanceMap)map;
            return cast;
        }
        return new Builder<B>().putAll((Map<? extends Class<?>, ?>)map).build();
    }
    
    private ImmutableClassToInstanceMap(final ImmutableMap<Class<? extends B>, B> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }
    
    @Nullable
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return (T)this.delegate.get(Preconditions.checkNotNull(type));
    }
    
    @Deprecated
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    public static final class Builder<B>
    {
        private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder;
        
        public Builder() {
            this.mapBuilder = ImmutableMap.builder();
        }
        
        public <T extends B> Builder<B> put(final Class<T> key, final T value) {
            this.mapBuilder.put((Class<? extends B>)key, value);
            return this;
        }
        
        public <T extends B> Builder<B> putAll(final Map<? extends Class<? extends T>, ? extends T> map) {
            for (final Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
                final Class<? extends T> type = (Class<? extends T>)entry.getKey();
                final T value = (T)entry.getValue();
                this.mapBuilder.put((Class<? extends B>)type, cast((Class<B>)type, value));
            }
            return this;
        }
        
        private static <B, T extends B> T cast(final Class<T> type, final B value) {
            return Primitives.wrap(type).cast(value);
        }
        
        public ImmutableClassToInstanceMap<B> build() {
            return new ImmutableClassToInstanceMap<B>(this.mapBuilder.build(), null);
        }
    }
}
