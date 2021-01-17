// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

final class APIUtil
{
    private static final int INITIAL_BUFFER_SIZE = 256;
    private static final int INITIAL_LENGTHS_SIZE = 4;
    private static final int BUFFERS_SIZE = 32;
    private char[] array;
    private ByteBuffer buffer;
    private IntBuffer lengths;
    private final IntBuffer ints;
    private final LongBuffer longs;
    private final FloatBuffer floats;
    private final DoubleBuffer doubles;
    
    APIUtil() {
        this.array = new char[256];
        this.buffer = BufferUtils.createByteBuffer(256);
        this.lengths = BufferUtils.createIntBuffer(4);
        this.ints = BufferUtils.createIntBuffer(32);
        this.longs = BufferUtils.createLongBuffer(32);
        this.floats = BufferUtils.createFloatBuffer(32);
        this.doubles = BufferUtils.createDoubleBuffer(32);
    }
    
    private static char[] getArray(final ContextCapabilities caps, final int size) {
        char[] array = caps.util.array;
        if (array.length < size) {
            for (int sizeNew = array.length << 1; sizeNew < size; sizeNew <<= 1) {}
            array = new char[size];
            caps.util.array = array;
        }
        return array;
    }
    
    static ByteBuffer getBufferByte(final ContextCapabilities caps, final int size) {
        ByteBuffer buffer = caps.util.buffer;
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {}
            buffer = BufferUtils.createByteBuffer(size);
            caps.util.buffer = buffer;
        }
        else {
            buffer.clear();
        }
        return buffer;
    }
    
    private static ByteBuffer getBufferByteOffset(final ContextCapabilities caps, final int size) {
        ByteBuffer buffer = caps.util.buffer;
        if (buffer.capacity() < size) {
            for (int sizeNew = buffer.capacity() << 1; sizeNew < size; sizeNew <<= 1) {}
            final ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
            bufferNew.put(buffer);
            buffer = (caps.util.buffer = bufferNew);
        }
        else {
            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        }
        return buffer;
    }
    
    static IntBuffer getBufferInt(final ContextCapabilities caps) {
        return caps.util.ints;
    }
    
    static LongBuffer getBufferLong(final ContextCapabilities caps) {
        return caps.util.longs;
    }
    
    static FloatBuffer getBufferFloat(final ContextCapabilities caps) {
        return caps.util.floats;
    }
    
    static DoubleBuffer getBufferDouble(final ContextCapabilities caps) {
        return caps.util.doubles;
    }
    
    static IntBuffer getLengths(final ContextCapabilities caps) {
        return getLengths(caps, 1);
    }
    
    static IntBuffer getLengths(final ContextCapabilities caps, final int size) {
        IntBuffer lengths = caps.util.lengths;
        if (lengths.capacity() < size) {
            for (int sizeNew = lengths.capacity(); sizeNew < size; sizeNew <<= 1) {}
            lengths = BufferUtils.createIntBuffer(size);
            caps.util.lengths = lengths;
        }
        else {
            lengths.clear();
        }
        return lengths;
    }
    
    private static ByteBuffer encode(final ByteBuffer buffer, final CharSequence string) {
        for (int i = 0; i < string.length(); ++i) {
            final char c = string.charAt(i);
            if (LWJGLUtil.DEBUG && '\u0080' <= c) {
                buffer.put((byte)26);
            }
            else {
                buffer.put((byte)c);
            }
        }
        return buffer;
    }
    
    static String getString(final ContextCapabilities caps, final ByteBuffer buffer) {
        final int length = buffer.remaining();
        final char[] charArray = getArray(caps, length);
        for (int i = buffer.position(); i < buffer.limit(); ++i) {
            charArray[i - buffer.position()] = (char)buffer.get(i);
        }
        return new String(charArray, 0, length);
    }
    
    static long getBuffer(final ContextCapabilities caps, final CharSequence string) {
        final ByteBuffer buffer = encode(getBufferByte(caps, string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getBuffer(final ContextCapabilities caps, final CharSequence string, final int offset) {
        final ByteBuffer buffer = encode(getBufferByteOffset(caps, offset + string.length()), string);
        buffer.flip();
        return MemoryUtil.getAddress(buffer);
    }
    
    static long getBufferNT(final ContextCapabilities caps, final CharSequence string) {
        final ByteBuffer buffer = encode(getBufferByte(caps, string.length() + 1), string);
        buffer.put((byte)0);
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static int getTotalLength(final CharSequence[] strings) {
        int length = 0;
        for (final CharSequence string : strings) {
            length += string.length();
        }
        return length;
    }
    
    static long getBuffer(final ContextCapabilities caps, final CharSequence[] strings) {
        final ByteBuffer buffer = getBufferByte(caps, getTotalLength(strings));
        for (final CharSequence string : strings) {
            encode(buffer, string);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getBufferNT(final ContextCapabilities caps, final CharSequence[] strings) {
        final ByteBuffer buffer = getBufferByte(caps, getTotalLength(strings) + strings.length);
        for (final CharSequence string : strings) {
            encode(buffer, string);
            buffer.put((byte)0);
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getLengths(final ContextCapabilities caps, final CharSequence[] strings) {
        final IntBuffer buffer = getLengths(caps, strings.length);
        for (final CharSequence string : strings) {
            buffer.put(string.length());
        }
        buffer.flip();
        return MemoryUtil.getAddress0(buffer);
    }
    
    static long getInt(final ContextCapabilities caps, final int value) {
        return MemoryUtil.getAddress0(getBufferInt(caps).put(0, value));
    }
    
    static long getBufferByte0(final ContextCapabilities caps) {
        return MemoryUtil.getAddress0(getBufferByte(caps, 0));
    }
}
