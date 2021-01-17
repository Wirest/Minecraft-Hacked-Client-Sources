// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpEntityEnclosingRequest;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.util.Args;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.message.LineFormatter;
import org.apache.http.impl.io.HttpResponseWriter;
import org.apache.http.message.LineParser;
import org.apache.http.impl.io.DefaultHttpRequestParser;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpRequestFactory;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.EofSensor;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.impl.entity.EntityDeserializer;
import org.apache.http.impl.entity.EntitySerializer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpServerConnection;

@Deprecated
@NotThreadSafe
public abstract class AbstractHttpServerConnection implements HttpServerConnection
{
    private final EntitySerializer entityserializer;
    private final EntityDeserializer entitydeserializer;
    private SessionInputBuffer inbuffer;
    private SessionOutputBuffer outbuffer;
    private EofSensor eofSensor;
    private HttpMessageParser<HttpRequest> requestParser;
    private HttpMessageWriter<HttpResponse> responseWriter;
    private HttpConnectionMetricsImpl metrics;
    
    public AbstractHttpServerConnection() {
        this.inbuffer = null;
        this.outbuffer = null;
        this.eofSensor = null;
        this.requestParser = null;
        this.responseWriter = null;
        this.metrics = null;
        this.entityserializer = this.createEntitySerializer();
        this.entitydeserializer = this.createEntityDeserializer();
    }
    
    protected abstract void assertOpen() throws IllegalStateException;
    
    protected EntityDeserializer createEntityDeserializer() {
        return new EntityDeserializer(new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0)));
    }
    
    protected EntitySerializer createEntitySerializer() {
        return new EntitySerializer(new StrictContentLengthStrategy());
    }
    
    protected HttpRequestFactory createHttpRequestFactory() {
        return DefaultHttpRequestFactory.INSTANCE;
    }
    
    protected HttpMessageParser<HttpRequest> createRequestParser(final SessionInputBuffer buffer, final HttpRequestFactory requestFactory, final HttpParams params) {
        return new DefaultHttpRequestParser(buffer, null, requestFactory, params);
    }
    
    protected HttpMessageWriter<HttpResponse> createResponseWriter(final SessionOutputBuffer buffer, final HttpParams params) {
        return new HttpResponseWriter(buffer, null, params);
    }
    
    protected HttpConnectionMetricsImpl createConnectionMetrics(final HttpTransportMetrics inTransportMetric, final HttpTransportMetrics outTransportMetric) {
        return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
    }
    
    protected void init(final SessionInputBuffer inbuffer, final SessionOutputBuffer outbuffer, final HttpParams params) {
        this.inbuffer = Args.notNull(inbuffer, "Input session buffer");
        this.outbuffer = Args.notNull(outbuffer, "Output session buffer");
        if (inbuffer instanceof EofSensor) {
            this.eofSensor = (EofSensor)inbuffer;
        }
        this.requestParser = this.createRequestParser(inbuffer, this.createHttpRequestFactory(), params);
        this.responseWriter = this.createResponseWriter(outbuffer, params);
        this.metrics = this.createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
    }
    
    public HttpRequest receiveRequestHeader() throws HttpException, IOException {
        this.assertOpen();
        final HttpRequest request = this.requestParser.parse();
        this.metrics.incrementRequestCount();
        return request;
    }
    
    public void receiveRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.assertOpen();
        final HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, request);
        request.setEntity(entity);
    }
    
    protected void doFlush() throws IOException {
        this.outbuffer.flush();
    }
    
    public void flush() throws IOException {
        this.assertOpen();
        this.doFlush();
    }
    
    public void sendResponseHeader(final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        this.assertOpen();
        this.responseWriter.write(response);
        if (response.getStatusLine().getStatusCode() >= 200) {
            this.metrics.incrementResponseCount();
        }
    }
    
    public void sendResponseEntity(final HttpResponse response) throws HttpException, IOException {
        if (response.getEntity() == null) {
            return;
        }
        this.entityserializer.serialize(this.outbuffer, response, response.getEntity());
    }
    
    protected boolean isEof() {
        return this.eofSensor != null && this.eofSensor.isEof();
    }
    
    public boolean isStale() {
        if (!this.isOpen()) {
            return true;
        }
        if (this.isEof()) {
            return true;
        }
        try {
            this.inbuffer.isDataAvailable(1);
            return this.isEof();
        }
        catch (IOException ex) {
            return true;
        }
    }
    
    public HttpConnectionMetrics getMetrics() {
        return this.metrics;
    }
}
