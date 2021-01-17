// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.oio;

import io.netty.util.internal.StringUtil;
import io.netty.channel.FileRegion;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelConfig;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.ChannelOption;
import java.io.IOException;
import io.netty.channel.Channel;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ChannelMetadata;

public abstract class AbstractOioByteChannel extends AbstractOioChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private RecvByteBufAllocator.Handle allocHandle;
    private volatile boolean inputShutdown;
    
    protected AbstractOioByteChannel(final Channel parent) {
        super(parent);
    }
    
    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractOioByteChannel.METADATA;
    }
    
    protected boolean checkInputShutdown() {
        if (this.inputShutdown) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ex) {}
            return true;
        }
        return false;
    }
    
    @Override
    protected void doRead() {
        if (this.checkInputShutdown()) {
            return;
        }
        final ChannelConfig config = this.config();
        final ChannelPipeline pipeline = this.pipeline();
        RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
        if (allocHandle == null) {
            allocHandle = (this.allocHandle = config.getRecvByteBufAllocator().newHandle());
        }
        ByteBuf byteBuf = allocHandle.allocate(this.alloc());
        boolean closed = false;
        boolean read = false;
        Throwable exception = null;
        int localReadAmount = 0;
        try {
            int totalReadAmount = 0;
            do {
                localReadAmount = this.doReadBytes(byteBuf);
                if (localReadAmount > 0) {
                    read = true;
                }
                else if (localReadAmount < 0) {
                    closed = true;
                }
                final int available = this.available();
                if (available <= 0) {
                    break;
                }
                if (!byteBuf.isWritable()) {
                    final int capacity = byteBuf.capacity();
                    final int maxCapacity = byteBuf.maxCapacity();
                    if (capacity == maxCapacity) {
                        if (read) {
                            read = false;
                            pipeline.fireChannelRead(byteBuf);
                            byteBuf = this.alloc().buffer();
                        }
                    }
                    else {
                        final int writerIndex = byteBuf.writerIndex();
                        if (writerIndex + available > maxCapacity) {
                            byteBuf.capacity(maxCapacity);
                        }
                        else {
                            byteBuf.ensureWritable(available);
                        }
                    }
                }
                if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
                    totalReadAmount = Integer.MAX_VALUE;
                    break;
                }
                totalReadAmount += localReadAmount;
            } while (config.isAutoRead());
            allocHandle.record(totalReadAmount);
        }
        catch (Throwable t) {
            exception = t;
        }
        finally {
            if (read) {
                pipeline.fireChannelRead(byteBuf);
            }
            else {
                byteBuf.release();
            }
            pipeline.fireChannelReadComplete();
            if (exception != null) {
                if (exception instanceof IOException) {
                    closed = true;
                    this.pipeline().fireExceptionCaught(exception);
                }
                else {
                    pipeline.fireExceptionCaught(exception);
                    this.unsafe().close(this.voidPromise());
                }
            }
            if (closed) {
                this.inputShutdown = true;
                if (this.isOpen()) {
                    if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                        pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                    }
                    else {
                        this.unsafe().close(this.unsafe().voidPromise());
                    }
                }
            }
            if (localReadAmount == 0 && this.isActive()) {
                this.read();
            }
        }
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                break;
            }
            if (msg instanceof ByteBuf) {
                final ByteBuf buf = (ByteBuf)msg;
                int newReadableBytes;
                for (int readableBytes = buf.readableBytes(); readableBytes > 0; readableBytes = newReadableBytes) {
                    this.doWriteBytes(buf);
                    newReadableBytes = buf.readableBytes();
                    in.progress(readableBytes - newReadableBytes);
                }
                in.remove();
            }
            else if (msg instanceof FileRegion) {
                final FileRegion region = (FileRegion)msg;
                final long transfered = region.transfered();
                this.doWriteFileRegion(region);
                in.progress(region.transfered() - transfered);
                in.remove();
            }
            else {
                in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
            }
        }
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        if (msg instanceof ByteBuf || msg instanceof FileRegion) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + AbstractOioByteChannel.EXPECTED_TYPES);
    }
    
    protected abstract int available();
    
    protected abstract int doReadBytes(final ByteBuf p0) throws Exception;
    
    protected abstract void doWriteBytes(final ByteBuf p0) throws Exception;
    
    protected abstract void doWriteFileRegion(final FileRegion p0) throws Exception;
    
    static {
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    }
}
