// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

final class b
{
    private static final int do = 512;
    private static final int long = 128;
    g[] s;
    int goto;
    int byte;
    private static int l;
    TinyPoint null;
    int char;
    int g;
    int a;
    int for;
    int[] r;
    int n;
    g case;
    g if;
    private c else;
    private c i;
    private c o;
    private c q;
    public f m;
    private f new;
    private f p;
    private f int;
    private TinyPoint h;
    private TinyPoint f;
    private TinyPoint e;
    private TinyPoint c;
    private TinyPoint void;
    private TinyMatrix try;
    private int[] d;
    private int[] b;
    private int[] k;
    private int[] j;
    
    public b() {
    }
    
    public void for() {
        this.else = new c(128);
        this.i = new c(16);
        this.m = new f();
        this.new = new f();
        this.p = new f();
        this.int = null;
        this.null = new TinyPoint();
        this.h = new TinyPoint(0, 0);
        this.f = new TinyPoint(0, 0);
        this.e = new TinyPoint(0, 0);
        this.c = new TinyPoint(0, 0);
        this.void = new TinyPoint(0, 0);
        this.d = new int[5];
        this.b = new int[5];
        this.k = new int[2];
        this.j = new int[2];
        this.do();
    }
    
    public void if() {
        this.else = null;
        this.i = null;
        this.m = null;
        this.new = null;
        this.p = null;
        this.int = null;
        this.null = null;
        this.h = null;
        this.f = null;
        this.e = null;
        this.c = null;
        this.void = null;
        this.d = null;
        this.b = null;
        this.k = null;
        this.j = null;
        this.s = null;
    }
    
    public void a(final TinyPath tinyPath, final TinyMatrix tinyMatrix) {
        final TinyPoint h = this.h;
        final TinyPoint h2 = this.h;
        final int n = 0;
        h2.y = n;
        h.x = n;
        final TinyPoint f = this.f;
        final TinyPoint f2 = this.f;
        final int n2 = 0;
        f2.y = n2;
        f.x = n2;
        final TinyPoint e = this.e;
        final TinyPoint e2 = this.e;
        final int n3 = 0;
        e2.y = n3;
        e.x = n3;
        final TinyPoint void1 = this.void;
        final TinyPoint void2 = this.void;
        final int n4 = 0;
        void2.y = n4;
        void1.x = n4;
        this.m.a();
        final int numPoints = tinyPath.numPoints();
        int i = 0;
        c c = null;
        while (i < numPoints) {
            switch (tinyPath.a[i]) {
                case 1: {
                    if (this.a(c)) {
                        this.m.a(c.if, c.do, c.for);
                    }
                    this.else.if();
                    c = this.else;
                    this.h.x = tinyPath.int[i];
                    this.h.y = tinyPath.do[i];
                    tinyMatrix.transformToDev(this.h, this.f);
                    c.a(this.f.x, this.f.y);
                    ++i;
                    continue;
                }
                case 2: {
                    if (c == null) {
                        this.else.if();
                        c = this.else;
                    }
                    this.h.x = tinyPath.int[i];
                    this.h.y = tinyPath.do[i];
                    tinyMatrix.transformToDev(this.h, this.f);
                    c.a(this.f.x, this.f.y);
                    ++i;
                    continue;
                }
                case 3: {
                    if (c == null) {
                        this.else.if();
                        c = this.else;
                    }
                    this.h.x = tinyPath.int[i - 1];
                    this.h.y = tinyPath.do[i - 1];
                    tinyMatrix.transformToDev(this.h, this.f);
                    this.h.x = tinyPath.int[i];
                    this.h.y = tinyPath.do[i];
                    tinyMatrix.transformToDev(this.h, this.e);
                    this.h.x = tinyPath.int[i + 1];
                    this.h.y = tinyPath.do[i + 1];
                    tinyMatrix.transformToDev(this.h, this.void);
                    this.a(this.f.x << 8, this.f.y << 8, this.e.x << 8, this.e.y << 8, this.void.x << 8, this.void.y << 8);
                    i += 2;
                    continue;
                }
                case 4: {
                    if (c == null) {
                        this.else.if();
                        c = this.else;
                    }
                    this.h.x = tinyPath.int[i - 1];
                    this.h.y = tinyPath.do[i - 1];
                    tinyMatrix.transformToDev(this.h, this.f);
                    this.h.x = tinyPath.int[i];
                    this.h.y = tinyPath.do[i];
                    tinyMatrix.transformToDev(this.h, this.e);
                    this.h.x = tinyPath.int[i + 1];
                    this.h.y = tinyPath.do[i + 1];
                    tinyMatrix.transformToDev(this.h, this.c);
                    this.h.x = tinyPath.int[i + 2];
                    this.h.y = tinyPath.do[i + 2];
                    tinyMatrix.transformToDev(this.h, this.void);
                    this.a(this.f.x << 8, this.f.y << 8, this.e.x << 8, this.e.y << 8, this.c.x << 8, this.c.y << 8, this.void.x << 8, this.void.y << 8);
                    i += 3;
                    continue;
                }
                case 5: {
                    if (c != null) {
                        c.do();
                    }
                    if (this.a(c)) {
                        this.m.a(c.if, c.do, c.for);
                    }
                    c = null;
                    ++i;
                    continue;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        if (this.a(c)) {
            this.m.a(c.if, c.do, c.for);
        }
    }
    
    void case() {
        for (int i = 0; i < this.m.do; ++i) {
            final int n = this.m.case[i];
            final int n2 = n + this.m.try[i];
            for (int j = n; j < n2 - 1; ++j) {
                this.a(this.m.a[j], this.m.int[j], this.m.a[j + 1], this.m.int[j + 1], false);
            }
            if (!this.m.a(i)) {
                this.a(this.m.a[n2 - 1], this.m.int[n2 - 1], this.m.a[n], this.m.int[n], false);
            }
        }
    }
    
    void new() {
        this.int = this.m;
        if (this.a <= 65536) {
            this.a = 65536;
            this.for = 0;
        }
        else {
            this.for = TinyUtil.div(131072, TinyUtil.mul(this.a, this.a)) - 65536;
        }
        if (this.char == 2 || this.g == 2) {
            this.int();
        }
        if (this.null.x < 65536 && this.null.y < 65536) {
            return;
        }
        if (this.try()) {
            this.p.a();
            final int do1 = this.int.do;
            this.new.a();
            for (int i = 0; i < do1; ++i) {
                this.if(i);
            }
            this.int = this.p;
        }
        final int do2 = this.int.do;
        this.new.a();
        for (int j = 0; j < do2; ++j) {
            this.a(j);
        }
        for (int k = 0; k < this.new.do; ++k) {
            final int n = this.new.case[k];
            for (int n2 = n + this.new.try[k], l = n; l < n2 - 1; ++l) {
                this.a(this.new.a[l], this.new.int[l], this.new.a[l + 1], this.new.int[l + 1], true);
            }
        }
    }
    
    private boolean try() {
        int n = 0;
        if (this.r != null) {
            int length = this.r.length;
            while (length-- > 0) {
                if (this.r[length] < 0) {
                    this.r = null;
                    break;
                }
                n += this.r[length];
            }
            if (n <= 0) {
                this.r = null;
            }
        }
        return this.r != null;
    }
    
    private boolean a(final c c) {
        return c != null && c.for >= 2 && (!c.a() || c.for >= 3);
    }
    
    private void if(final int n) {
        final int do1 = this.p.do;
        final c c = new c(0);
        final int for1 = this.int.try[n];
        c.if = new int[for1 + 1];
        c.do = new int[for1 + 1];
        System.arraycopy(this.int.a, this.int.case[n], c.if, 0, for1);
        System.arraycopy(this.int.int, this.int.case[n], c.do, 0, for1);
        c.for = for1;
        final int length = this.r.length;
        int n2 = 0;
        int n3 = 1;
        int n4 = 0;
        while (n4 != this.n && n4 + this.r[n2] <= this.n) {
            n4 += this.r[n2];
            if (++n2 == length) {
                n2 = 0;
            }
            n3 ^= 0x1;
        }
        int n5 = n4 + this.r[n2] - this.n;
        if (c.for > 1) {
            int n6 = c.if[0];
            int n7 = c.do[0];
            int i = 1;
            while (i < c.for) {
                this.i.if();
                this.i.a(n6, n7);
                while (n5 > 0 && i < c.for) {
                    final int n8 = c.if[i];
                    final int n9 = c.do[i];
                    final int n10 = n8 - n6;
                    final int n11 = n9 - n7;
                    int a = TinyUtil.a(n10 << 16, n11 << 16);
                    if (a == 0) {
                        a = 2;
                    }
                    if (a <= n5) {
                        n6 = n8;
                        n7 = n9;
                        this.i.a(n6, n7);
                        n5 -= a;
                        ++i;
                    }
                    else {
                        final int div = TinyUtil.div(n5, a);
                        n6 += TinyUtil.round(TinyUtil.mul(n10 << 16, div));
                        n7 += TinyUtil.round(TinyUtil.mul(n11 << 16, div));
                        this.i.a(n6, n7);
                        n5 = 0;
                    }
                }
                if (this.char != 1 && this.i.for == 1) {
                    int n12;
                    int n13;
                    if (i < c.for) {
                        n12 = c.if[i];
                        n13 = c.do[i];
                    }
                    else {
                        n12 = c.if[i - 1];
                        n13 = c.do[i - 1];
                        if (n6 == n12 && n7 == n13 && i > 1) {
                            n12 = c.if[i - 2];
                            n13 = c.do[i - 2];
                        }
                    }
                    final int n14 = n12 - n6;
                    final int n15 = n13 - n7;
                    if (n14 != 0 || n15 != 0) {
                        int abs = TinyUtil.abs(n14);
                        final int abs2 = TinyUtil.abs(n15);
                        if (abs < abs2) {
                            abs = abs2;
                        }
                        this.i.a(n6 + TinyUtil.round(TinyUtil.div(n14 << 13, abs)), n7 + TinyUtil.round(TinyUtil.div(n15 << 13, abs)));
                    }
                }
                if (n3 != 0) {
                    this.p.a(this.i.if, this.i.do, this.i.for);
                }
                if (++n2 == length) {
                    n2 = 0;
                }
                n5 = this.r[n2];
                n3 ^= 0x1;
            }
        }
        else if (n3 != 0) {
            this.p.a(c.if, c.do, c.for);
        }
        if (this.p.do - do1 > 1) {
            final int n16 = this.p.do - 1;
            if (this.p.a[this.p.case[do1]] == this.p.a[this.p.new - 1] && this.p.int[this.p.case[do1]] == this.p.int[this.p.new - 1]) {
                for (int j = 1 + this.p.case[do1]; j < this.p.try[do1]; ++j) {
                    this.p.a(this.p.a[j], this.p.int[j]);
                }
                this.p.do(do1);
            }
        }
    }
    
    private void a(final int n) {
        boolean b = false;
        final int n2 = this.int.try[n];
        this.else.if = new int[n2 + 1];
        this.else.do = new int[n2 + 1];
        final int[] if1 = this.else.if;
        final int[] do1 = this.else.do;
        System.arraycopy(this.int.a, this.int.case[n], if1, 0, n2);
        System.arraycopy(this.int.int, this.int.case[n], do1, 0, n2);
        int n3 = 0;
        for (int i = 1; i < n2; ++i) {
            if (if1[n3] != if1[i] || do1[n3] != do1[i]) {
                ++n3;
                if (i != n3) {
                    if1[n3] = if1[i];
                    do1[n3] = do1[i];
                }
            }
        }
        if (n3 > 0 && if1[n3] == if1[0] && do1[n3] == do1[0]) {
            b = true;
            if1[n3 + 1] = if1[1];
            do1[n3 + 1] = do1[1];
        }
        final int n4 = n3 + 1;
        if (n4 == 0) {
            return;
        }
        if (n4 == 1) {
            if (this.char == 2) {
                this.a(if1[0], do1[0]);
                this.new.a(this.q.if, this.q.do, this.q.for);
            }
            return;
        }
        int n5 = if1[0];
        int n6 = do1[0];
        int n7 = if1[1];
        int n8 = do1[1];
        final int n9 = n7 - n5;
        final int n10 = n8 - n6;
        int a = TinyUtil.a(n9 << 16, n10 << 16);
        if (a == 0) {
            a = 2;
        }
        int div = TinyUtil.div(n9 << 16, a);
        int div2 = TinyUtil.div(n10 << 16, a);
        int round = TinyUtil.round(TinyUtil.mul(this.null.x, div2));
        int round2 = TinyUtil.round(TinyUtil.mul(this.null.y, div));
        if (!b) {
            switch (this.char) {
                case 2: {
                    this.a(n5, n6);
                    this.new.a(this.q.if, this.q.do, this.q.for);
                    break;
                }
                case 3: {
                    n5 -= TinyUtil.round(TinyUtil.mul(this.null.x, div));
                    n6 -= TinyUtil.round(TinyUtil.mul(this.null.y, div2));
                    break;
                }
            }
        }
        for (int j = 1; j < n4; ++j) {
            final boolean b2 = j == n4 - 1;
            if (b2 && !b) {
                switch (this.char) {
                    case 2: {
                        this.a(n7, n8);
                        this.new.a(this.q.if, this.q.do, this.q.for);
                        break;
                    }
                    case 3: {
                        n7 += TinyUtil.round(TinyUtil.mul(this.null.x, div));
                        n8 += TinyUtil.round(TinyUtil.mul(this.null.y, div2));
                        break;
                    }
                }
            }
            this.d[4] = (this.d[0] = n5 - round);
            this.b[4] = (this.b[0] = n6 + round2);
            this.d[1] = n7 - round;
            this.b[1] = n8 + round2;
            this.d[2] = n7 + round;
            this.b[2] = n8 - round2;
            this.d[3] = n5 + round;
            this.b[3] = n6 - round2;
            this.new.a(this.d, this.b, 5, 7);
            if (!b2 || b) {
                final int n11 = if1[j + 1];
                final int n12 = do1[j + 1];
                final int n13 = n11 - n7;
                final int n14 = n12 - n8;
                int a2 = TinyUtil.a(n13 << 16, n14 << 16);
                if (a2 == 0) {
                    a2 = 2;
                }
                final int div3 = TinyUtil.div(n13 << 16, a2);
                final int div4 = TinyUtil.div(n14 << 16, a2);
                final int round3 = TinyUtil.round(TinyUtil.mul(this.null.x, div4));
                final int round4 = TinyUtil.round(TinyUtil.mul(this.null.y, div3));
                Label_1502: {
                    if (div3 != div || div4 != div2) {
                        final int n15 = TinyUtil.mul(div4, div) - TinyUtil.mul(div3, div2);
                        switch (this.g) {
                            case 2: {
                                this.a(n7, n8);
                                this.new.a(this.q.if, this.q.do, this.q.for);
                                break Label_1502;
                            }
                            case 1: {
                                if (TinyUtil.mul(div3, div) + TinyUtil.mul(div4, div2) < this.for) {
                                    break;
                                }
                                int n16 = div3 + div;
                                if (n16 == 0) {
                                    n16 = 2;
                                }
                                int n17 = div4 + div2;
                                if (n17 == 0) {
                                    n17 = 2;
                                }
                                if (n15 < 0) {
                                    this.k[0] = n7 - round3;
                                    this.j[0] = n8 + round4;
                                    this.k[1] = n7;
                                    this.j[1] = n8;
                                    int n18;
                                    if (Math.abs(n16) >= Math.abs(n17)) {
                                        n18 = TinyUtil.div(this.k[0] - this.d[1] << 16, n16);
                                    }
                                    else {
                                        n18 = TinyUtil.div(this.j[0] - this.b[1] << 16, n17);
                                    }
                                    this.new.a(1, this.d[1] + TinyUtil.round(TinyUtil.mul(n18, div)), this.b[1] + TinyUtil.round(TinyUtil.mul(n18, div2)));
                                    this.new.a(1, this.k, this.j, 2);
                                    break Label_1502;
                                }
                                this.k[0] = n7;
                                this.j[0] = n8;
                                this.k[1] = n7 + round3;
                                this.j[1] = n8 - round4;
                                int n19;
                                if (Math.abs(n16) >= Math.abs(n17)) {
                                    n19 = TinyUtil.div(this.k[1] - this.d[2] << 16, n16);
                                }
                                else {
                                    n19 = TinyUtil.div(this.j[1] - this.b[2] << 16, n17);
                                }
                                this.new.a(2, this.d[2] + TinyUtil.round(TinyUtil.mul(n19, div)), this.b[2] + TinyUtil.round(TinyUtil.mul(n19, div2)));
                                this.new.a(1, this.k, this.j, 2);
                                break Label_1502;
                            }
                        }
                        if (n15 < 0) {
                            this.k[0] = n7 - round3;
                            this.j[0] = n8 + round4;
                            this.k[1] = n7;
                            this.j[1] = n8;
                        }
                        else {
                            this.k[0] = n7;
                            this.j[0] = n8;
                            this.k[1] = n7 + round3;
                            this.j[1] = n8 - round4;
                        }
                        this.new.a(1, this.k, this.j, 2);
                    }
                }
                n5 = n7;
                n6 = n8;
                n7 = n11;
                n8 = n12;
                div = div3;
                div2 = div4;
                round = round3;
                round2 = round4;
            }
        }
    }
    
    private void int() {
        final int max = Math.max(this.null.x + 32768 >> 16, this.null.y + 32768 >> 16);
        final TinyPath ovalToPath = TinyPath.ovalToPath(0 - max, 0 - max, 2 * max, 2 * max);
        final TinyPoint f = this.f;
        final TinyPoint f2 = this.f;
        final int n = 0;
        f2.y = n;
        f.x = n;
        final int numPoints = ovalToPath.numPoints();
        int i = 0;
        c else1 = null;
        while (i < numPoints) {
            switch (ovalToPath.a[i]) {
                case 1: {
                    this.else.if();
                    else1 = this.else;
                    this.f.x = ovalToPath.int[i];
                    this.f.y = ovalToPath.do[i];
                    else1.a(this.f.x, this.f.y);
                    ++i;
                    continue;
                }
                case 2: {
                    this.f.x = ovalToPath.int[i];
                    this.f.y = ovalToPath.do[i];
                    else1.a(this.f.x, this.f.y);
                    ++i;
                    continue;
                }
                case 4: {
                    this.a(ovalToPath.int[i - 1] << 8, ovalToPath.do[i - 1] << 8, ovalToPath.int[i] << 8, ovalToPath.do[i] << 8, ovalToPath.int[i + 1] << 8, ovalToPath.do[i + 1] << 8, ovalToPath.int[i + 2] << 8, ovalToPath.do[i + 2] << 8);
                    i += 3;
                    continue;
                }
                case 5: {
                    else1.do();
                    ++i;
                    continue;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        else1.do();
        this.o = new c(this.else.if, this.else.do, this.else.for, this.else.for);
        this.q = new c(this.else.for);
    }
    
    private c a(final int n, final int n2) {
        this.q.for = this.o.for;
        int n3 = this.q.for - 1;
        for (int i = 0; i < this.q.for; ++i) {
            this.q.if[i] = this.o.if[n3] + n;
            this.q.do[i] = this.o.do[n3] + n2;
            --n3;
        }
        return this.q;
    }
    
    private final void a(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final int n7 = 16;
        int n8 = n + n5 - n3 << 2;
        if (n8 < 0) {
            n8 = -n8;
        }
        int n9 = n2 + n6 - n4 << 2;
        if (n9 < 0) {
            n9 = -n9;
        }
        if (n8 < n9) {
            n8 = n9;
        }
        int n10 = 1;
        for (int i = n8 / n7; i > 0; i >>= 2, ++n10) {}
        this.a(n, n2, n3, n4, n5, n6, n10 / 2 + 1);
    }
    
    private final void a(final int n, final int n2, int n3, int n4, int n5, int n6, final int n7) {
        final int n8 = (n2 + 128) / 256;
        final int n9 = (n6 + 128) / 256;
        final int n10 = n9 - n8;
        if (n7 == 1) {
            this.else.a(n >> 8, n8);
            this.else.a(n5 >> 8, n9);
            return;
        }
        boolean b = false;
        if (TinyUtil.abs(n10) <= 8) {
            if (TinyUtil.abs(n - n5) <= 2048) {
                if (n10 != 0) {
                    this.else.a(n >> 8, n8);
                    this.else.a(n5 >> 8, n9);
                }
                return;
            }
            b = true;
        }
        final int n11 = n5;
        final int n12 = n6;
        final int n13 = (n3 + n5) / 2;
        final int n14 = (n4 + n6) / 2;
        n3 = (n + n3) / 2;
        n4 = (n2 + n4) / 2;
        n5 = (n13 + n3) / 2;
        n6 = (n14 + n4) / 2;
        if (b) {
            final int n15 = (n6 + 128) / 256 - (n2 + 128) / 256;
            if (n15 != 0) {
                this.a(n, n2, n3, n4, n5, n6, n7 - 1);
            }
            final int n16 = n12 - n6;
            if (n16 != 0) {
                this.a(n5, n6, n13, n14, n11, n12, n7 - 1);
            }
            if (n15 == 0 && n16 == 0) {
                this.else.a(n >> 8, n8);
                this.else.a(n11 >> 8, n9);
            }
        }
        else {
            this.a(n, n2, n3, n4, n5, n6, n7 - 1);
            this.a(n5, n6, n13, n14, n11, n12, n7 - 1);
        }
    }
    
    private final void a(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        final int n9 = 16;
        int n10 = n + n7 - n3 << 2;
        if (n10 < 0) {
            n10 = -n10;
        }
        int n11 = n2 + n8 - n4 << 2;
        if (n11 < 0) {
            n11 = -n11;
        }
        if (n10 < n11) {
            n10 = n11;
        }
        final int n12 = n10;
        int n13 = n + n7 - 3 * (n3 + n5);
        if (n13 < 0) {
            n13 = -n13;
        }
        int n14 = n2 + n8 - 3 * (n3 + n6);
        if (n14 < 0) {
            n14 = -n14;
        }
        if (n13 < n14) {
            n13 = n14;
        }
        final int n15 = n13;
        int n16 = 1;
        for (int n17 = n12 / n9, n18 = n15 / n9; n17 > 0 || n18 > 0; n17 >>= 2, n18 >>= 3, ++n16) {}
        this.a(n, n2, n3, n4, n5, n6, n7, n8, n16 / 2 + 1);
    }
    
    private final void a(final int n, final int n2, int n3, int n4, int n5, int n6, int n7, int n8, final int n9) {
        final int n10 = (n2 + 128) / 256;
        final int n11 = (n8 + 128) / 256;
        final int n12 = n11 - n10;
        if (n9 == 1) {
            this.else.a(n >> 8, n10);
            this.else.a(n7 >> 8, n11);
            return;
        }
        boolean b = false;
        if (TinyUtil.abs(n12) <= 8) {
            if (TinyUtil.abs(n - n7) <= 2048) {
                if (n12 != 0) {
                    this.else.a(n >> 8, n10);
                    this.else.a(n7 >> 8, n11);
                }
                return;
            }
            b = true;
        }
        final int n13 = n7;
        final int n14 = n8;
        final int n15 = (n3 + n5) / 2;
        final int n16 = (n4 + n6) / 2;
        final int n17 = (n5 + n7) / 2;
        final int n18 = (n6 + n8) / 2;
        n3 = (n + n3) / 2;
        n4 = (n2 + n4) / 2;
        n5 = (n3 + n15) / 2;
        n6 = (n4 + n16) / 2;
        final int n19 = (n15 + n17) / 2;
        final int n20 = (n16 + n18) / 2;
        n7 = (n5 + n19) / 2;
        n8 = (n6 + n20) / 2;
        if (b) {
            final int n21 = (n8 + 128) / 256 - (n2 + 128) / 256;
            if (n21 != 0) {
                this.a(n, n2, n3, n4, n5, n6, n7, n8, n9 - 1);
            }
            final int n22 = n14 - n8;
            if (n22 != 0) {
                this.a(n7, n8, n19, n20, n17, n18, n13, n14, n9 - 1);
            }
            if (n21 == 0 && n22 == 0) {
                this.else.a(n >> 8, n10);
                this.else.a(n13 >> 8, n11);
            }
        }
        else {
            this.a(n, n2, n3, n4, n5, n6, n7, n8, n9 - 1);
            this.a(n7, n8, n19, n20, n17, n18, n13, n14, n9 - 1);
        }
    }
    
    private void a(final int n, final int n2, final int n3, final int n4, final boolean b) {
        if (n2 == n4) {
            return;
        }
        final g byte1 = this.byte();
        if (n2 <= n4) {
            byte1.if = n;
            byte1.byte = n2;
            byte1.a = n3;
            byte1.new = n4;
            byte1.for = 1;
        }
        else {
            byte1.if = n3;
            byte1.byte = n4;
            byte1.a = n;
            byte1.new = n2;
            byte1.for = -1;
        }
        if (b) {
            byte1.int = this.if;
            this.if = byte1;
        }
        else {
            byte1.int = this.case;
            this.case = byte1;
        }
    }
    
    void do() {
        this.goto = 512;
        this.s = new g[this.goto];
        for (int i = 0; i < this.goto; ++i) {
            this.s[i] = new g();
        }
        this.byte = 0;
    }
    
    g byte() {
        if (this.byte == this.goto) {
            this.goto += 128;
            final g[] s = new g[this.goto];
            System.arraycopy(this.s, 0, s, 0, this.byte);
            this.s = s;
            for (int i = this.byte; i < this.goto; ++i) {
                this.s[i] = new g();
            }
        }
        final g g = this.s[this.byte++];
        g.for = 1;
        return g;
    }
    
    void a() {
        if (this.goto - 512 > 128) {
            this.goto = 512;
            final g[] s = new g[this.goto];
            System.arraycopy(this.s, 0, s, 0, this.goto);
            this.s = s;
        }
        this.byte = 0;
    }
    
    static {
        b.l = 4;
    }
}
