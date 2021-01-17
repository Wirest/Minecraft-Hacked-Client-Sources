// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSession;
import java.net.Socket;
import java.net.InetAddress;
import org.apache.http.HttpRequest;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import java.io.IOException;
import org.apache.http.HttpConnectionMetrics;
import java.io.InterruptedIOException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HttpContext;
import org.apache.http.conn.ManagedClientConnection;

@Deprecated
@NotThreadSafe
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext
{
    private final ClientConnectionManager connManager;
    private volatile OperatedClientConnection wrappedConnection;
    private volatile boolean markedReusable;
    private volatile boolean released;
    private volatile long duration;
    
    protected AbstractClientConnAdapter(final ClientConnectionManager mgr, final OperatedClientConnection conn) {
        this.connManager = mgr;
        this.wrappedConnection = conn;
        this.markedReusable = false;
        this.released = false;
        this.duration = Long.MAX_VALUE;
    }
    
    protected synchronized void detach() {
        this.wrappedConnection = null;
        this.duration = Long.MAX_VALUE;
    }
    
    protected OperatedClientConnection getWrappedConnection() {
        return this.wrappedConnection;
    }
    
    protected ClientConnectionManager getManager() {
        return this.connManager;
    }
    
    @Deprecated
    protected final void assertNotAborted() throws InterruptedIOException {
        if (this.isReleased()) {
            throw new InterruptedIOException("Connection has been shut down");
        }
    }
    
    protected boolean isReleased() {
        return this.released;
    }
    
    protected final void assertValid(final OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
        if (this.isReleased() || wrappedConn == null) {
            throw new ConnectionShutdownException();
        }
    }
    
    public boolean isOpen() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        return conn != null && conn.isOpen();
    }
    
    public boolean isStale() {
        if (this.isReleased()) {
            return true;
        }
        final OperatedClientConnection conn = this.getWrappedConnection();
        return conn == null || conn.isStale();
    }
    
    public void setSocketTimeout(final int timeout) {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        conn.setSocketTimeout(timeout);
    }
    
    public int getSocketTimeout() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getSocketTimeout();
    }
    
    public HttpConnectionMetrics getMetrics() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getMetrics();
    }
    
    public void flush() throws IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        conn.flush();
    }
    
    public boolean isResponseAvailable(final int timeout) throws IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.isResponseAvailable(timeout);
    }
    
    public void receiveResponseEntity(final HttpResponse response) throws HttpException, IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        this.unmarkReusable();
        conn.receiveResponseEntity(response);
    }
    
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        this.unmarkReusable();
        return conn.receiveResponseHeader();
    }
    
    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        this.unmarkReusable();
        conn.sendRequestEntity(request);
    }
    
    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        this.unmarkReusable();
        conn.sendRequestHeader(request);
    }
    
    public InetAddress getLocalAddress() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getLocalAddress();
    }
    
    public int getLocalPort() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getLocalPort();
    }
    
    public InetAddress getRemoteAddress() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getRemoteAddress();
    }
    
    public int getRemotePort() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.getRemotePort();
    }
    
    public boolean isSecure() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        return conn.isSecure();
    }
    
    public void bind(final Socket socket) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public Socket getSocket() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        if (!this.isOpen()) {
            return null;
        }
        return conn.getSocket();
    }
    
    public SSLSession getSSLSession() {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        if (!this.isOpen()) {
            return null;
        }
        SSLSession result = null;
        final Socket sock = conn.getSocket();
        if (sock instanceof SSLSocket) {
            result = ((SSLSocket)sock).getSession();
        }
        return result;
    }
    
    public void markReusable() {
        this.markedReusable = true;
    }
    
    public void unmarkReusable() {
        this.markedReusable = false;
    }
    
    public boolean isMarkedReusable() {
        return this.markedReusable;
    }
    
    public void setIdleDuration(final long duration, final TimeUnit unit) {
        if (duration > 0L) {
            this.duration = unit.toMillis(duration);
        }
        else {
            this.duration = -1L;
        }
    }
    
    public synchronized void releaseConnection() {
        if (this.released) {
            return;
        }
        this.released = true;
        this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
    }
    
    public synchronized void abortConnection() {
        if (this.released) {
            return;
        }
        this.released = true;
        this.unmarkReusable();
        try {
            this.shutdown();
        }
        catch (IOException ex) {}
        this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
    }
    
    public Object getAttribute(final String id) {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        if (conn instanceof HttpContext) {
            return ((HttpContext)conn).getAttribute(id);
        }
        return null;
    }
    
    public Object removeAttribute(final String id) {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        if (conn instanceof HttpContext) {
            return ((HttpContext)conn).removeAttribute(id);
        }
        return null;
    }
    
    public void setAttribute(final String id, final Object obj) {
        final OperatedClientConnection conn = this.getWrappedConnection();
        this.assertValid(conn);
        if (conn instanceof HttpContext) {
            ((HttpContext)conn).setAttribute(id, obj);
        }
    }
}
