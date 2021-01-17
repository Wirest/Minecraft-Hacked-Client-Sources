// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import org.apache.http.HttpRequest;

public interface HttpRequestHandlerMapper
{
    HttpRequestHandler lookup(final HttpRequest p0);
}
