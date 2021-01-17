// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.Collections;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.util.Map;

public class Memory extends Pointer
{
    private static final Map buffers;
    protected long size;
    
    public static void purge() {
        Memory.buffers.size();
    }
    
    public Memory(final long size) {
        this.size = size;
        if (size <= 0L) {
            throw new IllegalArgumentException("Allocation size must be greater than zero");
        }
        this.peer = malloc(size);
        if (this.peer == 0L) {
            throw new OutOfMemoryError("Cannot allocate " + size + " bytes");
        }
    }
    
    protected Memory() {
    }
    
    public Pointer share(final long offset) {
        return this.share(offset, this.getSize() - offset);
    }
    
    public Pointer share(final long offset, final long sz) {
        if (offset == 0L && sz == this.getSize()) {
            return this;
        }
        this.boundsCheck(offset, sz);
        return new SharedMemory(offset);
    }
    
    public Memory align(final int byteBoundary) {
        if (byteBoundary <= 0) {
            throw new IllegalArgumentException("Byte boundary must be positive: " + byteBoundary);
        }
        int i = 0;
        while (i < 32) {
            if (byteBoundary == 1 << i) {
                final long mask = ~(byteBoundary - 1L);
                if ((this.peer & mask) == this.peer) {
                    return this;
                }
                final long newPeer = this.peer + byteBoundary - 1L & mask;
                final long newSize = this.peer + this.size - newPeer;
                if (newSize <= 0L) {
                    throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
                }
                return (Memory)this.share(newPeer - this.peer, newSize);
            }
            else {
                ++i;
            }
        }
        throw new IllegalArgumentException("Byte boundary must be a power of two");
    }
    
    protected void finalize() {
        this.dispose();
    }
    
    protected synchronized void dispose() {
        free(this.peer);
        this.peer = 0L;
    }
    
    public void clear() {
        this.clear(this.size);
    }
    
    public boolean isValid() {
        return this.valid();
    }
    
    public boolean valid() {
        return this.peer != 0L;
    }
    
    public long size() {
        return this.size;
    }
    
    public long getSize() {
        return this.size();
    }
    
    protected void boundsCheck(final long off, final long sz) {
        if (off < 0L) {
            throw new IndexOutOfBoundsException("Invalid offset: " + off);
        }
        if (off + sz > this.size) {
            final String msg = "Bounds exceeds available space : size=" + this.size + ", offset=" + (off + sz);
            throw new IndexOutOfBoundsException(msg);
        }
    }
    
    public void read(final long bOff, final byte[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 1L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final short[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 2L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final char[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 2L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final int[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 4L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final long[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 8L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final float[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 4L);
        super.read(bOff, buf, index, length);
    }
    
    public void read(final long bOff, final double[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 8L);
        super.read(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final byte[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 1L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final short[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 2L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final char[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 2L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final int[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 4L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final long[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 8L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final float[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 4L);
        super.write(bOff, buf, index, length);
    }
    
    public void write(final long bOff, final double[] buf, final int index, final int length) {
        this.boundsCheck(bOff, length * 8L);
        super.write(bOff, buf, index, length);
    }
    
    public byte getByte(final long offset) {
        this.boundsCheck(offset, 1L);
        return super.getByte(offset);
    }
    
    public char getChar(final long offset) {
        this.boundsCheck(offset, 1L);
        return super.getChar(offset);
    }
    
    public short getShort(final long offset) {
        this.boundsCheck(offset, 2L);
        return super.getShort(offset);
    }
    
    public int getInt(final long offset) {
        this.boundsCheck(offset, 4L);
        return super.getInt(offset);
    }
    
    public long getLong(final long offset) {
        this.boundsCheck(offset, 8L);
        return super.getLong(offset);
    }
    
    public float getFloat(final long offset) {
        this.boundsCheck(offset, 4L);
        return super.getFloat(offset);
    }
    
    public double getDouble(final long offset) {
        this.boundsCheck(offset, 8L);
        return super.getDouble(offset);
    }
    
    public Pointer getPointer(final long offset) {
        this.boundsCheck(offset, Pointer.SIZE);
        return super.getPointer(offset);
    }
    
    public ByteBuffer getByteBuffer(final long offset, final long length) {
        this.boundsCheck(offset, length);
        final ByteBuffer b = super.getByteBuffer(offset, length);
        Memory.buffers.put(b, this);
        return b;
    }
    
    public String getString(final long offset, final boolean wide) {
        this.boundsCheck(offset, 0L);
        return super.getString(offset, wide);
    }
    
    public void setByte(final long offset, final byte value) {
        this.boundsCheck(offset, 1L);
        super.setByte(offset, value);
    }
    
    public void setChar(final long offset, final char value) {
        this.boundsCheck(offset, Native.WCHAR_SIZE);
        super.setChar(offset, value);
    }
    
    public void setShort(final long offset, final short value) {
        this.boundsCheck(offset, 2L);
        super.setShort(offset, value);
    }
    
    public void setInt(final long offset, final int value) {
        this.boundsCheck(offset, 4L);
        super.setInt(offset, value);
    }
    
    public void setLong(final long offset, final long value) {
        this.boundsCheck(offset, 8L);
        super.setLong(offset, value);
    }
    
    public void setFloat(final long offset, final float value) {
        this.boundsCheck(offset, 4L);
        super.setFloat(offset, value);
    }
    
    public void setDouble(final long offset, final double value) {
        this.boundsCheck(offset, 8L);
        super.setDouble(offset, value);
    }
    
    public void setPointer(final long offset, final Pointer value) {
        this.boundsCheck(offset, Pointer.SIZE);
        super.setPointer(offset, value);
    }
    
    public void setString(final long offset, final String value, final boolean wide) {
        if (wide) {
            this.boundsCheck(offset, (value.length() + 1L) * Native.WCHAR_SIZE);
        }
        else {
            this.boundsCheck(offset, value.getBytes().length + 1L);
        }
        super.setString(offset, value, wide);
    }
    
    public String toString() {
        return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
    }
    
    protected static void free(final long p) {
        Native.free(p);
    }
    
    protected static long malloc(final long size) {
        return Native.malloc(size);
    }
    
    static {
        buffers = Collections.synchronizedMap((Map<Object, Object>)(Platform.HAS_BUFFERS ? new WeakIdentityHashMap() : new HashMap<Object, Object>()));
    }
    
    private class SharedMemory extends Memory
    {
        public SharedMemory(final long offset) {
            this.size = Memory.this.size - offset;
            this.peer = Memory.this.peer + offset;
        }
        
        protected void finalize() {
        }
        
        protected void boundsCheck(final long off, final long sz) {
            Memory.this.boundsCheck(this.peer - Memory.this.peer + off, sz);
        }
        
        public String toString() {
            return super.toString() + " (shared from " + Memory.this.toString() + ")";
        }
    }
}
