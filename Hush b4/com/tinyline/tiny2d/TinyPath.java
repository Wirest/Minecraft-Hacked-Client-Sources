// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyPath
{
    public static final int FILL_STYLE_WIND = 1;
    public static final int FILL_STYLE_EO = 2;
    public static final int JOIN_MITER = 1;
    public static final int JOIN_ROUND = 2;
    public static final int JOIN_BEVEL = 3;
    public static final int CAP_BUTT = 1;
    public static final int CAP_ROUND = 2;
    public static final int CAP_SQUARE = 3;
    public static final int TEXT_ANCHOR_START = 1;
    public static final int TEXT_ANCHOR_MIDDLE = 2;
    public static final int TEXT_ANCHOR_END = 3;
    public static final byte TYPE_MOVETO = 1;
    public static final byte TYPE_LINETO = 2;
    public static final byte TYPE_CURVETO = 3;
    public static final byte TYPE_CURVETO_CUBIC = 4;
    public static final byte TYPE_CLOSE = 5;
    int[] int;
    int[] do;
    byte[] a;
    int new;
    private static final int if = 29341;
    private static final int for = 16;
    
    public TinyPath(final int n) {
        this.int = new int[n];
        this.do = new int[n];
        this.a = new byte[n];
        this.new = 0;
    }
    
    public TinyPath(final TinyPath tinyPath) {
        this.new = tinyPath.new;
        this.int = new int[this.new];
        this.do = new int[this.new];
        this.a = new byte[this.new];
        System.arraycopy(tinyPath.int, 0, this.int, 0, this.new);
        System.arraycopy(tinyPath.do, 0, this.do, 0, this.new);
        System.arraycopy(tinyPath.a, 0, this.a, 0, this.new);
    }
    
    public void compact() {
        if (this.a.length - this.new > 16 && this.new != 0) {
            final int new1 = this.new;
            final int[] int1 = new int[new1];
            final int[] do1 = new int[new1];
            final byte[] a = new byte[new1];
            System.arraycopy(this.int, 0, int1, 0, this.new);
            System.arraycopy(this.do, 0, do1, 0, this.new);
            System.arraycopy(this.a, 0, a, 0, this.new);
            this.int = int1;
            this.do = do1;
            this.a = a;
        }
    }
    
    public void lineTo(final int n, final int n2) {
        this.addPoint(n, n2, (byte)2);
    }
    
    public void moveTo(final int n, final int n2) {
        this.addPoint(n, n2, (byte)1);
    }
    
    public int numPoints() {
        return this.new;
    }
    
    public void reset() {
        this.new = 0;
    }
    
    public void closePath() {
        if (this.new > 0 && this.a[this.new - 1] == 5) {
            return;
        }
        this.addPoint(0, 0, (byte)5);
    }
    
    public void curveTo(final int n, final int n2, final int n3, final int n4) {
        this.addPoint(n, n2, (byte)3);
        this.addPoint(n3, n4, (byte)3);
    }
    
    public void curveToCubic(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        this.addPoint(n, n2, (byte)4);
        this.addPoint(n3, n4, (byte)4);
        this.addPoint(n5, n6, (byte)4);
    }
    
    public TinyRect getBBox() {
        int n = Integer.MAX_VALUE;
        int n2 = Integer.MIN_VALUE;
        int n3 = Integer.MAX_VALUE;
        int n4 = Integer.MIN_VALUE;
        for (int i = 0; i < this.new; ++i) {
            switch (this.a[i]) {
                case 1:
                case 2:
                case 3:
                case 4: {
                    if (this.int[i] < n) {
                        n = this.int[i];
                    }
                    if (this.int[i] > n2) {
                        n2 = this.int[i];
                    }
                    if (this.do[i] < n3) {
                        n3 = this.do[i];
                    }
                    if (this.do[i] > n4) {
                        n4 = this.do[i];
                        break;
                    }
                    break;
                }
            }
        }
        return new TinyRect(n, n3, n2, n4);
    }
    
    public void addPoint(final int n, final int n2, final byte b) {
        if (this.new >= this.a.length - 1) {
            final int n3 = this.a.length + 16;
            final int[] int1 = new int[n3];
            final int[] do1 = new int[n3];
            final byte[] a = new byte[n3];
            System.arraycopy(this.int, 0, int1, 0, this.new);
            System.arraycopy(this.do, 0, do1, 0, this.new);
            System.arraycopy(this.a, 0, a, 0, this.new);
            this.int = int1;
            this.do = do1;
            this.a = a;
        }
        this.int[this.new] = n;
        this.do[this.new] = n2;
        this.a[this.new] = b;
        ++this.new;
    }
    
    public TinyPoint getCurrentPoint() {
        final TinyPoint tinyPoint = new TinyPoint();
        for (int i = this.new - 1; i >= 0; --i) {
            if (this.a[i] == 1) {
                tinyPoint.x = this.int[i];
                tinyPoint.y = this.do[i];
                break;
            }
        }
        return tinyPoint;
    }
    
    public int getX(final int n) {
        return this.int[n];
    }
    
    public int getY(final int n) {
        return this.do[n];
    }
    
    public byte getType(final int n) {
        return this.a[n];
    }
    
    public static TinyPath lineToPath(final int n, final int n2, final int n3, final int n4) {
        final TinyPath tinyPath = new TinyPath(4);
        tinyPath.moveTo(n, n2);
        tinyPath.lineTo(n3, n4);
        return tinyPath;
    }
    
    public static TinyPath rectToPath(final int n, final int n2, final int n3, final int n4) {
        final TinyPath tinyPath = new TinyPath(6);
        tinyPath.moveTo(n, n2);
        tinyPath.lineTo(n3, n2);
        tinyPath.lineTo(n3, n4);
        tinyPath.lineTo(n, n4);
        tinyPath.closePath();
        return tinyPath;
    }
    
    public static TinyPath ovalToPath(final int n, final int n2, final int n3, final int n4) {
        return roundRectToPath(n, n2, n3, n4, n3 / 2, n4 / 2);
    }
    
    public static TinyPath roundRectToPath(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final TinyPath tinyPath = new TinyPath(18);
        final int min = TinyUtil.min(n3 / 2, n5);
        final int min2 = TinyUtil.min(n4 / 2, n6);
        final int n7 = n + n3;
        final int n8 = n2 + n4;
        if (min == 0 || min2 == 0) {
            tinyPath.moveTo(n, n2);
            tinyPath.lineTo(n7, n2);
            tinyPath.lineTo(n7, n8);
            tinyPath.lineTo(n, n8);
            tinyPath.closePath();
        }
        else {
            final int mul = TinyUtil.mul(29341, min);
            final int mul2 = TinyUtil.mul(29341, min2);
            tinyPath.moveTo(n7 - min, n2);
            tinyPath.curveToCubic(n7 - mul, n2, n7, n2 + mul2, n7, n2 + min2);
            tinyPath.lineTo(n7, n8 - min2);
            tinyPath.curveToCubic(n7, n8 - mul2, n7 - mul, n8, n7 - min, n8);
            tinyPath.lineTo(n + min, n8);
            tinyPath.curveToCubic(n + mul, n8, n, n8 - mul2, n, n8 - min2);
            tinyPath.lineTo(n, n2 + min2);
            tinyPath.curveToCubic(n, n2 + mul2, n + mul, n2, n + min, n2);
            tinyPath.closePath();
        }
        return tinyPath;
    }
    
    public static TinyPath charsToPath(final TinyFont tinyFont, final char[] array, final int n, final int n2) {
        if (tinyFont == null || array == null) {
            return null;
        }
        final TinyPath tinyPath = new TinyPath(20);
        int n3 = 0;
        final TinyNumber tinyNumber = new TinyNumber(0);
        for (int i = n; i < n2; ++i) {
            tinyNumber.val = array[i];
            TinyGlyph missing_glyph = (TinyGlyph)tinyFont.glyphs.get(tinyNumber);
            if (missing_glyph == null) {
                missing_glyph = tinyFont.missing_glyph;
            }
            if (missing_glyph == null) {
                n3 += tinyFont.horizAdvX;
            }
            else {
                if (missing_glyph.path != null) {
                    for (int numPoints = missing_glyph.path.numPoints(), j = 0; j < numPoints; ++j) {
                        tinyPath.addPoint(missing_glyph.path.getX(j) + n3 << 8, missing_glyph.path.getY(j) << 8, missing_glyph.path.getType(j));
                    }
                }
                n3 += missing_glyph.horizAdvX;
            }
        }
        tinyPath.compact();
        return tinyPath;
    }
    
    public static TinyPath pointsToPath(final TinyVector tinyVector) {
        if (tinyVector == null || tinyVector.count == 0) {
            return null;
        }
        final TinyPath tinyPath = new TinyPath(20);
        int i = 0;
        final TinyPoint tinyPoint = (TinyPoint)tinyVector.data[i];
        if (tinyPoint == null) {
            return null;
        }
        tinyPath.moveTo(tinyPoint.x, tinyPoint.y);
        ++i;
        while (i < tinyVector.count) {
            final TinyPoint tinyPoint2 = (TinyPoint)tinyVector.data[i];
            if (tinyPoint2 != null) {
                tinyPath.lineTo(tinyPoint2.x, tinyPoint2.y);
            }
            ++i;
        }
        tinyPath.compact();
        return tinyPath;
    }
    
    public static TinyVector pathToPoints(final TinyPath tinyPath) {
        final TinyVector tinyVector = new TinyVector(10);
        final b b = new b();
        b.for();
        b.a(tinyPath, new TinyMatrix());
        final f m = b.m;
        int new1 = m.new;
        if (m.a(0)) {
            --new1;
        }
        int n = 2;
        if (new1 == 2) {
            n = 1;
        }
        for (int i = 0; i < new1; i += n) {
            tinyVector.addElement(new TinyPoint(m.a[i] << 8, m.int[i] << 8));
        }
        b.if();
        return tinyVector;
    }
}
