// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyString
{
    public char[] data;
    public int count;
    
    public TinyString(final char[] array) {
        this.count = array.length;
        System.arraycopy(array, 0, this.data = new char[this.count], 0, this.count);
    }
    
    public TinyString(final char[] array, final int n, final int count) {
        this.count = count;
        System.arraycopy(array, n, this.data = new char[this.count], 0, this.count);
    }
    
    public boolean startsWith(final TinyString tinyString, final int n) {
        final char[] data = this.data;
        int n2 = n;
        final int count = this.count;
        final char[] data2 = tinyString.data;
        int n3 = 0;
        int count2 = tinyString.count;
        if (n < 0 || n > this.count - count2) {
            return false;
        }
        while (--count2 >= 0) {
            if (data[n2++] != data2[n3++]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean endsWith(final TinyString tinyString) {
        return this.startsWith(tinyString, this.count - tinyString.count);
    }
    
    public TinyString trim() {
        int count;
        int n;
        int n2;
        char[] data;
        for (count = this.count, n = 0, n2 = 0, data = this.data; n < count && data[n2 + n] <= ' '; ++n) {}
        while (n < count && data[n2 + count - 1] <= ' ') {
            --count;
        }
        return (n <= 0 && count >= this.count) ? this : new TinyString(this.data, count, n - count);
    }
    
    public TinyString substring(final int n) {
        return this.substring(n, this.count);
    }
    
    public TinyString substring(final int n, final int n2) {
        if (n < 0 || n2 > this.count || n > n2) {
            return null;
        }
        if (n == 0 && n2 == this.count) {
            return new TinyString(this.data, 0, this.count);
        }
        return new TinyString(this.data, n, n2 - n);
    }
    
    public int indexOf(final int n, int n2) {
        final int count = this.count;
        final char[] data = this.data;
        if (n2 < 0) {
            n2 = 0;
        }
        else if (n2 >= this.count) {
            return -1;
        }
        for (int i = n2; i < count; ++i) {
            if (data[i] == n) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final int n, final int n2) {
        final char[] data = this.data;
        for (int i = (n2 >= this.count) ? (this.count - 1) : n2; i >= 0; --i) {
            if (data[i] == n) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof TinyString) {
            final TinyString tinyString = (TinyString)o;
            return compareTo(this.data, 0, this.count, tinyString.data, 0, tinyString.count) == 0;
        }
        return false;
    }
    
    public static int compareTo(final char[] array, int n, final int n2, final char[] array2, int n3, final int n4) {
        int n5 = (n2 <= n4) ? n2 : n4;
        while (n5-- != 0) {
            final char c = array[n++];
            final char c2 = array2[n3++];
            if (c != c2) {
                return c - c2;
            }
        }
        return n2 - n4;
    }
    
    public static int hashCode(final char[] array, int n, final int n2) {
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            n3 = 31 * n3 + array[n++];
        }
        return n3;
    }
    
    public static int getIndex(final char[][] array, final char[] array2, final int n, final int n2) {
        int n3 = -1;
        int length = array.length;
        while (length - n3 > 1) {
            final int n4 = (length + n3) / 2;
            if (compareTo(array[n4], 0, array[n4].length, array2, n, n2) < 0) {
                n3 = n4;
            }
            else {
                length = n4;
            }
        }
        if (length < array.length && compareTo(array[length], 0, array[length].length, array2, n, n2) == 0) {
            return length;
        }
        return array.length;
    }
}
