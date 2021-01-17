// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyVector
{
    public Object[] data;
    public int count;
    
    public TinyVector(int n) {
        if (n <= 0) {
            n = 10;
        }
        this.data = new Object[n];
        this.count = 0;
    }
    
    public TinyVector(final TinyVector tinyVector) {
        this.count = tinyVector.count;
        this.data = new Object[tinyVector.data.length];
        System.arraycopy(tinyVector.data, 0, this.data, 0, this.data.length);
    }
    
    public void copyInto(final Object[] array) {
        int count = this.count;
        while (count-- > 0) {
            array[count] = this.data[count];
        }
    }
    
    public int indexOf(final Object o, final int n) {
        if (o == null) {
            return -1;
        }
        for (int i = n; i < this.count; ++i) {
            if (o.equals(this.data[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final Object o, final int n) {
        if (n >= this.count) {
            return -1;
        }
        for (int i = n; i >= 0; --i) {
            if (o.equals(this.data[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public int removeElementAt(final int n) {
        if (n >= this.count || n < 0) {
            return -1;
        }
        final int n2 = this.count - n - 1;
        if (n2 > 0) {
            System.arraycopy(this.data, n + 1, this.data, n, n2);
        }
        --this.count;
        this.data[this.count] = null;
        return 0;
    }
    
    public int insertElementAt(final Object o, final int n) {
        final int n2 = this.count + 1;
        if (n >= n2) {
            return -1;
        }
        if (n2 > this.data.length) {
            this.a(n2);
        }
        System.arraycopy(this.data, n, this.data, n + 1, this.count - n);
        this.data[n] = o;
        ++this.count;
        return 0;
    }
    
    public void addElement(final Object o) {
        final int n = this.count + 1;
        if (n > this.data.length) {
            this.a(n);
        }
        this.data[this.count++] = o;
    }
    
    private void a(final int n) {
        final int length = this.data.length;
        final Object[] data = this.data;
        int n2 = length * 2;
        if (n2 < n) {
            n2 = n;
        }
        System.arraycopy(data, 0, this.data = new Object[n2], 0, this.count);
    }
}
