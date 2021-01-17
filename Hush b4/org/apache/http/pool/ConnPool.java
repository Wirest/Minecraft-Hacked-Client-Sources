// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;

public interface ConnPool<T, E>
{
    Future<E> lease(final T p0, final Object p1, final FutureCallback<E> p2);
    
    void release(final E p0, final boolean p1);
}
