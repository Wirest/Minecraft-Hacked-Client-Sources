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
class SchemeLayeredSocketFactoryAdaptor2 implements SchemeLayeredSocketFactory
{
    private final LayeredSchemeSocketFactory factory;
    
    SchemeLayeredSocketFactoryAdaptor2(final LayeredSchemeSocketFactory factory) {
        this.factory = factory;
    }
    
    public Socket createSocket(final HttpParams params) throws IOException {
        return this.factory.createSocket(params);
    }
    
    public Socket connectSocket(final Socket sock, final InetSocketAddress remoteAddress, final InetSocketAddress localAddress, final HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        return this.factory.connectSocket(sock, remoteAddress, localAddress, params);
    }
    
    public boolean isSecure(final Socket sock) throws IllegalArgumentException {
        return this.factory.isSecure(sock);
    }
    
    public Socket createLayeredSocket(final Socket socket, final String target, final int port, final HttpParams params) throws IOException, UnknownHostException {
        return this.factory.createLayeredSocket(socket, target, port, true);
    }
}
