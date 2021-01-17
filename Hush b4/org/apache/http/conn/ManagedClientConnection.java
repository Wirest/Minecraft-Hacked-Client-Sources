// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import javax.net.ssl.SSLSession;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.HttpClientConnection;

@Deprecated
public interface ManagedClientConnection extends HttpClientConnection, HttpRoutedConnection, ManagedHttpClientConnection, ConnectionReleaseTrigger
{
    boolean isSecure();
    
    HttpRoute getRoute();
    
    SSLSession getSSLSession();
    
    void open(final HttpRoute p0, final HttpContext p1, final HttpParams p2) throws IOException;
    
    void tunnelTarget(final boolean p0, final HttpParams p1) throws IOException;
    
    void tunnelProxy(final HttpHost p0, final boolean p1, final HttpParams p2) throws IOException;
    
    void layerProtocol(final HttpContext p0, final HttpParams p1) throws IOException;
    
    void markReusable();
    
    void unmarkReusable();
    
    boolean isMarkedReusable();
    
    void setState(final Object p0);
    
    Object getState();
    
    void setIdleDuration(final long p0, final TimeUnit p1);
}
