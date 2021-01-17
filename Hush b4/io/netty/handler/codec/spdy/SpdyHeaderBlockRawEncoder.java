// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Iterator;
import java.util.Set;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class SpdyHeaderBlockRawEncoder extends SpdyHeaderBlockEncoder
{
    private final int version;
    
    public SpdyHeaderBlockRawEncoder(final SpdyVersion version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version.getVersion();
    }
    
    private static void setLengthField(final ByteBuf buffer, final int writerIndex, final int length) {
        buffer.setInt(writerIndex, length);
    }
    
    private static void writeLengthField(final ByteBuf buffer, final int length) {
        buffer.writeInt(length);
    }
    
    public ByteBuf encode(final SpdyHeadersFrame frame) throws Exception {
        final Set<String> names = frame.headers().names();
        final int numHeaders = names.size();
        if (numHeaders == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (numHeaders > 65535) {
            throw new IllegalArgumentException("header block contains too many headers");
        }
        final ByteBuf headerBlock = Unpooled.buffer();
        writeLengthField(headerBlock, numHeaders);
        for (final String name : names) {
            final byte[] nameBytes = name.getBytes("UTF-8");
            writeLengthField(headerBlock, nameBytes.length);
            headerBlock.writeBytes(nameBytes);
            final int savedIndex = headerBlock.writerIndex();
            int valueLength = 0;
            writeLengthField(headerBlock, valueLength);
            for (final String value : frame.headers().getAll(name)) {
                final byte[] valueBytes = value.getBytes("UTF-8");
                if (valueBytes.length > 0) {
                    headerBlock.writeBytes(valueBytes);
                    headerBlock.writeByte(0);
                    valueLength += valueBytes.length + 1;
                }
            }
            if (valueLength != 0) {
                --valueLength;
            }
            if (valueLength > 65535) {
                throw new IllegalArgumentException("header exceeds allowable length: " + name);
            }
            if (valueLength <= 0) {
                continue;
            }
            setLengthField(headerBlock, savedIndex, valueLength);
            headerBlock.writerIndex(headerBlock.writerIndex() - 1);
        }
        return headerBlock;
    }
    
    @Override
    void end() {
    }
}
