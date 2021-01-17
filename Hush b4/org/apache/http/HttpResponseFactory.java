// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import org.apache.http.protocol.HttpContext;

public interface HttpResponseFactory
{
    HttpResponse newHttpResponse(final ProtocolVersion p0, final int p1, final HttpContext p2);
    
    HttpResponse newHttpResponse(final StatusLine p0, final HttpContext p1);
}
