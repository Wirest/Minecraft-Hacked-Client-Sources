package com.sun.jna;

import java.nio.CharBuffer;

public final class WString
        implements CharSequence, Comparable {
    private String string;

    public WString(String paramString) {
        if (paramString == null) {
            throw new NullPointerException("String initializer must be non-null");
        }
        this.string = paramString;
    }

    public String toString() {
        return this.string;
    }

    public boolean equals(Object paramObject) {
        return ((paramObject instanceof WString)) && (toString().equals(paramObject.toString()));
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public int compareTo(Object paramObject) {
        return toString().compareTo(paramObject.toString());
    }

    public int length() {
        return toString().length();
    }

    public char charAt(int paramInt) {
        return toString().charAt(paramInt);
    }

    public CharSequence subSequence(int paramInt1, int paramInt2) {
        return CharBuffer.wrap(toString()).subSequence(paramInt1, paramInt2);
    }
}




