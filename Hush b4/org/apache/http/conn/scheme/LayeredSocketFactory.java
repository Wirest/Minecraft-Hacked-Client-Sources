// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.scheme;

import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Socket;

@Deprecated
public interface LayeredSocketFactory extends SocketFactory
{
    Socket createSocket(final Socket p0, final String p1, final int p2, final boolean p3) throws IOException, UnknownHostException;
}
