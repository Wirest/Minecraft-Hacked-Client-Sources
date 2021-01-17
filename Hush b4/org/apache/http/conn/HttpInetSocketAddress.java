// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import org.apache.http.util.Args;
import java.net.InetAddress;
import org.apache.http.HttpHost;
import java.net.InetSocketAddress;

@Deprecated
public class HttpInetSocketAddress extends InetSocketAddress
{
    private static final long serialVersionUID = -6650701828361907957L;
    private final HttpHost httphost;
    
    public HttpInetSocketAddress(final HttpHost httphost, final InetAddress addr, final int port) {
        super(addr, port);
        Args.notNull(httphost, "HTTP host");
        this.httphost = httphost;
    }
    
    public HttpHost getHttpHost() {
        return this.httphost;
    }
    
    @Override
    public String toString() {
        return this.httphost.getHostName() + ":" + this.getPort();
    }
}
