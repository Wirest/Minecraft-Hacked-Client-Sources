// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

public interface FullHttpResponse extends HttpResponse, FullHttpMessage
{
    FullHttpResponse copy();
    
    FullHttpResponse retain(final int p0);
    
    FullHttpResponse retain();
    
    FullHttpResponse setProtocolVersion(final HttpVersion p0);
    
    FullHttpResponse setStatus(final HttpResponseStatus p0);
}
