// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSession;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpResponse;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.HttpRequest;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.config.MessageConstraints;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HttpContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnection;

@NotThreadSafe
public class DefaultManagedHttpClientConnection extends DefaultBHttpClientConnection implements ManagedHttpClientConnection, HttpContext
{
    private final String id;
    private final Map<String, Object> attributes;
    private volatile boolean shutdown;
    
    public DefaultManagedHttpClientConnection(final String id, final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy, final HttpMessageWriterFactory<HttpRequest> requestWriterFactory, final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
        this.id = id;
        this.attributes = new ConcurrentHashMap<String, Object>();
    }
    
    public DefaultManagedHttpClientConnection(final String id, final int buffersize) {
        this(id, buffersize, buffersize, null, null, null, null, null, null, null);
    }
    
    public String getId() {
        return this.id;
    }
    
    @Override
    public void shutdown() throws IOException {
        this.shutdown = true;
        super.shutdown();
    }
    
    public Object getAttribute(final String id) {
        return this.attributes.get(id);
    }
    
    public Object removeAttribute(final String id) {
        return this.attributes.remove(id);
    }
    
    public void setAttribute(final String id, final Object obj) {
        this.attributes.put(id, obj);
    }
    
    @Override
    public void bind(final Socket socket) throws IOException {
        if (this.shutdown) {
            socket.close();
            throw new InterruptedIOException("Connection already shutdown");
        }
        super.bind(socket);
    }
    
    public Socket getSocket() {
        return super.getSocket();
    }
    
    public SSLSession getSSLSession() {
        final Socket socket = super.getSocket();
        if (socket instanceof SSLSocket) {
            return ((SSLSocket)socket).getSession();
        }
        return null;
    }
}
