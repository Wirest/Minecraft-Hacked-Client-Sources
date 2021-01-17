// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public interface MessageSizeEstimator
{
    Handle newHandle();
    
    public interface Handle
    {
        int size(final Object p0);
    }
}
