// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

public interface FullHttpMessage extends HttpMessage, LastHttpContent
{
    FullHttpMessage copy();
    
    FullHttpMessage retain(final int p0);
    
    FullHttpMessage retain();
}
