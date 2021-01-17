// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.AbstractConnPool;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.apache.http.config.ConnectionConfig;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.protocol.HttpContext;
import java.util.concurrent.TimeoutException;
import org.apache.http.util.Asserts;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpClientConnection;
import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.pool.PoolStats;
import org.apache.http.config.Lookup;
import org.apache.http.pool.ConnFactory;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.HttpConnectionFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.config.Registry;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import java.io.Closeable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.conn.HttpClientConnectionManager;

@ThreadSafe
public class PoolingHttpClientConnectionManager implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable
{
    private final Log log;
    private final ConfigData configData;
    private final CPool pool;
    private final HttpClientConnectionOperator connectionOperator;
    
    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return (Registry<ConnectionSocketFactory>)RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", (PlainConnectionSocketFactory)SSLConnectionSocketFactory.getSocketFactory()).build();
    }
    
    public PoolingHttpClientConnectionManager() {
        this(getDefaultRegistry());
    }
    
    public PoolingHttpClientConnectionManager(final long timeToLive, final TimeUnit tunit) {
        this(getDefaultRegistry(), null, null, null, timeToLive, tunit);
    }
    
    public PoolingHttpClientConnectionManager(final Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        this(socketFactoryRegistry, null, null);
    }
    
    public PoolingHttpClientConnectionManager(final Registry<ConnectionSocketFactory> socketFactoryRegistry, final DnsResolver dnsResolver) {
        this(socketFactoryRegistry, null, dnsResolver);
    }
    
    public PoolingHttpClientConnectionManager(final Registry<ConnectionSocketFactory> socketFactoryRegistry, final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
        this(socketFactoryRegistry, connFactory, null);
    }
    
    public PoolingHttpClientConnectionManager(final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
        this(getDefaultRegistry(), connFactory, null);
    }
    
    public PoolingHttpClientConnectionManager(final Registry<ConnectionSocketFactory> socketFactoryRegistry, final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, final DnsResolver dnsResolver) {
        this(socketFactoryRegistry, connFactory, null, dnsResolver, -1L, TimeUnit.MILLISECONDS);
    }
    
    public PoolingHttpClientConnectionManager(final Registry<ConnectionSocketFactory> socketFactoryRegistry, final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, final SchemePortResolver schemePortResolver, final DnsResolver dnsResolver, final long timeToLive, final TimeUnit tunit) {
        this.log = LogFactory.getLog(this.getClass());
        this.configData = new ConfigData();
        this.pool = new CPool(new InternalConnectionFactory(this.configData, connFactory), 2, 20, timeToLive, tunit);
        this.connectionOperator = new HttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver);
    }
    
    PoolingHttpClientConnectionManager(final CPool pool, final Lookup<ConnectionSocketFactory> socketFactoryRegistry, final SchemePortResolver schemePortResolver, final DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(this.getClass());
        this.configData = new ConfigData();
        this.pool = pool;
        this.connectionOperator = new HttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver);
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.shutdown();
        }
        finally {
            super.finalize();
        }
    }
    
    public void close() {
        this.shutdown();
    }
    
    private String format(final HttpRoute route, final Object state) {
        final StringBuilder buf = new StringBuilder();
        buf.append("[route: ").append(route).append("]");
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }
    
    private String formatStats(final HttpRoute route) {
        final StringBuilder buf = new StringBuilder();
        final PoolStats totals = this.pool.getTotalStats();
        final PoolStats stats = ((AbstractConnPool<HttpRoute, C, E>)this.pool).getStats(route);
        buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
        buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
        buf.append(" of ").append(stats.getMax()).append("; ");
        buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
        buf.append(" of ").append(totals.getMax()).append("]");
        return buf.toString();
    }
    
    private String format(final CPoolEntry entry) {
        final StringBuilder buf = new StringBuilder();
        buf.append("[id: ").append(entry.getId()).append("]");
        buf.append("[route: ").append(((PoolEntry<Object, C>)entry).getRoute()).append("]");
        final Object state = entry.getState();
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }
    
    public ConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        Args.notNull(route, "HTTP route");
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + this.format(route, state) + this.formatStats(route));
        }
        final Future<CPoolEntry> future = ((AbstractConnPool<HttpRoute, C, CPoolEntry>)this.pool).lease(route, state, null);
        return new ConnectionRequest() {
            public boolean cancel() {
                return future.cancel(true);
            }
            
            public HttpClientConnection get(final long timeout, final TimeUnit tunit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
                return PoolingHttpClientConnectionManager.this.leaseConnection(future, timeout, tunit);
            }
        };
    }
    
    protected HttpClientConnection leaseConnection(final Future<CPoolEntry> future, final long timeout, final TimeUnit tunit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
        try {
            final CPoolEntry entry = future.get(timeout, tunit);
            if (entry == null || future.isCancelled()) {
                throw new InterruptedException();
            }
            Asserts.check(entry.getConnection() != null, "Pool entry with no connection");
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection leased: " + this.format(entry) + this.formatStats(((PoolEntry<HttpRoute, C>)entry).getRoute()));
            }
            return CPoolProxy.newProxy(entry);
        }
        catch (TimeoutException ex) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
        }
    }
    
    public void releaseConnection(final HttpClientConnection managedConn, final Object state, final long keepalive, final TimeUnit tunit) {
        Args.notNull(managedConn, "Managed connection");
        synchronized (managedConn) {
            final CPoolEntry entry = CPoolProxy.detach(managedConn);
            if (entry == null) {
                return;
            }
            final ManagedHttpClientConnection conn = ((PoolEntry<T, ManagedHttpClientConnection>)entry).getConnection();
            try {
                if (conn.isOpen()) {
                    entry.setState(state);
                    entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
                    if (this.log.isDebugEnabled()) {
                        String s;
                        if (keepalive > 0L) {
                            s = "for " + keepalive / 1000.0 + " seconds";
                        }
                        else {
                            s = "indefinitely";
                        }
                        this.log.debug("Connection " + this.format(entry) + " can be kept alive " + s);
                    }
                }
            }
            finally {
                ((AbstractConnPool<T, C, CPoolEntry>)this.pool).release(entry, conn.isOpen() && entry.isRouteComplete());
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Connection released: " + this.format(entry) + this.formatStats(((PoolEntry<HttpRoute, C>)entry).getRoute()));
                }
            }
        }
    }
    
    public void connect(final HttpClientConnection managedConn, final HttpRoute route, final int connectTimeout, final HttpContext context) throws IOException {
        Args.notNull(managedConn, "Managed Connection");
        Args.notNull(route, "HTTP route");
        final ManagedHttpClientConnection conn;
        synchronized (managedConn) {
            final CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            conn = ((PoolEntry<T, ManagedHttpClientConnection>)entry).getConnection();
        }
        HttpHost host;
        if (route.getProxyHost() != null) {
            host = route.getProxyHost();
        }
        else {
            host = route.getTargetHost();
        }
        final InetSocketAddress localAddress = route.getLocalSocketAddress();
        SocketConfig socketConfig = this.configData.getSocketConfig(host);
        if (socketConfig == null) {
            socketConfig = this.configData.getDefaultSocketConfig();
        }
        if (socketConfig == null) {
            socketConfig = SocketConfig.DEFAULT;
        }
        this.connectionOperator.connect(conn, host, localAddress, connectTimeout, socketConfig, context);
    }
    
    public void upgrade(final HttpClientConnection managedConn, final HttpRoute route, final HttpContext context) throws IOException {
        Args.notNull(managedConn, "Managed Connection");
        Args.notNull(route, "HTTP route");
        final ManagedHttpClientConnection conn;
        synchronized (managedConn) {
            final CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            conn = ((PoolEntry<T, ManagedHttpClientConnection>)entry).getConnection();
        }
        this.connectionOperator.upgrade(conn, route.getTargetHost(), context);
    }
    
    public void routeComplete(final HttpClientConnection managedConn, final HttpRoute route, final HttpContext context) throws IOException {
        Args.notNull(managedConn, "Managed Connection");
        Args.notNull(route, "HTTP route");
        synchronized (managedConn) {
            final CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            entry.markRouteComplete();
        }
    }
    
    public void shutdown() {
        this.log.debug("Connection manager is shutting down");
        try {
            this.pool.shutdown();
        }
        catch (IOException ex) {
            this.log.debug("I/O exception shutting down connection manager", ex);
        }
        this.log.debug("Connection manager shut down");
    }
    
    public void closeIdleConnections(final long idleTimeout, final TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
        }
        this.pool.closeIdle(idleTimeout, tunit);
    }
    
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }
    
    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }
    
    public void setMaxTotal(final int max) {
        this.pool.setMaxTotal(max);
    }
    
    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }
    
    public void setDefaultMaxPerRoute(final int max) {
        this.pool.setDefaultMaxPerRoute(max);
    }
    
    public int getMaxPerRoute(final HttpRoute route) {
        return ((AbstractConnPool<HttpRoute, C, E>)this.pool).getMaxPerRoute(route);
    }
    
    public void setMaxPerRoute(final HttpRoute route, final int max) {
        ((AbstractConnPool<HttpRoute, C, E>)this.pool).setMaxPerRoute(route, max);
    }
    
    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }
    
    public PoolStats getStats(final HttpRoute route) {
        return ((AbstractConnPool<HttpRoute, C, E>)this.pool).getStats(route);
    }
    
    public SocketConfig getDefaultSocketConfig() {
        return this.configData.getDefaultSocketConfig();
    }
    
    public void setDefaultSocketConfig(final SocketConfig defaultSocketConfig) {
        this.configData.setDefaultSocketConfig(defaultSocketConfig);
    }
    
    public ConnectionConfig getDefaultConnectionConfig() {
        return this.configData.getDefaultConnectionConfig();
    }
    
    public void setDefaultConnectionConfig(final ConnectionConfig defaultConnectionConfig) {
        this.configData.setDefaultConnectionConfig(defaultConnectionConfig);
    }
    
    public SocketConfig getSocketConfig(final HttpHost host) {
        return this.configData.getSocketConfig(host);
    }
    
    public void setSocketConfig(final HttpHost host, final SocketConfig socketConfig) {
        this.configData.setSocketConfig(host, socketConfig);
    }
    
    public ConnectionConfig getConnectionConfig(final HttpHost host) {
        return this.configData.getConnectionConfig(host);
    }
    
    public void setConnectionConfig(final HttpHost host, final ConnectionConfig connectionConfig) {
        this.configData.setConnectionConfig(host, connectionConfig);
    }
    
    static class ConfigData
    {
        private final Map<HttpHost, SocketConfig> socketConfigMap;
        private final Map<HttpHost, ConnectionConfig> connectionConfigMap;
        private volatile SocketConfig defaultSocketConfig;
        private volatile ConnectionConfig defaultConnectionConfig;
        
        ConfigData() {
            this.socketConfigMap = new ConcurrentHashMap<HttpHost, SocketConfig>();
            this.connectionConfigMap = new ConcurrentHashMap<HttpHost, ConnectionConfig>();
        }
        
        public SocketConfig getDefaultSocketConfig() {
            return this.defaultSocketConfig;
        }
        
        public void setDefaultSocketConfig(final SocketConfig defaultSocketConfig) {
            this.defaultSocketConfig = defaultSocketConfig;
        }
        
        public ConnectionConfig getDefaultConnectionConfig() {
            return this.defaultConnectionConfig;
        }
        
        public void setDefaultConnectionConfig(final ConnectionConfig defaultConnectionConfig) {
            this.defaultConnectionConfig = defaultConnectionConfig;
        }
        
        public SocketConfig getSocketConfig(final HttpHost host) {
            return this.socketConfigMap.get(host);
        }
        
        public void setSocketConfig(final HttpHost host, final SocketConfig socketConfig) {
            this.socketConfigMap.put(host, socketConfig);
        }
        
        public ConnectionConfig getConnectionConfig(final HttpHost host) {
            return this.connectionConfigMap.get(host);
        }
        
        public void setConnectionConfig(final HttpHost host, final ConnectionConfig connectionConfig) {
            this.connectionConfigMap.put(host, connectionConfig);
        }
    }
    
    static class InternalConnectionFactory implements ConnFactory<HttpRoute, ManagedHttpClientConnection>
    {
        private final ConfigData configData;
        private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
        
        InternalConnectionFactory(final ConfigData configData, final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
            this.configData = ((configData != null) ? configData : new ConfigData());
            this.connFactory = ((connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE);
        }
        
        public ManagedHttpClientConnection create(final HttpRoute route) throws IOException {
            ConnectionConfig config = null;
            if (route.getProxyHost() != null) {
                config = this.configData.getConnectionConfig(route.getProxyHost());
            }
            if (config == null) {
                config = this.configData.getConnectionConfig(route.getTargetHost());
            }
            if (config == null) {
                config = this.configData.getDefaultConnectionConfig();
            }
            if (config == null) {
                config = ConnectionConfig.DEFAULT;
            }
            return this.connFactory.create(route, config);
        }
    }
}
