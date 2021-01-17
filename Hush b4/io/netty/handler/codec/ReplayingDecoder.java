// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;

public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
{
    static final Signal REPLAY;
    private final ReplayingDecoderBuffer replayable;
    private S state;
    private int checkpoint;
    
    protected ReplayingDecoder() {
        this(null);
    }
    
    protected ReplayingDecoder(final S initialState) {
        this.replayable = new ReplayingDecoderBuffer();
        this.checkpoint = -1;
        this.state = initialState;
    }
    
    protected void checkpoint() {
        this.checkpoint = this.internalBuffer().readerIndex();
    }
    
    protected void checkpoint(final S state) {
        this.checkpoint();
        this.state(state);
    }
    
    protected S state() {
        return this.state;
    }
    
    protected S state(final S newState) {
        final S oldState = this.state;
        this.state = newState;
        return oldState;
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        final RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            this.replayable.terminate();
            this.callDecode(ctx, this.internalBuffer(), out);
            this.decodeLast(ctx, this.replayable, out);
        }
        catch (Signal replay) {
            replay.expect(ReplayingDecoder.REPLAY);
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
    
    @Override
    protected void callDecode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        this.replayable.setCumulation(in);
        try {
            while (in.isReadable()) {
                final int readerIndex = in.readerIndex();
                this.checkpoint = readerIndex;
                final int oldReaderIndex = readerIndex;
                final int outSize = out.size();
                final S oldState = this.state;
                final int oldInputLength = in.readableBytes();
                try {
                    this.decode(ctx, this.replayable, out);
                    if (ctx.isRemoved()) {
                        break;
                    }
                    if (outSize == out.size()) {
                        if (oldInputLength == in.readableBytes() && oldState == this.state) {
                            throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
                        }
                        continue;
                    }
                }
                catch (Signal replay) {
                    replay.expect(ReplayingDecoder.REPLAY);
                    if (ctx.isRemoved()) {
                        break;
                    }
                    final int checkpoint = this.checkpoint;
                    if (checkpoint >= 0) {
                        in.readerIndex(checkpoint);
                    }
                    break;
                }
                if (oldReaderIndex == in.readerIndex() && oldState == this.state) {
                    throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
                }
                if (this.isSingleDecode()) {
                    break;
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
    
    static {
        REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");
    }
}
