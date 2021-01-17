// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.conn.routing.HttpRoute;

public interface BackoffManager
{
    void backOff(final HttpRoute p0);
    
    void probe(final HttpRoute p0);
}
