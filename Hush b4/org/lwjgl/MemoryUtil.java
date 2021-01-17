// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.lang.reflect.Field;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;
import java.nio.DoubleBuffer;
import java.nio.LongBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.nio.charset.Charset;

public final class MemoryUtil
{
    private static final Charset ascii;
    private static final Charset utf8;
    private static final Charset utf16;
    private static final Accessor memUtil;
    
    private MemoryUtil() {
    }
    
    public static long getAddress0(final Buffer buffer) {
        return MemoryUtil.memUtil.getAddress(buffer);
    }
    
    public static long getAddress0Safe(final Buffer buffer) {
        return (buffer == null) ? 0L : MemoryUtil.memUtil.getAddress(buffer);
    }
    
    public static long getAddress0(final PointerBuffer buffer) {
        return MemoryUtil.memUtil.getAddress(buffer.getBuffer());
    }
    
    public static long getAddress0Safe(final PointerBuffer buffer) {
        return (buffer == null) ? 0L : MemoryUtil.memUtil.getAddress(buffer.getBuffer());
    }
    
    public static long getAddress(final ByteBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final ByteBuffer buffer, final int position) {
        return getAddress0(buffer) + position;
    }
    
    public static long getAddress(final ShortBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final ShortBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 1);
    }
    
    public static long getAddress(final CharBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final CharBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 1);
    }
    
    public static long getAddress(final IntBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final IntBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 2);
    }
    
    public static long getAddress(final FloatBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final FloatBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 2);
    }
    
    public static long getAddress(final LongBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final LongBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 3);
    }
    
    public static long getAddress(final DoubleBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final DoubleBuffer buffer, final int position) {
        return getAddress0(buffer) + (position << 3);
    }
    
    public static long getAddress(final PointerBuffer buffer) {
        return getAddress(buffer, buffer.position());
    }
    
    public static long getAddress(final PointerBuffer buffer, final int position) {
        return getAddress0(buffer) + position * PointerBuffer.getPointerSize();
    }
    
    public static long getAddressSafe(final ByteBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final ByteBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final ShortBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final ShortBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final CharBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final CharBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final IntBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final IntBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final FloatBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final FloatBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final LongBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final LongBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final DoubleBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final DoubleBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static long getAddressSafe(final PointerBuffer buffer) {
        return (buffer == null) ? 0L : getAddress(buffer);
    }
    
    public static long getAddressSafe(final PointerBuffer buffer, final int position) {
        return (buffer == null) ? 0L : getAddress(buffer, position);
    }
    
    public static ByteBuffer encodeASCII(final CharSequence text) {
        return encode(text, MemoryUtil.ascii);
    }
    
    public static ByteBuffer encodeUTF8(final CharSequence text) {
        return encode(text, MemoryUtil.utf8);
    }
    
    public static ByteBuffer encodeUTF16(final CharSequence text) {
        return encode(text, MemoryUtil.utf16);
    }
    
    private static ByteBuffer encode(final CharSequence text, final Charset charset) {
        if (text == null) {
            return null;
        }
        return encode(CharBuffer.wrap(new CharSequenceNT(text)), charset);
    }
    
    private static ByteBuffer encode(final CharBuffer in, final Charset charset) {
        final CharsetEncoder encoder = charset.newEncoder();
        int n = (int)(in.remaining() * encoder.averageBytesPerChar());
        ByteBuffer out = BufferUtils.createByteBuffer(n);
        if (n == 0 && in.remaining() == 0) {
            return out;
        }
        encoder.reset();
        while (true) {
            CoderResult cr = in.hasRemaining() ? encoder.encode(in, out, true) : CoderResult.UNDERFLOW;
            if (cr.isUnderflow()) {
                cr = encoder.flush(out);
            }
            if (cr.isUnderflow()) {
                break;
            }
            if (cr.isOverflow()) {
                n = 2 * n + 1;
                final ByteBuffer o = BufferUtils.createByteBuffer(n);
                out.flip();
                o.put(out);
                out = o;
            }
            else {
                try {
                    cr.throwException();
                }
                catch (CharacterCodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        out.flip();
        return out;
    }
    
    public static String decodeASCII(final ByteBuffer buffer) {
        return decode(buffer, MemoryUtil.ascii);
    }
    
    public static String decodeUTF8(final ByteBuffer buffer) {
        return decode(buffer, MemoryUtil.utf8);
    }
    
    public static String decodeUTF16(final ByteBuffer buffer) {
        return decode(buffer, MemoryUtil.utf16);
    }
    
    private static String decode(final ByteBuffer buffer, final Charset charset) {
        if (buffer == null) {
            return null;
        }
        return decodeImpl(buffer, charset);
    }
    
    private static String decodeImpl(final ByteBuffer in, final Charset charset) {
        final CharsetDecoder decoder = charset.newDecoder();
        int n = (int)(in.remaining() * decoder.averageCharsPerByte());
        CharBuffer out = BufferUtils.createCharBuffer(n);
        if (n == 0 && in.remaining() == 0) {
            return "";
        }
        decoder.reset();
        while (true) {
            CoderResult cr = in.hasRemaining() ? decoder.decode(in, out, true) : CoderResult.UNDERFLOW;
            if (cr.isUnderflow()) {
                cr = decoder.flush(out);
            }
            if (cr.isUnderflow()) {
                break;
            }
            if (cr.isOverflow()) {
                n = 2 * n + 1;
                final CharBuffer o = BufferUtils.createCharBuffer(n);
                out.flip();
                o.put(out);
                out = o;
            }
            else {
                try {
                    cr.throwException();
                }
                catch (CharacterCodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        out.flip();
        return out.toString();
    }
    
    private static Accessor loadAccessor(final String className) throws Exception {
        return (Accessor)Class.forName(className).newInstance();
    }
    
    static Field getAddressField() throws NoSuchFieldException {
        return getDeclaredFieldRecursive(ByteBuffer.class, "address");
    }
    
    private static Field getDeclaredFieldRecursive(final Class<?> root, final String fieldName) throws NoSuchFieldException {
        Class<?> type = root;
        try {
            return type.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            type = type.getSuperclass();
            if (type == null) {
                throw new NoSuchFieldException(fieldName + " does not exist in " + root.getSimpleName() + " or any of its superclasses.");
            }
            return type.getDeclaredField(fieldName);
        }
    }
    
    static {
        ascii = Charset.forName("ISO-8859-1");
        utf8 = Charset.forName("UTF-8");
        utf16 = Charset.forName("UTF-16LE");
        Accessor util;
        try {
            util = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorUnsafe");
        }
        catch (Exception e0) {
            try {
                util = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorReflectFast");
            }
            catch (Exception e2) {
                try {
                    util = new AccessorReflect();
                }
                catch (Exception e3) {
                    LWJGLUtil.log("Unsupported JVM detected, this will likely result in low performance. Please inform LWJGL developers.");
                    util = new AccessorJNI();
                }
            }
        }
        LWJGLUtil.log("MemoryUtil Accessor: " + util.getClass().getSimpleName());
        memUtil = util;
    }
    
    private static class CharSequenceNT implements CharSequence
    {
        final CharSequence source;
        
        CharSequenceNT(final CharSequence source) {
            this.source = source;
        }
        
        @Override
        public int length() {
            return this.source.length() + 1;
        }
        
        @Override
        public char charAt(final int index) {
            return (index == this.source.length()) ? '\0' : this.source.charAt(index);
        }
        
        @Override
        public CharSequence subSequence(final int start, final int end) {
            return new CharSequenceNT(this.source.subSequence(start, Math.min(end, this.source.length())));
        }
    }
    
    private static class AccessorJNI implements Accessor
    {
        @Override
        public long getAddress(final Buffer buffer) {
            return BufferUtils.getBufferAddress(buffer);
        }
    }
    
    private static class AccessorReflect implements Accessor
    {
        private final Field address;
        
        AccessorReflect() {
            try {
                this.address = MemoryUtil.getAddressField();
            }
            catch (NoSuchFieldException e) {
                throw new UnsupportedOperationException(e);
            }
            this.address.setAccessible(true);
        }
        
        @Override
        public long getAddress(final Buffer buffer) {
            try {
                return this.address.getLong(buffer);
            }
            catch (IllegalAccessException e) {
                return 0L;
            }
        }
    }
    
    interface Accessor
    {
        long getAddress(final Buffer p0);
    }
}
