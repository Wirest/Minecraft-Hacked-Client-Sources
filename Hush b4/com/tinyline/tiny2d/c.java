// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

final class c
{
    int for;
    int[] if;
    int[] do;
    private static final int a = 16;
    
    c(final int n) {
        this.if = new int[n];
        this.do = new int[n];
        this.for = 0;
    }
    
    public void if() {
        this.for = 0;
    }
    
    c(final int[] array, final int[] array2, final int for1, final int n) {
        this.for = for1;
        this.if = new int[n];
        this.do = new int[n];
        System.arraycopy(array, 0, this.if, 0, this.for);
        System.arraycopy(array2, 0, this.do, 0, this.for);
    }
    
    public void a(final int n, final int n2) {
        this.a(1);
        this.if[this.for] = n;
        this.do[this.for] = n2;
        ++this.for;
    }
    
    public void do() {
        if (!this.a() && this.for > 1) {
            this.a(this.if[0], this.do[0]);
        }
    }
    
    public boolean a() {
        return this.for > 1 && this.if[this.for - 1] == this.if[0] && this.do[this.for - 1] == this.do[0];
    }
    
    private void a(int max) {
        if (this.for + max > this.if.length) {
            max = TinyUtil.max(max, 16);
            final int[] if1 = new int[this.for + max];
            System.arraycopy(this.if, 0, if1, 0, this.for);
            this.if = if1;
            final int[] do1 = new int[this.for + max];
            System.arraycopy(this.do, 0, do1, 0, this.for);
            this.do = do1;
        }
    }
}
