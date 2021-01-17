// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyColor
{
    public static final TinyColor NONE;
    public static final TinyColor CURRENT;
    public static final TinyColor INHERIT;
    public static final int FILL_SOLID = 0;
    public static final int FILL_LINEAR_GRADIENT = 1;
    public static final int FILL_RADIAL_GRADIENT = 2;
    public static final int FILL_BITMAP = 3;
    public static final int FILL_PATTERN = 4;
    public static final int FILL_URI = 5;
    public static final int GRADIENT_PAD = 0;
    public static final int GRADIENT_REFLECT = 1;
    public static final int GRADIENT_REPEAT = 2;
    public static final int GRADIENT_USER = 0;
    public static final int GRADIENT_OBJECT = 1;
    public int visible;
    public int fillType;
    public int value;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public int r;
    public TinyVector gStops;
    public int[] gColorRamp;
    public int spread;
    public int units;
    public TinyString uri;
    private int case;
    private int new;
    private int byte;
    private int int;
    private int do;
    private int if;
    private int a;
    private int for;
    public TinyMatrix matrix;
    public TinyBitmap bitmap;
    TinyMatrix try;
    
    public TinyColor(final int value) {
        this.fillType = 0;
        this.value = value;
        this.matrix = new TinyMatrix();
    }
    
    public TinyColor(final int fillType, final TinyMatrix matrix) {
        this.fillType = fillType;
        this.matrix = matrix;
        switch (fillType) {
            case 1:
            case 2: {
                this.gStops = new TinyVector(4);
                this.gColorRamp = new int[257];
                break;
            }
        }
    }
    
    public TinyColor(final TinyString uri) {
        this.fillType = 5;
        this.uri = uri;
        this.matrix = new TinyMatrix();
    }
    
    public TinyColor(final TinyColor tinyColor) {
        this.fillType = tinyColor.fillType;
        if (tinyColor.matrix != null) {
            this.matrix = new TinyMatrix(tinyColor.matrix);
        }
        switch (this.fillType) {
            case 0: {
                this.value = tinyColor.value;
                break;
            }
            case 3:
            case 4: {
                this.bitmap = new TinyBitmap(tinyColor.bitmap);
                break;
            }
            case 1:
            case 2: {
                this.x1 = tinyColor.x1;
                this.y1 = tinyColor.y1;
                this.x2 = tinyColor.x2;
                this.y2 = tinyColor.y2;
                this.r = tinyColor.r;
                this.spread = tinyColor.spread;
                this.units = tinyColor.units;
                final int count = tinyColor.gStops.count;
                this.gStops = new TinyVector(count);
                for (int i = 0; i < count; ++i) {
                    this.gStops.data[i] = new d((d)tinyColor.gStops.data[i]);
                }
                this.gColorRamp = new int[257];
                this.createColorRamp();
                break;
            }
            case 5: {
                this.uri = new TinyString(tinyColor.uri.data);
                break;
            }
        }
    }
    
    public TinyColor copyColor() {
        if (this == TinyColor.NONE || this == TinyColor.CURRENT || this == TinyColor.INHERIT) {
            return this;
        }
        return new TinyColor(this);
    }
    
    public void addStop(final int a, final int if1) {
        if (this.fillType != 1 && this.fillType != 2) {
            return;
        }
        final d d = new d();
        d.a = a;
        d.if = if1;
        this.gStops.addElement(d);
    }
    
    public void createColorRamp() {
        if (this.fillType != 1 && this.fillType != 2) {
            return;
        }
        if (this.gStops.count < 2) {
            return;
        }
        int n = 0;
        final d d = (d)this.gStops.data[0];
        int n2 = d.if;
        int a2;
        int a = a2 = d.a;
        int n3 = 1;
        int n4 = 0;
        do {
            if (n4 > n2) {
                n = n2;
                a2 = a;
                if (n3 < this.gStops.count) {
                    final d d2 = (d)this.gStops.data[n3];
                    n2 = d2.if;
                    a = d2.a;
                    ++n3;
                }
                else {
                    n2 = 256;
                }
            }
            final int n5 = n2 - n4;
            final int n6 = n4 - n;
            final int n7 = n5 + n6;
            if (n7 > 0) {
                this.gColorRamp[n4] = (((a2 >> 24 & 0xFF) * n5 + (a >> 24 & 0xFF) * n6) / n7 << 24 | ((a2 >> 16 & 0xFF) * n5 + (a >> 16 & 0xFF) * n6) / n7 << 16 | ((a2 >> 8 & 0xFF) * n5 + (a >> 8 & 0xFF) * n6) / n7 << 8 | ((a2 & 0xFF) * n5 + (a & 0xFF) * n6) / n7);
            }
            else {
                this.gColorRamp[n4] = a2;
            }
        } while (++n4 <= 256);
    }
    
    private int if(int n, final int n2) {
        if (TinyUtil.abs(n) > 512 && this.spread != 0) {
            n &= 0xFF;
        }
        if (n2 > 256) {
            if (this.spread == 2) {
                n += 256;
                if (n < 0) {
                    n += 256;
                }
            }
            else if (this.spread == 1) {
                n = -n;
            }
            else {
                n = 0;
            }
        }
        if (n > 256) {
            if (this.spread == 2) {
                n -= 256;
            }
            else if (this.spread == 1) {
                n = 512 - n;
            }
            else {
                n = 256;
            }
        }
        if (n < 0) {
            n = 0;
        }
        if (n > 256) {
            n = 256;
        }
        return n;
    }
    
    int a(final int n, final int n2) {
        int n3 = 0;
        switch (this.fillType) {
            case 3: {
                n3 = this.bitmap.a(n >> 8, n2 >> 8, true);
                break;
            }
            case 4: {
                n3 = this.bitmap.a(n >> 8, n2 >> 8, false);
                break;
            }
            case 1: {
                n3 = this.gColorRamp[this.if(TinyUtil.div(TinyUtil.div(TinyUtil.mul(n - this.case, this.if) + TinyUtil.mul(n2 - this.new, this.a), this.for), this.for) >> 8, TinyUtil.div(TinyUtil.div(TinyUtil.mul(this.byte - n, this.if) + TinyUtil.mul(this.int - n2, this.a), this.for), this.for) >> 8)];
                break;
            }
            case 2: {
                n3 = this.gColorRamp[this.if(TinyUtil.div(TinyUtil.a(n - this.case, n2 - this.new), this.do) >> 8, 0)];
                break;
            }
        }
        return n3;
    }
    
    public void setCoords(final TinyMatrix tinyMatrix, final TinyRect tinyRect) {
        this.try = new TinyMatrix(tinyMatrix);
        if (this.units == 0) {
            this.case = this.x1;
            this.new = this.y1;
            this.byte = this.x2;
            this.int = this.y2;
            this.do = this.r;
        }
        else {
            final TinyMatrix tinyMatrix2 = new TinyMatrix();
            tinyMatrix2.a = tinyRect.xmax - tinyRect.xmin;
            tinyMatrix2.d = tinyRect.ymax - tinyRect.ymin;
            tinyMatrix2.tx = tinyRect.xmin;
            tinyMatrix2.ty = tinyRect.ymin;
            this.case = this.x1 * 256;
            this.new = this.y1 * 256;
            this.byte = this.x2 * 256;
            this.int = this.y2 * 256;
            this.do = this.r * 256;
            this.try.preConcatenate(tinyMatrix2);
        }
        this.try.concatenate(this.matrix);
        this.try = this.try.inverse();
    }
    
    void a() {
        this.visible = 0;
        switch (this.fillType) {
            case 3:
            case 4: {
                this.value = -256;
                break;
            }
            case 1:
            case 2: {
                this.if = this.byte - this.case;
                this.a = this.int - this.new;
                this.for = TinyUtil.a(this.if, this.a);
                break;
            }
        }
    }
    
    static {
        NONE = new TinyColor(0);
        CURRENT = new TinyColor(0);
        INHERIT = new TinyColor(0);
    }
}
