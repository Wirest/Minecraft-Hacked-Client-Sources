// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestInterceptor
{
    void process(final HttpRequest p0, final HttpContext p1) throws HttpException, IOException;
}
