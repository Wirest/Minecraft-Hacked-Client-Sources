// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.Header;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.config.MessageConstraints;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LoggingManagedHttpClientConnection extends DefaultManagedHttpClientConnection
{
    private final Log log;
    private final Log headerlog;
    private final Wire wire;
    
    public LoggingManagedHttpClientConnection(final String id, final Log log, final Log headerlog, final Log wirelog, final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy, final HttpMessageWriterFactory<HttpRequest> requestWriterFactory, final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(id, buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
        this.log = log;
        this.headerlog = headerlog;
        this.wire = new Wire(wirelog, id);
    }
    
    @Override
    public void close() throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.getId() + ": Close connection");
        }
        super.close();
    }
    
    @Override
    public void shutdown() throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.getId() + ": Shutdown connection");
        }
        super.shutdown();
    }
    
    @Override
    protected InputStream getSocketInputStream(final Socket socket) throws IOException {
        InputStream in = super.getSocketInputStream(socket);
        if (this.wire.enabled()) {
            in = new LoggingInputStream(in, this.wire);
        }
        return in;
    }
    
    @Override
    protected OutputStream getSocketOutputStream(final Socket socket) throws IOException {
        OutputStream out = super.getSocketOutputStream(socket);
        if (this.wire.enabled()) {
            out = new LoggingOutputStream(out, this.wire);
        }
        return out;
    }
    
    @Override
    protected void onResponseReceived(final HttpResponse response) {
        if (response != null && this.headerlog.isDebugEnabled()) {
            this.headerlog.debug(this.getId() + " << " + response.getStatusLine().toString());
            final Header[] arr$;
            final Header[] headers = arr$ = response.getAllHeaders();
            for (final Header header : arr$) {
                this.headerlog.debug(this.getId() + " << " + header.toString());
            }
        }
    }
    
    @Override
    protected void onRequestSubmitted(final HttpRequest request) {
        if (request != null && this.headerlog.isDebugEnabled()) {
            this.headerlog.debug(this.getId() + " >> " + request.getRequestLine().toString());
            final Header[] arr$;
            final Header[] headers = arr$ = request.getAllHeaders();
            for (final Header header : arr$) {
                this.headerlog.debug(this.getId() + " >> " + header.toString());
            }
        }
    }
}
