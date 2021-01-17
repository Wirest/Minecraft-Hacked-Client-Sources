// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.socket;

import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.protocol.HttpContext;

public interface ConnectionSocketFactory
{
    Socket createSocket(final HttpContext p0) throws IOException;
    
    Socket connectSocket(final int p0, final Socket p1, final HttpHost p2, final InetSocketAddress p3, final InetSocketAddress p4, final HttpContext p5) throws IOException;
}
