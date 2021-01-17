// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;

public interface ByteBufHolder extends ReferenceCounted
{
    ByteBuf content();
    
    ByteBufHolder copy();
    
    ByteBufHolder duplicate();
    
    ByteBufHolder retain();
    
    ByteBufHolder retain(final int p0);
}
