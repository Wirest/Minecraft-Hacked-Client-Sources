// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.net.SocketAddress;
import org.apache.http.util.NetUtils;
import org.apache.http.HttpConnectionMetrics;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.net.InetAddress;
import org.apache.http.Header;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.HttpEntity;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.impl.io.ChunkedOutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.http.util.Asserts;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.util.Args;
import org.apache.http.config.MessageConstraints;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.net.Socket;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpConnection;

@NotThreadSafe
public class BHttpConnectionBase implements HttpConnection, HttpInetConnection
{
    private final SessionInputBufferImpl inbuffer;
    private final SessionOutputBufferImpl outbuffer;
    private final HttpConnectionMetricsImpl connMetrics;
    private final ContentLengthStrategy incomingContentStrategy;
    private final ContentLengthStrategy outgoingContentStrategy;
    private volatile boolean open;
    private volatile Socket socket;
    
    protected BHttpConnectionBase(final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder, final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy) {
        Args.positive(buffersize, "Buffer size");
        final HttpTransportMetricsImpl inTransportMetrics = new HttpTransportMetricsImpl();
        final HttpTransportMetricsImpl outTransportMetrics = new HttpTransportMetricsImpl();
        this.inbuffer = new SessionInputBufferImpl(inTransportMetrics, buffersize, -1, (constraints != null) ? constraints : MessageConstraints.DEFAULT, chardecoder);
        this.outbuffer = new SessionOutputBufferImpl(outTransportMetrics, buffersize, fragmentSizeHint, charencoder);
        this.connMetrics = new HttpConnectionMetricsImpl(inTransportMetrics, outTransportMetrics);
        this.incomingContentStrategy = ((incomingContentStrategy != null) ? incomingContentStrategy : LaxContentLengthStrategy.INSTANCE);
        this.outgoingContentStrategy = ((outgoingContentStrategy != null) ? outgoingContentStrategy : StrictContentLengthStrategy.INSTANCE);
    }
    
    protected void ensureOpen() throws IOException {
        Asserts.check(this.open, "Connection is not open");
        if (!this.inbuffer.isBound()) {
            this.inbuffer.bind(this.getSocketInputStream(this.socket));
        }
        if (!this.outbuffer.isBound()) {
            this.outbuffer.bind(this.getSocketOutputStream(this.socket));
        }
    }
    
    protected InputStream getSocketInputStream(final Socket socket) throws IOException {
        return socket.getInputStream();
    }
    
    protected OutputStream getSocketOutputStream(final Socket socket) throws IOException {
        return socket.getOutputStream();
    }
    
    protected void bind(final Socket socket) throws IOException {
        Args.notNull(socket, "Socket");
        this.socket = socket;
        this.open = true;
        this.inbuffer.bind(null);
        this.outbuffer.bind(null);
    }
    
    protected SessionInputBuffer getSessionInputBuffer() {
        return this.inbuffer;
    }
    
    protected SessionOutputBuffer getSessionOutputBuffer() {
        return this.outbuffer;
    }
    
    protected void doFlush() throws IOException {
        this.outbuffer.flush();
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    protected Socket getSocket() {
        return this.socket;
    }
    
    protected OutputStream createOutputStream(final long len, final SessionOutputBuffer outbuffer) {
        if (len == -2L) {
            return new ChunkedOutputStream(2048, outbuffer);
        }
        if (len == -1L) {
            return new IdentityOutputStream(outbuffer);
        }
        return new ContentLengthOutputStream(outbuffer, len);
    }
    
    protected OutputStream prepareOutput(final HttpMessage message) throws HttpException {
        final long len = this.outgoingContentStrategy.determineLength(message);
        return this.createOutputStream(len, this.outbuffer);
    }
    
    protected InputStream createInputStream(final long len, final SessionInputBuffer inbuffer) {
        if (len == -2L) {
            return new ChunkedInputStream(inbuffer);
        }
        if (len == -1L) {
            return new IdentityInputStream(inbuffer);
        }
        return new ContentLengthInputStream(inbuffer, len);
    }
    
    protected HttpEntity prepareInput(final HttpMessage message) throws HttpException {
        final BasicHttpEntity entity = new BasicHttpEntity();
        final long len = this.incomingContentStrategy.determineLength(message);
        final InputStream instream = this.createInputStream(len, this.inbuffer);
        if (len == -2L) {
            entity.setChunked(true);
            entity.setContentLength(-1L);
            entity.setContent(instream);
        }
        else if (len == -1L) {
            entity.setChunked(false);
            entity.setContentLength(-1L);
            entity.setContent(instream);
        }
        else {
            entity.setChunked(false);
            entity.setContentLength(len);
            entity.setContent(instream);
        }
        final Header contentTypeHeader = message.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            entity.setContentType(contentTypeHeader);
        }
        final Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
        if (contentEncodingHeader != null) {
            entity.setContentEncoding(contentEncodingHeader);
        }
        return entity;
    }
    
    public InetAddress getLocalAddress() {
        if (this.socket != null) {
            return this.socket.getLocalAddress();
        }
        return null;
    }
    
    public int getLocalPort() {
        if (this.socket != null) {
            return this.socket.getLocalPort();
        }
        return -1;
    }
    
    public InetAddress getRemoteAddress() {
        if (this.socket != null) {
            return this.socket.getInetAddress();
        }
        return null;
    }
    
    public int getRemotePort() {
        if (this.socket != null) {
            return this.socket.getPort();
        }
        return -1;
    }
    
    public void setSocketTimeout(final int timeout) {
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(timeout);
            }
            catch (SocketException ex) {}
        }
    }
    
    public int getSocketTimeout() {
        if (this.socket != null) {
            try {
                return this.socket.getSoTimeout();
            }
            catch (SocketException ignore) {
                return -1;
            }
        }
        return -1;
    }
    
    public void shutdown() throws IOException {
        this.open = false;
        final Socket tmpsocket = this.socket;
        if (tmpsocket != null) {
            tmpsocket.close();
        }
    }
    
    public void close() throws IOException {
        if (!this.open) {
            return;
        }
        this.open = false;
        final Socket sock = this.socket;
        try {
            this.inbuffer.clear();
            this.outbuffer.flush();
            try {
                try {
                    sock.shutdownOutput();
                }
                catch (IOException ex) {}
                try {
                    sock.shutdownInput();
                }
                catch (IOException ex2) {}
            }
            catch (UnsupportedOperationException ex3) {}
        }
        finally {
            sock.close();
        }
    }
    
    private int fillInputBuffer(final int timeout) throws IOException {
        final int oldtimeout = this.socket.getSoTimeout();
        try {
            this.socket.setSoTimeout(timeout);
            return this.inbuffer.fillBuffer();
        }
        finally {
            this.socket.setSoTimeout(oldtimeout);
        }
    }
    
    protected boolean awaitInput(final int timeout) throws IOException {
        if (this.inbuffer.hasBufferedData()) {
            return true;
        }
        this.fillInputBuffer(timeout);
        return this.inbuffer.hasBufferedData();
    }
    
    public boolean isStale() {
        if (!this.isOpen()) {
            return true;
        }
        try {
            final int bytesRead = this.fillInputBuffer(1);
            return bytesRead < 0;
        }
        catch (SocketTimeoutException ex) {
            return false;
        }
        catch (IOException ex2) {
            return true;
        }
    }
    
    protected void incrementRequestCount() {
        this.connMetrics.incrementRequestCount();
    }
    
    protected void incrementResponseCount() {
        this.connMetrics.incrementResponseCount();
    }
    
    public HttpConnectionMetrics getMetrics() {
        return this.connMetrics;
    }
    
    @Override
    public String toString() {
        if (this.socket != null) {
            final StringBuilder buffer = new StringBuilder();
            final SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
            final SocketAddress localAddress = this.socket.getLocalSocketAddress();
            if (remoteAddress != null && localAddress != null) {
                NetUtils.formatAddress(buffer, localAddress);
                buffer.append("<->");
                NetUtils.formatAddress(buffer, remoteAddress);
            }
            return buffer.toString();
        }
        return "[Not bound]";
    }
}
