// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EncodingUtils;
import org.apache.http.ProtocolException;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.MethodNotSupportedException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpException;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.util.Args;
import org.apache.http.HttpResponseFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;

@Immutable
public class HttpService
{
    private volatile HttpParams params;
    private volatile HttpProcessor processor;
    private volatile HttpRequestHandlerMapper handlerMapper;
    private volatile ConnectionReuseStrategy connStrategy;
    private volatile HttpResponseFactory responseFactory;
    private volatile HttpExpectationVerifier expectationVerifier;
    
    @Deprecated
    public HttpService(final HttpProcessor processor, final ConnectionReuseStrategy connStrategy, final HttpResponseFactory responseFactory, final HttpRequestHandlerResolver handlerResolver, final HttpExpectationVerifier expectationVerifier, final HttpParams params) {
        this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), expectationVerifier);
        this.params = params;
    }
    
    @Deprecated
    public HttpService(final HttpProcessor processor, final ConnectionReuseStrategy connStrategy, final HttpResponseFactory responseFactory, final HttpRequestHandlerResolver handlerResolver, final HttpParams params) {
        this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), null);
        this.params = params;
    }
    
    @Deprecated
    public HttpService(final HttpProcessor proc, final ConnectionReuseStrategy connStrategy, final HttpResponseFactory responseFactory) {
        this.params = null;
        this.processor = null;
        this.handlerMapper = null;
        this.connStrategy = null;
        this.responseFactory = null;
        this.expectationVerifier = null;
        this.setHttpProcessor(proc);
        this.setConnReuseStrategy(connStrategy);
        this.setResponseFactory(responseFactory);
    }
    
    public HttpService(final HttpProcessor processor, final ConnectionReuseStrategy connStrategy, final HttpResponseFactory responseFactory, final HttpRequestHandlerMapper handlerMapper, final HttpExpectationVerifier expectationVerifier) {
        this.params = null;
        this.processor = null;
        this.handlerMapper = null;
        this.connStrategy = null;
        this.responseFactory = null;
        this.expectationVerifier = null;
        this.processor = Args.notNull(processor, "HTTP processor");
        this.connStrategy = ((connStrategy != null) ? connStrategy : DefaultConnectionReuseStrategy.INSTANCE);
        this.responseFactory = ((responseFactory != null) ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
        this.handlerMapper = handlerMapper;
        this.expectationVerifier = expectationVerifier;
    }
    
    public HttpService(final HttpProcessor processor, final ConnectionReuseStrategy connStrategy, final HttpResponseFactory responseFactory, final HttpRequestHandlerMapper handlerMapper) {
        this(processor, connStrategy, responseFactory, handlerMapper, null);
    }
    
    public HttpService(final HttpProcessor processor, final HttpRequestHandlerMapper handlerMapper) {
        this(processor, null, null, handlerMapper, null);
    }
    
    @Deprecated
    public void setHttpProcessor(final HttpProcessor processor) {
        Args.notNull(processor, "HTTP processor");
        this.processor = processor;
    }
    
    @Deprecated
    public void setConnReuseStrategy(final ConnectionReuseStrategy connStrategy) {
        Args.notNull(connStrategy, "Connection reuse strategy");
        this.connStrategy = connStrategy;
    }
    
    @Deprecated
    public void setResponseFactory(final HttpResponseFactory responseFactory) {
        Args.notNull(responseFactory, "Response factory");
        this.responseFactory = responseFactory;
    }
    
    @Deprecated
    public void setParams(final HttpParams params) {
        this.params = params;
    }
    
    @Deprecated
    public void setHandlerResolver(final HttpRequestHandlerResolver handlerResolver) {
        this.handlerMapper = new HttpRequestHandlerResolverAdapter(handlerResolver);
    }
    
    @Deprecated
    public void setExpectationVerifier(final HttpExpectationVerifier expectationVerifier) {
        this.expectationVerifier = expectationVerifier;
    }
    
    @Deprecated
    public HttpParams getParams() {
        return this.params;
    }
    
    public void handleRequest(final HttpServerConnection conn, final HttpContext context) throws IOException, HttpException {
        context.setAttribute("http.connection", conn);
        HttpResponse response = null;
        try {
            final HttpRequest request = conn.receiveRequestHeader();
            if (request instanceof HttpEntityEnclosingRequest) {
                if (((HttpEntityEnclosingRequest)request).expectContinue()) {
                    response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 100, context);
                    if (this.expectationVerifier != null) {
                        try {
                            this.expectationVerifier.verify(request, response, context);
                        }
                        catch (HttpException ex) {
                            response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, context);
                            this.handleException(ex, response);
                        }
                    }
                    if (response.getStatusLine().getStatusCode() < 200) {
                        conn.sendResponseHeader(response);
                        conn.flush();
                        response = null;
                        conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
                    }
                }
                else {
                    conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
                }
            }
            context.setAttribute("http.request", request);
            if (response == null) {
                response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, context);
                this.processor.process(request, context);
                this.doService(request, response, context);
            }
            if (request instanceof HttpEntityEnclosingRequest) {
                final HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
                EntityUtils.consume(entity);
            }
        }
        catch (HttpException ex2) {
            response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, context);
            this.handleException(ex2, response);
        }
        context.setAttribute("http.response", response);
        this.processor.process(response, context);
        conn.sendResponseHeader(response);
        conn.sendResponseEntity(response);
        conn.flush();
        if (!this.connStrategy.keepAlive(response, context)) {
            conn.close();
        }
    }
    
    protected void handleException(final HttpException ex, final HttpResponse response) {
        if (ex instanceof MethodNotSupportedException) {
            response.setStatusCode(501);
        }
        else if (ex instanceof UnsupportedHttpVersionException) {
            response.setStatusCode(505);
        }
        else if (ex instanceof ProtocolException) {
            response.setStatusCode(400);
        }
        else {
            response.setStatusCode(500);
        }
        String message = ex.getMessage();
        if (message == null) {
            message = ex.toString();
        }
        final byte[] msg = EncodingUtils.getAsciiBytes(message);
        final ByteArrayEntity entity = new ByteArrayEntity(msg);
        entity.setContentType("text/plain; charset=US-ASCII");
        response.setEntity(entity);
    }
    
    protected void doService(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
        HttpRequestHandler handler = null;
        if (this.handlerMapper != null) {
            handler = this.handlerMapper.lookup(request);
        }
        if (handler != null) {
            handler.handle(request, response, context);
        }
        else {
            response.setStatusCode(501);
        }
    }
    
    @Deprecated
    private static class HttpRequestHandlerResolverAdapter implements HttpRequestHandlerMapper
    {
        private final HttpRequestHandlerResolver resolver;
        
        public HttpRequestHandlerResolverAdapter(final HttpRequestHandlerResolver resolver) {
            this.resolver = resolver;
        }
        
        public HttpRequestHandler lookup(final HttpRequest request) {
            return this.resolver.lookup(request.getRequestLine().getUri());
        }
    }
}
