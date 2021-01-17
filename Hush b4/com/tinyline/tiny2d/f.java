// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

final class f
{
    public int new;
    public int[] a;
    public int[] int;
    public int do;
    public int[] case;
    public int[] try;
    private static final int for = 1024;
    private static final int if = 64;
    private static final int byte = 128;
    private static final int char = 16;
    
    public f() {
        this.a = new int[1024];
        this.int = new int[1024];
        this.case = new int[128];
        this.try = new int[128];
        this.new = 0;
        this.do = 0;
    }
    
    public void a() {
        if (this.a.length - 1024 > 64) {
            this.a = new int[1024];
            this.int = new int[1024];
        }
        if (this.a.length - 128 > 16) {
            this.case = new int[128];
            this.try = new int[128];
        }
        this.new = 0;
        this.do = 0;
    }
    
    public boolean a(final int n) {
        return this.try[n] > 1 && this.a[this.case[n] + this.try[n] - 1] == this.a[this.case[n]] && this.int[this.case[n] + this.try[n] - 1] == this.int[this.case[n]];
    }
    
    public void a(final int[] array, final int[] array2, final int n) {
        this.a(array, array2, n, n);
    }
    
    public void a(final int[] array, final int[] array2, final int n, final int n2) {
        final int do1 = this.do;
        this.for(1);
        this.case[do1] = this.new;
        this.try[do1] = n;
        ++this.do;
        this.if(n2);
        this.new += n2;
        System.arraycopy(array, 0, this.a, this.case[do1], this.try[do1]);
        System.arraycopy(array2, 0, this.int, this.case[do1], this.try[do1]);
    }
    
    public void do(final int n) {
        if (n < 0 || n > this.do - 1) {
            return;
        }
        final int n2 = this.case[n];
        final int n3 = this.try[n];
        final int n4 = this.new - n2 - n3;
        if (n4 > 0) {
            System.arraycopy(this.a, n2 + n3, this.a, n2, n4);
            System.arraycopy(this.int, n2 + n3, this.int, n2, n4);
        }
        this.new -= n3;
        int[] case1;
        int n5;
        for (int i = n + 1; i < this.do; n5 = i++, case1[n5] -= n3) {
            case1 = this.case;
        }
        final int n6 = this.do - n - 1;
        if (n6 > 0) {
            System.arraycopy(this.case, n + 1, this.case, n, n6);
            System.arraycopy(this.try, n + 1, this.try, n, n6);
        }
        --this.do;
    }
    
    public void a(final int n, final int n2) {
        this.if(1);
        this.a[this.new] = n;
        this.int[this.new] = n2;
        ++this.new;
        final int[] try1 = this.try;
        final int n3 = this.do - 1;
        ++try1[n3];
    }
    
    public void a(final int n, final int n2, final int n3) {
        final int n4 = n + this.case[this.do - 1];
        this.a[n4] = n2;
        this.int[n4] = n3;
    }
    
    public void a(int n, final int[] array, final int[] array2, final int n2) {
        final int n3 = this.do - 1;
        final int n4 = this.try[n3];
        if (n >= n4 - 1) {
            n = n4 - 1;
        }
        final int n5 = n4 - n - 1;
        n += this.case[n3];
        System.arraycopy(this.a, n + 1, this.a, n + 1 + n2, n5);
        System.arraycopy(this.int, n + 1, this.int, n + 1 + n2, n5);
        for (int i = 0; i < n2; ++i) {
            this.a[n + 1 + i] = array[i];
            this.int[n + 1 + i] = array2[i];
        }
        final int[] try1 = this.try;
        final int n6 = n3;
        try1[n6] += n2;
    }
    
    private void if(int max) {
        if (this.new + max > this.a.length) {
            max = TinyUtil.max(max, 64);
            final int[] a = new int[this.new + max];
            System.arraycopy(this.a, 0, a, 0, this.new);
            this.a = a;
            final int[] int1 = new int[this.new + max];
            System.arraycopy(this.int, 0, int1, 0, this.new);
            this.int = int1;
        }
    }
    
    private void for(int max) {
        if (this.do + max > this.case.length) {
            max = TinyUtil.max(max, 16);
            final int[] case1 = new int[this.do + max];
            System.arraycopy(this.case, 0, case1, 0, this.do);
            this.case = case1;
            final int[] try1 = new int[this.do + max];
            System.arraycopy(this.try, 0, try1, 0, this.do);
            this.try = try1;
        }
    }
}
