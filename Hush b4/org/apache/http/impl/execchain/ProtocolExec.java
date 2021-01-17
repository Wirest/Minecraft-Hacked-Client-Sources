// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpException;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.AuthScope;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.apache.http.util.Args;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HttpProcessor;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;

@Immutable
public class ProtocolExec implements ClientExecChain
{
    private final Log log;
    private final ClientExecChain requestExecutor;
    private final HttpProcessor httpProcessor;
    
    public ProtocolExec(final ClientExecChain requestExecutor, final HttpProcessor httpProcessor) {
        this.log = LogFactory.getLog(this.getClass());
        Args.notNull(requestExecutor, "HTTP client request executor");
        Args.notNull(httpProcessor, "HTTP protocol processor");
        this.requestExecutor = requestExecutor;
        this.httpProcessor = httpProcessor;
    }
    
    void rewriteRequestURI(final HttpRequestWrapper request, final HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (uri != null) {
                if (route.getProxyHost() != null && !route.isTunnelled()) {
                    if (!uri.isAbsolute()) {
                        final HttpHost target = route.getTargetHost();
                        uri = URIUtils.rewriteURI(uri, target, true);
                    }
                    else {
                        uri = URIUtils.rewriteURI(uri);
                    }
                }
                else if (uri.isAbsolute()) {
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
        final HttpRequest original = request.getOriginal();
        URI uri = null;
        if (original instanceof HttpUriRequest) {
            uri = ((HttpUriRequest)original).getURI();
        }
        else {
            final String uriString = original.getRequestLine().getUri();
            try {
                uri = URI.create(uriString);
            }
            catch (IllegalArgumentException ex) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Unable to parse '" + uriString + "' as a valid URI; " + "request URI and Host header may be inconsistent", ex);
                }
            }
        }
        request.setURI(uri);
        this.rewriteRequestURI(request, route);
        final HttpParams params = request.getParams();
        HttpHost virtualHost = (HttpHost)params.getParameter("http.virtual-host");
        if (virtualHost != null && virtualHost.getPort() == -1) {
            final int port = route.getTargetHost().getPort();
            if (port != -1) {
                virtualHost = new HttpHost(virtualHost.getHostName(), port, virtualHost.getSchemeName());
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Using virtual host" + virtualHost);
            }
        }
        HttpHost target = null;
        if (virtualHost != null) {
            target = virtualHost;
        }
        else if (uri != null && uri.isAbsolute() && uri.getHost() != null) {
            target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        }
        if (target == null) {
            target = route.getTargetHost();
        }
        if (uri != null) {
            final String userinfo = uri.getUserInfo();
            if (userinfo != null) {
                CredentialsProvider credsProvider = context.getCredentialsProvider();
                if (credsProvider == null) {
                    credsProvider = new BasicCredentialsProvider();
                    context.setCredentialsProvider(credsProvider);
                }
                credsProvider.setCredentials(new AuthScope(target), new UsernamePasswordCredentials(userinfo));
            }
        }
        context.setAttribute("http.target_host", target);
        context.setAttribute("http.route", route);
        context.setAttribute("http.request", request);
        this.httpProcessor.process(request, context);
        final CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
        try {
            context.setAttribute("http.response", response);
            this.httpProcessor.process(response, context);
            return response;
        }
        catch (RuntimeException ex2) {
            response.close();
            throw ex2;
        }
        catch (IOException ex3) {
            response.close();
            throw ex3;
        }
        catch (HttpException ex4) {
            response.close();
            throw ex4;
        }
    }
}
