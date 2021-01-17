// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public final class Atomics
{
    private Atomics() {
    }
    
    public static <V> AtomicReference<V> newReference() {
        return new AtomicReference<V>();
    }
    
    public static <V> AtomicReference<V> newReference(@Nullable final V initialValue) {
        return new AtomicReference<V>(initialValue);
    }
    
    public static <E> AtomicReferenceArray<E> newReferenceArray(final int length) {
        return new AtomicReferenceArray<E>(length);
    }
    
    public static <E> AtomicReferenceArray<E> newReferenceArray(final E[] array) {
        return new AtomicReferenceArray<E>(array);
    }
}
