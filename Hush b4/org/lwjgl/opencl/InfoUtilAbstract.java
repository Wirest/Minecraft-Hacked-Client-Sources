// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;

abstract class InfoUtilAbstract<T extends CLObject> implements InfoUtil<T>
{
    protected InfoUtilAbstract() {
    }
    
    protected abstract int getInfo(final T p0, final int p1, final ByteBuffer p2, final PointerBuffer p3);
    
    protected int getInfoSizeArraySize(final T object, final int param_name) {
        throw new UnsupportedOperationException();
    }
    
    protected PointerBuffer getSizesBuffer(final T object, final int param_name) {
        final int size = this.getInfoSizeArraySize(object, param_name);
        final PointerBuffer buffer = APIUtil.getBufferPointer(size);
        buffer.limit(size);
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        return buffer;
    }
    
    public int getInfoInt(final T object, final int param_name) {
        object.checkValid();
        final ByteBuffer buffer = APIUtil.getBufferByte(4);
        this.getInfo(object, param_name, buffer, null);
        return buffer.getInt(0);
    }
    
    public long getInfoSize(final T object, final int param_name) {
        object.checkValid();
        final PointerBuffer buffer = APIUtil.getBufferPointer();
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        return buffer.get(0);
    }
    
    public long[] getInfoSizeArray(final T object, final int param_name) {
        object.checkValid();
        final int size = this.getInfoSizeArraySize(object, param_name);
        final PointerBuffer buffer = APIUtil.getBufferPointer(size);
        this.getInfo(object, param_name, buffer.getBuffer(), null);
        final long[] array = new long[size];
        for (int i = 0; i < size; ++i) {
            array[i] = buffer.get(i);
        }
        return array;
    }
    
    public long getInfoLong(final T object, final int param_name) {
        object.checkValid();
        final ByteBuffer buffer = APIUtil.getBufferByte(8);
        this.getInfo(object, param_name, buffer, null);
        return buffer.getLong(0);
    }
    
    public String getInfoString(final T object, final int param_name) {
        object.checkValid();
        final int bytes = this.getSizeRet(object, param_name);
        if (bytes <= 1) {
            return null;
        }
        final ByteBuffer buffer = APIUtil.getBufferByte(bytes);
        this.getInfo(object, param_name, buffer, null);
        buffer.limit(bytes - 1);
        return APIUtil.getString(buffer);
    }
    
    protected final int getSizeRet(final T object, final int param_name) {
        final PointerBuffer bytes = APIUtil.getBufferPointer();
        final int errcode = this.getInfo(object, param_name, null, bytes);
        if (errcode != 0) {
            throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));
        }
        return (int)bytes.get(0);
    }
}
