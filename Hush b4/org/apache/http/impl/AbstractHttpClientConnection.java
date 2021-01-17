// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.util.Args;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.message.LineFormatter;
import org.apache.http.impl.io.HttpRequestWriter;
import org.apache.http.message.LineParser;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.EofSensor;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.impl.entity.EntityDeserializer;
import org.apache.http.impl.entity.EntitySerializer;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpClientConnection;

@Deprecated
@NotThreadSafe
public abstract class AbstractHttpClientConnection implements HttpClientConnection
{
    private final EntitySerializer entityserializer;
    private final EntityDeserializer entitydeserializer;
    private SessionInputBuffer inbuffer;
    private SessionOutputBuffer outbuffer;
    private EofSensor eofSensor;
    private HttpMessageParser<HttpResponse> responseParser;
    private HttpMessageWriter<HttpRequest> requestWriter;
    private HttpConnectionMetricsImpl metrics;
    
    public AbstractHttpClientConnection() {
        this.inbuffer = null;
        this.outbuffer = null;
        this.eofSensor = null;
        this.responseParser = null;
        this.requestWriter = null;
        this.metrics = null;
        this.entityserializer = this.createEntitySerializer();
        this.entitydeserializer = this.createEntityDeserializer();
    }
    
    protected abstract void assertOpen() throws IllegalStateException;
    
    protected EntityDeserializer createEntityDeserializer() {
        return new EntityDeserializer(new LaxContentLengthStrategy());
    }
    
    protected EntitySerializer createEntitySerializer() {
        return new EntitySerializer(new StrictContentLengthStrategy());
    }
    
    protected HttpResponseFactory createHttpResponseFactory() {
        return DefaultHttpResponseFactory.INSTANCE;
    }
    
    protected HttpMessageParser<HttpResponse> createResponseParser(final SessionInputBuffer buffer, final HttpResponseFactory responseFactory, final HttpParams params) {
        return new DefaultHttpResponseParser(buffer, null, responseFactory, params);
    }
    
    protected HttpMessageWriter<HttpRequest> createRequestWriter(final SessionOutputBuffer buffer, final HttpParams params) {
        return new HttpRequestWriter(buffer, null, params);
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
        this.responseParser = this.createResponseParser(inbuffer, this.createHttpResponseFactory(), params);
        this.requestWriter = this.createRequestWriter(outbuffer, params);
        this.metrics = this.createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
    }
    
    public boolean isResponseAvailable(final int timeout) throws IOException {
        this.assertOpen();
        try {
            return this.inbuffer.isDataAvailable(timeout);
        }
        catch (SocketTimeoutException ex) {
            return false;
        }
    }
    
    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.assertOpen();
        this.requestWriter.write(request);
        this.metrics.incrementRequestCount();
    }
    
    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.assertOpen();
        if (request.getEntity() == null) {
            return;
        }
        this.entityserializer.serialize(this.outbuffer, request, request.getEntity());
    }
    
    protected void doFlush() throws IOException {
        this.outbuffer.flush();
    }
    
    public void flush() throws IOException {
        this.assertOpen();
        this.doFlush();
    }
    
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        this.assertOpen();
        final HttpResponse response = this.responseParser.parse();
        if (response.getStatusLine().getStatusCode() >= 200) {
            this.metrics.incrementResponseCount();
        }
        return response;
    }
    
    public void receiveResponseEntity(final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        this.assertOpen();
        final HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, response);
        response.setEntity(entity);
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
        catch (SocketTimeoutException ex) {
            return false;
        }
        catch (IOException ex2) {
            return true;
        }
    }
    
    public HttpConnectionMetrics getMetrics() {
        return this.metrics;
    }
}
