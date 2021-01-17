// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.oio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ConnectTimeoutException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelPromise;
import java.net.SocketTimeoutException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.ServerSocketChannel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.Channel;
import java.net.Socket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.oio.OioByteStreamChannel;

public class OioSocketChannel extends OioByteStreamChannel implements SocketChannel
{
    private static final InternalLogger logger;
    private final Socket socket;
    private final OioSocketChannelConfig config;
    
    public OioSocketChannel() {
        this(new Socket());
    }
    
    public OioSocketChannel(final Socket socket) {
        this(null, socket);
    }
    
    public OioSocketChannel(final Channel parent, final Socket socket) {
        super(parent);
        this.socket = socket;
        this.config = new DefaultOioSocketChannelConfig(this, socket);
        boolean success = false;
        try {
            if (socket.isConnected()) {
                this.activate(socket.getInputStream(), socket.getOutputStream());
            }
            socket.setSoTimeout(1000);
            success = true;
        }
        catch (Exception e) {
            throw new ChannelException("failed to initialize a socket", e);
        }
        finally {
            if (!success) {
                try {
                    socket.close();
                }
                catch (IOException e2) {
                    OioSocketChannel.logger.warn("Failed to close a socket.", e2);
                }
            }
        }
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    @Override
    public OioSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isOpen() {
        return !this.socket.isClosed();
    }
    
    @Override
    public boolean isActive() {
        return !this.socket.isClosed() && this.socket.isConnected();
    }
    
    @Override
    public boolean isInputShutdown() {
        return super.isInputShutdown();
    }
    
    @Override
    public boolean isOutputShutdown() {
        return this.socket.isOutputShutdown() || !this.isActive();
    }
    
    @Override
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }
    
    @Override
    protected int doReadBytes(final ByteBuf buf) throws Exception {
        if (this.socket.isClosed()) {
            return -1;
        }
        try {
            return super.doReadBytes(buf);
        }
        catch (SocketTimeoutException ignored) {
            return 0;
        }
    }
    
    @Override
    public ChannelFuture shutdownOutput(final ChannelPromise future) {
        final EventLoop loop = this.eventLoop();
        if (loop.inEventLoop()) {
            try {
                this.socket.shutdownOutput();
                future.setSuccess();
            }
            catch (Throwable t) {
                future.setFailure(t);
            }
        }
        else {
            loop.execute(new Runnable() {
                @Override
                public void run() {
                    OioSocketChannel.this.shutdownOutput(future);
                }
            });
        }
        return future;
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.socket.getLocalSocketAddress();
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.socket.getRemoteSocketAddress();
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
    }
    
    @Override
    protected void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.socket.bind(localAddress);
        }
        boolean success = false;
        try {
            this.socket.connect(remoteAddress, this.config().getConnectTimeoutMillis());
            this.activate(this.socket.getInputStream(), this.socket.getOutputStream());
            success = true;
        }
        catch (SocketTimeoutException e) {
            final ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
            cause.setStackTrace(e.getStackTrace());
            throw cause;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.socket.close();
    }
    
    @Override
    protected boolean checkInputShutdown() {
        if (this.isInputShutdown()) {
            try {
                Thread.sleep(this.config().getSoTimeout());
            }
            catch (Throwable t) {}
            return true;
        }
        return false;
    }
    
    @Override
    protected void setReadPending(final boolean readPending) {
        super.setReadPending(readPending);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OioSocketChannel.class);
    }
}
