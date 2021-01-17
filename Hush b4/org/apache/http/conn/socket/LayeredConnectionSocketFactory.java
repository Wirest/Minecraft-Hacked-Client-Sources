// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.socket;

import java.net.UnknownHostException;
import java.io.IOException;
import org.apache.http.protocol.HttpContext;
import java.net.Socket;

public interface LayeredConnectionSocketFactory extends ConnectionSocketFactory
{
    Socket createLayeredSocket(final Socket p0, final String p1, final int p2, final HttpContext p3) throws IOException, UnknownHostException;
}
