// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.nio.ReadOnlyBufferException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.LongBuffer;
import java.nio.IntBuffer;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class PointerBuffer implements Comparable
{
    private static final boolean is64Bit;
    protected final ByteBuffer pointers;
    protected final Buffer view;
    protected final IntBuffer view32;
    protected final LongBuffer view64;
    
    public PointerBuffer(final int capacity) {
        this(BufferUtils.createByteBuffer(capacity * getPointerSize()));
    }
    
    public PointerBuffer(final ByteBuffer source) {
        if (LWJGLUtil.CHECKS) {
            checkSource(source);
        }
        this.pointers = source.slice().order(source.order());
        if (PointerBuffer.is64Bit) {
            this.view32 = null;
            final LongBuffer longBuffer = this.pointers.asLongBuffer();
            this.view64 = longBuffer;
            this.view = longBuffer;
        }
        else {
            final IntBuffer intBuffer = this.pointers.asIntBuffer();
            this.view32 = intBuffer;
            this.view = intBuffer;
            this.view64 = null;
        }
    }
    
    private static void checkSource(final ByteBuffer source) {
        if (!source.isDirect()) {
            throw new IllegalArgumentException("The source buffer is not direct.");
        }
        final int alignment = PointerBuffer.is64Bit ? 8 : 4;
        if ((MemoryUtil.getAddress0(source) + source.position()) % alignment != 0L || source.remaining() % alignment != 0) {
            throw new IllegalArgumentException("The source buffer is not aligned to " + alignment + " bytes.");
        }
    }
    
    public ByteBuffer getBuffer() {
        return this.pointers;
    }
    
    public static boolean is64Bit() {
        return PointerBuffer.is64Bit;
    }
    
    public static int getPointerSize() {
        return PointerBuffer.is64Bit ? 8 : 4;
    }
    
    public final int capacity() {
        return this.view.capacity();
    }
    
    public final int position() {
        return this.view.position();
    }
    
    public final int positionByte() {
        return this.position() * getPointerSize();
    }
    
    public final PointerBuffer position(final int newPosition) {
        this.view.position(newPosition);
        return this;
    }
    
    public final int limit() {
        return this.view.limit();
    }
    
    public final PointerBuffer limit(final int newLimit) {
        this.view.limit(newLimit);
        return this;
    }
    
    public final PointerBuffer mark() {
        this.view.mark();
        return this;
    }
    
    public final PointerBuffer reset() {
        this.view.reset();
        return this;
    }
    
    public final PointerBuffer clear() {
        this.view.clear();
        return this;
    }
    
    public final PointerBuffer flip() {
        this.view.flip();
        return this;
    }
    
    public final PointerBuffer rewind() {
        this.view.rewind();
        return this;
    }
    
    public final int remaining() {
        return this.view.remaining();
    }
    
    public final int remainingByte() {
        return this.remaining() * getPointerSize();
    }
    
    public final boolean hasRemaining() {
        return this.view.hasRemaining();
    }
    
    public static PointerBuffer allocateDirect(final int capacity) {
        return new PointerBuffer(capacity);
    }
    
    protected PointerBuffer newInstance(final ByteBuffer source) {
        return new PointerBuffer(source);
    }
    
    public PointerBuffer slice() {
        final int pointerSize = getPointerSize();
        this.pointers.position(this.view.position() * pointerSize);
        this.pointers.limit(this.view.limit() * pointerSize);
        try {
            return this.newInstance(this.pointers);
        }
        finally {
            this.pointers.clear();
        }
    }
    
    public PointerBuffer duplicate() {
        final PointerBuffer buffer = this.newInstance(this.pointers);
        buffer.position(this.view.position());
        buffer.limit(this.view.limit());
        return buffer;
    }
    
    public PointerBuffer asReadOnlyBuffer() {
        final PointerBuffer buffer = new PointerBufferR(this.pointers);
        buffer.position(this.view.position());
        buffer.limit(this.view.limit());
        return buffer;
    }
    
    public boolean isReadOnly() {
        return false;
    }
    
    public long get() {
        if (PointerBuffer.is64Bit) {
            return this.view64.get();
        }
        return (long)this.view32.get() & 0xFFFFFFFFL;
    }
    
    public PointerBuffer put(final long l) {
        if (PointerBuffer.is64Bit) {
            this.view64.put(l);
        }
        else {
            this.view32.put((int)l);
        }
        return this;
    }
    
    public PointerBuffer put(final PointerWrapper pointer) {
        return this.put(pointer.getPointer());
    }
    
    public static void put(final ByteBuffer target, final long l) {
        if (PointerBuffer.is64Bit) {
            target.putLong(l);
        }
        else {
            target.putInt((int)l);
        }
    }
    
    public long get(final int index) {
        if (PointerBuffer.is64Bit) {
            return this.view64.get(index);
        }
        return (long)this.view32.get(index) & 0xFFFFFFFFL;
    }
    
    public PointerBuffer put(final int index, final long l) {
        if (PointerBuffer.is64Bit) {
            this.view64.put(index, l);
        }
        else {
            this.view32.put(index, (int)l);
        }
        return this;
    }
    
    public PointerBuffer put(final int index, final PointerWrapper pointer) {
        return this.put(index, pointer.getPointer());
    }
    
    public static void put(final ByteBuffer target, final int index, final long l) {
        if (PointerBuffer.is64Bit) {
            target.putLong(index, l);
        }
        else {
            target.putInt(index, (int)l);
        }
    }
    
    public PointerBuffer get(final long[] dst, final int offset, final int length) {
        if (PointerBuffer.is64Bit) {
            this.view64.get(dst, offset, length);
        }
        else {
            checkBounds(offset, length, dst.length);
            if (length > this.view32.remaining()) {
                throw new BufferUnderflowException();
            }
            for (int end = offset + length, i = offset; i < end; ++i) {
                dst[i] = ((long)this.view32.get() & 0xFFFFFFFFL);
            }
        }
        return this;
    }
    
    public PointerBuffer get(final long[] dst) {
        return this.get(dst, 0, dst.length);
    }
    
    public PointerBuffer put(final PointerBuffer src) {
        if (PointerBuffer.is64Bit) {
            this.view64.put(src.view64);
        }
        else {
            this.view32.put(src.view32);
        }
        return this;
    }
    
    public PointerBuffer put(final long[] src, final int offset, final int length) {
        if (PointerBuffer.is64Bit) {
            this.view64.put(src, offset, length);
        }
        else {
            checkBounds(offset, length, src.length);
            if (length > this.view32.remaining()) {
                throw new BufferOverflowException();
            }
            for (int end = offset + length, i = offset; i < end; ++i) {
                this.view32.put((int)src[i]);
            }
        }
        return this;
    }
    
    public final PointerBuffer put(final long[] src) {
        return this.put(src, 0, src.length);
    }
    
    public PointerBuffer compact() {
        if (PointerBuffer.is64Bit) {
            this.view64.compact();
        }
        else {
            this.view32.compact();
        }
        return this;
    }
    
    public ByteOrder order() {
        if (PointerBuffer.is64Bit) {
            return this.view64.order();
        }
        return this.view32.order();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(48);
        sb.append(this.getClass().getName());
        sb.append("[pos=");
        sb.append(this.position());
        sb.append(" lim=");
        sb.append(this.limit());
        sb.append(" cap=");
        sb.append(this.capacity());
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int h = 1;
        for (int p = this.position(), i = this.limit() - 1; i >= p; --i) {
            h = 31 * h + (int)this.get(i);
        }
        return h;
    }
    
    @Override
    public boolean equals(final Object ob) {
        if (!(ob instanceof PointerBuffer)) {
            return false;
        }
        final PointerBuffer that = (PointerBuffer)ob;
        if (this.remaining() != that.remaining()) {
            return false;
        }
        for (int p = this.position(), i = this.limit() - 1, j = that.limit() - 1; i >= p; --i, --j) {
            final long v1 = this.get(i);
            final long v2 = that.get(j);
            if (v1 != v2) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final Object o) {
        final PointerBuffer that = (PointerBuffer)o;
        final int n = this.position() + Math.min(this.remaining(), that.remaining());
        int i = this.position();
        int j = that.position();
        while (i < n) {
            final long v1 = this.get(i);
            final long v2 = that.get(j);
            if (v1 == v2) {
                ++i;
                ++j;
            }
            else {
                if (v1 < v2) {
                    return -1;
                }
                return 1;
            }
        }
        return this.remaining() - that.remaining();
    }
    
    private static void checkBounds(final int off, final int len, final int size) {
        if ((off | len | off + len | size - (off + len)) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    static {
        boolean is64 = false;
        try {
            final Method m = Class.forName("org.lwjgl.Sys").getDeclaredMethod("is64Bit", (Class<?>[])null);
            is64 = (boolean)m.invoke(null, (Object[])null);
        }
        catch (Throwable t) {}
        finally {
            is64Bit = is64;
        }
    }
    
    private static final class PointerBufferR extends PointerBuffer
    {
        PointerBufferR(final ByteBuffer source) {
            super(source);
        }
        
        @Override
        public boolean isReadOnly() {
            return true;
        }
        
        @Override
        protected PointerBuffer newInstance(final ByteBuffer source) {
            return new PointerBufferR(source);
        }
        
        @Override
        public PointerBuffer asReadOnlyBuffer() {
            return this.duplicate();
        }
        
        @Override
        public PointerBuffer put(final long l) {
            throw new ReadOnlyBufferException();
        }
        
        @Override
        public PointerBuffer put(final int index, final long l) {
            throw new ReadOnlyBufferException();
        }
        
        @Override
        public PointerBuffer put(final PointerBuffer src) {
            throw new ReadOnlyBufferException();
        }
        
        @Override
        public PointerBuffer put(final long[] src, final int offset, final int length) {
            throw new ReadOnlyBufferException();
        }
        
        @Override
        public PointerBuffer compact() {
            throw new ReadOnlyBufferException();
        }
    }
}
