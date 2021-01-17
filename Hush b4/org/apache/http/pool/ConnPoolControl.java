// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

public interface ConnPoolControl<T>
{
    void setMaxTotal(final int p0);
    
    int getMaxTotal();
    
    void setDefaultMaxPerRoute(final int p0);
    
    int getDefaultMaxPerRoute();
    
    void setMaxPerRoute(final T p0, final int p1);
    
    int getMaxPerRoute(final T p0);
    
    PoolStats getTotalStats();
    
    PoolStats getStats(final T p0);
}
