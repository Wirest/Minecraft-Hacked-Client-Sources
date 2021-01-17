// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Geom
{
    private Geom() {
    }
    
    static double EdgeEval(final GLUvertex u, final GLUvertex v, final GLUvertex w) {
        assert VertLeq(u, v) && VertLeq(v, w);
        final double gapL = v.s - u.s;
        final double gapR = w.s - v.s;
        if (gapL + gapR <= 0.0) {
            return 0.0;
        }
        if (gapL < gapR) {
            return v.t - u.t + (u.t - w.t) * (gapL / (gapL + gapR));
        }
        return v.t - w.t + (w.t - u.t) * (gapR / (gapL + gapR));
    }
    
    static double EdgeSign(final GLUvertex u, final GLUvertex v, final GLUvertex w) {
        assert VertLeq(u, v) && VertLeq(v, w);
        final double gapL = v.s - u.s;
        final double gapR = w.s - v.s;
        if (gapL + gapR > 0.0) {
            return (v.t - w.t) * gapL + (v.t - u.t) * gapR;
        }
        return 0.0;
    }
    
    static double TransEval(final GLUvertex u, final GLUvertex v, final GLUvertex w) {
        assert TransLeq(u, v) && TransLeq(v, w);
        final double gapL = v.t - u.t;
        final double gapR = w.t - v.t;
        if (gapL + gapR <= 0.0) {
            return 0.0;
        }
        if (gapL < gapR) {
            return v.s - u.s + (u.s - w.s) * (gapL / (gapL + gapR));
        }
        return v.s - w.s + (w.s - u.s) * (gapR / (gapL + gapR));
    }
    
    static double TransSign(final GLUvertex u, final GLUvertex v, final GLUvertex w) {
        assert TransLeq(u, v) && TransLeq(v, w);
        final double gapL = v.t - u.t;
        final double gapR = w.t - v.t;
        if (gapL + gapR > 0.0) {
            return (v.s - w.s) * gapL + (v.s - u.s) * gapR;
        }
        return 0.0;
    }
    
    static boolean VertCCW(final GLUvertex u, final GLUvertex v, final GLUvertex w) {
        return u.s * (v.t - w.t) + v.s * (w.t - u.t) + w.s * (u.t - v.t) >= 0.0;
    }
    
    static double Interpolate(double a, final double x, double b, final double y) {
        a = ((a < 0.0) ? 0.0 : a);
        b = ((b < 0.0) ? 0.0 : b);
        if (a > b) {
            return y + (x - y) * (b / (a + b));
        }
        if (b == 0.0) {
            return (x + y) / 2.0;
        }
        return x + (y - x) * (a / (a + b));
    }
    
    static void EdgeIntersect(GLUvertex o1, GLUvertex d1, GLUvertex o2, GLUvertex d2, final GLUvertex v) {
        if (!VertLeq(o1, d1)) {
            final GLUvertex temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!VertLeq(o2, d2)) {
            final GLUvertex temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!VertLeq(o1, o2)) {
            GLUvertex temp = o1;
            o1 = o2;
            o2 = temp;
            temp = d1;
            d1 = d2;
            d2 = temp;
        }
        if (!VertLeq(o2, d1)) {
            v.s = (o2.s + d1.s) / 2.0;
        }
        else if (VertLeq(d1, d2)) {
            double z1 = EdgeEval(o1, o2, d1);
            double z2 = EdgeEval(o2, d1, d2);
            if (z1 + z2 < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Interpolate(z1, o2.s, z2, d1.s);
        }
        else {
            double z1 = EdgeSign(o1, o2, d1);
            double z2 = -EdgeSign(o1, d2, d1);
            if (z1 + z2 < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Interpolate(z1, o2.s, z2, d2.s);
        }
        if (!TransLeq(o1, d1)) {
            final GLUvertex temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!TransLeq(o2, d2)) {
            final GLUvertex temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!TransLeq(o1, o2)) {
            GLUvertex temp = o2;
            o2 = o1;
            o1 = temp;
            temp = d2;
            d2 = d1;
            d1 = temp;
        }
        if (!TransLeq(o2, d1)) {
            v.t = (o2.t + d1.t) / 2.0;
        }
        else if (TransLeq(d1, d2)) {
            double z1 = TransEval(o1, o2, d1);
            double z2 = TransEval(o2, d1, d2);
            if (z1 + z2 < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Interpolate(z1, o2.t, z2, d1.t);
        }
        else {
            double z1 = TransSign(o1, o2, d1);
            double z2 = -TransSign(o1, d2, d1);
            if (z1 + z2 < 0.0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Interpolate(z1, o2.t, z2, d2.t);
        }
    }
    
    static boolean VertEq(final GLUvertex u, final GLUvertex v) {
        return u.s == v.s && u.t == v.t;
    }
    
    static boolean VertLeq(final GLUvertex u, final GLUvertex v) {
        return u.s < v.s || (u.s == v.s && u.t <= v.t);
    }
    
    static boolean TransLeq(final GLUvertex u, final GLUvertex v) {
        return u.t < v.t || (u.t == v.t && u.s <= v.s);
    }
    
    static boolean EdgeGoesLeft(final GLUhalfEdge e) {
        return VertLeq(e.Sym.Org, e.Org);
    }
    
    static boolean EdgeGoesRight(final GLUhalfEdge e) {
        return VertLeq(e.Org, e.Sym.Org);
    }
    
    static double VertL1dist(final GLUvertex u, final GLUvertex v) {
        return Math.abs(u.s - v.s) + Math.abs(u.t - v.t);
    }
}
