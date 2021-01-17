// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.util.internal.StringUtil;
import java.util.List;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter
{
    ByteBuf cumulation;
    private boolean singleDecode;
    private boolean decodeWasNull;
    private boolean first;
    
    protected ByteToMessageDecoder() {
        if (this.isSharable()) {
            throw new IllegalStateException("@Sharable annotation is not allowed");
        }
    }
    
    public void setSingleDecode(final boolean singleDecode) {
        this.singleDecode = singleDecode;
    }
    
    public boolean isSingleDecode() {
        return this.singleDecode;
    }
    
    protected int actualReadableBytes() {
        return this.internalBuffer().readableBytes();
    }
    
    protected ByteBuf internalBuffer() {
        if (this.cumulation != null) {
            return this.cumulation;
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    @Override
    public final void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        final ByteBuf buf = this.internalBuffer();
        final int readable = buf.readableBytes();
        if (buf.isReadable()) {
            final ByteBuf bytes = buf.readBytes(readable);
            buf.release();
            ctx.fireChannelRead(bytes);
        }
        else {
            buf.release();
        }
        this.cumulation = null;
        ctx.fireChannelReadComplete();
        this.handlerRemoved0(ctx);
    }
    
    protected void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final RecyclableArrayList out = RecyclableArrayList.newInstance();
            try {
                final ByteBuf data = (ByteBuf)msg;
                this.first = (this.cumulation == null);
                if (this.first) {
                    this.cumulation = data;
                }
                else {
                    if (this.cumulation.writerIndex() > this.cumulation.maxCapacity() - data.readableBytes() || this.cumulation.refCnt() > 1) {
                        this.expandCumulation(ctx, data.readableBytes());
                    }
                    this.cumulation.writeBytes(data);
                    data.release();
                }
                this.callDecode(ctx, this.cumulation, out);
            }
            catch (DecoderException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new DecoderException(t);
            }
            finally {
                if (this.cumulation != null && !this.cumulation.isReadable()) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                final int size = out.size();
                this.decodeWasNull = (size == 0);
                for (int i = 0; i < size; ++i) {
                    ctx.fireChannelRead(out.get(i));
                }
                out.recycle();
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
    
    private void expandCumulation(final ChannelHandlerContext ctx, final int readable) {
        final ByteBuf oldCumulation = this.cumulation;
        (this.cumulation = ctx.alloc().buffer(oldCumulation.readableBytes() + readable)).writeBytes(oldCumulation);
        oldCumulation.release();
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1) {
            this.cumulation.discardSomeReadBytes();
        }
        if (this.decodeWasNull) {
            this.decodeWasNull = false;
            if (!ctx.channel().config().isAutoRead()) {
                ctx.read();
            }
        }
        ctx.fireChannelReadComplete();
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        final RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            if (this.cumulation != null) {
                this.callDecode(ctx, this.cumulation, out);
                this.decodeLast(ctx, this.cumulation, out);
            }
            else {
                this.decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
            }
        }
        catch (DecoderException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new DecoderException(e2);
        }
        finally {
            try {
                if (this.cumulation != null) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                final int size = out.size();
                for (int i = 0; i < size; ++i) {
                    ctx.fireChannelRead(out.get(i));
                }
                if (size > 0) {
                    ctx.fireChannelReadComplete();
                }
                ctx.fireChannelInactive();
            }
            finally {
                out.recycle();
            }
        }
    }
    
    protected void callDecode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        try {
            while (in.isReadable()) {
                final int outSize = out.size();
                final int oldInputLength = in.readableBytes();
                this.decode(ctx, in, out);
                if (ctx.isRemoved()) {
                    break;
                }
                if (outSize == out.size()) {
                    if (oldInputLength == in.readableBytes()) {
                        break;
                    }
                    continue;
                }
                else {
                    if (oldInputLength == in.readableBytes()) {
                        throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() did not read anything but decoded a message.");
                    }
                    if (this.isSingleDecode()) {
                        break;
                    }
                    continue;
                }
            }
        }
        catch (DecoderException e) {
            throw e;
        }
        catch (Throwable cause) {
            throw new DecoderException(cause);
        }
    }
    
    protected abstract void decode(final ChannelHandlerContext p0, final ByteBuf p1, final List<Object> p2) throws Exception;
    
    protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        this.decode(ctx, in, out);
    }
}
