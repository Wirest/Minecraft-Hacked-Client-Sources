// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

class a
{
    public TinyMatrix m;
    public TinyRect w;
    public int long;
    public TinyColor A;
    public TinyColor null;
    public int q;
    public int c;
    public int g;
    public int t;
    public int[] do;
    public int byte;
    public boolean new;
    public TinyRect n;
    public boolean else;
    public TinyColor if;
    public int b;
    public int j;
    public int x;
    TinyRect y;
    private TinyRect goto;
    TinyMatrix e;
    TinyMatrix h;
    private TinyColor i;
    TinyPixbuf s;
    private b z;
    private g[] char;
    private int case;
    private int r;
    private g[] int;
    private int p;
    private static final int o = 512;
    private static final int u = 256;
    private e d;
    private e for;
    private e[] a;
    private int try;
    private int l;
    private static final int k = 512;
    private static final int f = 128;
    private static final int[] v;
    private static final int[] void;
    
    public a() {
        (this.z = new b()).for();
        this.i = null;
        this.p = 512;
        this.int = new g[this.p];
        this.y = new TinyRect();
        this.goto = new TinyRect();
        this.e = new TinyMatrix();
        final TinyMatrix e = this.e;
        final TinyMatrix e2 = this.e;
        final int n = 262144;
        e2.d = n;
        e.a = n;
        this.if = new TinyColor(-1);
        this.b = 256;
        this.j = 256;
        this.x = 256;
        this.if();
    }
    
    public void a() {
        this.z.if();
        this.z = null;
        this.i = null;
        this.int = null;
        this.y = null;
        this.goto = null;
        this.e = null;
        this.char = null;
        this.a = null;
        this.if = null;
    }
    
    public void a(final TinyPixbuf s) {
        this.s = s;
        this.char = new g[4 * s.height + 1];
    }
    
    public int[] int() {
        return this.s.pixels32;
    }
    
    public void a(final TinyRect tinyRect) {
        if (tinyRect.isEmpty() || this.s == null) {
            return;
        }
        this.s.a(this.if.value, tinyRect);
    }
    
    public void a(final TinyPath tinyPath) {
        if (this.x == 0) {
            return;
        }
        if (!this.w.intersects(this.n)) {
            return;
        }
        this.y = this.n.intersection(this.w);
        this.y.xmin = TinyUtil.max(this.y.xmin, 0);
        this.y.xmax = TinyUtil.min(this.y.xmax, this.s.width);
        this.y.ymin = TinyUtil.max(this.y.ymin, 0);
        this.y.ymax = TinyUtil.min(this.y.ymax, this.s.height);
        this.h = new TinyMatrix(this.m);
        if (this.else) {
            this.goto.xmin = this.y.xmin << 2;
            this.goto.xmax = this.y.xmax << 2;
            this.goto.ymin = this.y.ymin << 2;
            this.goto.ymax = this.y.ymax << 2;
            this.m.concatenate(this.e);
        }
        else {
            this.goto.xmin = this.y.xmin;
            this.goto.xmax = this.y.xmax;
            this.goto.ymin = this.y.ymin;
            this.goto.ymax = this.y.ymax;
        }
        this.case = this.goto.ymax - this.goto.ymin + 1;
        this.z.case = null;
        this.z.if = null;
        this.z.a();
        this.z.a(tinyPath, this.m);
        final int x = this.x;
        if (this.A != TinyColor.NONE && this.b != 0) {
            (this.i = this.A).a();
            this.x = this.x * this.b >> 8;
            this.z.case();
            this.a(this.z.case);
            this.for();
            this.z.a();
            this.z.case = null;
        }
        this.x = x;
        if (this.null != TinyColor.NONE && this.j != 0) {
            this.a(this.z.null, this.q, this.m);
            this.z.char = this.c;
            this.z.g = this.g;
            this.z.a = this.t << 8;
            this.z.r = this.do();
            this.z.n = this.try();
            this.long = 1;
            (this.i = this.null).a();
            this.x = this.x * this.j >> 8;
            this.z.new();
            this.a(this.z.if);
            this.for();
            this.z.a();
            this.z.if = null;
        }
        this.x = x;
    }
    
    public void a(final TinyPoint tinyPoint, final int n, final TinyMatrix tinyMatrix) {
        final int n2 = 0;
        tinyPoint.y = n2;
        tinyPoint.x = n2;
        final int n3 = n << 7;
        if (n3 == 0) {
            return;
        }
        final int a = tinyMatrix.a(n3);
        tinyPoint.y = a;
        tinyPoint.x = a;
    }
    
    private int try() {
        final int n = this.byte << 8;
        if (n == 0) {
            return n;
        }
        int a = this.m.a(n);
        if (a > 0 && a < 65536) {
            a = 65536;
        }
        return a;
    }
    
    private int[] do() {
        if (this.do == null) {
            return null;
        }
        final int length = this.do.length;
        final int[] array = new int[length];
        System.arraycopy(this.do, 0, array, 0, length);
        for (int i = 0; i < length; ++i) {
            int a = array[i] << 8;
            if (a != 0) {
                a = this.m.a(a);
            }
            if (a > 0 && a < 65536) {
                a = 65536;
            }
            array[i] = a;
        }
        return array;
    }
    
    private final void a(g g) {
        int case1 = this.case;
        while (--case1 >= 0) {
            if (this.char[case1] != null) {
                this.char[case1] = null;
            }
        }
        int n = 0;
        TinyColor tinyColor = null;
        g g2 = null;
        while (g != null) {
            if (g.byte <= this.goto.ymax && g.new > this.goto.ymin) {
                int n2 = g.byte - this.goto.ymin;
                if (n2 < 0) {
                    n2 = 0;
                }
                g.try = this.char[n2];
                this.char[n2] = g;
            }
            g = g.int;
        }
        this.s.pixelOffset = this.s.width * this.y.ymin;
        this.r = this.goto.ymin;
        while (this.r < this.goto.ymax) {
            g[] int1;
            int n3;
            for (g = this.char[this.r - this.goto.ymin]; g != null; g = g.try) {
                g.a(this.r);
                if (n == this.p) {
                    this.p += 256;
                    int1 = new g[this.p];
                    System.arraycopy(this.int, 0, int1, 0, n);
                    this.int = int1;
                }
                for (n3 = n; n3 != 0 && this.int[n3 - 1].char >= g.char; --n3) {
                    this.int[n3] = this.int[n3 - 1];
                }
                this.int[n3] = g;
                ++n;
            }
            int n4 = n;
            while (--n4 >= 0) {
                boolean b = false;
                for (int i = 0; i < n4; ++i) {
                    if (this.int[i].char > this.int[i + 1].char) {
                        final g g3 = this.int[i];
                        this.int[i] = this.int[i + 1];
                        this.int[i + 1] = g3;
                        b = true;
                    }
                }
                if (!b) {
                    break;
                }
            }
            if (this.else) {
                if (this.d == null) {
                    this.d = this.new();
                    this.d.for = this.goto.xmin;
                    this.d.a = this.goto.xmax;
                }
                this.for = this.d;
            }
            int n5 = 0;
            for (int j = 0; j < n; ++j) {
                final g g4 = this.int[j];
                if (this.long == 2) {
                    if (this.i.visible != 0) {
                        this.i.visible = 0;
                        if (this.i == tinyColor) {
                            if (this.else) {
                                this.a(g2.char, g4.char);
                            }
                            else {
                                this.a(g2, g4, this.r);
                            }
                            tinyColor = null;
                            g2 = g4;
                        }
                    }
                    else {
                        this.i.visible = 1;
                        if (tinyColor == null) {
                            tinyColor = this.i;
                            g2 = g4;
                        }
                    }
                }
                else if (this.i.visible == 0) {
                    final TinyColor k = this.i;
                    k.visible += g4.for;
                    if (tinyColor == null) {
                        tinyColor = this.i;
                        g2 = g4;
                    }
                }
                else {
                    final TinyColor l = this.i;
                    l.visible += g4.for;
                    if (this.i.visible == 0 && this.i == tinyColor) {
                        if (this.else) {
                            this.a(g2.char, g4.char);
                        }
                        else {
                            this.a(g2, g4, this.r);
                        }
                        tinyColor = null;
                        g2 = g4;
                    }
                }
                if (g4.new > this.r + 1) {
                    g4.a();
                    this.int[n5++] = g4;
                }
            }
            n = n5;
            if (this.else) {
                if ((this.r & 0x3) == 0x3) {
                    this.a(this.r >> 2);
                    this.byte();
                    final TinyPixbuf s = this.s;
                    s.pixelOffset += this.s.width;
                }
            }
            else {
                final TinyPixbuf s2 = this.s;
                s2.pixelOffset += this.s.width;
            }
            ++this.r;
        }
    }
    
    private final void a(final int n) {
        int n2 = 0;
        int n3 = 134217728;
        e e = this.d;
        while (true) {
            if (e.do > 0) {
                if (e.do == 4) {
                    while (true) {
                        final e if1 = e.if;
                        if (if1 == null || if1.do < 4) {
                            break;
                        }
                        e.a = if1.a;
                        e.if = if1.if;
                    }
                }
                int n4 = e.for >> 2;
                final int n5 = e.for & 0x3;
                final int n6 = e.a >> 2;
                final int n7 = e.a & 0x3;
                final int n8 = e.do * 16711680;
                if (n4 == n6) {
                    if (n3 != n4) {
                        if (n2 > 0) {
                            this.a(n2 >> 20 & 0xFF, n3, n3 + 1, n);
                            n2 = 0;
                        }
                        n3 = n4;
                    }
                    n2 += (n7 - n5) * n8;
                }
                else {
                    if (n5 > 0) {
                        if (n3 != n4) {
                            if (n2 > 0) {
                                this.a(n2 >> 20 & 0xFF, n3, n3 + 1, n);
                                n2 = 0;
                            }
                            n3 = n4;
                        }
                        n2 += (4 - n5) * n8;
                        ++n4;
                    }
                    if (n4 < n6) {
                        if (e.do == 4) {
                            this.a(255, n4, n6, n);
                        }
                        else {
                            this.a(n8 >> 18 & 0xFF, n4, n6, n);
                        }
                    }
                    if (n7 > 0) {
                        if (n3 != n6) {
                            if (n2 > 0) {
                                this.a(n2 >> 20 & 0xFF, n3, n3 + 1, n);
                                n2 = 0;
                            }
                            n3 = n6;
                        }
                        n2 += n7 * n8;
                    }
                }
            }
            if (e.if == null) {
                break;
            }
            e = e.if;
        }
        this.d = null;
        if (n2 > 0) {
            this.a(n2 >> 20 & 0xFF, n3, n3 + 1, n);
        }
    }
    
    private final void a(final int n, final int n2) {
        e for1 = this.for;
        if (for1 == null || for1.for >= n2) {
            return;
        }
        while (for1.a < n) {
            for1 = for1.if;
            if (for1 == null) {
                this.for = null;
                return;
            }
        }
        if (for1.for < n) {
            for1 = this.a(for1, n);
        }
        while (for1 != null && for1.for < n2) {
            if (for1.a > n2) {
                this.for = this.a(for1, n2);
                final e e = for1;
                ++e.do;
                return;
            }
            final e e2 = for1;
            ++e2.do;
            for1 = for1.if;
        }
        this.for = for1;
    }
    
    private final e a(final e e, final int n) {
        final e new1 = this.new();
        new1.for = n;
        new1.a = e.a;
        new1.do = e.do;
        new1.if = e.if;
        e.a = n;
        return e.if = new1;
    }
    
    private final void a(int n, int xmin, int xmax, final int n2) {
        if (xmin < this.y.xmin) {
            xmin = this.y.xmin;
        }
        if (xmax > this.y.xmax) {
            xmax = this.y.xmax;
        }
        n = n * this.x >> 8;
        this.s.a(this.i, n, xmin, xmax, n2);
    }
    
    private final void a(final g g, final g g2, final int n) {
        final int n2 = g.case + 32768 >> 13;
        final int n3 = g2.case + 32768 >> 13;
        final int n4 = n2 & 0x7;
        final int n5 = n3 & 0x7;
        final int char1 = g.char;
        final int char2 = g2.char;
        if (char2 != char1) {
            this.a(com.tinyline.tiny2d.a.v[n4], char1, char1 + 1, n);
            if (char2 > char1 + 1) {
                this.a(255, char1 + 1, char2, n);
            }
            this.a(com.tinyline.tiny2d.a.void[n5], char2, char2 + 1, n);
        }
        else {
            this.a(com.tinyline.tiny2d.a.v[n4] & com.tinyline.tiny2d.a.void[n5], char1, char1 + 1, n);
        }
    }
    
    private final void if() {
        this.try = 512;
        this.a = new e[this.try];
        for (int i = 0; i < this.try; ++i) {
            this.a[i] = new e();
        }
        this.l = 0;
    }
    
    private final e new() {
        if (this.l == this.try) {
            this.try += 128;
            final e[] a = new e[this.try];
            System.arraycopy(this.a, 0, a, 0, this.l);
            this.a = a;
            for (int i = this.l; i < this.try; ++i) {
                this.a[i] = new e();
            }
        }
        final e e = this.a[this.l++];
        e.do = 0;
        e.if = null;
        return e;
    }
    
    private final void byte() {
        if (this.try - 512 > 128) {
            this.try = 512;
            final e[] a = new e[this.try];
            System.arraycopy(this.a, 0, a, 0, this.try);
            this.a = a;
        }
        this.l = 0;
    }
    
    private final void for() {
        if (this.p - 512 > 256) {
            this.p = 512;
            this.int = new g[this.p];
        }
    }
    
    static {
        v = new int[] { 255, 127, 63, 31, 15, 7, 3, 1 };
        void = new int[] { 128, 192, 224, 240, 248, 252, 254, 255 };
    }
}
