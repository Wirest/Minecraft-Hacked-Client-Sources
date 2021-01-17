// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

final class g
{
    int if;
    int byte;
    int a;
    int new;
    int for;
    int do;
    int case;
    int char;
    g int;
    g try;
    
    g() {
        this.for = 1;
    }
    
    final void a(final int n) {
        this.do = (this.a - this.if << 16) / (this.new - this.byte);
        this.case = this.if << 16;
        final int n2 = n - this.byte;
        if (n2 != 0) {
            this.case += this.do * n2;
        }
        this.char = this.case + 32768 >> 16;
    }
    
    final void a() {
        this.case += this.do;
        this.char = this.case + 32768 >> 16;
    }
}
