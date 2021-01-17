// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.client.protocol.HttpClientContext;
import java.net.Socket;
import java.net.InetAddress;
import java.net.ConnectException;
import org.apache.http.conn.HttpHostConnectException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.config.SocketConfig;
import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.config.Lookup;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;

@Immutable
class HttpClientConnectionOperator
{
    static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
    private final Log log;
    private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;
    private final SchemePortResolver schemePortResolver;
    private final DnsResolver dnsResolver;
    
    HttpClientConnectionOperator(final Lookup<ConnectionSocketFactory> socketFactoryRegistry, final SchemePortResolver schemePortResolver, final DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(socketFactoryRegistry, "Socket factory registry");
        this.socketFactoryRegistry = socketFactoryRegistry;
        this.schemePortResolver = ((schemePortResolver != null) ? schemePortResolver : DefaultSchemePortResolver.INSTANCE);
        this.dnsResolver = ((dnsResolver != null) ? dnsResolver : SystemDefaultDnsResolver.INSTANCE);
    }
    
    private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(final HttpContext context) {
        Lookup<ConnectionSocketFactory> reg = (Lookup<ConnectionSocketFactory>)context.getAttribute("http.socket-factory-registry");
        if (reg == null) {
            reg = this.socketFactoryRegistry;
        }
        return reg;
    }
    
    public void connect(final ManagedHttpClientConnection conn, final HttpHost host, final InetSocketAddress localAddress, final int connectTimeout, final SocketConfig socketConfig, final HttpContext context) throws IOException {
        final Lookup<ConnectionSocketFactory> registry = this.getSocketFactoryRegistry(context);
        final ConnectionSocketFactory sf = registry.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
        }
        final InetAddress[] addresses = this.dnsResolver.resolve(host.getHostName());
        final int port = this.schemePortResolver.resolve(host);
        for (int i = 0; i < addresses.length; ++i) {
            final InetAddress address = addresses[i];
            final boolean last = i == addresses.length - 1;
            Socket sock = sf.createSocket(context);
            sock.setReuseAddress(socketConfig.isSoReuseAddress());
            conn.bind(sock);
            final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connecting to " + remoteAddress);
            }
            try {
                sock.setSoTimeout(socketConfig.getSoTimeout());
                sock = sf.connectSocket(connectTimeout, sock, host, remoteAddress, localAddress, context);
                sock.setTcpNoDelay(socketConfig.isTcpNoDelay());
                sock.setKeepAlive(socketConfig.isSoKeepAlive());
                final int linger = socketConfig.getSoLinger();
                if (linger >= 0) {
                    sock.setSoLinger(linger > 0, linger);
                }
                conn.bind(sock);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Connection established " + conn);
                }
                return;
            }
            catch (SocketTimeoutException ex) {
                if (last) {
                    throw new ConnectTimeoutException(ex, host, addresses);
                }
            }
            catch (ConnectException ex2) {
                if (last) {
                    final String msg = ex2.getMessage();
                    if ("Connection timed out".equals(msg)) {
                        throw new ConnectTimeoutException(ex2, host, addresses);
                    }
                    throw new HttpHostConnectException(ex2, host, addresses);
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connect to " + remoteAddress + " timed out. " + "Connection will be retried using another IP address");
            }
        }
    }
    
    public void upgrade(final ManagedHttpClientConnection conn, final HttpHost host, final HttpContext context) throws IOException {
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final Lookup<ConnectionSocketFactory> registry = this.getSocketFactoryRegistry(clientContext);
        final ConnectionSocketFactory sf = registry.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
        }
        if (!(sf instanceof LayeredConnectionSocketFactory)) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol does not support connection upgrade");
        }
        final LayeredConnectionSocketFactory lsf = (LayeredConnectionSocketFactory)sf;
        Socket sock = conn.getSocket();
        final int port = this.schemePortResolver.resolve(host);
        sock = lsf.createLayeredSocket(sock, host.getHostName(), port, context);
        conn.bind(sock);
    }
}
