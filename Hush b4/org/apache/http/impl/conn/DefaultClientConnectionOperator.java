// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.net.UnknownHostException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ConnectTimeoutException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.util.Asserts;
import org.apache.http.params.HttpParams;
import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.protocol.HttpContext;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;

@Deprecated
@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator
{
    private final Log log;
    protected final SchemeRegistry schemeRegistry;
    protected final DnsResolver dnsResolver;
    
    public DefaultClientConnectionOperator(final SchemeRegistry schemes) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(schemes, "Scheme registry");
        this.schemeRegistry = schemes;
        this.dnsResolver = new SystemDefaultDnsResolver();
    }
    
    public DefaultClientConnectionOperator(final SchemeRegistry schemes, final DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(schemes, "Scheme registry");
        Args.notNull(dnsResolver, "DNS resolver");
        this.schemeRegistry = schemes;
        this.dnsResolver = dnsResolver;
    }
    
    public OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }
    
    private SchemeRegistry getSchemeRegistry(final HttpContext context) {
        SchemeRegistry reg = (SchemeRegistry)context.getAttribute("http.scheme-registry");
        if (reg == null) {
            reg = this.schemeRegistry;
        }
        return reg;
    }
    
    public void openConnection(final OperatedClientConnection conn, final HttpHost target, final InetAddress local, final HttpContext context, final HttpParams params) throws IOException {
        Args.notNull(conn, "Connection");
        Args.notNull(target, "Target host");
        Args.notNull(params, "HTTP parameters");
        Asserts.check(!conn.isOpen(), "Connection must not be open");
        final SchemeRegistry registry = this.getSchemeRegistry(context);
        final Scheme schm = registry.getScheme(target.getSchemeName());
        final SchemeSocketFactory sf = schm.getSchemeSocketFactory();
        final InetAddress[] addresses = this.resolveHostname(target.getHostName());
        final int port = schm.resolvePort(target.getPort());
        for (int i = 0; i < addresses.length; ++i) {
            final InetAddress address = addresses[i];
            final boolean last = i == addresses.length - 1;
            Socket sock = sf.createSocket(params);
            conn.opening(sock, target);
            final InetSocketAddress remoteAddress = new HttpInetSocketAddress(target, address, port);
            InetSocketAddress localAddress = null;
            if (local != null) {
                localAddress = new InetSocketAddress(local, 0);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connecting to " + remoteAddress);
            }
            try {
                final Socket connsock = sf.connectSocket(sock, remoteAddress, localAddress, params);
                if (sock != connsock) {
                    sock = connsock;
                    conn.opening(sock, target);
                }
                this.prepareSocket(sock, context, params);
                conn.openCompleted(sf.isSecure(sock), params);
                return;
            }
            catch (ConnectException ex) {
                if (last) {
                    throw ex;
                }
            }
            catch (ConnectTimeoutException ex2) {
                if (last) {
                    throw ex2;
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connect to " + remoteAddress + " timed out. " + "Connection will be retried using another IP address");
            }
        }
    }
    
    public void updateSecureConnection(final OperatedClientConnection conn, final HttpHost target, final HttpContext context, final HttpParams params) throws IOException {
        Args.notNull(conn, "Connection");
        Args.notNull(target, "Target host");
        Args.notNull(params, "Parameters");
        Asserts.check(conn.isOpen(), "Connection must be open");
        final SchemeRegistry registry = this.getSchemeRegistry(context);
        final Scheme schm = registry.getScheme(target.getSchemeName());
        Asserts.check(schm.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
        final SchemeLayeredSocketFactory lsf = (SchemeLayeredSocketFactory)schm.getSchemeSocketFactory();
        final Socket sock = lsf.createLayeredSocket(conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()), params);
        this.prepareSocket(sock, context, params);
        conn.update(sock, target, lsf.isSecure(sock), params);
    }
    
    protected void prepareSocket(final Socket sock, final HttpContext context, final HttpParams params) throws IOException {
        sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
        sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
        final int linger = HttpConnectionParams.getLinger(params);
        if (linger >= 0) {
            sock.setSoLinger(linger > 0, linger);
        }
    }
    
    protected InetAddress[] resolveHostname(final String host) throws UnknownHostException {
        return this.dnsResolver.resolve(host);
    }
}
