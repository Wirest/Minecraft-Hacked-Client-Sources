// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

public abstract class PointerType implements NativeMapped
{
    private Pointer pointer;
    
    protected PointerType() {
        this.pointer = Pointer.NULL;
    }
    
    protected PointerType(final Pointer p) {
        this.pointer = p;
    }
    
    public Class nativeType() {
        return Pointer.class;
    }
    
    public Object toNative() {
        return this.getPointer();
    }
    
    public Pointer getPointer() {
        return this.pointer;
    }
    
    public void setPointer(final Pointer p) {
        this.pointer = p;
    }
    
    public Object fromNative(final Object nativeValue, final FromNativeContext context) {
        if (nativeValue == null) {
            return null;
        }
        try {
            final PointerType pt = (PointerType)this.getClass().newInstance();
            pt.pointer = (Pointer)nativeValue;
            return pt;
        }
        catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't instantiate " + this.getClass());
        }
        catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
        }
    }
    
    public int hashCode() {
        return (this.pointer != null) ? this.pointer.hashCode() : 0;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PointerType)) {
            return false;
        }
        final Pointer p = ((PointerType)o).getPointer();
        if (this.pointer == null) {
            return p == null;
        }
        return this.pointer.equals(p);
    }
    
    public String toString() {
        return (this.pointer == null) ? "NULL" : this.pointer.toString();
    }
}
