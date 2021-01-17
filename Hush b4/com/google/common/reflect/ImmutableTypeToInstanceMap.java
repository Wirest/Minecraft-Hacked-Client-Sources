// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;

@Beta
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B>
{
    private final ImmutableMap<TypeToken<? extends B>, B> delegate;
    
    public static <B> ImmutableTypeToInstanceMap<B> of() {
        return new ImmutableTypeToInstanceMap<B>(ImmutableMap.of());
    }
    
    public static <B> Builder<B> builder() {
        return new Builder<B>();
    }
    
    private ImmutableTypeToInstanceMap(final ImmutableMap<TypeToken<? extends B>, B> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public <T extends B> T getInstance(final TypeToken<T> type) {
        return (T)this.trustedGet((TypeToken<Object>)type.rejectTypeVariables());
    }
    
    @Override
    public <T extends B> T putInstance(final TypeToken<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return this.trustedGet((TypeToken<T>)TypeToken.of((Class<T>)type));
    }
    
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Map<TypeToken<? extends B>, B> delegate() {
        return this.delegate;
    }
    
    private <T extends B> T trustedGet(final TypeToken<T> type) {
        return (T)this.delegate.get(type);
    }
    
    @Beta
    public static final class Builder<B>
    {
        private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder;
        
        private Builder() {
            this.mapBuilder = ImmutableMap.builder();
        }
        
        public <T extends B> Builder<B> put(final Class<T> key, final T value) {
            this.mapBuilder.put(TypeToken.of((Class<? extends B>)key), value);
            return this;
        }
        
        public <T extends B> Builder<B> put(final TypeToken<T> key, final T value) {
            this.mapBuilder.put((TypeToken<? extends B>)key.rejectTypeVariables(), value);
            return this;
        }
        
        public ImmutableTypeToInstanceMap<B> build() {
            return new ImmutableTypeToInstanceMap<B>(this.mapBuilder.build(), null);
        }
    }
}
