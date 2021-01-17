// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public final class i
{
    private a a;
    
    public i() {
        this.a = new a();
    }
    
    public void d() {
        if (this.a != null) {
            this.a.a();
            this.a = null;
        }
    }
    
    public TinyPixbuf c() {
        return this.a.s;
    }
    
    public void a(final TinyPixbuf tinyPixbuf) {
        this.a.a(tinyPixbuf);
    }
    
    public void char(final int value) {
        this.a.if.value = value;
    }
    
    public int e() {
        return this.a.if.value;
    }
    
    public void a(final boolean else1) {
        this.a.else = else1;
    }
    
    public boolean byte() {
        return this.a.else;
    }
    
    public void if(final TinyRect tinyRect) {
        this.a.a(tinyRect);
    }
    
    public void a(final TinyMatrix m) {
        this.a.m = m;
    }
    
    public TinyMatrix goto() {
        return this.a.m;
    }
    
    public void a(final TinyRect n) {
        this.a.n = n;
    }
    
    public TinyRect int() {
        return this.a.n;
    }
    
    public void do(final TinyRect w) {
        this.a.w = w;
    }
    
    public TinyRect new() {
        return this.a.w;
    }
    
    public int b() {
        return this.a.long;
    }
    
    public void byte(final int long1) {
        this.a.long = long1;
    }
    
    public TinyColor for() {
        return this.a.A;
    }
    
    public void a(final TinyColor a) {
        this.a.A = a;
    }
    
    public TinyColor case() {
        return this.a.null;
    }
    
    public void if(final TinyColor null) {
        this.a.null = null;
    }
    
    public int[] if() {
        return this.a.do;
    }
    
    public void a(final int[] do1) {
        this.a.do = do1;
    }
    
    public void for(final int byte1) {
        this.a.byte = byte1;
    }
    
    public int null() {
        return this.a.byte;
    }
    
    public int a() {
        return this.a.q;
    }
    
    public void case(final int q) {
        this.a.q = q;
    }
    
    public int long() {
        return this.a.c;
    }
    
    public void int(final int c) {
        this.a.c = c;
    }
    
    public int try() {
        return this.a.g;
    }
    
    public void try(final int g) {
        this.a.g = g;
    }
    
    public int char() {
        return this.a.t;
    }
    
    public void new(final int t) {
        this.a.t = t;
    }
    
    public int do() {
        return this.a.x;
    }
    
    public void a(final int x) {
        this.a.x = x;
    }
    
    public int void() {
        return this.a.j;
    }
    
    public void do(final int j) {
        this.a.j = j;
    }
    
    public int else() {
        return this.a.b;
    }
    
    public void if(final int b) {
        this.a.b = b;
    }
    
    public TinyRect a(final TinyMatrix tinyMatrix, final int n, final TinyRect tinyRect) {
        final TinyRect tinyRect2 = new TinyRect();
        final TinyPoint tinyPoint = new TinyPoint();
        TinyRect tinyRect3 = tinyMatrix.transformToDev(tinyRect);
        this.a.a(tinyPoint, n, tinyMatrix);
        if (tinyPoint.x != 0) {
            tinyRect3 = tinyRect3.grow(TinyUtil.round(tinyPoint.x) + 1, TinyUtil.round(tinyPoint.y) + 1);
        }
        return tinyRect3;
    }
    
    public void a(final TinyPath tinyPath) {
        this.a.a(tinyPath);
    }
}
