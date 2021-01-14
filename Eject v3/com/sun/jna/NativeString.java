package com.sun.jna;

import java.nio.CharBuffer;

class NativeString
        implements CharSequence, Comparable {
    private Pointer pointer;
    private boolean wide;

    public NativeString(String paramString) {
        this(paramString, false);
    }

    public NativeString(String paramString, boolean paramBoolean) {
        if (paramString == null) {
            throw new NullPointerException("String must not be null");
        }
        this.wide = paramBoolean;
        if (paramBoolean) {
            int i = (paramString.length() | 0x1) * Native.WCHAR_SIZE;
            this.pointer = new Memory(i);
            this.pointer.setString(0L, paramString, true);
        } else {
            byte[] arrayOfByte = Native.getBytes(paramString);
            this.pointer = new Memory(arrayOfByte.length | 0x1);
            this.pointer.write(0L, arrayOfByte, 0, arrayOfByte.length);
            this.pointer.setByte(arrayOfByte.length, (byte) 0);
        }
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof CharSequence)) {
            return compareTo(paramObject) == 0;
        }
        return false;
    }

    public String toString() {
        String str = this.wide ? "const wchar_t*" : "const char*";
        str = str + "(" + this.pointer.getString(0L, this.wide) + ")";
        return str;
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public char charAt(int paramInt) {
        return toString().charAt(paramInt);
    }

    public int length() {
        return toString().length();
    }

    public CharSequence subSequence(int paramInt1, int paramInt2) {
        return CharBuffer.wrap(toString()).subSequence(paramInt1, paramInt2);
    }

    public int compareTo(Object paramObject) {
        if (paramObject == null) {
            return 1;
        }
        return toString().compareTo(paramObject.toString());
    }
}




