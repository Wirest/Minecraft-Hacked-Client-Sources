// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyParsers
{
    static final int int = 0;
    static final int byte = 1;
    static final int if = 2;
    static final int a = 4;
    static final int e = 8;
    static final int h = 16;
    static final int b = 32;
    private int void;
    private char[] long;
    private int g;
    private int else;
    private char[] c;
    private int for;
    private int goto;
    private boolean f;
    private TinyPath char;
    private int d;
    private int null;
    private int case;
    private int try;
    private TinyVector do;
    private TinyTransform new;
    
    public TinyParsers() {
        this.c = new char[32];
    }
    
    public TinyColor parseRGB(final char[] array, final int n) {
        boolean b = false;
        for (int i = 0; i < n; ++i) {
            if (array[i] == '%') {
                array[i] = ' ';
                b = true;
            }
        }
        this.a(array, n);
        this.goto();
        this.null();
        final int n2 = 255;
        int n3 = this.f() >> 8;
        this.long();
        int n4 = this.f() >> 8;
        this.long();
        int n5 = this.f() >> 8;
        final TinyColor tinyColor = new TinyColor(-16777216);
        if (b) {
            n3 = n3 * 255 / 100;
            n4 = n4 * 255 / 100;
            n5 = n5 * 255 / 100;
        }
        tinyColor.value = (n2 << 24) + (n3 << 16) + (n4 << 8) + n5;
        return tinyColor;
    }
    
    public TinyRect parseRect(final char[] array, final int n) {
        this.void = 0;
        this.a(array, n);
        this.goto();
        this.null();
        final TinyRect tinyRect = new TinyRect();
        tinyRect.xmin = this.f();
        this.long();
        tinyRect.ymin = this.f();
        this.long();
        tinyRect.xmax = tinyRect.xmin + this.f();
        this.long();
        tinyRect.ymax = tinyRect.ymin + this.f();
        return tinyRect;
    }
    
    public TinyPoint parsePoint(final char[] array, final int n) {
        this.void = 0;
        this.a(array, n);
        final TinyPoint tinyPoint = new TinyPoint();
        this.goto();
        this.null();
        tinyPoint.x = this.f();
        this.long();
        tinyPoint.y = this.f();
        return tinyPoint;
    }
    
    public TinyVector parsePoints(final char[] array, final int n) {
        final TinyVector tinyVector = new TinyVector(11);
        this.void = 0;
        this.a(array, n);
        this.goto();
        this.null();
        final int f = this.f();
        this.long();
        tinyVector.addElement(new TinyPoint(f, this.f()));
        this.long();
        while (this.goto != -1) {
            final int f2 = this.f();
            this.long();
            tinyVector.addElement(new TinyPoint(f2, this.f()));
            this.long();
        }
        return tinyVector;
    }
    
    public int[] parseDashArray(final char[] array, final int n) {
        this.void = 0;
        this.a(array, n);
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (array[i] == ',') {
                ++n2;
            }
        }
        if (++n2 == 1) {
            return null;
        }
        final int[] array2 = new int[n2];
        this.goto();
        this.null();
        for (int j = 0; j < n2; ++j) {
            array2[j] = this.f();
            this.long();
        }
        return array2;
    }
    
    private final int f() {
        this.new();
        return TinyNumber.parseFix(this.c, 0, this.for);
    }
    
    private final void new() {
        this.for = 0;
        this.a();
        while (true) {
            this.goto();
            switch (this.goto) {
                case 9:
                case 10:
                case 13:
                case 32:
                case 44:
                case 59: {}
                default: {
                    if (this.goto == -1) {
                        return;
                    }
                    this.a();
                    continue;
                }
            }
        }
    }
    
    private final void a() {
        if (this.for >= this.c.length) {
            final char[] c = new char[this.c.length * 2];
            for (int i = 0; i < this.for; ++i) {
                c[i] = this.c[i];
            }
            this.c = c;
        }
        this.c[this.for++] = (char)this.goto;
    }
    
    private final void null() {
        while (true) {
            switch (this.goto) {
                default: {}
                case 9:
                case 10:
                case 13:
                case 32: {
                    this.goto();
                    continue;
                }
            }
        }
    }
    
    private final void long() {
        while (true) {
            switch (this.goto) {
                default: {
                    Label_0134: {
                        if (this.goto == 44 || this.goto == 59) {
                            while (true) {
                                this.goto();
                                switch (this.goto) {
                                    default: {
                                        break Label_0134;
                                    }
                                    case 9:
                                    case 10:
                                    case 13:
                                    case 32: {
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                case 9:
                case 10:
                case 13:
                case 32: {
                    this.goto();
                    continue;
                }
            }
        }
    }
    
    private final void a(final char[] long1, final int else1) {
        this.long = long1;
        this.g = 0;
        this.else = else1;
    }
    
    private final void goto() {
        if (this.g == this.else) {
            this.goto = -1;
        }
        else {
            this.goto = this.long[this.g++];
        }
    }
    
    private final void a(final int n) {
        this.goto();
        this.null();
        this.void = 0;
        final int void1 = this.void();
        this.long();
        final int void2 = this.void();
        if (this.void != 0) {
            return;
        }
        if (n == 109) {
            final TinyPath char1 = this.char;
            final int n2 = this.d + void1;
            this.d = n2;
            this.case = n2;
            final int n3 = this.null + void2;
            this.null = n3;
            char1.moveTo(n2, this.try = n3);
        }
        else {
            final TinyPath char2 = this.char;
            final int n4 = void1;
            this.d = n4;
            this.case = n4;
            final int n5 = void2;
            this.null = n5;
            char2.moveTo(n4, this.try = n5);
        }
        this.long();
    }
    
    private final void do(final int n) {
        if (this.goto == n) {
            this.goto();
        }
        this.null();
        this.void = 0;
        while (true) {
            switch (this.goto) {
                case 43:
                case 45:
                case 46:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57: {
                    final int void1 = this.void();
                    this.long();
                    final int void2 = this.void();
                    if (this.void != 0) {
                        return;
                    }
                    if (n == 108) {
                        final TinyPath char1 = this.char;
                        final int n2 = this.d + void1;
                        this.d = n2;
                        this.case = n2;
                        final int n3 = this.null + void2;
                        this.null = n3;
                        char1.lineTo(n2, this.try = n3);
                    }
                    else {
                        final TinyPath char2 = this.char;
                        final int n4 = void1;
                        this.d = n4;
                        this.case = n4;
                        final int n5 = void2;
                        this.null = n5;
                        char2.lineTo(n4, this.try = n5);
                    }
                    this.long();
                    continue;
                }
                default: {}
            }
        }
    }
    
    private final void if(final int n) {
        this.goto();
        this.null();
        this.void = 0;
        while (true) {
            switch (this.goto) {
                case 43:
                case 45:
                case 46:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57: {
                    switch (n) {
                        case 104: {
                            final int void1 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            final TinyPath char1 = this.char;
                            final int n2 = this.d + void1;
                            this.d = n2;
                            char1.lineTo(this.case = n2, this.try = this.null);
                            break;
                        }
                        case 72: {
                            final int void2 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            final TinyPath char2 = this.char;
                            final int n3 = void2;
                            this.d = n3;
                            char2.lineTo(this.case = n3, this.try = this.null);
                            break;
                        }
                        case 118: {
                            final int void3 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            final TinyPath char3 = this.char;
                            final int d = this.d;
                            this.case = d;
                            final int n4 = this.null + void3;
                            this.null = n4;
                            char3.lineTo(d, this.try = n4);
                            break;
                        }
                        case 86: {
                            final int void4 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            final TinyPath char4 = this.char;
                            final int d2 = this.d;
                            this.case = d2;
                            final int n5 = void4;
                            this.null = n5;
                            char4.lineTo(d2, this.try = n5);
                            break;
                        }
                        case 99: {
                            final int void5 = this.void();
                            this.long();
                            final int void6 = this.void();
                            this.long();
                            final int void7 = this.void();
                            this.long();
                            final int void8 = this.void();
                            this.long();
                            final int void9 = this.void();
                            this.long();
                            final int void10 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveToCubic(this.d + void5, this.null + void6, this.case = this.d + void7, this.try = this.null + void8, this.d += void9, this.null += void10);
                            break;
                        }
                        case 67: {
                            final int void11 = this.void();
                            this.long();
                            final int void12 = this.void();
                            this.long();
                            final int void13 = this.void();
                            this.long();
                            final int void14 = this.void();
                            this.long();
                            final int void15 = this.void();
                            this.long();
                            final int void16 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveToCubic(void11, void12, this.case = void13, this.try = void14, this.d = void15, this.null = void16);
                            break;
                        }
                        case 113: {
                            final int void17 = this.void();
                            this.long();
                            final int void18 = this.void();
                            this.long();
                            final int void19 = this.void();
                            this.long();
                            final int void20 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveTo(this.case = this.d + void17, this.try = this.null + void18, this.d += void19, this.null += void20);
                            break;
                        }
                        case 81: {
                            final int void21 = this.void();
                            this.long();
                            final int void22 = this.void();
                            this.long();
                            final int void23 = this.void();
                            this.long();
                            final int void24 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveTo(this.case = void21, this.try = void22, this.d = void23, this.null = void24);
                            break;
                        }
                        case 115: {
                            final int void25 = this.void();
                            this.long();
                            final int void26 = this.void();
                            this.long();
                            final int void27 = this.void();
                            this.long();
                            final int void28 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveToCubic(this.d * 2 - this.case, this.null * 2 - this.try, this.case = this.d + void25, this.try = this.null + void26, this.d += void27, this.null += void28);
                            break;
                        }
                        case 83: {
                            final int void29 = this.void();
                            this.long();
                            final int void30 = this.void();
                            this.long();
                            final int void31 = this.void();
                            this.long();
                            final int void32 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveToCubic(this.d * 2 - this.case, this.null * 2 - this.try, this.case = void29, this.try = void30, this.d = void31, this.null = void32);
                            break;
                        }
                        case 116: {
                            final int void33 = this.void();
                            this.long();
                            final int void34 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveTo(this.case = this.d * 2 - this.case, this.try = this.null * 2 - this.try, this.d += void33, this.null += void34);
                            break;
                        }
                        case 84: {
                            final int void35 = this.void();
                            this.long();
                            final int void36 = this.void();
                            if (this.void != 0) {
                                return;
                            }
                            this.char.curveTo(this.case = this.d * 2 - this.case, this.try = this.null * 2 - this.try, this.d = void35, this.null = void36);
                            break;
                        }
                    }
                    this.long();
                    continue;
                }
                default: {}
            }
        }
    }
    
    private final void e() {
        while (true) {
            switch (this.goto) {
                case 77:
                case 109: {}
                default: {
                    if (this.goto == -1) {
                        return;
                    }
                    this.goto();
                    continue;
                }
            }
        }
    }
    
    private final int void() {
        this.byte();
        int n;
        if (this.f) {
            n = TinyNumber.parseInt(this.c, 0, this.for, 10);
            this.void |= TinyNumber.error;
        }
        else {
            n = TinyNumber.parseFix(this.c, 0, this.for);
            this.void |= TinyNumber.error;
        }
        return n;
    }
    
    private final void byte() {
        this.for = 0;
        this.a();
        while (true) {
            this.goto();
            switch (this.goto) {
                case 9:
                case 10:
                case 13:
                case 32:
                case 43:
                case 44:
                case 45:
                case 65:
                case 67:
                case 72:
                case 76:
                case 77:
                case 81:
                case 83:
                case 84:
                case 86:
                case 90:
                case 97:
                case 99:
                case 104:
                case 108:
                case 109:
                case 113:
                case 115:
                case 116:
                case 118:
                case 122: {}
                default: {
                    if (this.goto == -1) {
                        return;
                    }
                    this.a();
                    continue;
                }
            }
        }
    }
    
    private final void for() {
    }
    
    private final void else() {
        this.char.closePath();
        final TinyPoint currentPoint = this.char.getCurrentPoint();
        this.d = currentPoint.x;
        this.null = currentPoint.y;
    }
    
    public TinyVector parseNumbers(final char[] array, final int n) {
        final TinyVector tinyVector = new TinyVector(4);
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (array[i] == ';') {
                ++n2;
            }
        }
        if (++n2 == 1) {
            return tinyVector;
        }
        final TinyVector tinyVector2 = new TinyVector(n2);
        this.a(array, n);
        this.goto();
        this.null();
        for (int j = 0; j < n2; ++j) {
            tinyVector2.addElement(new TinyNumber(this.f()));
            this.long();
        }
        return tinyVector2;
    }
    
    public TinyPath parseSpline(final char[] array, final int n) {
        this.a(array, n);
        final TinyPath tinyPath = new TinyPath(6);
        this.goto();
        this.null();
        final int n2 = 256;
        final int n3 = 256;
        final int n4 = this.f() << 8;
        this.long();
        final int n5 = this.f() << 8;
        this.long();
        final int n6 = this.f() << 8;
        this.long();
        final int n7 = this.f() << 8;
        tinyPath.moveTo(0, 0);
        tinyPath.curveToCubic(n2, n3, n4, n5, n6, n7);
        return tinyPath;
    }
    
    public TinyPath parsePath(final char[] array, final int n, final boolean f) {
        this.char = new TinyPath(20);
        this.void = 0;
        this.f = f;
        this.a(array, n);
        this.d = 0;
        this.null = 0;
        this.case = 0;
        this.try = 0;
        this.goto();
    Label_0387:
        while (true) {
            switch (this.goto) {
                case 9:
                case 10:
                case 13:
                case 32: {
                    this.goto();
                    continue;
                }
                case 90:
                case 122: {
                    this.goto();
                    this.else();
                    continue;
                }
                case 109: {
                    this.a(109);
                    if (this.void != 0) {
                        break Label_0387;
                    }
                }
                case 108: {
                    this.do(108);
                    if (this.void != 0) {
                        break Label_0387;
                    }
                    continue;
                }
                case 77: {
                    this.a(77);
                    if (this.void != 0) {
                        break Label_0387;
                    }
                }
                case 76: {
                    this.do(76);
                    if (this.void != 0) {
                        break Label_0387;
                    }
                    continue;
                }
                case 67:
                case 72:
                case 81:
                case 83:
                case 84:
                case 86:
                case 99:
                case 104:
                case 113:
                case 115:
                case 116:
                case 118: {
                    this.if(this.goto);
                    if (this.void != 0) {
                        break Label_0387;
                    }
                    continue;
                }
                default: {
                    if (this.goto == -1) {
                        break Label_0387;
                    }
                    this.void |= 0x10;
                    this.e();
                    continue;
                }
            }
        }
        this.null();
        if (this.goto != -1) {}
        if (this.void != 0) {
            return TinyPath.rectToPath(0, 0, 256, 256);
        }
        this.for();
        this.char.compact();
        return this.char;
    }
    
    public TinyVector parseTransfroms(final char[] array, final int n) {
        this.void = 0;
        this.do = new TinyVector(4);
        this.a(array, n);
    Label_0244:
        do {
            this.goto();
            switch (this.goto) {
                case 9:
                case 10:
                case 13:
                case 32:
                case 44: {
                    break;
                }
                case 109: {
                    this.int();
                    break;
                }
                case 114: {
                    this.char();
                    break;
                }
                case 116: {
                    this.do();
                    break;
                }
                case 115: {
                    this.goto();
                    switch (this.goto) {
                        case 99: {
                            this.case();
                            break;
                        }
                        case 107: {
                            this.d();
                            break;
                        }
                        default: {
                            this.void |= 0x10;
                            this.b();
                            break;
                        }
                    }
                    break;
                }
                default: {
                    if (this.goto == -1) {
                        break Label_0244;
                    }
                    this.void |= 0x10;
                    this.b();
                    break;
                }
            }
        } while (this.void == 0);
        this.null();
        if (this.goto != -1) {}
        if (this.void != 0) {
            this.do.addElement(new TinyTransform());
        }
        return this.do;
    }
    
    private final void int() {
        this.goto();
        this.void = 0;
        if (this.goto != 97) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 116) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 114) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 105) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 120) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        if (this.goto != 40) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        final TinyMatrix matrix = new TinyMatrix();
        matrix.a = this.if();
        this.long();
        matrix.b = this.if();
        this.long();
        matrix.c = this.if();
        this.long();
        matrix.d = this.if();
        this.long();
        matrix.tx = this.try();
        this.long();
        matrix.ty = this.try();
        if (this.void != 0) {
            return;
        }
        this.null();
        if (this.goto != 41) {
            this.void |= 0x10;
            this.b();
            return;
        }
        (this.new = new TinyTransform()).setMatrix(matrix);
        this.do.addElement(this.new);
    }
    
    private final void char() {
        this.goto();
        this.void = 0;
        if (this.goto != 111) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 116) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 97) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 116) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 101) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        if (this.goto != 40) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        final int try1 = this.try();
        if (this.void != 0) {
            return;
        }
        this.null();
        switch (this.goto) {
            case 41: {
                (this.new = new TinyTransform()).setRotate(try1, 0, 0);
                this.do.addElement(this.new);
                return;
            }
            case 44: {
                this.goto();
                this.null();
                break;
            }
        }
        final int try2 = this.try();
        this.long();
        final int try3 = this.try();
        if (this.void != 0) {
            return;
        }
        this.null();
        if (this.goto != 41) {
            this.void |= 0x10;
            this.b();
            return;
        }
        (this.new = new TinyTransform()).setRotate(try1, try2, try3);
        this.do.addElement(this.new);
    }
    
    private final void do() {
        final int n = 0;
        this.goto();
        this.void = 0;
        if (this.goto != 114) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 97) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 110) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 115) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 108) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 97) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 116) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 101) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        if (this.goto != 40) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        final int try1 = this.try();
        if (this.void != 0) {
            return;
        }
        this.null();
        switch (this.goto) {
            case 41: {
                (this.new = new TinyTransform()).setTranslate(try1, n);
                this.do.addElement(this.new);
                return;
            }
            case 44: {
                this.goto();
                this.null();
                break;
            }
        }
        final int try2 = this.try();
        if (this.void != 0) {
            return;
        }
        this.null();
        if (this.goto != 41) {
            this.void |= 0x10;
            this.b();
            return;
        }
        (this.new = new TinyTransform()).setTranslate(try1, try2);
        this.do.addElement(this.new);
    }
    
    private final void case() {
        this.goto();
        this.void = 0;
        if (this.goto != 97) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 108) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 101) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        if (this.goto != 40) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        this.null();
        final int if1 = this.if();
        if (this.void != 0) {
            return;
        }
        this.null();
        switch (this.goto) {
            case 41: {
                (this.new = new TinyTransform()).setScale(if1, if1);
                this.do.addElement(this.new);
                return;
            }
            case 44: {
                this.goto();
                this.null();
                break;
            }
        }
        final int if2 = this.if();
        if (this.void != 0) {
            return;
        }
        this.null();
        if (this.goto != 41) {
            this.void |= 0x10;
            this.b();
            return;
        }
        (this.new = new TinyTransform()).setScale(if1, if2);
        this.do.addElement(this.new);
    }
    
    private final void d() {
        this.goto();
        this.void = 0;
        if (this.goto != 101) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        if (this.goto != 119) {
            this.void |= 0x10;
            this.b();
            return;
        }
        this.goto();
        boolean b = false;
        switch (this.goto) {
            case 88: {
                b = true;
            }
            case 89: {
                this.goto();
                this.null();
                if (this.goto != 40) {
                    this.void |= 0x10;
                    this.b();
                    return;
                }
                this.goto();
                this.null();
                final int try1 = this.try();
                if (this.void != 0) {
                    return;
                }
                this.null();
                if (this.goto != 41) {
                    this.void |= 0x10;
                    this.b();
                    return;
                }
                if (b) {
                    (this.new = new TinyTransform()).setSkewX(try1);
                    this.do.addElement(this.new);
                }
                else {
                    (this.new = new TinyTransform()).setSkewY(try1);
                    this.do.addElement(this.new);
                }
            }
            default: {
                this.void |= 0x10;
                this.b();
            }
        }
    }
    
    private final void b() {
    Label_0045:
        while (true) {
            this.goto();
            switch (this.goto) {
                case 41: {
                    break Label_0045;
                }
                default: {
                    if (this.goto == -1) {
                        break Label_0045;
                    }
                    continue;
                }
            }
        }
    }
    
    private final int if() {
        this.c();
        final int doubleFix = TinyNumber.parseDoubleFix(this.c, 0, this.for);
        this.void |= TinyNumber.error;
        return doubleFix;
    }
    
    private final int try() {
        this.c();
        final int fix = TinyNumber.parseFix(this.c, 0, this.for);
        this.void |= TinyNumber.error;
        return fix;
    }
    
    private final void c() {
        this.for = 0;
        this.a();
        while (true) {
            this.goto();
            switch (this.goto) {
                case 9:
                case 10:
                case 13:
                case 32:
                case 41:
                case 44: {}
                default: {
                    if (this.goto == -1) {
                        this.void |= 0x20;
                        this.b();
                        return;
                    }
                    this.a();
                    continue;
                }
            }
        }
    }
}
