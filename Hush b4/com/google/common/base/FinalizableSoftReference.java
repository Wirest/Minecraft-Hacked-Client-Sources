// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference
{
    protected FinalizableSoftReference(final T referent, final FinalizableReferenceQueue queue) {
        super(referent, queue.queue);
        queue.cleanUp();
    }
}
