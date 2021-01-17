// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import java.net.SocketAddress;
import java.nio.channels.UnresolvedAddressException;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.OneTimeTask;
import io.netty.channel.EventLoop;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.AbstractChannel;

abstract class AbstractEpollChannel extends AbstractChannel
{
    private static final ChannelMetadata DATA;
    private final int readFlag;
    protected int flags;
    protected volatile boolean active;
    volatile int fd;
    int id;
    
    AbstractEpollChannel(final int fd, final int flag) {
        this(null, fd, flag, false);
    }
    
    AbstractEpollChannel(final Channel parent, final int fd, final int flag, final boolean active) {
        super(parent);
        this.fd = fd;
        this.readFlag = flag;
        this.flags |= flag;
        this.active = active;
    }
    
    @Override
    public boolean isActive() {
        return this.active;
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractEpollChannel.DATA;
    }
    
    @Override
    protected void doClose() throws Exception {
        this.active = false;
        this.doDeregister();
        final int fd = this.fd;
        this.fd = -1;
        Native.close(fd);
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }
    
    @Override
    public boolean isOpen() {
        return this.fd != -1;
    }
    
    @Override
    protected void doDeregister() throws Exception {
        ((EpollEventLoop)this.eventLoop()).remove(this);
    }
    
    @Override
    protected void doBeginRead() throws Exception {
        ((AbstractEpollUnsafe)this.unsafe()).readPending = true;
        if ((this.flags & this.readFlag) == 0x0) {
            this.flags |= this.readFlag;
            this.modifyEvents();
        }
    }
    
    final void clearEpollIn() {
        if (this.isRegistered()) {
            final EventLoop loop = this.eventLoop();
            final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)this.unsafe();
            if (loop.inEventLoop()) {
                unsafe.clearEpollIn0();
            }
            else {
                loop.execute(new OneTimeTask() {
                    @Override
                    public void run() {
                        if (!AbstractEpollChannel.this.config().isAutoRead() && !unsafe.readPending) {
                            unsafe.clearEpollIn0();
                        }
                    }
                });
            }
        }
        else {
            this.flags &= ~this.readFlag;
        }
    }
    
    protected final void setEpollOut() {
        if ((this.flags & 0x2) == 0x0) {
            this.flags |= 0x2;
            this.modifyEvents();
        }
    }
    
    protected final void clearEpollOut() {
        if ((this.flags & 0x2) != 0x0) {
            this.flags &= 0xFFFFFFFD;
            this.modifyEvents();
        }
    }
    
    private void modifyEvents() {
        if (this.isOpen()) {
            ((EpollEventLoop)this.eventLoop()).modify(this);
        }
    }
    
    @Override
    protected void doRegister() throws Exception {
        final EpollEventLoop loop = (EpollEventLoop)this.eventLoop();
        loop.add(this);
    }
    
    @Override
    protected abstract AbstractEpollUnsafe newUnsafe();
    
    protected final ByteBuf newDirectBuffer(final ByteBuf buf) {
        return this.newDirectBuffer(buf, buf);
    }
    
    protected final ByteBuf newDirectBuffer(final Object holder, final ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(holder);
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBufAllocator alloc = this.alloc();
        if (alloc.isDirectBufferPooled()) {
            return newDirectBuffer0(holder, buf, alloc, readableBytes);
        }
        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf == null) {
            return newDirectBuffer0(holder, buf, alloc, readableBytes);
        }
        directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
        ReferenceCountUtil.safeRelease(holder);
        return directBuf;
    }
    
    private static ByteBuf newDirectBuffer0(final Object holder, final ByteBuf buf, final ByteBufAllocator alloc, final int capacity) {
        final ByteBuf directBuf = alloc.directBuffer(capacity);
        directBuf.writeBytes(buf, buf.readerIndex(), capacity);
        ReferenceCountUtil.safeRelease(holder);
        return directBuf;
    }
    
    protected static void checkResolvable(final InetSocketAddress addr) {
        if (addr.isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }
    
    static {
        DATA = new ChannelMetadata(false);
    }
    
    protected abstract class AbstractEpollUnsafe extends AbstractUnsafe
    {
        protected boolean readPending;
        
        abstract void epollInReady();
        
        void epollRdHupReady() {
        }
        
        @Override
        protected void flush0() {
            if (this.isFlushPending()) {
                return;
            }
            super.flush0();
        }
        
        void epollOutReady() {
            super.flush0();
        }
        
        private boolean isFlushPending() {
            return (AbstractEpollChannel.this.flags & 0x2) != 0x0;
        }
        
        protected final void clearEpollIn0() {
            if ((AbstractEpollChannel.this.flags & AbstractEpollChannel.this.readFlag) != 0x0) {
                final AbstractEpollChannel this$0 = AbstractEpollChannel.this;
                this$0.flags &= ~AbstractEpollChannel.this.readFlag;
                AbstractEpollChannel.this.modifyEvents();
            }
        }
    }
}
