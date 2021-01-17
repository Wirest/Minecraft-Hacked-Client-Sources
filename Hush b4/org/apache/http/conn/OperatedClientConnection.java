// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import org.apache.http.params.HttpParams;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpClientConnection;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection
{
    HttpHost getTargetHost();
    
    boolean isSecure();
    
    Socket getSocket();
    
    void opening(final Socket p0, final HttpHost p1) throws IOException;
    
    void openCompleted(final boolean p0, final HttpParams p1) throws IOException;
    
    void update(final Socket p0, final HttpHost p1, final boolean p2, final HttpParams p3) throws IOException;
}
