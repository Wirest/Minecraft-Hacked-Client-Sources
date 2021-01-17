// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.util.Args;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.config.MessageConstraints;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpServerConnection;

@NotThreadSafe
public class DefaultBHttpServerConnection extends BHttpConnectionBase implements HttpServerConnection
{
    private final HttpMessageParser<HttpRequest> requestParser;
    private final HttpMessageWriter<HttpResponse> responseWriter;
    
    public DefaultBHttpServerConnection(final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy, final HttpMessageParserFactory<HttpRequest> requestParserFactory, final HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
        super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, (incomingContentStrategy != null) ? incomingContentStrategy : DisallowIdentityContentLengthStrategy.INSTANCE, outgoingContentStrategy);
        this.requestParser = ((requestParserFactory != null) ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE).create(this.getSessionInputBuffer(), constraints);
        this.responseWriter = ((responseWriterFactory != null) ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE).create(this.getSessionOutputBuffer());
    }
    
    public DefaultBHttpServerConnection(final int buffersize, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints) {
        this(buffersize, buffersize, chardecoder, charencoder, constraints, null, null, null, null);
    }
    
    public DefaultBHttpServerConnection(final int buffersize) {
        this(buffersize, buffersize, null, null, null, null, null, null, null);
    }
    
    protected void onRequestReceived(final HttpRequest request) {
    }
    
    protected void onResponseSubmitted(final HttpResponse response) {
    }
    
    public void bind(final Socket socket) throws IOException {
        super.bind(socket);
    }
    
    public HttpRequest receiveRequestHeader() throws HttpException, IOException {
        this.ensureOpen();
        final HttpRequest request = this.requestParser.parse();
        this.onRequestReceived(request);
        this.incrementRequestCount();
        return request;
    }
    
    public void receiveRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        this.ensureOpen();
        final HttpEntity entity = this.prepareInput(request);
        request.setEntity(entity);
    }
    
    public void sendResponseHeader(final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        this.ensureOpen();
        this.responseWriter.write(response);
        this.onResponseSubmitted(response);
        if (response.getStatusLine().getStatusCode() >= 200) {
            this.incrementResponseCount();
        }
    }
    
    public void sendResponseEntity(final HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        this.ensureOpen();
        final HttpEntity entity = response.getEntity();
        if (entity == null) {
            return;
        }
        final OutputStream outstream = this.prepareOutput(response);
        entity.writeTo(outstream);
        outstream.close();
    }
    
    public void flush() throws IOException {
        this.ensureOpen();
        this.doFlush();
    }
}
