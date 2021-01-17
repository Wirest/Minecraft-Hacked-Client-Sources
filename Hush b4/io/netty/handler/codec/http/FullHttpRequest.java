// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

public interface FullHttpRequest extends HttpRequest, FullHttpMessage
{
    FullHttpRequest copy();
    
    FullHttpRequest retain(final int p0);
    
    FullHttpRequest retain();
    
    FullHttpRequest setProtocolVersion(final HttpVersion p0);
    
    FullHttpRequest setMethod(final HttpMethod p0);
    
    FullHttpRequest setUri(final String p0);
}
