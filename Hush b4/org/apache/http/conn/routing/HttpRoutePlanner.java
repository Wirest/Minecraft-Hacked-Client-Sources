// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.routing;

import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;

public interface HttpRoutePlanner
{
    HttpRoute determineRoute(final HttpHost p0, final HttpRequest p1, final HttpContext p2) throws HttpException;
}
