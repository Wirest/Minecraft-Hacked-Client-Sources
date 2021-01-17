// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.scheme;

import org.apache.http.conn.ConnectTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.params.HttpParams;
import java.net.InetAddress;
import java.io.IOException;
import java.net.Socket;

@Deprecated
public interface SocketFactory
{
    Socket createSocket() throws IOException;
    
    Socket connectSocket(final Socket p0, final String p1, final int p2, final InetAddress p3, final int p4, final HttpParams p5) throws IOException, UnknownHostException, ConnectTimeoutException;
    
    boolean isSecure(final Socket p0) throws IllegalArgumentException;
}
