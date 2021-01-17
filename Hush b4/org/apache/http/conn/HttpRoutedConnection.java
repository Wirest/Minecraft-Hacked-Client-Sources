// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import javax.net.ssl.SSLSession;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.HttpInetConnection;

@Deprecated
public interface HttpRoutedConnection extends HttpInetConnection
{
    boolean isSecure();
    
    HttpRoute getRoute();
    
    SSLSession getSSLSession();
}
