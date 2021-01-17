// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.util.Args;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.config.MessageConstraints;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpClientConnection;

@NotThreadSafe
public class DefaultBHttpClientConnection extends BHttpConnectionBase implements HttpClientConnection
{
    private final HttpMessageParser<HttpResponse> responseParser;
    private final HttpMessageWriter<HttpRequest> requestWriter;
    
    public DefaultBHttpClientConnection(final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy, final HttpMessageWriterFactory<HttpRequest> requestWriterFactory, final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy);
        this.requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create(this.getSessionOutputBuffer());
        this.responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create(this.getSessionInputBuffer(), constraints);
    }
    
    public DefaultBHttpClientConnection(final int buffersize, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints) {
        this(buffersize, buffersize, chardecoder, charencoder, constraints, null, null, null, null);
    }
    
    public DefaultBHttpClientConnection(final int buffersize) {
        this(buffersize, buffersize, null, null, null, null, null, null, null);
    }
    
    protected void onResponseReceived(final HttpResponse response) {
    }
    
    protected void onRequestSubmitted(final HttpRequest request) {
    }
    
    public void bind(final Socket socket) throws IOException {
        super.bind(socket);
    }
    
    public boolean isResponseAvailable(final int timeout) throws IOException {
        this.ensureOpen();
        try {
            return this.awaitInput(timeout);
        }
        catch (SocketTimeoutException ex) {
            return false;
        }
    }
    
    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.ensureOpen();
        this.requestWriter.write(request);
        this.onRequestSubmitted(request);
        this.incrementRequestCount();
    }
    
    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.ensureOpen();
        final HttpEntity entity = request.getEntity();
        if (entity == null) {
            return;
        }
        final OutputStream outstream = this.prepareOutput(request);
        entity.writeTo(outstream);
        outstream.close();
    }
    
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        this.ensureOpen();
        final HttpResponse response = this.responseParser.parse();
        this.onResponseReceived(response);
        if (response.getStatusLine().getStatusCode() >= 200) {
            this.incrementResponseCount();
        }
        return response;
    }
    
    public void receiveResponseEntity(final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        this.ensureOpen();
        final HttpEntity entity = this.prepareInput(response);
        response.setEntity(entity);
    }
    
    public void flush() throws IOException {
        this.ensureOpen();
        this.doFlush();
    }
}
