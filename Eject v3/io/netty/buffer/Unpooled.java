package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class Unpooled {
    public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
    public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
    private static final ByteBufAllocator ALLOC = UnpooledByteBufAllocator.DEFAULT;
    public static final ByteBuf EMPTY_BUFFER = ALLOC.buffer(0, 0);

    public static ByteBuf buffer() {
        return ALLOC.heapBuffer();
    }

    public static ByteBuf directBuffer() {
        return ALLOC.directBuffer();
    }

    public static ByteBuf buffer(int paramInt) {
        return ALLOC.heapBuffer(paramInt);
    }

    public static ByteBuf directBuffer(int paramInt) {
        return ALLOC.directBuffer(paramInt);
    }

    public static ByteBuf buffer(int paramInt1, int paramInt2) {
        return ALLOC.heapBuffer(paramInt1, paramInt2);
    }

    public static ByteBuf directBuffer(int paramInt1, int paramInt2) {
        return ALLOC.directBuffer(paramInt1, paramInt2);
    }

    public static ByteBuf wrappedBuffer(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length == 0) {
            return EMPTY_BUFFER;
        }
        return new UnpooledHeapByteBuf(ALLOC, paramArrayOfByte, paramArrayOfByte.length);
    }

    public static ByteBuf wrappedBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramInt2 == 0) {
            return EMPTY_BUFFER;
        }
        if ((paramInt1 == 0) && (paramInt2 == paramArrayOfByte.length)) {
            return wrappedBuffer(paramArrayOfByte);
        }
        return wrappedBuffer(paramArrayOfByte).slice(paramInt1, paramInt2);
    }

    public static ByteBuf wrappedBuffer(ByteBuffer paramByteBuffer) {
        if (!paramByteBuffer.hasRemaining()) {
            return EMPTY_BUFFER;
        }
        if (paramByteBuffer.hasArray()) {
            return wrappedBuffer(paramByteBuffer.array(), paramByteBuffer.arrayOffset() | paramByteBuffer.position(), paramByteBuffer.remaining()).order(paramByteBuffer.order());
        }
        if (PlatformDependent.hasUnsafe()) {
            if (paramByteBuffer.isReadOnly()) {
                if (paramByteBuffer.isDirect()) {
                    return new ReadOnlyUnsafeDirectByteBuf(ALLOC, paramByteBuffer);
                }
                return new ReadOnlyByteBufferBuf(ALLOC, paramByteBuffer);
            }
            return new UnpooledUnsafeDirectByteBuf(ALLOC, paramByteBuffer, paramByteBuffer.remaining());
        }
        if (paramByteBuffer.isReadOnly()) {
            return new ReadOnlyByteBufferBuf(ALLOC, paramByteBuffer);
        }
        return new UnpooledDirectByteBuf(ALLOC, paramByteBuffer, paramByteBuffer.remaining());
    }

    public static ByteBuf wrappedBuffer(ByteBuf paramByteBuf) {
        if (paramByteBuf.isReadable()) {
            return paramByteBuf.slice();
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf wrappedBuffer(byte[]... paramVarArgs) {
        return wrappedBuffer(16, paramVarArgs);
    }

    public static ByteBuf wrappedBuffer(ByteBuf... paramVarArgs) {
        return wrappedBuffer(16, paramVarArgs);
    }

    public static ByteBuf wrappedBuffer(ByteBuffer... paramVarArgs) {
        return wrappedBuffer(16, paramVarArgs);
    }

    public static ByteBuf wrappedBuffer(int paramInt, byte[]... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                break;
            case 1:
                if (paramVarArgs[0].length != 0) {
                    return wrappedBuffer(paramVarArgs[0]);
                }
                break;
            default:
                ArrayList localArrayList = new ArrayList(paramVarArgs.length);
                for (byte[] arrayOfByte1 : paramVarArgs) {
                    if (arrayOfByte1 == null) {
                        break;
                    }
                    if (arrayOfByte1.length > 0) {
                        localArrayList.add(wrappedBuffer(arrayOfByte1));
                    }
                }
                if (!localArrayList.isEmpty()) {
                    return new CompositeByteBuf(ALLOC, false, paramInt, localArrayList);
                }
                break;
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf wrappedBuffer(int paramInt, ByteBuf... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                break;
            case 1:
                if (paramVarArgs[0].isReadable()) {
                    return wrappedBuffer(paramVarArgs[0].order(BIG_ENDIAN));
                }
                break;
            default:
                for (ByteBuf localByteBuf : paramVarArgs) {
                    if (localByteBuf.isReadable()) {
                        return new CompositeByteBuf(ALLOC, false, paramInt, paramVarArgs);
                    }
                }
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf wrappedBuffer(int paramInt, ByteBuffer... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                break;
            case 1:
                if (paramVarArgs[0].hasRemaining()) {
                    return wrappedBuffer(paramVarArgs[0].order(BIG_ENDIAN));
                }
                break;
            default:
                ArrayList localArrayList = new ArrayList(paramVarArgs.length);
                for (ByteBuffer localByteBuffer : paramVarArgs) {
                    if (localByteBuffer == null) {
                        break;
                    }
                    if (localByteBuffer.remaining() > 0) {
                        localArrayList.add(wrappedBuffer(localByteBuffer.order(BIG_ENDIAN)));
                    }
                }
                if (!localArrayList.isEmpty()) {
                    return new CompositeByteBuf(ALLOC, false, paramInt, localArrayList);
                }
                break;
        }
        return EMPTY_BUFFER;
    }

    public static CompositeByteBuf compositeBuffer() {
        return compositeBuffer(16);
    }

    public static CompositeByteBuf compositeBuffer(int paramInt) {
        return new CompositeByteBuf(ALLOC, false, paramInt);
    }

    public static ByteBuf copiedBuffer(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length == 0) {
            return EMPTY_BUFFER;
        }
        return wrappedBuffer((byte[]) paramArrayOfByte.clone());
    }

    public static ByteBuf copiedBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramInt2 == 0) {
            return EMPTY_BUFFER;
        }
        byte[] arrayOfByte = new byte[paramInt2];
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
        return wrappedBuffer(arrayOfByte);
    }

    public static ByteBuf copiedBuffer(ByteBuffer paramByteBuffer) {
        int i = paramByteBuffer.remaining();
        if (i == 0) {
            return EMPTY_BUFFER;
        }
        byte[] arrayOfByte = new byte[i];
        int j = paramByteBuffer.position();
        try {
            paramByteBuffer.get(arrayOfByte);
        } finally {
            paramByteBuffer.position(j);
        }
        return wrappedBuffer(arrayOfByte).order(paramByteBuffer.order());
    }

    public static ByteBuf copiedBuffer(ByteBuf paramByteBuf) {
        int i = paramByteBuf.readableBytes();
        if (i > 0) {
            ByteBuf localByteBuf = buffer(i);
            localByteBuf.writeBytes(paramByteBuf, paramByteBuf.readerIndex(), i);
            return localByteBuf;
        }
        return EMPTY_BUFFER;
    }

    public static ByteBuf copiedBuffer(byte[]... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                return EMPTY_BUFFER;
            case 1:
                if (paramVarArgs[0].length == 0) {
                    return EMPTY_BUFFER;
                }
                return copiedBuffer(paramVarArgs[0]);
        }
        int i = 0;
        byte[] arrayOfByte;
        for (arrayOfByte:
             paramVarArgs) {
            if (Integer.MAX_VALUE - i < arrayOfByte.length) {
                throw new IllegalArgumentException("The total length of the specified arrays is too big.");
            }
            i |= arrayOfByte.length;
        }
        if (i == 0) {
            return EMPTY_BUFFER;
        }
    ??? =new byte[i];
    ??? =0;
    ??? =0;
        while (??? <paramVarArgs.length)
        {
            arrayOfByte = paramVarArgs[ ???];
            System.arraycopy(arrayOfByte, 0, ???, ???,arrayOfByte.length);
      ??? |=arrayOfByte.length;
      ???++;
        }
        return wrappedBuffer(( byte[])???);
    }

    public static ByteBuf copiedBuffer(ByteBuf... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                return EMPTY_BUFFER;
            case 1:
                return copiedBuffer(paramVarArgs[0]);
        }
        ByteOrder localByteOrder = null;
        int i = 0;
        ByteBuf localByteBuf;
        int m;
        for (localByteBuf:
             paramVarArgs) {
            m = localByteBuf.readableBytes();
            if (m > 0) {
                if (Integer.MAX_VALUE - i < m) {
                    throw new IllegalArgumentException("The total length of the specified buffers is too big.");
                }
                i |= m;
                if (localByteOrder != null) {
                    if (!localByteOrder.equals(localByteBuf.order())) {
                        throw new IllegalArgumentException("inconsistent byte order");
                    }
                } else {
                    localByteOrder = localByteBuf.order();
                }
            }
        }
        if (i == 0) {
            return EMPTY_BUFFER;
        }
    ??? =new byte[i];
    ??? =0;
    ??? =0;
        while (??? <paramVarArgs.length)
        {
            localByteBuf = paramVarArgs[ ???];
            m = localByteBuf.readableBytes();
            localByteBuf.getBytes(localByteBuf.readerIndex(), ( byte[])???, ???,m);
      ??? |=m;
      ???++;
        }
        return wrappedBuffer(( byte[])???).order(localByteOrder);
    }

    public static ByteBuf copiedBuffer(ByteBuffer... paramVarArgs) {
        switch (paramVarArgs.length) {
            case 0:
                return EMPTY_BUFFER;
            case 1:
                return copiedBuffer(paramVarArgs[0]);
        }
        ByteOrder localByteOrder = null;
        int i = 0;
        ByteBuffer localByteBuffer;
        int m;
        for (localByteBuffer:
             paramVarArgs) {
            m = localByteBuffer.remaining();
            if (m > 0) {
                if (Integer.MAX_VALUE - i < m) {
                    throw new IllegalArgumentException("The total length of the specified buffers is too big.");
                }
                i |= m;
                if (localByteOrder != null) {
                    if (!localByteOrder.equals(localByteBuffer.order())) {
                        throw new IllegalArgumentException("inconsistent byte order");
                    }
                } else {
                    localByteOrder = localByteBuffer.order();
                }
            }
        }
        if (i == 0) {
            return EMPTY_BUFFER;
        }
    ??? =new byte[i];
    ??? =0;
    ??? =0;
        while (??? <paramVarArgs.length)
        {
            localByteBuffer = paramVarArgs[ ???];
            m = localByteBuffer.remaining();
            int n = localByteBuffer.position();
            localByteBuffer.get(( byte[])???, ???,m);
            localByteBuffer.position(n);
      ??? |=m;
      ???++;
        }
        return wrappedBuffer(( byte[])???).order(localByteOrder);
    }

    public static ByteBuf copiedBuffer(CharSequence paramCharSequence, Charset paramCharset) {
        if (paramCharSequence == null) {
            throw new NullPointerException("string");
        }
        if ((paramCharSequence instanceof CharBuffer)) {
            return copiedBuffer((CharBuffer) paramCharSequence, paramCharset);
        }
        return copiedBuffer(CharBuffer.wrap(paramCharSequence), paramCharset);
    }

    public static ByteBuf copiedBuffer(CharSequence paramCharSequence, int paramInt1, int paramInt2, Charset paramCharset) {
        if (paramCharSequence == null) {
            throw new NullPointerException("string");
        }
        if (paramInt2 == 0) {
            return EMPTY_BUFFER;
        }
        if ((paramCharSequence instanceof CharBuffer)) {
            CharBuffer localCharBuffer = (CharBuffer) paramCharSequence;
            if (localCharBuffer.hasArray()) {
                return copiedBuffer(localCharBuffer.array(), localCharBuffer.arrayOffset() | localCharBuffer.position() | paramInt1, paramInt2, paramCharset);
            }
            localCharBuffer = localCharBuffer.slice();
            localCharBuffer.limit(paramInt2);
            localCharBuffer.position(paramInt1);
            return copiedBuffer(localCharBuffer, paramCharset);
        }
        return copiedBuffer(CharBuffer.wrap(paramCharSequence, paramInt1, paramInt1 | paramInt2), paramCharset);
    }

    public static ByteBuf copiedBuffer(char[] paramArrayOfChar, Charset paramCharset) {
        if (paramArrayOfChar == null) {
            throw new NullPointerException("array");
        }
        return copiedBuffer(paramArrayOfChar, 0, paramArrayOfChar.length, paramCharset);
    }

    public static ByteBuf copiedBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2, Charset paramCharset) {
        if (paramArrayOfChar == null) {
            throw new NullPointerException("array");
        }
        if (paramInt2 == 0) {
            return EMPTY_BUFFER;
        }
        return copiedBuffer(CharBuffer.wrap(paramArrayOfChar, paramInt1, paramInt2), paramCharset);
    }

    private static ByteBuf copiedBuffer(CharBuffer paramCharBuffer, Charset paramCharset) {
        return ByteBufUtil.encodeString0(ALLOC, true, paramCharBuffer, paramCharset);
    }

    public static ByteBuf unmodifiableBuffer(ByteBuf paramByteBuf) {
        ByteOrder localByteOrder = paramByteBuf.order();
        if (localByteOrder == BIG_ENDIAN) {
            return new ReadOnlyByteBuf(paramByteBuf);
        }
        return new ReadOnlyByteBuf(paramByteBuf.order(BIG_ENDIAN)).order(LITTLE_ENDIAN);
    }

    public static ByteBuf copyInt(int paramInt) {
        ByteBuf localByteBuf = buffer(4);
        localByteBuf.writeInt(paramInt);
        return localByteBuf;
    }

    public static ByteBuf copyInt(int... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 4);
        for (int k : paramVarArgs) {
            localByteBuf.writeInt(k);
        }
        return localByteBuf;
    }

    public static ByteBuf copyShort(int paramInt) {
        ByteBuf localByteBuf = buffer(2);
        localByteBuf.writeShort(paramInt);
        return localByteBuf;
    }

    public static ByteBuf copyShort(short... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 2);
        for (int k : paramVarArgs) {
            localByteBuf.writeShort(k);
        }
        return localByteBuf;
    }

    public static ByteBuf copyShort(int... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 2);
        for (int k : paramVarArgs) {
            localByteBuf.writeShort(k);
        }
        return localByteBuf;
    }

    public static ByteBuf copyMedium(int paramInt) {
        ByteBuf localByteBuf = buffer(3);
        localByteBuf.writeMedium(paramInt);
        return localByteBuf;
    }

    public static ByteBuf copyMedium(int... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 3);
        for (int k : paramVarArgs) {
            localByteBuf.writeMedium(k);
        }
        return localByteBuf;
    }

    public static ByteBuf copyLong(long paramLong) {
        ByteBuf localByteBuf = buffer(8);
        localByteBuf.writeLong(paramLong);
        return localByteBuf;
    }

    public static ByteBuf copyLong(long... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 8);
        for (long l : paramVarArgs) {
            localByteBuf.writeLong(l);
        }
        return localByteBuf;
    }

    public static ByteBuf copyBoolean(boolean paramBoolean) {
        ByteBuf localByteBuf = buffer(1);
        localByteBuf.writeBoolean(paramBoolean);
        return localByteBuf;
    }

    public static ByteBuf copyBoolean(boolean... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length);
        for (int k : paramVarArgs) {
            localByteBuf.writeBoolean(k);
        }
        return localByteBuf;
    }

    public static ByteBuf copyFloat(float paramFloat) {
        ByteBuf localByteBuf = buffer(4);
        localByteBuf.writeFloat(paramFloat);
        return localByteBuf;
    }

    public static ByteBuf copyFloat(float... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 4);
        for (float f : paramVarArgs) {
            localByteBuf.writeFloat(f);
        }
        return localByteBuf;
    }

    public static ByteBuf copyDouble(double paramDouble) {
        ByteBuf localByteBuf = buffer(8);
        localByteBuf.writeDouble(paramDouble);
        return localByteBuf;
    }

    public static ByteBuf copyDouble(double... paramVarArgs) {
        if ((paramVarArgs == null) || (paramVarArgs.length == 0)) {
            return EMPTY_BUFFER;
        }
        ByteBuf localByteBuf = buffer(paramVarArgs.length * 8);
        for (double d : paramVarArgs) {
            localByteBuf.writeDouble(d);
        }
        return localByteBuf;
    }

    public static ByteBuf unreleasableBuffer(ByteBuf paramByteBuf) {
        return new UnreleasableByteBuf(paramByteBuf);
    }
}




