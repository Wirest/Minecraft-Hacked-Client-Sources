// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyMatrix
{
    public int a;
    public int b;
    public int c;
    public int d;
    public int tx;
    public int ty;
    
    public TinyMatrix() {
        this.a = 65536;
        this.d = 65536;
    }
    
    public TinyMatrix(final TinyMatrix tinyMatrix) {
        this.a = 65536;
        this.d = 65536;
        this.a = tinyMatrix.a;
        this.b = tinyMatrix.b;
        this.c = tinyMatrix.c;
        this.d = tinyMatrix.d;
        this.tx = tinyMatrix.tx;
        this.ty = tinyMatrix.ty;
    }
    
    public final void reset() {
        this.a = 65536;
        this.d = 65536;
        final int n = 0;
        this.ty = n;
        this.tx = n;
        this.c = n;
        this.b = n;
    }
    
    public final void concatenate(final TinyMatrix tinyMatrix) {
        int b = 0;
        int c = 0;
        int mul = TinyUtil.mul(this.a, tinyMatrix.a);
        int mul2 = TinyUtil.mul(this.d, tinyMatrix.d);
        int tx = TinyUtil.mul(this.tx, tinyMatrix.a) + tinyMatrix.tx;
        int ty = TinyUtil.mul(this.ty, tinyMatrix.d) + tinyMatrix.ty;
        if (this.b != 0 || this.c != 0 || tinyMatrix.b != 0 || tinyMatrix.c != 0) {
            mul += TinyUtil.mul(this.b, tinyMatrix.c);
            mul2 += TinyUtil.mul(this.c, tinyMatrix.b);
            b += TinyUtil.mul(this.a, tinyMatrix.b) + TinyUtil.mul(this.b, tinyMatrix.d);
            c += TinyUtil.mul(this.c, tinyMatrix.a) + TinyUtil.mul(this.d, tinyMatrix.c);
            tx += TinyUtil.mul(this.ty, tinyMatrix.c);
            ty += TinyUtil.mul(this.tx, tinyMatrix.b);
        }
        this.a = mul;
        this.b = b;
        this.c = c;
        this.d = mul2;
        this.tx = tx;
        this.ty = ty;
    }
    
    public final void preConcatenate(final TinyMatrix tinyMatrix) {
        int b = 0;
        int c = 0;
        int mul = TinyUtil.mul(tinyMatrix.a, this.a);
        int mul2 = TinyUtil.mul(tinyMatrix.d, this.d);
        int tx = TinyUtil.mul(tinyMatrix.tx, this.a) + this.tx;
        int ty = TinyUtil.mul(tinyMatrix.ty, this.d) + this.ty;
        if (tinyMatrix.b != 0 || tinyMatrix.c != 0 || this.b != 0 || this.c != 0) {
            mul += TinyUtil.mul(tinyMatrix.b, this.c);
            mul2 += TinyUtil.mul(tinyMatrix.c, this.b);
            b += TinyUtil.mul(tinyMatrix.a, this.b) + TinyUtil.mul(tinyMatrix.b, this.d);
            c += TinyUtil.mul(tinyMatrix.c, this.a) + TinyUtil.mul(tinyMatrix.d, this.c);
            tx += TinyUtil.mul(tinyMatrix.ty, this.c);
            ty += TinyUtil.mul(tinyMatrix.tx, this.b);
        }
        this.a = mul;
        this.b = b;
        this.c = c;
        this.d = mul2;
        this.tx = tx;
        this.ty = ty;
    }
    
    public final TinyMatrix inverse() {
        final TinyMatrix tinyMatrix = new TinyMatrix();
        final TinyPoint tinyPoint = new TinyPoint();
        if (this.b == 0 && this.c == 0) {
            tinyMatrix.a = TinyUtil.div(65536, this.a);
            tinyMatrix.d = TinyUtil.div(65536, this.d);
            tinyMatrix.tx = -TinyUtil.mul(tinyMatrix.a, this.tx);
            tinyMatrix.ty = -TinyUtil.mul(tinyMatrix.d, this.ty);
        }
        else {
            final int n = TinyUtil.mul(TinyUtil.div(this.a, 65536), TinyUtil.div(this.d, 65536)) - TinyUtil.mul(TinyUtil.div(this.b, 65536), TinyUtil.div(this.c, 65536));
            if (n != 0) {
                tinyMatrix.a = TinyUtil.div(this.d, n);
                tinyMatrix.b = -TinyUtil.div(this.b, n);
                tinyMatrix.c = -TinyUtil.div(this.c, n);
                tinyMatrix.d = TinyUtil.div(this.a, n);
                tinyPoint.x = this.tx;
                tinyPoint.y = this.ty;
                tinyMatrix.a(tinyPoint);
                tinyMatrix.tx = -tinyPoint.x;
                tinyMatrix.ty = -tinyPoint.y;
            }
        }
        return tinyMatrix;
    }
    
    public final void translate(final int tx, final int ty) {
        this.a = 65536;
        this.d = 65536;
        final int n = 0;
        this.c = n;
        this.b = n;
        this.tx = tx;
        this.ty = ty;
    }
    
    public final void scale(final int a, final int d) {
        this.a = a;
        this.d = d;
        final int n = 0;
        this.c = n;
        this.b = n;
        final int n2 = 0;
        this.ty = n2;
        this.tx = n2;
    }
    
    public final void rotate(final int n, final int n2, final int n3) {
        this.a = TinyUtil.cos(n);
        this.b = TinyUtil.sin(n);
        this.c = -this.b;
        this.d = this.a;
        this.tx = n2 - TinyUtil.mul(this.a, n2) + TinyUtil.mul(this.b, n3);
        this.ty = n3 - TinyUtil.mul(this.b, n2) - TinyUtil.mul(this.a, n3);
    }
    
    public void skew(final int n, final int n2) {
        this.a = 65536;
        this.d = 65536;
        final int n3 = 0;
        this.ty = n3;
        this.tx = n3;
        this.b = TinyUtil.tan(n2);
        this.c = TinyUtil.tan(n);
    }
    
    private final void a(final TinyPoint tinyPoint) {
        int mul = TinyUtil.mul(this.a, tinyPoint.x);
        if (this.c != 0) {
            mul += TinyUtil.mul(this.c, tinyPoint.y);
        }
        int mul2 = TinyUtil.mul(this.d, tinyPoint.y);
        if (this.b != 0) {
            mul2 += TinyUtil.mul(this.b, tinyPoint.x);
        }
        tinyPoint.x = mul;
        tinyPoint.y = mul2;
    }
    
    public final void transform(final TinyPoint tinyPoint) {
        int x = TinyUtil.mul(this.a, tinyPoint.x) + this.tx;
        if (this.c != 0) {
            x += TinyUtil.mul(this.c, tinyPoint.y);
        }
        int y = TinyUtil.mul(this.d, tinyPoint.y) + this.ty;
        if (this.b != 0) {
            y += TinyUtil.mul(this.b, tinyPoint.x);
        }
        tinyPoint.x = x;
        tinyPoint.y = y;
    }
    
    public final void transformToDev(final TinyPoint tinyPoint, final TinyPoint tinyPoint2) {
        int n = TinyUtil.mul(this.a, tinyPoint.x) + this.tx;
        if (this.c != 0) {
            n += TinyUtil.mul(this.c, tinyPoint.y);
        }
        int n2 = TinyUtil.mul(this.d, tinyPoint.y) + this.ty;
        if (this.b != 0) {
            n2 += TinyUtil.mul(this.b, tinyPoint.x);
        }
        if (n < 0) {
            n -= 128;
        }
        else {
            n += 128;
        }
        if (n2 < 0) {
            n2 -= 128;
        }
        else {
            n2 += 128;
        }
        tinyPoint2.x = n >> 8;
        tinyPoint2.y = n2 >> 8;
    }
    
    public final TinyRect transformToDev(final TinyRect tinyRect) {
        final TinyRect tinyRect2 = new TinyRect();
        final TinyPoint tinyPoint = new TinyPoint();
        final TinyPoint tinyPoint2 = new TinyPoint();
        if (!tinyRect.isEmpty()) {
            tinyPoint.x = tinyRect.xmin;
            tinyPoint.y = tinyRect.ymin;
            tinyPoint2.x = 0;
            tinyPoint2.y = 0;
            this.transformToDev(tinyPoint, tinyPoint2);
            tinyRect2.add(tinyPoint2);
            tinyPoint.x = tinyRect.xmax;
            this.transformToDev(tinyPoint, tinyPoint2);
            tinyRect2.add(tinyPoint2);
            tinyPoint.y = tinyRect.ymax;
            this.transformToDev(tinyPoint, tinyPoint2);
            tinyRect2.add(tinyPoint2);
            tinyPoint.x = tinyRect.xmin;
            this.transformToDev(tinyPoint, tinyPoint2);
            tinyRect2.add(tinyPoint2);
        }
        return tinyRect2;
    }
    
    int a(final int n) {
        final TinyPoint tinyPoint = new TinyPoint();
        final TinyPoint tinyPoint2 = new TinyPoint();
        final TinyPoint tinyPoint3 = new TinyPoint();
        final TinyPoint tinyPoint4 = tinyPoint;
        final TinyPoint tinyPoint5 = tinyPoint;
        final int n2 = 0;
        tinyPoint5.y = n2;
        tinyPoint4.x = n2;
        this.a(tinyPoint);
        tinyPoint2.x = n;
        tinyPoint2.y = 0;
        this.a(tinyPoint2);
        tinyPoint3.x = 0;
        tinyPoint3.y = n;
        this.a(tinyPoint3);
        return TinyUtil.max(tinyPoint.distance(tinyPoint2), tinyPoint.distance(tinyPoint3));
    }
}
