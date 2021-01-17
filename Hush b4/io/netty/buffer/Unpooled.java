// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Unpooled
{
    private static final ByteBufAllocator ALLOC;
    public static final ByteOrder BIG_ENDIAN;
    public static final ByteOrder LITTLE_ENDIAN;
    public static final ByteBuf EMPTY_BUFFER;
    
    public static ByteBuf buffer() {
        return Unpooled.ALLOC.heapBuffer();
    }
    
    public static ByteBuf directBuffer() {
        return Unpooled.ALLOC.directBuffer();
    }
    
    public static ByteBuf buffer(final int initialCapacity) {
        return Unpooled.ALLOC.heapBuffer(initialCapacity);
    }
    
    public static ByteBuf directBuffer(final int initialCapacity) {
        return Unpooled.ALLOC.directBuffer(initialCapacity);
    }
    
    public static ByteBuf buffer(final int initialCapacity, final int maxCapacity) {
        return Unpooled.ALLOC.heapBuffer(initialCapacity, maxCapacity);
    }
    
    public static ByteBuf directBuffer(final int initialCapacity, final int maxCapacity) {
        return Unpooled.ALLOC.directBuffer(initialCapacity, maxCapacity);
    }
    
    public static ByteBuf wrappedBuffer(final byte[] array) {
        if (array.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return new UnpooledHeapByteBuf(Unpooled.ALLOC, array, array.length);
    }
    
    public static ByteBuf wrappedBuffer(final byte[] array, final int offset, final int length) {
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (offset == 0 && length == array.length) {
            return wrappedBuffer(array);
        }
        return wrappedBuffer(array).slice(offset, length);
    }
    
    public static ByteBuf wrappedBuffer(final ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (buffer.hasArray()) {
            return wrappedBuffer(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining()).order(buffer.order());
        }
        if (PlatformDependent.hasUnsafe()) {
            if (!buffer.isReadOnly()) {
                return new UnpooledUnsafeDirectByteBuf(Unpooled.ALLOC, buffer, buffer.remaining());
            }
            if (buffer.isDirect()) {
                return new ReadOnlyUnsafeDirectByteBuf(Unpooled.ALLOC, buffer);
            }
            return new ReadOnlyByteBufferBuf(Unpooled.ALLOC, buffer);
        }
        else {
            if (buffer.isReadOnly()) {
                return new ReadOnlyByteBufferBuf(Unpooled.ALLOC, buffer);
            }
            return new UnpooledDirectByteBuf(Unpooled.ALLOC, buffer, buffer.remaining());
        }
    }
    
    public static ByteBuf wrappedBuffer(final ByteBuf buffer) {
        if (buffer.isReadable()) {
            return buffer.slice();
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    public static ByteBuf wrappedBuffer(final byte[]... arrays) {
        return wrappedBuffer(16, arrays);
    }
    
    public static ByteBuf wrappedBuffer(final ByteBuf... buffers) {
        return wrappedBuffer(16, buffers);
    }
    
    public static ByteBuf wrappedBuffer(final ByteBuffer... buffers) {
        return wrappedBuffer(16, buffers);
    }
    
    public static ByteBuf wrappedBuffer(final int maxNumComponents, final byte[]... arrays) {
        switch (arrays.length) {
            case 0: {
                break;
            }
            case 1: {
                if (arrays[0].length != 0) {
                    return wrappedBuffer(arrays[0]);
                }
                break;
            }
            default: {
                final List<ByteBuf> components = new ArrayList<ByteBuf>(arrays.length);
                for (final byte[] a : arrays) {
                    if (a == null) {
                        break;
                    }
                    if (a.length > 0) {
                        components.add(wrappedBuffer(a));
                    }
                }
                if (!components.isEmpty()) {
                    return new CompositeByteBuf(Unpooled.ALLOC, false, maxNumComponents, components);
                }
                break;
            }
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    public static ByteBuf wrappedBuffer(final int maxNumComponents, final ByteBuf... buffers) {
        switch (buffers.length) {
            case 0: {
                break;
            }
            case 1: {
                if (buffers[0].isReadable()) {
                    return wrappedBuffer(buffers[0].order(Unpooled.BIG_ENDIAN));
                }
                break;
            }
            default: {
                for (final ByteBuf b : buffers) {
                    if (b.isReadable()) {
                        return new CompositeByteBuf(Unpooled.ALLOC, false, maxNumComponents, buffers);
                    }
                }
                break;
            }
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    public static ByteBuf wrappedBuffer(final int maxNumComponents, final ByteBuffer... buffers) {
        switch (buffers.length) {
            case 0: {
                break;
            }
            case 1: {
                if (buffers[0].hasRemaining()) {
                    return wrappedBuffer(buffers[0].order(Unpooled.BIG_ENDIAN));
                }
                break;
            }
            default: {
                final List<ByteBuf> components = new ArrayList<ByteBuf>(buffers.length);
                for (final ByteBuffer b : buffers) {
                    if (b == null) {
                        break;
                    }
                    if (b.remaining() > 0) {
                        components.add(wrappedBuffer(b.order(Unpooled.BIG_ENDIAN)));
                    }
                }
                if (!components.isEmpty()) {
                    return new CompositeByteBuf(Unpooled.ALLOC, false, maxNumComponents, components);
                }
                break;
            }
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    public static CompositeByteBuf compositeBuffer() {
        return compositeBuffer(16);
    }
    
    public static CompositeByteBuf compositeBuffer(final int maxNumComponents) {
        return new CompositeByteBuf(Unpooled.ALLOC, false, maxNumComponents);
    }
    
    public static ByteBuf copiedBuffer(final byte[] array) {
        if (array.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return wrappedBuffer(array.clone());
    }
    
    public static ByteBuf copiedBuffer(final byte[] array, final int offset, final int length) {
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final byte[] copy = new byte[length];
        System.arraycopy(array, offset, copy, 0, length);
        return wrappedBuffer(copy);
    }
    
    public static ByteBuf copiedBuffer(final ByteBuffer buffer) {
        final int length = buffer.remaining();
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final byte[] copy = new byte[length];
        final int position = buffer.position();
        try {
            buffer.get(copy);
        }
        finally {
            buffer.position(position);
        }
        return wrappedBuffer(copy).order(buffer.order());
    }
    
    public static ByteBuf copiedBuffer(final ByteBuf buffer) {
        final int readable = buffer.readableBytes();
        if (readable > 0) {
            final ByteBuf copy = buffer(readable);
            copy.writeBytes(buffer, buffer.readerIndex(), readable);
            return copy;
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    public static ByteBuf copiedBuffer(final byte[]... arrays) {
        switch (arrays.length) {
            case 0: {
                return Unpooled.EMPTY_BUFFER;
            }
            case 1: {
                if (arrays[0].length == 0) {
                    return Unpooled.EMPTY_BUFFER;
                }
                return copiedBuffer(arrays[0]);
            }
            default: {
                int length = 0;
                for (final byte[] a : arrays) {
                    if (Integer.MAX_VALUE - length < a.length) {
                        throw new IllegalArgumentException("The total length of the specified arrays is too big.");
                    }
                    length += a.length;
                }
                if (length == 0) {
                    return Unpooled.EMPTY_BUFFER;
                }
                final byte[] mergedArray = new byte[length];
                int i = 0;
                int j = 0;
                while (i < arrays.length) {
                    final byte[] a = arrays[i];
                    System.arraycopy(a, 0, mergedArray, j, a.length);
                    j += a.length;
                    ++i;
                }
                return wrappedBuffer(mergedArray);
            }
        }
    }
    
    public static ByteBuf copiedBuffer(final ByteBuf... buffers) {
        switch (buffers.length) {
            case 0: {
                return Unpooled.EMPTY_BUFFER;
            }
            case 1: {
                return copiedBuffer(buffers[0]);
            }
            default: {
                ByteOrder order = null;
                int length = 0;
                for (final ByteBuf b : buffers) {
                    final int bLen = b.readableBytes();
                    if (bLen > 0) {
                        if (Integer.MAX_VALUE - length < bLen) {
                            throw new IllegalArgumentException("The total length of the specified buffers is too big.");
                        }
                        length += bLen;
                        if (order != null) {
                            if (!order.equals(b.order())) {
                                throw new IllegalArgumentException("inconsistent byte order");
                            }
                        }
                        else {
                            order = b.order();
                        }
                    }
                }
                if (length == 0) {
                    return Unpooled.EMPTY_BUFFER;
                }
                final byte[] mergedArray = new byte[length];
                int i = 0;
                int j = 0;
                while (i < buffers.length) {
                    final ByteBuf b = buffers[i];
                    final int bLen = b.readableBytes();
                    b.getBytes(b.readerIndex(), mergedArray, j, bLen);
                    j += bLen;
                    ++i;
                }
                return wrappedBuffer(mergedArray).order(order);
            }
        }
    }
    
    public static ByteBuf copiedBuffer(final ByteBuffer... buffers) {
        switch (buffers.length) {
            case 0: {
                return Unpooled.EMPTY_BUFFER;
            }
            case 1: {
                return copiedBuffer(buffers[0]);
            }
            default: {
                ByteOrder order = null;
                int length = 0;
                for (final ByteBuffer b : buffers) {
                    final int bLen = b.remaining();
                    if (bLen > 0) {
                        if (Integer.MAX_VALUE - length < bLen) {
                            throw new IllegalArgumentException("The total length of the specified buffers is too big.");
                        }
                        length += bLen;
                        if (order != null) {
                            if (!order.equals(b.order())) {
                                throw new IllegalArgumentException("inconsistent byte order");
                            }
                        }
                        else {
                            order = b.order();
                        }
                    }
                }
                if (length == 0) {
                    return Unpooled.EMPTY_BUFFER;
                }
                final byte[] mergedArray = new byte[length];
                int i = 0;
                int j = 0;
                while (i < buffers.length) {
                    final ByteBuffer b = buffers[i];
                    final int bLen = b.remaining();
                    final int oldPos = b.position();
                    b.get(mergedArray, j, bLen);
                    b.position(oldPos);
                    j += bLen;
                    ++i;
                }
                return wrappedBuffer(mergedArray).order(order);
            }
        }
    }
    
    public static ByteBuf copiedBuffer(final CharSequence string, final Charset charset) {
        if (string == null) {
            throw new NullPointerException("string");
        }
        if (string instanceof CharBuffer) {
            return copiedBuffer((CharBuffer)string, charset);
        }
        return copiedBuffer(CharBuffer.wrap(string), charset);
    }
    
    public static ByteBuf copiedBuffer(final CharSequence string, final int offset, final int length, final Charset charset) {
        if (string == null) {
            throw new NullPointerException("string");
        }
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (!(string instanceof CharBuffer)) {
            return copiedBuffer(CharBuffer.wrap(string, offset, offset + length), charset);
        }
        CharBuffer buf = (CharBuffer)string;
        if (buf.hasArray()) {
            return copiedBuffer(buf.array(), buf.arrayOffset() + buf.position() + offset, length, charset);
        }
        buf = buf.slice();
        buf.limit(length);
        buf.position(offset);
        return copiedBuffer(buf, charset);
    }
    
    public static ByteBuf copiedBuffer(final char[] array, final Charset charset) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        return copiedBuffer(array, 0, array.length, charset);
    }
    
    public static ByteBuf copiedBuffer(final char[] array, final int offset, final int length, final Charset charset) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        return copiedBuffer(CharBuffer.wrap(array, offset, length), charset);
    }
    
    private static ByteBuf copiedBuffer(final CharBuffer buffer, final Charset charset) {
        return ByteBufUtil.encodeString0(Unpooled.ALLOC, true, buffer, charset);
    }
    
    public static ByteBuf unmodifiableBuffer(final ByteBuf buffer) {
        final ByteOrder endianness = buffer.order();
        if (endianness == Unpooled.BIG_ENDIAN) {
            return new ReadOnlyByteBuf(buffer);
        }
        return new ReadOnlyByteBuf(buffer.order(Unpooled.BIG_ENDIAN)).order(Unpooled.LITTLE_ENDIAN);
    }
    
    public static ByteBuf copyInt(final int value) {
        final ByteBuf buf = buffer(4);
        buf.writeInt(value);
        return buf;
    }
    
    public static ByteBuf copyInt(final int... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 4);
        for (final int v : values) {
            buffer.writeInt(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyShort(final int value) {
        final ByteBuf buf = buffer(2);
        buf.writeShort(value);
        return buf;
    }
    
    public static ByteBuf copyShort(final short... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 2);
        for (final int v : values) {
            buffer.writeShort(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyShort(final int... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 2);
        for (final int v : values) {
            buffer.writeShort(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyMedium(final int value) {
        final ByteBuf buf = buffer(3);
        buf.writeMedium(value);
        return buf;
    }
    
    public static ByteBuf copyMedium(final int... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 3);
        for (final int v : values) {
            buffer.writeMedium(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyLong(final long value) {
        final ByteBuf buf = buffer(8);
        buf.writeLong(value);
        return buf;
    }
    
    public static ByteBuf copyLong(final long... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 8);
        for (final long v : values) {
            buffer.writeLong(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyBoolean(final boolean value) {
        final ByteBuf buf = buffer(1);
        buf.writeBoolean(value);
        return buf;
    }
    
    public static ByteBuf copyBoolean(final boolean... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length);
        for (final boolean v : values) {
            buffer.writeBoolean(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyFloat(final float value) {
        final ByteBuf buf = buffer(4);
        buf.writeFloat(value);
        return buf;
    }
    
    public static ByteBuf copyFloat(final float... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 4);
        for (final float v : values) {
            buffer.writeFloat(v);
        }
        return buffer;
    }
    
    public static ByteBuf copyDouble(final double value) {
        final ByteBuf buf = buffer(8);
        buf.writeDouble(value);
        return buf;
    }
    
    public static ByteBuf copyDouble(final double... values) {
        if (values == null || values.length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buffer = buffer(values.length * 8);
        for (final double v : values) {
            buffer.writeDouble(v);
        }
        return buffer;
    }
    
    public static ByteBuf unreleasableBuffer(final ByteBuf buf) {
        return new UnreleasableByteBuf(buf);
    }
    
    private Unpooled() {
    }
    
    static {
        ALLOC = UnpooledByteBufAllocator.DEFAULT;
        BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        EMPTY_BUFFER = Unpooled.ALLOC.buffer(0, 0);
    }
}
