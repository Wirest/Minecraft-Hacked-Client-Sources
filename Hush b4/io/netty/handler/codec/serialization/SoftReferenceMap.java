// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.util.Map;

final class SoftReferenceMap<K, V> extends ReferenceMap<K, V>
{
    SoftReferenceMap(final Map<K, Reference<V>> delegate) {
        super(delegate);
    }
    
    @Override
    Reference<V> fold(final V value) {
        return new SoftReference<V>(value);
    }
}
