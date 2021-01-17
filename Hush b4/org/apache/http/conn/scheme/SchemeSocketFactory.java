// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.scheme;

import org.apache.http.conn.ConnectTimeoutException;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.params.HttpParams;

@Deprecated
public interface SchemeSocketFactory
{
    Socket createSocket(final HttpParams p0) throws IOException;
    
    Socket connectSocket(final Socket p0, final InetSocketAddress p1, final InetSocketAddress p2, final HttpParams p3) throws IOException, UnknownHostException, ConnectTimeoutException;
    
    boolean isSecure(final Socket p0) throws IllegalArgumentException;
}
