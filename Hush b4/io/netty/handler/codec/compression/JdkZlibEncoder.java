// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.util.zip.CRC32;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Deflater;

public class JdkZlibEncoder extends ZlibEncoder
{
    private final ZlibWrapper wrapper;
    private final Deflater deflater;
    private volatile boolean finished;
    private volatile ChannelHandlerContext ctx;
    private final CRC32 crc;
    private static final byte[] gzipHeader;
    private boolean writeHeader;
    
    public JdkZlibEncoder() {
        this(6);
    }
    
    public JdkZlibEncoder(final int compressionLevel) {
        this(ZlibWrapper.ZLIB, compressionLevel);
    }
    
    public JdkZlibEncoder(final ZlibWrapper wrapper) {
        this(wrapper, 6);
    }
    
    public JdkZlibEncoder(final ZlibWrapper wrapper, final int compressionLevel) {
        this.crc = new CRC32();
        this.writeHeader = true;
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
        }
        this.wrapper = wrapper;
        this.deflater = new Deflater(compressionLevel, wrapper != ZlibWrapper.ZLIB);
    }
    
    public JdkZlibEncoder(final byte[] dictionary) {
        this(6, dictionary);
    }
    
    public JdkZlibEncoder(final int compressionLevel, final byte[] dictionary) {
        this.crc = new CRC32();
        this.writeHeader = true;
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (dictionary == null) {
            throw new NullPointerException("dictionary");
        }
        this.wrapper = ZlibWrapper.ZLIB;
        (this.deflater = new Deflater(compressionLevel)).setDictionary(dictionary);
    }
    
    @Override
    public ChannelFuture close() {
        return this.close(this.ctx().newPromise());
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        final ChannelHandlerContext ctx = this.ctx();
        final EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            return this.finishEncode(ctx, promise);
        }
        final ChannelPromise p = ctx.newPromise();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final ChannelFuture f = JdkZlibEncoder.this.finishEncode(JdkZlibEncoder.this.ctx(), p);
                f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
            }
        });
        return p;
    }
    
    private ChannelHandlerContext ctx() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }
    
    @Override
    public boolean isClosed() {
        return this.finished;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf uncompressed, final ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(uncompressed);
            return;
        }
        final int len = uncompressed.readableBytes();
        byte[] inAry;
        int offset;
        if (uncompressed.hasArray()) {
            inAry = uncompressed.array();
            offset = uncompressed.arrayOffset() + uncompressed.readerIndex();
            uncompressed.skipBytes(len);
        }
        else {
            inAry = new byte[len];
            uncompressed.readBytes(inAry);
            offset = 0;
        }
        if (this.writeHeader) {
            this.writeHeader = false;
            if (this.wrapper == ZlibWrapper.GZIP) {
                out.writeBytes(JdkZlibEncoder.gzipHeader);
            }
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            this.crc.update(inAry, offset, len);
        }
        this.deflater.setInput(inAry, offset, len);
        while (!this.deflater.needsInput()) {
            this.deflate(out);
        }
    }
    
    @Override
    protected final ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final ByteBuf msg, final boolean preferDirect) throws Exception {
        int sizeEstimate = (int)Math.ceil(msg.readableBytes() * 1.001) + 12;
        if (this.writeHeader) {
            switch (this.wrapper) {
                case GZIP: {
                    sizeEstimate += JdkZlibEncoder.gzipHeader.length;
                    break;
                }
                case ZLIB: {
                    sizeEstimate += 2;
                    break;
                }
            }
        }
        return ctx.alloc().heapBuffer(sizeEstimate);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        final ChannelFuture f = this.finishEncode(ctx, ctx.newPromise());
        f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture f) throws Exception {
                ctx.close(promise);
            }
        });
        if (!f.isDone()) {
            ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    ctx.close(promise);
                }
            }, 10L, TimeUnit.SECONDS);
        }
    }
    
    private ChannelFuture finishEncode(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        if (this.finished) {
            promise.setSuccess();
            return promise;
        }
        this.finished = true;
        ByteBuf footer = ctx.alloc().heapBuffer();
        if (this.writeHeader && this.wrapper == ZlibWrapper.GZIP) {
            this.writeHeader = false;
            footer.writeBytes(JdkZlibEncoder.gzipHeader);
        }
        this.deflater.finish();
        while (!this.deflater.finished()) {
            this.deflate(footer);
            if (!footer.isWritable()) {
                ctx.write(footer);
                footer = ctx.alloc().heapBuffer();
            }
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            final int crcValue = (int)this.crc.getValue();
            final int uncBytes = this.deflater.getTotalIn();
            footer.writeByte(crcValue);
            footer.writeByte(crcValue >>> 8);
            footer.writeByte(crcValue >>> 16);
            footer.writeByte(crcValue >>> 24);
            footer.writeByte(uncBytes);
            footer.writeByte(uncBytes >>> 8);
            footer.writeByte(uncBytes >>> 16);
            footer.writeByte(uncBytes >>> 24);
        }
        this.deflater.end();
        return ctx.writeAndFlush(footer, promise);
    }
    
    private void deflate(final ByteBuf out) {
        int numBytes;
        do {
            final int writerIndex = out.writerIndex();
            numBytes = this.deflater.deflate(out.array(), out.arrayOffset() + writerIndex, out.writableBytes(), 2);
            out.writerIndex(writerIndex + numBytes);
        } while (numBytes > 0);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    static {
        gzipHeader = new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 };
    }
}
