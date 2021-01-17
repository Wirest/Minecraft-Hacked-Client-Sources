// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.pool;

import org.apache.http.annotation.Immutable;

@Immutable
public class PoolStats
{
    private final int leased;
    private final int pending;
    private final int available;
    private final int max;
    
    public PoolStats(final int leased, final int pending, final int free, final int max) {
        this.leased = leased;
        this.pending = pending;
        this.available = free;
        this.max = max;
    }
    
    public int getLeased() {
        return this.leased;
    }
    
    public int getPending() {
        return this.pending;
    }
    
    public int getAvailable() {
        return this.available;
    }
    
    public int getMax() {
        return this.max;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[leased: ");
        buffer.append(this.leased);
        buffer.append("; pending: ");
        buffer.append(this.pending);
        buffer.append("; available: ");
        buffer.append(this.available);
        buffer.append("; max: ");
        buffer.append(this.max);
        buffer.append("]");
        return buffer.toString();
    }
}
