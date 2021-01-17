// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpClientConnection;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionRequest;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.impl.conn.ConnectionShutdownException;
import java.io.InterruptedIOException;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.protocol.HttpClientContext;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.ProtocolException;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.VersionInfo;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestContent;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;

@Immutable
public class MinimalClientExec implements ClientExecChain
{
    private final Log log;
    private final HttpRequestExecutor requestExecutor;
    private final HttpClientConnectionManager connManager;
    private final ConnectionReuseStrategy reuseStrategy;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final HttpProcessor httpProcessor;
    
    public MinimalClientExec(final HttpRequestExecutor requestExecutor, final HttpClientConnectionManager connManager, final ConnectionReuseStrategy reuseStrategy, final ConnectionKeepAliveStrategy keepAliveStrategy) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(connManager, "Client connection manager");
        Args.notNull(reuseStrategy, "Connection reuse strategy");
        Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
        this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] { new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", this.getClass())) });
        this.requestExecutor = requestExecutor;
        this.connManager = connManager;
        this.reuseStrategy = reuseStrategy;
        this.keepAliveStrategy = keepAliveStrategy;
    }
    
    static void rewriteRequestURI(final HttpRequestWrapper request, final HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (uri != null) {
                if (uri.isAbsolute()) {
                    uri = URIUtils.rewriteURI(uri, null, true);
                }
                else {
                    uri = URIUtils.rewriteURI(uri);
                }
                request.setURI(uri);
            }
        }
        catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }
    
    public CloseableHttpResponse execute(final HttpRoute route, final HttpRequestWrapper request, final HttpClientContext context, final HttpExecutionAware execAware) throws IOException, HttpException {
        Args.notNull(route, "HTTP route");
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");
        rewriteRequestURI(request, route);
        final ConnectionRequest connRequest = this.connManager.requestConnection(route, null);
        if (execAware != null) {
            if (execAware.isAborted()) {
                connRequest.cancel();
                throw new RequestAbortedException("Request aborted");
            }
            execAware.setCancellable(connRequest);
        }
        final RequestConfig config = context.getRequestConfig();
        HttpClientConnection managedConn;
        try {
            final int timeout = config.getConnectionRequestTimeout();
            managedConn = connRequest.get((timeout > 0) ? ((long)timeout) : 0L, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new RequestAbortedException("Request aborted", interrupted);
        }
        catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
            throw new RequestAbortedException("Request execution failed", cause);
        }
        final ConnectionHolder releaseTrigger = new ConnectionHolder(this.log, this.connManager, managedConn);
        try {
            if (execAware != null) {
                if (execAware.isAborted()) {
                    releaseTrigger.close();
                    throw new RequestAbortedException("Request aborted");
                }
                execAware.setCancellable(releaseTrigger);
            }
            if (!managedConn.isOpen()) {
                final int timeout2 = config.getConnectTimeout();
                this.connManager.connect(managedConn, route, (timeout2 > 0) ? timeout2 : 0, context);
                this.connManager.routeComplete(managedConn, route, context);
            }
            final int timeout2 = config.getSocketTimeout();
            if (timeout2 >= 0) {
                managedConn.setSocketTimeout(timeout2);
            }
            HttpHost target = null;
            final HttpRequest original = request.getOriginal();
            if (original instanceof HttpUriRequest) {
                final URI uri = ((HttpUriRequest)original).getURI();
                if (uri.isAbsolute()) {
                    target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                }
            }
            if (target == null) {
                target = route.getTargetHost();
            }
            context.setAttribute("http.target_host", target);
            context.setAttribute("http.request", request);
            context.setAttribute("http.connection", managedConn);
            context.setAttribute("http.route", route);
            this.httpProcessor.process(request, context);
            final HttpResponse response = this.requestExecutor.execute(request, managedConn, context);
            this.httpProcessor.process(response, context);
            if (this.reuseStrategy.keepAlive(response, context)) {
                final long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
                releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
                releaseTrigger.markReusable();
            }
            else {
                releaseTrigger.markNonReusable();
            }
            final HttpEntity entity = response.getEntity();
            if (entity == null || !entity.isStreaming()) {
                releaseTrigger.releaseConnection();
                return Proxies.enhanceResponse(response, null);
            }
            return Proxies.enhanceResponse(response, releaseTrigger);
        }
        catch (ConnectionShutdownException ex2) {
            final InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
            ioex.initCause(ex2);
            throw ioex;
        }
        catch (HttpException ex3) {
            releaseTrigger.abortConnection();
            throw ex3;
        }
        catch (IOException ex4) {
            releaseTrigger.abortConnection();
            throw ex4;
        }
        catch (RuntimeException ex5) {
            releaseTrigger.abortConnection();
            throw ex5;
        }
    }
}
