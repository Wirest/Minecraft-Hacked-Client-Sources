// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.Inflater;

public class JZlibDecoder extends ZlibDecoder
{
    private final Inflater z;
    private byte[] dictionary;
    private volatile boolean finished;
    
    public JZlibDecoder() {
        this(ZlibWrapper.ZLIB);
    }
    
    public JZlibDecoder(final ZlibWrapper wrapper) {
        this.z = new Inflater();
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        final int resultCode = this.z.init(ZlibUtil.convertWrapperType(wrapper));
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
    }
    
    public JZlibDecoder(final byte[] dictionary) {
        this.z = new Inflater();
        if (dictionary == null) {
            throw new NullPointerException("dictionary");
        }
        this.dictionary = dictionary;
        final int resultCode = this.z.inflateInit(JZlib.W_ZLIB);
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
    }
    
    @Override
    public boolean isClosed() {
        return this.finished;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.finished) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (!in.isReadable()) {
            return;
        }
        try {
            final int inputLength = in.readableBytes();
            this.z.avail_in = inputLength;
            if (in.hasArray()) {
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
            final int maxOutputLength = inputLength << 1;
            final ByteBuf decompressed = ctx.alloc().heapBuffer(maxOutputLength);
            try {
            Label_0392:
                while (true) {
                    decompressed.ensureWritable(this.z.avail_out = maxOutputLength);
                    this.z.next_out = decompressed.array();
                    this.z.next_out_index = decompressed.arrayOffset() + decompressed.writerIndex();
                    final int oldNextOutIndex = this.z.next_out_index;
                    int resultCode = this.z.inflate(2);
                    final int outputLength = this.z.next_out_index - oldNextOutIndex;
                    if (outputLength > 0) {
                        decompressed.writerIndex(decompressed.writerIndex() + outputLength);
                    }
                    switch (resultCode) {
                        case 2: {
                            if (this.dictionary == null) {
                                ZlibUtil.fail(this.z, "decompression failure", resultCode);
                                continue;
                            }
                            resultCode = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
                            if (resultCode != 0) {
                                ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode);
                                continue;
                            }
                            continue;
                        }
                        case 1: {
                            this.finished = true;
                            this.z.inflateEnd();
                            break Label_0392;
                        }
                        case 0: {
                            continue;
                        }
                        case -5: {
                            if (this.z.avail_in <= 0) {
                                break Label_0392;
                            }
                            continue;
                        }
                        default: {
                            ZlibUtil.fail(this.z, "decompression failure", resultCode);
                            continue;
                        }
                    }
                }
            }
            finally {
                in.skipBytes(this.z.next_in_index - oldNextInIndex);
                if (decompressed.isReadable()) {
                    out.add(decompressed);
                }
                else {
                    decompressed.release();
                }
            }
        }
        finally {
            this.z.next_in = null;
            this.z.next_out = null;
        }
    }
}
