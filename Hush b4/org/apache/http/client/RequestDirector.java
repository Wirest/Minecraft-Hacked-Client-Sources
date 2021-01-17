// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;

@Deprecated
public interface RequestDirector
{
    HttpResponse execute(final HttpHost p0, final HttpRequest p1, final HttpContext p2) throws HttpException, IOException;
}
