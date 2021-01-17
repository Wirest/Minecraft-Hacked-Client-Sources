// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.pool;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import org.apache.http.impl.DefaultBHttpClientConnection;
import java.net.Socket;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.config.SocketConfig;
import javax.net.ssl.SSLSocketFactory;
import javax.net.SocketFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.pool.ConnFactory;

@Immutable
public class BasicConnFactory implements ConnFactory<HttpHost, HttpClientConnection>
{
    private final SocketFactory plainfactory;
    private final SSLSocketFactory sslfactory;
    private final int connectTimeout;
    private final SocketConfig sconfig;
    private final HttpConnectionFactory<? extends HttpClientConnection> connFactory;
    
    @Deprecated
    public BasicConnFactory(final SSLSocketFactory sslfactory, final HttpParams params) {
        Args.notNull(params, "HTTP params");
        this.plainfactory = null;
        this.sslfactory = sslfactory;
        this.connectTimeout = params.getIntParameter("http.connection.timeout", 0);
        this.sconfig = HttpParamConfig.getSocketConfig(params);
        this.connFactory = new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(params));
    }
    
    @Deprecated
    public BasicConnFactory(final HttpParams params) {
        this(null, params);
    }
    
    public BasicConnFactory(final SocketFactory plainfactory, final SSLSocketFactory sslfactory, final int connectTimeout, final SocketConfig sconfig, final ConnectionConfig cconfig) {
        this.plainfactory = plainfactory;
        this.sslfactory = sslfactory;
        this.connectTimeout = connectTimeout;
        this.sconfig = ((sconfig != null) ? sconfig : SocketConfig.DEFAULT);
        this.connFactory = new DefaultBHttpClientConnectionFactory((cconfig != null) ? cconfig : ConnectionConfig.DEFAULT);
    }
    
    public BasicConnFactory(final int connectTimeout, final SocketConfig sconfig, final ConnectionConfig cconfig) {
        this(null, null, connectTimeout, sconfig, cconfig);
    }
    
    public BasicConnFactory(final SocketConfig sconfig, final ConnectionConfig cconfig) {
        this(null, null, 0, sconfig, cconfig);
    }
    
    public BasicConnFactory() {
        this(null, null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
    }
    
    @Deprecated
    protected HttpClientConnection create(final Socket socket, final HttpParams params) throws IOException {
        final int bufsize = params.getIntParameter("http.socket.buffer-size", 8192);
        final DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(bufsize);
        conn.bind(socket);
        return conn;
    }
    
    public HttpClientConnection create(final HttpHost host) throws IOException {
        final String scheme = host.getSchemeName();
        Socket socket = null;
        if ("http".equalsIgnoreCase(scheme)) {
            socket = ((this.plainfactory != null) ? this.plainfactory.createSocket() : new Socket());
        }
        if ("https".equalsIgnoreCase(scheme)) {
            socket = ((this.sslfactory != null) ? this.sslfactory : SSLSocketFactory.getDefault()).createSocket();
        }
        if (socket == null) {
            throw new IOException(scheme + " scheme is not supported");
        }
        final String hostname = host.getHostName();
        int port = host.getPort();
        if (port == -1) {
            if (host.getSchemeName().equalsIgnoreCase("http")) {
                port = 80;
            }
            else if (host.getSchemeName().equalsIgnoreCase("https")) {
                port = 443;
            }
        }
        socket.setSoTimeout(this.sconfig.getSoTimeout());
        socket.connect(new InetSocketAddress(hostname, port), this.connectTimeout);
        socket.setTcpNoDelay(this.sconfig.isTcpNoDelay());
        final int linger = this.sconfig.getSoLinger();
        if (linger >= 0) {
            socket.setSoLinger(linger > 0, linger);
        }
        socket.setKeepAlive(this.sconfig.isSoKeepAlive());
        return (HttpClientConnection)this.connFactory.createConnection(socket);
    }
}
