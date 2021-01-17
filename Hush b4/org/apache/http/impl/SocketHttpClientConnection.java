// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.InetAddress;
import org.apache.http.util.Args;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import java.io.IOException;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Asserts;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HttpInetConnection;

@Deprecated
@NotThreadSafe
public class SocketHttpClientConnection extends AbstractHttpClientConnection implements HttpInetConnection
{
    private volatile boolean open;
    private volatile Socket socket;
    
    public SocketHttpClientConnection() {
        this.socket = null;
    }
    
    protected void assertNotOpen() {
        Asserts.check(!this.open, "Connection is already open");
    }
    
    @Override
    protected void assertOpen() {
        Asserts.check(this.open, "Connection is not open");
    }
    
    protected SessionInputBuffer createSessionInputBuffer(final Socket socket, final int buffersize, final HttpParams params) throws IOException {
        return new SocketInputBuffer(socket, buffersize, params);
    }
    
    protected SessionOutputBuffer createSessionOutputBuffer(final Socket socket, final int buffersize, final HttpParams params) throws IOException {
        return new SocketOutputBuffer(socket, buffersize, params);
    }
    
    protected void bind(final Socket socket, final HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        Args.notNull(params, "HTTP parameters");
        this.socket = socket;
        final int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
        this.init(this.createSessionInputBuffer(socket, buffersize, params), this.createSessionOutputBuffer(socket, buffersize, params), params);
        this.open = true;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    protected Socket getSocket() {
        return this.socket;
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
        this.assertOpen();
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
            this.doFlush();
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
    
    private static void formatAddress(final StringBuilder buffer, final SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            final InetSocketAddress addr = (InetSocketAddress)socketAddress;
            buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
        }
        else {
            buffer.append(socketAddress);
        }
    }
    
    @Override
    public String toString() {
        if (this.socket != null) {
            final StringBuilder buffer = new StringBuilder();
            final SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
            final SocketAddress localAddress = this.socket.getLocalSocketAddress();
            if (remoteAddress != null && localAddress != null) {
                formatAddress(buffer, localAddress);
                buffer.append("<->");
                formatAddress(buffer, remoteAddress);
            }
            return buffer.toString();
        }
        return super.toString();
    }
}
