// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import org.lwjgl.LWJGLUtil;
import java.nio.BufferOverflowException;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import java.nio.ByteBuffer;

public abstract class MappedObject
{
    static final boolean CHECKS;
    public long baseAddress;
    public long viewAddress;
    ByteBuffer preventGC;
    public static int SIZEOF;
    public int view;
    
    protected MappedObject() {
    }
    
    protected final long getViewAddress(final int view) {
        throw new InternalError("type not registered");
    }
    
    public final void setViewAddress(final long address) {
        if (MappedObject.CHECKS) {
            this.checkAddress(address);
        }
        this.viewAddress = address;
    }
    
    final void checkAddress(final long address) {
        final long base = MemoryUtil.getAddress0(this.preventGC);
        final int offset = (int)(address - base);
        if (address < base || this.preventGC.capacity() < offset + this.getSizeof()) {
            throw new IndexOutOfBoundsException(Integer.toString(offset / this.getSizeof()));
        }
    }
    
    final void checkRange(final int bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException();
        }
        if (this.preventGC.capacity() < this.viewAddress - MemoryUtil.getAddress0(this.preventGC) + bytes) {
            throw new BufferOverflowException();
        }
    }
    
    public final int getAlign() {
        throw new InternalError("type not registered");
    }
    
    public final int getSizeof() {
        throw new InternalError("type not registered");
    }
    
    public final int capacity() {
        throw new InternalError("type not registered");
    }
    
    public static <T extends MappedObject> T map(final ByteBuffer bb) {
        throw new InternalError("type not registered");
    }
    
    public static <T extends MappedObject> T map(final long address, final int capacity) {
        throw new InternalError("type not registered");
    }
    
    public static <T extends MappedObject> T malloc(final int elementCount) {
        throw new InternalError("type not registered");
    }
    
    public final <T extends MappedObject> T dup() {
        throw new InternalError("type not registered");
    }
    
    public final <T extends MappedObject> T slice() {
        throw new InternalError("type not registered");
    }
    
    public final void runViewConstructor() {
        throw new InternalError("type not registered");
    }
    
    public final void next() {
        throw new InternalError("type not registered");
    }
    
    public final <T extends MappedObject> void copyTo(final T target) {
        throw new InternalError("type not registered");
    }
    
    public final <T extends MappedObject> void copyRange(final T target, final int instances) {
        throw new InternalError("type not registered");
    }
    
    public static <T extends MappedObject> Iterable<T> foreach(final T mapped) {
        return foreach(mapped, mapped.capacity());
    }
    
    public static <T extends MappedObject> Iterable<T> foreach(final T mapped, final int elementCount) {
        return new MappedForeach<T>(mapped, elementCount);
    }
    
    public final <T extends MappedObject> T[] asArray() {
        throw new InternalError("type not registered");
    }
    
    public final ByteBuffer backingByteBuffer() {
        return this.preventGC;
    }
    
    static {
        CHECKS = LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.Checks");
        MappedObject.SIZEOF = -1;
    }
}
