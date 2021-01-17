// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyHash
{
    public static final int TINYHASH_NUMBER = 0;
    public static final int TINYHASH_STRING = 1;
    private h[] for;
    private int do;
    private int a;
    private int if;
    
    public TinyHash(final int if1, int n) {
        this.if = if1;
        if (n < 0 || n == 0) {
            n = 11;
        }
        this.for = new h[n];
        this.a = n * 75 / 100;
    }
    
    public Object get(final Object o) {
        if (o == null) {
            return null;
        }
        final h[] for1 = this.for;
        final int a = this.a(o);
        for (h if1 = for1[(a & Integer.MAX_VALUE) % for1.length]; if1 != null; if1 = if1.if) {
            if (if1.for == a && this.a(if1.a, o)) {
                return if1.do;
            }
        }
        return null;
    }
    
    public int put(final Object a, final Object o) {
        if (o == null || a == null) {
            return -1;
        }
        final h[] for1 = this.for;
        final int a2 = this.a(a);
        final int n = (a2 & Integer.MAX_VALUE) % for1.length;
        for (h if1 = for1[n]; if1 != null; if1 = if1.if) {
            if (if1.for == a2 && this.a(if1.a, a)) {
                if1.do = o;
                return 0;
            }
        }
        if (this.do >= this.a) {
            this.a();
            return this.put(a, o);
        }
        final h h = new h();
        h.for = a2;
        h.a = a;
        h.do = o;
        h.if = for1[n];
        for1[n] = h;
        ++this.do;
        return 0;
    }
    
    public int remove(final Object o) {
        if (o == null) {
            return -1;
        }
        final h[] for1 = this.for;
        final int a = this.a(o);
        final int n = (a & Integer.MAX_VALUE) % for1.length;
        h if1 = for1[n];
        h h = null;
        while (if1 != null) {
            if (if1.for == a && this.a(if1.a, o)) {
                if (h != null) {
                    h.if = if1.if;
                }
                else {
                    for1[n] = if1.if;
                }
                --this.do;
                return 0;
            }
            h = if1;
            if1 = if1.if;
        }
        return -1;
    }
    
    public void clear() {
        final h[] for1 = this.for;
        int length = for1.length;
        while (--length >= 0) {
            for1[length] = null;
        }
        this.do = 0;
    }
    
    private void a() {
        final int length = this.for.length;
        final h[] for1 = this.for;
        final int n = length * 2 + 1;
        final h[] for2 = new h[n];
        this.a = n * 75 / 100;
        this.for = for2;
        int n2 = length;
        while (n2-- > 0) {
            h h;
            int n3;
            for (h if1 = for1[n2]; if1 != null; if1 = if1.if, n3 = (h.for & Integer.MAX_VALUE) % n, h.if = for2[n3], for2[n3] = h) {
                h = if1;
            }
        }
    }
    
    int a(final Object o) {
        int n = 0;
        switch (this.if) {
            case 0: {
                n = ((TinyNumber)o).val;
                break;
            }
            case 1: {
                final TinyString tinyString = (TinyString)o;
                n = TinyString.hashCode(tinyString.data, 0, tinyString.count);
                break;
            }
        }
        return n;
    }
    
    boolean a(final Object o, final Object o2) {
        boolean b = false;
        switch (this.if) {
            case 0: {
                b = (((TinyNumber)o).val == ((TinyNumber)o).val);
                break;
            }
            case 1: {
                final TinyString tinyString = (TinyString)o;
                final TinyString tinyString2 = (TinyString)o2;
                b = (TinyString.compareTo(tinyString.data, 0, tinyString.count, tinyString2.data, 0, tinyString2.count) == 0);
                break;
            }
        }
        return b;
    }
}
