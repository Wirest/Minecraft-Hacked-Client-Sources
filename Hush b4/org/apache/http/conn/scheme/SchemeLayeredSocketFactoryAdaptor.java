// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.scheme;

import java.net.UnknownHostException;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import java.net.Socket;

@Deprecated
class SchemeLayeredSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor implements SchemeLayeredSocketFactory
{
    private final LayeredSocketFactory factory;
    
    SchemeLayeredSocketFactoryAdaptor(final LayeredSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }
    
    public Socket createLayeredSocket(final Socket socket, final String target, final int port, final HttpParams params) throws IOException, UnknownHostException {
        return this.factory.createSocket(socket, target, port, true);
    }
}
