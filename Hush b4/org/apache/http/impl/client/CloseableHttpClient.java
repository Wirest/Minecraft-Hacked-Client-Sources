// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ResponseHandler;
import java.net.URI;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.util.Args;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import java.io.Closeable;
import org.apache.http.client.HttpClient;

@ThreadSafe
public abstract class CloseableHttpClient implements HttpClient, Closeable
{
    private final Log log;
    
    public CloseableHttpClient() {
        this.log = LogFactory.getLog(this.getClass());
    }
    
    protected abstract CloseableHttpResponse doExecute(final HttpHost p0, final HttpRequest p1, final HttpContext p2) throws IOException, ClientProtocolException;
    
    public CloseableHttpResponse execute(final HttpHost target, final HttpRequest request, final HttpContext context) throws IOException, ClientProtocolException {
        return this.doExecute(target, request, context);
    }
    
    public CloseableHttpResponse execute(final HttpUriRequest request, final HttpContext context) throws IOException, ClientProtocolException {
        Args.notNull(request, "HTTP request");
        return this.doExecute(determineTarget(request), request, context);
    }
    
    private static HttpHost determineTarget(final HttpUriRequest request) throws ClientProtocolException {
        HttpHost target = null;
        final URI requestURI = request.getURI();
        if (requestURI.isAbsolute()) {
            target = URIUtils.extractHost(requestURI);
            if (target == null) {
                throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
            }
        }
        return target;
    }
    
    public CloseableHttpResponse execute(final HttpUriRequest request) throws IOException, ClientProtocolException {
        return this.execute(request, (HttpContext)null);
    }
    
    public CloseableHttpResponse execute(final HttpHost target, final HttpRequest request) throws IOException, ClientProtocolException {
        return this.doExecute(target, request, null);
    }
    
    public <T> T execute(final HttpUriRequest request, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.execute(request, responseHandler, (HttpContext)null);
    }
    
    public <T> T execute(final HttpUriRequest request, final ResponseHandler<? extends T> responseHandler, final HttpContext context) throws IOException, ClientProtocolException {
        final HttpHost target = determineTarget(request);
        return this.execute(target, (HttpRequest)request, responseHandler, context);
    }
    
    public <T> T execute(final HttpHost target, final HttpRequest request, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.execute(target, request, responseHandler, (HttpContext)null);
    }
    
    public <T> T execute(final HttpHost target, final HttpRequest request, final ResponseHandler<? extends T> responseHandler, final HttpContext context) throws IOException, ClientProtocolException {
        Args.notNull(responseHandler, "Response handler");
        final HttpResponse response = this.execute(target, request, context);
        T result;
        try {
            result = (T)responseHandler.handleResponse(response);
        }
        catch (Exception t3) {
            final HttpEntity entity = response.getEntity();
            try {
                EntityUtils.consume(entity);
            }
            catch (Exception t2) {
                this.log.warn("Error consuming content after an exception.", t2);
            }
            if (t3 instanceof RuntimeException) {
                throw (RuntimeException)t3;
            }
            if (t3 instanceof IOException) {
                throw (IOException)t3;
            }
            throw new UndeclaredThrowableException(t3);
        }
        final HttpEntity entity2 = response.getEntity();
        EntityUtils.consume(entity2);
        return result;
    }
}
