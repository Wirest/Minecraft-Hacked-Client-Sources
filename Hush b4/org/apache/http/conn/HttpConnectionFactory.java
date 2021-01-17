// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.HttpConnection;

public interface HttpConnectionFactory<T, C extends HttpConnection>
{
    C create(final T p0, final ConnectionConfig p1);
}
