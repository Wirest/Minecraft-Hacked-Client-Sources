// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.params;

import org.apache.http.conn.routing.HttpRoute;

@Deprecated
public interface ConnPerRoute
{
    int getMaxForRoute(final HttpRoute p0);
}
