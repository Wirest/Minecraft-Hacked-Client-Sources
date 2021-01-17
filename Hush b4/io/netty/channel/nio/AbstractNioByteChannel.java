// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.nio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.AbstractChannel;
import java.nio.channels.SelectionKey;
import io.netty.util.internal.StringUtil;
import io.netty.channel.FileRegion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;

public abstract class AbstractNioByteChannel extends AbstractNioChannel
{
    private static final String EXPECTED_TYPES;
    private Runnable flushTask;
    
    protected AbstractNioByteChannel(final Channel parent, final SelectableChannel ch) {
        super(parent, ch, 1);
    }
    
    @Override
    protected AbstractNioUnsafe newUnsafe() {
        return new NioByteUnsafe();
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        int writeSpinCount = -1;
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                this.clearOpWrite();
                break;
            }
            if (msg instanceof ByteBuf) {
                final ByteBuf buf = (ByteBuf)msg;
                final int readableBytes = buf.readableBytes();
                if (readableBytes == 0) {
                    in.remove();
                }
                else {
                    boolean setOpWrite = false;
                    boolean done = false;
                    long flushedAmount = 0L;
                    if (writeSpinCount == -1) {
                        writeSpinCount = this.config().getWriteSpinCount();
                    }
                    for (int i = writeSpinCount - 1; i >= 0; --i) {
                        final int localFlushedAmount = this.doWriteBytes(buf);
                        if (localFlushedAmount == 0) {
                            setOpWrite = true;
                            break;
                        }
                        flushedAmount += localFlushedAmount;
                        if (!buf.isReadable()) {
                            done = true;
                            break;
                        }
                    }
                    in.progress(flushedAmount);
                    if (!done) {
                        this.incompleteWrite(setOpWrite);
                        break;
                    }
                    in.remove();
                }
            }
            else {
                if (!(msg instanceof FileRegion)) {
                    throw new Error();
                }
                final FileRegion region = (FileRegion)msg;
                boolean setOpWrite2 = false;
                boolean done2 = false;
                long flushedAmount2 = 0L;
                if (writeSpinCount == -1) {
                    writeSpinCount = this.config().getWriteSpinCount();
                }
                for (int j = writeSpinCount - 1; j >= 0; --j) {
                    final long localFlushedAmount2 = this.doWriteFileRegion(region);
                    if (localFlushedAmount2 == 0L) {
                        setOpWrite2 = true;
                        break;
                    }
                    flushedAmount2 += localFlushedAmount2;
                    if (region.transfered() >= region.count()) {
                        done2 = true;
                        break;
                    }
                }
                in.progress(flushedAmount2);
                if (!done2) {
                    this.incompleteWrite(setOpWrite2);
                    break;
                }
                in.remove();
            }
        }
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf)msg;
            if (buf.isDirect()) {
                return msg;
            }
            return this.newDirectBuffer(buf);
        }
        else {
            if (msg instanceof FileRegion) {
                return msg;
            }
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + AbstractNioByteChannel.EXPECTED_TYPES);
        }
    }
    
    protected final void incompleteWrite(final boolean setOpWrite) {
        if (setOpWrite) {
            this.setOpWrite();
        }
        else {
            Runnable flushTask = this.flushTask;
            if (flushTask == null) {
                final Runnable flushTask2 = new Runnable() {
                    @Override
                    public void run() {
                        AbstractNioByteChannel.this.flush();
                    }
                };
                this.flushTask = flushTask2;
                flushTask = flushTask2;
            }
            this.eventLoop().execute(flushTask);
        }
    }
    
    protected abstract long doWriteFileRegion(final FileRegion p0) throws Exception;
    
    protected abstract int doReadBytes(final ByteBuf p0) throws Exception;
    
    protected abstract int doWriteBytes(final ByteBuf p0) throws Exception;
    
    protected final void setOpWrite() {
        final SelectionKey key = this.selectionKey();
        if (!key.isValid()) {
            return;
        }
        final int interestOps = key.interestOps();
        if ((interestOps & 0x4) == 0x0) {
            key.interestOps(interestOps | 0x4);
        }
    }
    
    protected final void clearOpWrite() {
        final SelectionKey key = this.selectionKey();
        if (!key.isValid()) {
            return;
        }
        final int interestOps = key.interestOps();
        if ((interestOps & 0x4) != 0x0) {
            key.interestOps(interestOps & 0xFFFFFFFB);
        }
    }
    
    static {
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    }
    
    private final class NioByteUnsafe extends AbstractNioUnsafe
    {
        private RecvByteBufAllocator.Handle allocHandle;
        
        private void closeOnRead(final ChannelPipeline pipeline) {
            final SelectionKey key = AbstractNioByteChannel.this.selectionKey();
            AbstractNioByteChannel.this.setInputShutdown();
            if (AbstractNioByteChannel.this.isOpen()) {
                if (Boolean.TRUE.equals(AbstractNioByteChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                    key.interestOps(key.interestOps() & ~AbstractNioByteChannel.this.readInterestOp);
                    pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                }
                else {
                    this.close(this.voidPromise());
                }
            }
        }
        
        private void handleReadException(final ChannelPipeline pipeline, final ByteBuf byteBuf, final Throwable cause, final boolean close) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    AbstractNioByteChannel.this.setReadPending(false);
                    pipeline.fireChannelRead(byteBuf);
                }
                else {
                    byteBuf.release();
                }
            }
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || cause instanceof IOException) {
                this.closeOnRead(pipeline);
            }
        }
        
        @Override
        public void read() {
            final ChannelConfig config = AbstractNioByteChannel.this.config();
            if (!config.isAutoRead() && !AbstractNioByteChannel.this.isReadPending()) {
                this.removeReadOp();
                return;
            }
            final ChannelPipeline pipeline = AbstractNioByteChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            final int maxMessagesPerRead = config.getMaxMessagesPerRead();
            RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
            if (allocHandle == null) {
                allocHandle = (this.allocHandle = config.getRecvByteBufAllocator().newHandle());
            }
            ByteBuf byteBuf = null;
            int messages = 0;
            boolean close = false;
            try {
                int totalReadAmount = 0;
                boolean readPendingReset = false;
                do {
                    byteBuf = allocHandle.allocate(allocator);
                    final int writable = byteBuf.writableBytes();
                    final int localReadAmount = AbstractNioByteChannel.this.doReadBytes(byteBuf);
                    if (localReadAmount <= 0) {
                        byteBuf.release();
                        close = (localReadAmount < 0);
                        break;
                    }
                    if (!readPendingReset) {
                        readPendingReset = true;
                        AbstractNioByteChannel.this.setReadPending(false);
                    }
                    pipeline.fireChannelRead(byteBuf);
                    byteBuf = null;
                    if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
                        totalReadAmount = Integer.MAX_VALUE;
                        break;
                    }
                    totalReadAmount += localReadAmount;
                    if (!config.isAutoRead()) {
                        break;
                    }
                    if (localReadAmount < writable) {
                        break;
                    }
                } while (++messages < maxMessagesPerRead);
                pipeline.fireChannelReadComplete();
                allocHandle.record(totalReadAmount);
                if (close) {
                    this.closeOnRead(pipeline);
                    close = false;
                }
            }
            catch (Throwable t) {
                this.handleReadException(pipeline, byteBuf, t, close);
            }
            finally {
                if (!config.isAutoRead() && !AbstractNioByteChannel.this.isReadPending()) {
                    this.removeReadOp();
                }
            }
        }
    }
}
