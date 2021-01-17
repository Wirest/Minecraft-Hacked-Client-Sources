// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.conn.scheme.SchemeRegistry;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ClientConnectionManager;
import java.io.IOException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.Configurable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.impl.execchain.MinimalClientExec;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
class MinimalHttpClient extends CloseableHttpClient
{
    private final HttpClientConnectionManager connManager;
    private final MinimalClientExec requestExecutor;
    private final HttpParams params;
    
    public MinimalHttpClient(final HttpClientConnectionManager connManager) {
        this.connManager = Args.notNull(connManager, "HTTP connection manager");
        this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), connManager, DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
        this.params = new BasicHttpParams();
    }
    
    @Override
    protected CloseableHttpResponse doExecute(final HttpHost target, final HttpRequest request, final HttpContext context) throws IOException, ClientProtocolException {
        Args.notNull(target, "Target host");
        Args.notNull(request, "HTTP request");
        HttpExecutionAware execAware = null;
        if (request instanceof HttpExecutionAware) {
            execAware = (HttpExecutionAware)request;
        }
        try {
            final HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request);
            final HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : new BasicHttpContext());
            final HttpRoute route = new HttpRoute(target);
            RequestConfig config = null;
            if (request instanceof Configurable) {
                config = ((Configurable)request).getConfig();
            }
            if (config != null) {
                localcontext.setRequestConfig(config);
            }
            return this.requestExecutor.execute(route, wrapper, localcontext, execAware);
        }
        catch (HttpException httpException) {
            throw new ClientProtocolException(httpException);
        }
    }
    
    public HttpParams getParams() {
        return this.params;
    }
    
    public void close() {
        this.connManager.shutdown();
    }
    
    public ClientConnectionManager getConnectionManager() {
        return new ClientConnectionManager() {
            public void shutdown() {
                MinimalHttpClient.this.connManager.shutdown();
            }
            
            public ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
                throw new UnsupportedOperationException();
            }
            
            public void releaseConnection(final ManagedClientConnection conn, final long validDuration, final TimeUnit timeUnit) {
                throw new UnsupportedOperationException();
            }
            
            public SchemeRegistry getSchemeRegistry() {
                throw new UnsupportedOperationException();
            }
            
            public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
                MinimalHttpClient.this.connManager.closeIdleConnections(idletime, tunit);
            }
            
            public void closeExpiredConnections() {
                MinimalHttpClient.this.connManager.closeExpiredConnections();
            }
        };
    }
}
