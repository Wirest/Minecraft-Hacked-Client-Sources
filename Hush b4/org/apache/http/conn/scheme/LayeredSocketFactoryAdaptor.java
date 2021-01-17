// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.scheme;

import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Socket;

@Deprecated
class LayeredSocketFactoryAdaptor extends SocketFactoryAdaptor implements LayeredSocketFactory
{
    private final LayeredSchemeSocketFactory factory;
    
    LayeredSocketFactoryAdaptor(final LayeredSchemeSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }
    
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException, UnknownHostException {
        return this.factory.createLayeredSocket(socket, host, port, autoClose);
    }
}
