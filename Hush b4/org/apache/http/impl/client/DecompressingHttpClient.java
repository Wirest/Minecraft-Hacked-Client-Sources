// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.HttpException;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.protocol.BasicHttpContext;
import java.net.URI;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;

@Deprecated
public class DecompressingHttpClient implements HttpClient
{
    private final HttpClient backend;
    private final HttpRequestInterceptor acceptEncodingInterceptor;
    private final HttpResponseInterceptor contentEncodingInterceptor;
    
    public DecompressingHttpClient() {
        this(new DefaultHttpClient());
    }
    
    public DecompressingHttpClient(final HttpClient backend) {
        this(backend, new RequestAcceptEncoding(), new ResponseContentEncoding());
    }
    
    DecompressingHttpClient(final HttpClient backend, final HttpRequestInterceptor requestInterceptor, final HttpResponseInterceptor responseInterceptor) {
        this.backend = backend;
        this.acceptEncodingInterceptor = requestInterceptor;
        this.contentEncodingInterceptor = responseInterceptor;
    }
    
    public HttpParams getParams() {
        return this.backend.getParams();
    }
    
    public ClientConnectionManager getConnectionManager() {
        return this.backend.getConnectionManager();
    }
    
    public HttpResponse execute(final HttpUriRequest request) throws IOException, ClientProtocolException {
        return this.execute(this.getHttpHost(request), request, (HttpContext)null);
    }
    
    public HttpClient getHttpClient() {
        return this.backend;
    }
    
    HttpHost getHttpHost(final HttpUriRequest request) {
        final URI uri = request.getURI();
        return URIUtils.extractHost(uri);
    }
    
    public HttpResponse execute(final HttpUriRequest request, final HttpContext context) throws IOException, ClientProtocolException {
        return this.execute(this.getHttpHost(request), request, context);
    }
    
    public HttpResponse execute(final HttpHost target, final HttpRequest request) throws IOException, ClientProtocolException {
        return this.execute(target, request, (HttpContext)null);
    }
    
    public HttpResponse execute(final HttpHost target, final HttpRequest request, final HttpContext context) throws IOException, ClientProtocolException {
        try {
            final HttpContext localContext = (context != null) ? context : new BasicHttpContext();
            HttpRequest wrapped;
            if (request instanceof HttpEntityEnclosingRequest) {
                wrapped = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
            }
            else {
                wrapped = new RequestWrapper(request);
            }
            this.acceptEncodingInterceptor.process(wrapped, localContext);
            final HttpResponse response = this.backend.execute(target, wrapped, localContext);
            try {
                this.contentEncodingInterceptor.process(response, localContext);
                if (Boolean.TRUE.equals(localContext.getAttribute("http.client.response.uncompressed"))) {
                    response.removeHeaders("Content-Length");
                    response.removeHeaders("Content-Encoding");
                    response.removeHeaders("Content-MD5");
                }
                return response;
            }
            catch (HttpException ex) {
                EntityUtils.consume(response.getEntity());
                throw ex;
            }
            catch (IOException ex2) {
                EntityUtils.consume(response.getEntity());
                throw ex2;
            }
            catch (RuntimeException ex3) {
                EntityUtils.consume(response.getEntity());
                throw ex3;
            }
        }
        catch (HttpException e) {
            throw new ClientProtocolException(e);
        }
    }
    
    public <T> T execute(final HttpUriRequest request, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.execute(this.getHttpHost(request), (HttpRequest)request, responseHandler);
    }
    
    public <T> T execute(final HttpUriRequest request, final ResponseHandler<? extends T> responseHandler, final HttpContext context) throws IOException, ClientProtocolException {
        return this.execute(this.getHttpHost(request), (HttpRequest)request, responseHandler, context);
    }
    
    public <T> T execute(final HttpHost target, final HttpRequest request, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return this.execute(target, request, responseHandler, (HttpContext)null);
    }
    
    public <T> T execute(final HttpHost target, final HttpRequest request, final ResponseHandler<? extends T> responseHandler, final HttpContext context) throws IOException, ClientProtocolException {
        final HttpResponse response = this.execute(target, request, context);
        try {
            return (T)responseHandler.handleResponse(response);
        }
        finally {
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        }
    }
}
