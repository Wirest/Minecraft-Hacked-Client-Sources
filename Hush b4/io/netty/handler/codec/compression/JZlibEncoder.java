// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.Unpooled;
import io.netty.util.internal.EmptyArrays;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import com.jcraft.jzlib.JZlib;
import io.netty.channel.ChannelHandlerContext;
import com.jcraft.jzlib.Deflater;

public class JZlibEncoder extends ZlibEncoder
{
    private final int wrapperOverhead;
    private final Deflater z;
    private volatile boolean finished;
    private volatile ChannelHandlerContext ctx;
    
    public JZlibEncoder() {
        this(6);
    }
    
    public JZlibEncoder(final int compressionLevel) {
        this(ZlibWrapper.ZLIB, compressionLevel);
    }
    
    public JZlibEncoder(final ZlibWrapper wrapper) {
        this(wrapper, 6);
    }
    
    public JZlibEncoder(final ZlibWrapper wrapper, final int compressionLevel) {
        this(wrapper, compressionLevel, 15, 8);
    }
    
    public JZlibEncoder(final ZlibWrapper wrapper, final int compressionLevel, final int windowBits, final int memLevel) {
        this.z = new Deflater();
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
        }
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
        }
        final int resultCode = this.z.init(compressionLevel, windowBits, memLevel, ZlibUtil.convertWrapperType(wrapper));
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
        this.wrapperOverhead = ZlibUtil.wrapperOverhead(wrapper);
    }
    
    public JZlibEncoder(final byte[] dictionary) {
        this(6, dictionary);
    }
    
    public JZlibEncoder(final int compressionLevel, final byte[] dictionary) {
        this(compressionLevel, 15, 8, dictionary);
    }
    
    public JZlibEncoder(final int compressionLevel, final int windowBits, final int memLevel, final byte[] dictionary) {
        this.z = new Deflater();
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
        }
        if (dictionary == null) {
            throw new NullPointerException("dictionary");
        }
        int resultCode = this.z.deflateInit(compressionLevel, windowBits, memLevel, JZlib.W_ZLIB);
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
        else {
            resultCode = this.z.deflateSetDictionary(dictionary, dictionary.length);
            if (resultCode != 0) {
                ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode);
            }
        }
        this.wrapperOverhead = ZlibUtil.wrapperOverhead(ZlibWrapper.ZLIB);
    }
    
    @Override
    public ChannelFuture close() {
        return this.close(this.ctx().channel().newPromise());
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
                final ChannelFuture f = JZlibEncoder.this.finishEncode(JZlibEncoder.this.ctx(), p);
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
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        if (this.finished) {
            return;
        }
        try {
            final int inputLength = in.readableBytes();
            final boolean inHasArray = in.hasArray();
            this.z.avail_in = inputLength;
            if (inHasArray) {
                this.z.next_in = in.array();
                this.z.next_in_index = in.arrayOffset() + in.readerIndex();
            }
            else {
                final byte[] array = new byte[inputLength];
                in.getBytes(in.readerIndex(), array);
                this.z.next_in = array;
                this.z.next_in_index = 0;
            }
            final int oldNextInIndex = this.z.next_in_index;
            final int maxOutputLength = (int)Math.ceil(inputLength * 1.001) + 12 + this.wrapperOverhead;
            out.ensureWritable(maxOutputLength);
            this.z.avail_out = maxOutputLength;
            this.z.next_out = out.array();
            this.z.next_out_index = out.arrayOffset() + out.writerIndex();
            final int oldNextOutIndex = this.z.next_out_index;
            int resultCode;
            try {
                resultCode = this.z.deflate(2);
            }
            finally {
                in.skipBytes(this.z.next_in_index - oldNextInIndex);
            }
            if (resultCode != 0) {
                ZlibUtil.fail(this.z, "compression failure", resultCode);
            }
            final int outputLength = this.z.next_out_index - oldNextOutIndex;
            if (outputLength > 0) {
                out.writerIndex(out.writerIndex() + outputLength);
            }
        }
        finally {
            this.z.next_in = null;
            this.z.next_out = null;
        }
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) {
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
        ByteBuf footer;
        try {
            this.z.next_in = EmptyArrays.EMPTY_BYTES;
            this.z.next_in_index = 0;
            this.z.avail_in = 0;
            final byte[] out = new byte[32];
            this.z.next_out = out;
            this.z.next_out_index = 0;
            this.z.avail_out = out.length;
            final int resultCode = this.z.deflate(4);
            if (resultCode != 0 && resultCode != 1) {
                promise.setFailure((Throwable)ZlibUtil.deflaterException(this.z, "compression failure", resultCode));
                return promise;
            }
            if (this.z.next_out_index != 0) {
                footer = Unpooled.wrappedBuffer(out, 0, this.z.next_out_index);
            }
            else {
                footer = Unpooled.EMPTY_BUFFER;
            }
        }
        finally {
            this.z.deflateEnd();
            this.z.next_in = null;
            this.z.next_out = null;
        }
        return ctx.writeAndFlush(footer, promise);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
}
