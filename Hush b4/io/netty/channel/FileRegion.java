// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import io.netty.util.ReferenceCounted;

public interface FileRegion extends ReferenceCounted
{
    long position();
    
    long transfered();
    
    long count();
    
    long transferTo(final WritableByteChannel p0, final long p1) throws IOException;
}
