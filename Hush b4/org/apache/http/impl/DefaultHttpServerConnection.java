// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.io.IOException;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;

@Deprecated
@NotThreadSafe
public class DefaultHttpServerConnection extends SocketHttpServerConnection
{
    public void bind(final Socket socket, final HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        Args.notNull(params, "HTTP parameters");
        this.assertNotOpen();
        socket.setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true));
        socket.setSoTimeout(params.getIntParameter("http.socket.timeout", 0));
        socket.setKeepAlive(params.getBooleanParameter("http.socket.keepalive", false));
        final int linger = params.getIntParameter("http.socket.linger", -1);
        if (linger >= 0) {
            socket.setSoLinger(linger > 0, linger);
        }
        if (linger >= 0) {
            socket.setSoLinger(linger > 0, linger);
        }
        super.bind(socket, params);
    }
}
