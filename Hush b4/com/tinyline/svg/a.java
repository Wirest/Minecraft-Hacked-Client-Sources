// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;

class a
{
    private int do;
    private int if;
    private int try;
    private TinyString new;
    private TinyString int;
    private boolean for;
    private char a;
    
    public a(final TinyString new1, final TinyString int1, final boolean for1) {
        this.do = 0;
        this.if = -1;
        this.new = new1;
        this.try = new1.count;
        this.int = int1;
        this.for = for1;
        if (this.int == null) {
            this.int = new TinyString(" \t\n\r\f".toCharArray());
        }
        char a = '\0';
        for (int i = 0; i < this.int.count; ++i) {
            final char c = this.int.data[i];
            if (a < c) {
                a = c;
            }
        }
        this.a = a;
    }
    
    private int a(final int n) {
        if (this.int == null) {
            throw new NullPointerException();
        }
        int n2;
        for (n2 = n; !this.for && n2 < this.try; ++n2) {
            final char c = this.new.data[n2];
            if (c > this.a || this.int.indexOf(c, 0) < 0) {
                break;
            }
        }
        return n2;
    }
    
    private int if(final int n) {
        int i;
        for (i = n; i < this.try; ++i) {
            final char c = this.new.data[i];
            if (c <= this.a && this.int.indexOf(c, 0) >= 0) {
                break;
            }
        }
        if (this.for && n == i) {
            final char c2 = this.new.data[i];
            if (c2 <= this.a && this.int.indexOf(c2, 0) >= 0) {
                ++i;
            }
        }
        return i;
    }
    
    public boolean do() {
        this.if = this.a(this.do);
        return this.if < this.try;
    }
    
    public TinyString if() {
        this.do = ((this.if >= 0) ? this.if : this.a(this.do));
        this.if = -1;
        if (this.do >= this.try) {
            return null;
        }
        final int do1 = this.do;
        this.do = this.if(this.do);
        return this.new.substring(do1, this.do);
    }
    
    public int a() {
        int n = 0;
        int a;
        for (int i = this.do; i < this.try; i = this.if(a), ++n) {
            a = this.a(i);
            if (a >= this.try) {
                break;
            }
        }
        return n;
    }
}
