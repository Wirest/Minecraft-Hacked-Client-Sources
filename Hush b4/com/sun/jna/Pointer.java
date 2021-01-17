// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.List;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.lang.reflect.Array;
import java.nio.Buffer;

public class Pointer
{
    public static final int SIZE;
    public static final Pointer NULL;
    protected long peer;
    
    public static final Pointer createConstant(final long peer) {
        return new Opaque(peer);
    }
    
    public static final Pointer createConstant(final int peer) {
        return new Opaque((long)peer & -1L);
    }
    
    Pointer() {
    }
    
    public Pointer(final long peer) {
        this.peer = peer;
    }
    
    public Pointer share(final long offset) {
        return this.share(offset, 0L);
    }
    
    public Pointer share(final long offset, final long sz) {
        if (offset == 0L) {
            return this;
        }
        return new Pointer(this.peer + offset);
    }
    
    public void clear(final long size) {
        this.setMemory(0L, size, (byte)0);
    }
    
    public boolean equals(final Object o) {
        return o == this || (o != null && o instanceof Pointer && ((Pointer)o).peer == this.peer);
    }
    
    public int hashCode() {
        return (int)((this.peer >>> 32) + (this.peer & -1L));
    }
    
    public long indexOf(final long offset, final byte value) {
        return Native.indexOf(this.peer + offset, value);
    }
    
    public void read(final long offset, final byte[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final short[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final char[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final int[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final long[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final float[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final double[] buf, final int index, final int length) {
        Native.read(this.peer + offset, buf, index, length);
    }
    
    public void read(final long offset, final Pointer[] buf, final int index, final int length) {
        for (int i = 0; i < length; ++i) {
            final Pointer p = this.getPointer(offset + i * Pointer.SIZE);
            final Pointer oldp = buf[i + index];
            if (oldp == null || p == null || p.peer != oldp.peer) {
                buf[i + index] = p;
            }
        }
    }
    
    public void write(final long offset, final byte[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final short[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final char[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final int[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final long[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final float[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long offset, final double[] buf, final int index, final int length) {
        Native.write(this.peer + offset, buf, index, length);
    }
    
    public void write(final long bOff, final Pointer[] buf, final int index, final int length) {
        for (int i = 0; i < length; ++i) {
            this.setPointer(bOff + i * Pointer.SIZE, buf[index + i]);
        }
    }
    
    Object getValue(final long offset, final Class type, final Object currentValue) {
        Object result = null;
        if (Structure.class.isAssignableFrom(type)) {
            Structure s = (Structure)currentValue;
            if (Structure.ByReference.class.isAssignableFrom(type)) {
                s = Structure.updateStructureByReference(type, s, this.getPointer(offset));
            }
            else {
                s.useMemory(this, (int)offset);
                s.read();
            }
            result = s;
        }
        else if (type == Boolean.TYPE || type == Boolean.class) {
            result = Function.valueOf(this.getInt(offset) != 0);
        }
        else if (type == Byte.TYPE || type == Byte.class) {
            result = new Byte(this.getByte(offset));
        }
        else if (type == Short.TYPE || type == Short.class) {
            result = new Short(this.getShort(offset));
        }
        else if (type == Character.TYPE || type == Character.class) {
            result = new Character(this.getChar(offset));
        }
        else if (type == Integer.TYPE || type == Integer.class) {
            result = new Integer(this.getInt(offset));
        }
        else if (type == Long.TYPE || type == Long.class) {
            result = new Long(this.getLong(offset));
        }
        else if (type == Float.TYPE || type == Float.class) {
            result = new Float(this.getFloat(offset));
        }
        else if (type == Double.TYPE || type == Double.class) {
            result = new Double(this.getDouble(offset));
        }
        else if (Pointer.class.isAssignableFrom(type)) {
            final Pointer p = this.getPointer(offset);
            if (p != null) {
                final Pointer oldp = (currentValue instanceof Pointer) ? ((Pointer)currentValue) : null;
                if (oldp == null || p.peer != oldp.peer) {
                    result = p;
                }
                else {
                    result = oldp;
                }
            }
        }
        else if (type == String.class) {
            final Pointer p = this.getPointer(offset);
            result = ((p != null) ? p.getString(0L) : null);
        }
        else if (type == WString.class) {
            final Pointer p = this.getPointer(offset);
            result = ((p != null) ? new WString(p.getString(0L, true)) : null);
        }
        else if (Callback.class.isAssignableFrom(type)) {
            final Pointer fp = this.getPointer(offset);
            if (fp == null) {
                result = null;
            }
            else {
                Callback cb = (Callback)currentValue;
                final Pointer oldfp = CallbackReference.getFunctionPointer(cb);
                if (!fp.equals(oldfp)) {
                    cb = CallbackReference.getCallback(type, fp);
                }
                result = cb;
            }
        }
        else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
            final Pointer bp = this.getPointer(offset);
            if (bp == null) {
                result = null;
            }
            else {
                final Pointer oldbp = (currentValue == null) ? null : Native.getDirectBufferPointer((Buffer)currentValue);
                if (oldbp == null || !oldbp.equals(bp)) {
                    throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
                }
                result = currentValue;
            }
        }
        else if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMapped nm = (NativeMapped)currentValue;
            if (nm != null) {
                final Object value = this.getValue(offset, nm.nativeType(), null);
                result = nm.fromNative(value, new FromNativeContext(type));
            }
            else {
                final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                final Object value2 = this.getValue(offset, tc.nativeType(), null);
                result = tc.fromNative(value2, new FromNativeContext(type));
            }
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Reading \"" + type + "\" from memory is not supported");
            }
            result = currentValue;
            if (result == null) {
                throw new IllegalStateException("Need an initialized array");
            }
            this.getArrayValue(offset, result, type.getComponentType());
        }
        return result;
    }
    
    private void getArrayValue(final long offset, final Object o, final Class cls) {
        int length = 0;
        length = Array.getLength(o);
        final Object result = o;
        if (cls == Byte.TYPE) {
            this.read(offset, (byte[])result, 0, length);
        }
        else if (cls == Short.TYPE) {
            this.read(offset, (short[])result, 0, length);
        }
        else if (cls == Character.TYPE) {
            this.read(offset, (char[])result, 0, length);
        }
        else if (cls == Integer.TYPE) {
            this.read(offset, (int[])result, 0, length);
        }
        else if (cls == Long.TYPE) {
            this.read(offset, (long[])result, 0, length);
        }
        else if (cls == Float.TYPE) {
            this.read(offset, (float[])result, 0, length);
        }
        else if (cls == Double.TYPE) {
            this.read(offset, (double[])result, 0, length);
        }
        else if (Pointer.class.isAssignableFrom(cls)) {
            this.read(offset, (Pointer[])result, 0, length);
        }
        else if (Structure.class.isAssignableFrom(cls)) {
            final Structure[] sarray = (Structure[])result;
            if (Structure.ByReference.class.isAssignableFrom(cls)) {
                final Pointer[] parray = this.getPointerArray(offset, sarray.length);
                for (int i = 0; i < sarray.length; ++i) {
                    sarray[i] = Structure.updateStructureByReference(cls, sarray[i], parray[i]);
                }
            }
            else {
                for (int j = 0; j < sarray.length; ++j) {
                    if (sarray[j] == null) {
                        sarray[j] = Structure.newInstance(cls);
                    }
                    sarray[j].useMemory(this, (int)(offset + j * sarray[j].size()));
                    sarray[j].read();
                }
            }
        }
        else {
            if (!NativeMapped.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Reading array of " + cls + " from memory not supported");
            }
            final NativeMapped[] array = (NativeMapped[])result;
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            final int size = Native.getNativeSize(result.getClass(), result) / array.length;
            for (int k = 0; k < array.length; ++k) {
                final Object value = this.getValue(offset + size * k, tc.nativeType(), array[k]);
                array[k] = (NativeMapped)tc.fromNative(value, new FromNativeContext(cls));
            }
        }
    }
    
    public byte getByte(final long offset) {
        return Native.getByte(this.peer + offset);
    }
    
    public char getChar(final long offset) {
        return Native.getChar(this.peer + offset);
    }
    
    public short getShort(final long offset) {
        return Native.getShort(this.peer + offset);
    }
    
    public int getInt(final long offset) {
        return Native.getInt(this.peer + offset);
    }
    
    public long getLong(final long offset) {
        return Native.getLong(this.peer + offset);
    }
    
    public NativeLong getNativeLong(final long offset) {
        return new NativeLong((NativeLong.SIZE == 8) ? this.getLong(offset) : this.getInt(offset));
    }
    
    public float getFloat(final long offset) {
        return Native.getFloat(this.peer + offset);
    }
    
    public double getDouble(final long offset) {
        return Native.getDouble(this.peer + offset);
    }
    
    public Pointer getPointer(final long offset) {
        return Native.getPointer(this.peer + offset);
    }
    
    public ByteBuffer getByteBuffer(final long offset, final long length) {
        return Native.getDirectByteBuffer(this.peer + offset, length).order(ByteOrder.nativeOrder());
    }
    
    public String getString(final long offset, final boolean wide) {
        return Native.getString(this.peer + offset, wide);
    }
    
    public String getString(final long offset) {
        final String encoding = System.getProperty("jna.encoding");
        if (encoding != null) {
            final long len = this.indexOf(offset, (byte)0);
            if (len != -1L) {
                if (len > 2147483647L) {
                    throw new OutOfMemoryError("String exceeds maximum length: " + len);
                }
                final byte[] data = this.getByteArray(offset, (int)len);
                try {
                    return new String(data, encoding);
                }
                catch (UnsupportedEncodingException ex) {}
            }
        }
        return this.getString(offset, false);
    }
    
    public byte[] getByteArray(final long offset, final int arraySize) {
        final byte[] buf = new byte[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public char[] getCharArray(final long offset, final int arraySize) {
        final char[] buf = new char[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public short[] getShortArray(final long offset, final int arraySize) {
        final short[] buf = new short[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public int[] getIntArray(final long offset, final int arraySize) {
        final int[] buf = new int[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public long[] getLongArray(final long offset, final int arraySize) {
        final long[] buf = new long[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public float[] getFloatArray(final long offset, final int arraySize) {
        final float[] buf = new float[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public double[] getDoubleArray(final long offset, final int arraySize) {
        final double[] buf = new double[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public Pointer[] getPointerArray(final long offset) {
        final List array = new ArrayList();
        int addOffset = 0;
        for (Pointer p = this.getPointer(offset); p != null; p = this.getPointer(offset + addOffset)) {
            array.add(p);
            addOffset += Pointer.SIZE;
        }
        return array.toArray(new Pointer[array.size()]);
    }
    
    public Pointer[] getPointerArray(final long offset, final int arraySize) {
        final Pointer[] buf = new Pointer[arraySize];
        this.read(offset, buf, 0, arraySize);
        return buf;
    }
    
    public String[] getStringArray(final long offset) {
        return this.getStringArray(offset, -1, false);
    }
    
    public String[] getStringArray(final long offset, final int length) {
        return this.getStringArray(offset, length, false);
    }
    
    public String[] getStringArray(final long offset, final boolean wide) {
        return this.getStringArray(offset, -1, wide);
    }
    
    public String[] getStringArray(final long offset, final int length, final boolean wide) {
        final List strings = new ArrayList();
        int addOffset = 0;
        if (length != -1) {
            Pointer p = this.getPointer(offset + addOffset);
            int count = 0;
            while (count++ < length) {
                final String s = (p == null) ? null : p.getString(0L, wide);
                strings.add(s);
                if (count < length) {
                    addOffset += Pointer.SIZE;
                    p = this.getPointer(offset + addOffset);
                }
            }
        }
        else {
            Pointer p;
            while ((p = this.getPointer(offset + addOffset)) != null) {
                final String s2 = (p == null) ? null : p.getString(0L, wide);
                strings.add(s2);
                addOffset += Pointer.SIZE;
            }
        }
        return strings.toArray(new String[strings.size()]);
    }
    
    void setValue(final long offset, final Object value, final Class type) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            this.setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
        }
        else if (type == Byte.TYPE || type == Byte.class) {
            this.setByte(offset, (byte)((value == null) ? 0 : ((byte)value)));
        }
        else if (type == Short.TYPE || type == Short.class) {
            this.setShort(offset, (short)((value == null) ? 0 : ((short)value)));
        }
        else if (type == Character.TYPE || type == Character.class) {
            this.setChar(offset, (value == null) ? '\0' : ((char)value));
        }
        else if (type == Integer.TYPE || type == Integer.class) {
            this.setInt(offset, (value == null) ? 0 : ((int)value));
        }
        else if (type == Long.TYPE || type == Long.class) {
            this.setLong(offset, (value == null) ? 0L : ((long)value));
        }
        else if (type == Float.TYPE || type == Float.class) {
            this.setFloat(offset, (value == null) ? 0.0f : ((float)value));
        }
        else if (type == Double.TYPE || type == Double.class) {
            this.setDouble(offset, (value == null) ? 0.0 : ((double)value));
        }
        else if (type == Pointer.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (type == String.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (type == WString.class) {
            this.setPointer(offset, (Pointer)value);
        }
        else if (Structure.class.isAssignableFrom(type)) {
            final Structure s = (Structure)value;
            if (Structure.ByReference.class.isAssignableFrom(type)) {
                this.setPointer(offset, (s == null) ? null : s.getPointer());
                if (s != null) {
                    s.autoWrite();
                }
            }
            else {
                s.useMemory(this, (int)offset);
                s.write();
            }
        }
        else if (Callback.class.isAssignableFrom(type)) {
            this.setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
        }
        else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
            final Pointer p = (value == null) ? null : Native.getDirectBufferPointer((Buffer)value);
            this.setPointer(offset, p);
        }
        else if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            final Class nativeType = tc.nativeType();
            this.setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
            }
            this.setArrayValue(offset, value, type.getComponentType());
        }
    }
    
    private void setArrayValue(final long offset, final Object value, final Class cls) {
        if (cls == Byte.TYPE) {
            final byte[] buf = (byte[])value;
            this.write(offset, buf, 0, buf.length);
        }
        else if (cls == Short.TYPE) {
            final short[] buf2 = (short[])value;
            this.write(offset, buf2, 0, buf2.length);
        }
        else if (cls == Character.TYPE) {
            final char[] buf3 = (char[])value;
            this.write(offset, buf3, 0, buf3.length);
        }
        else if (cls == Integer.TYPE) {
            final int[] buf4 = (int[])value;
            this.write(offset, buf4, 0, buf4.length);
        }
        else if (cls == Long.TYPE) {
            final long[] buf5 = (long[])value;
            this.write(offset, buf5, 0, buf5.length);
        }
        else if (cls == Float.TYPE) {
            final float[] buf6 = (float[])value;
            this.write(offset, buf6, 0, buf6.length);
        }
        else if (cls == Double.TYPE) {
            final double[] buf7 = (double[])value;
            this.write(offset, buf7, 0, buf7.length);
        }
        else if (Pointer.class.isAssignableFrom(cls)) {
            final Pointer[] buf8 = (Pointer[])value;
            this.write(offset, buf8, 0, buf8.length);
        }
        else if (Structure.class.isAssignableFrom(cls)) {
            final Structure[] sbuf = (Structure[])value;
            if (Structure.ByReference.class.isAssignableFrom(cls)) {
                final Pointer[] buf9 = new Pointer[sbuf.length];
                for (int i = 0; i < sbuf.length; ++i) {
                    if (sbuf[i] == null) {
                        buf9[i] = null;
                    }
                    else {
                        buf9[i] = sbuf[i].getPointer();
                        sbuf[i].write();
                    }
                }
                this.write(offset, buf9, 0, buf9.length);
            }
            else {
                for (int j = 0; j < sbuf.length; ++j) {
                    if (sbuf[j] == null) {
                        sbuf[j] = Structure.newInstance(cls);
                    }
                    sbuf[j].useMemory(this, (int)(offset + j * sbuf[j].size()));
                    sbuf[j].write();
                }
            }
        }
        else {
            if (!NativeMapped.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Writing array of " + cls + " to memory not supported");
            }
            final NativeMapped[] buf10 = (NativeMapped[])value;
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            final Class nativeType = tc.nativeType();
            final int size = Native.getNativeSize(value.getClass(), value) / buf10.length;
            for (int k = 0; k < buf10.length; ++k) {
                final Object element = tc.toNative(buf10[k], new ToNativeContext());
                this.setValue(offset + k * size, element, nativeType);
            }
        }
    }
    
    public void setMemory(final long offset, final long length, final byte value) {
        Native.setMemory(this.peer + offset, length, value);
    }
    
    public void setByte(final long offset, final byte value) {
        Native.setByte(this.peer + offset, value);
    }
    
    public void setShort(final long offset, final short value) {
        Native.setShort(this.peer + offset, value);
    }
    
    public void setChar(final long offset, final char value) {
        Native.setChar(this.peer + offset, value);
    }
    
    public void setInt(final long offset, final int value) {
        Native.setInt(this.peer + offset, value);
    }
    
    public void setLong(final long offset, final long value) {
        Native.setLong(this.peer + offset, value);
    }
    
    public void setNativeLong(final long offset, final NativeLong value) {
        if (NativeLong.SIZE == 8) {
            this.setLong(offset, value.longValue());
        }
        else {
            this.setInt(offset, value.intValue());
        }
    }
    
    public void setFloat(final long offset, final float value) {
        Native.setFloat(this.peer + offset, value);
    }
    
    public void setDouble(final long offset, final double value) {
        Native.setDouble(this.peer + offset, value);
    }
    
    public void setPointer(final long offset, final Pointer value) {
        Native.setPointer(this.peer + offset, (value != null) ? value.peer : 0L);
    }
    
    public void setString(final long offset, final String value, final boolean wide) {
        Native.setString(this.peer + offset, value, wide);
    }
    
    public void setString(final long offset, final String value) {
        final byte[] data = Native.getBytes(value);
        this.write(offset, data, 0, data.length);
        this.setByte(offset + data.length, (byte)0);
    }
    
    public String toString() {
        return "native@0x" + Long.toHexString(this.peer);
    }
    
    public static long nativeValue(final Pointer p) {
        return p.peer;
    }
    
    public static void nativeValue(final Pointer p, final long value) {
        p.peer = value;
    }
    
    static {
        if ((SIZE = Native.POINTER_SIZE) == 0) {
            throw new Error("Native library not initialized");
        }
        NULL = null;
    }
    
    private static class Opaque extends Pointer
    {
        private final String MSG;
        
        private Opaque(final long peer) {
            super(peer);
            this.MSG = "This pointer is opaque: " + this;
        }
        
        public long indexOf(final long offset, final byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final byte[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final char[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final short[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final int[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final long[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final float[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void read(final long bOff, final double[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final byte[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final char[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final short[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final int[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final long[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final float[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void write(final long bOff, final double[] buf, final int index, final int length) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public byte getByte(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public char getChar(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public short getShort(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public int getInt(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public long getLong(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public float getFloat(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public double getDouble(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public Pointer getPointer(final long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public String getString(final long bOff, final boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setByte(final long bOff, final byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setChar(final long bOff, final char value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setShort(final long bOff, final short value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setInt(final long bOff, final int value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setLong(final long bOff, final long value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setFloat(final long bOff, final float value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setDouble(final long bOff, final double value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setPointer(final long offset, final Pointer value) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public void setString(final long offset, final String value, final boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }
        
        public String toString() {
            return "opaque@0x" + Long.toHexString(this.peer);
        }
    }
}
