package com.sun.jna;

public abstract class PointerType
        implements NativeMapped {
    private Pointer pointer;

    protected PointerType() {
        this.pointer = Pointer.NULL;
    }

    protected PointerType(Pointer paramPointer) {
        this.pointer = paramPointer;
    }

    public Class nativeType() {
        return Pointer.class;
    }

    public Object toNative() {
        return getPointer();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public void setPointer(Pointer paramPointer) {
        this.pointer = paramPointer;
    }

    public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
        if (paramObject == null) {
            return null;
        }
        try {
            PointerType localPointerType = (PointerType) getClass().newInstance();
            localPointerType.pointer = ((Pointer) paramObject);
            return localPointerType;
        } catch (InstantiationException localInstantiationException) {
            throw new IllegalArgumentException("Can't instantiate " + getClass());
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
        }
    }

    public int hashCode() {
        return this.pointer != null ? this.pointer.hashCode() : 0;
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this) {
            return true;
        }
        if ((paramObject instanceof PointerType)) {
            Pointer localPointer = ((PointerType) paramObject).getPointer();
            if (this.pointer == null) {
                return localPointer == null;
            }
            return this.pointer.equals(localPointer);
        }
        return false;
    }

    public String toString() {
        return this.pointer == null ? "NULL" : this.pointer.toString();
    }
}




