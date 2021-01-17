// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Normal
{
    static boolean SLANTED_SWEEP;
    static double S_UNIT_X;
    static double S_UNIT_Y;
    private static final boolean TRUE_PROJECT = false;
    
    private Normal() {
    }
    
    private static double Dot(final double[] u, final double[] v) {
        return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
    }
    
    static void Normalize(final double[] v) {
        double len = v[0] * v[0] + v[1] * v[1] + v[2] * v[2];
        assert len > 0.0;
        len = Math.sqrt(len);
        final int n = 0;
        v[n] /= len;
        final int n2 = 1;
        v[n2] /= len;
        final int n3 = 2;
        v[n3] /= len;
    }
    
    static int LongAxis(final double[] v) {
        int i = 0;
        if (Math.abs(v[1]) > Math.abs(v[0])) {
            i = 1;
        }
        if (Math.abs(v[2]) > Math.abs(v[i])) {
            i = 2;
        }
        return i;
    }
    
    static void ComputeNormal(final GLUtessellatorImpl tess, final double[] norm) {
        final GLUvertex vHead = tess.mesh.vHead;
        final double[] maxVal = new double[3];
        final double[] minVal = new double[3];
        final GLUvertex[] minVert = new GLUvertex[3];
        final GLUvertex[] maxVert = new GLUvertex[3];
        final double[] d1 = new double[3];
        final double[] d2 = new double[3];
        final double[] tNorm = new double[3];
        final double[] array = maxVal;
        final int n = 0;
        final double[] array2 = maxVal;
        final int n2 = 1;
        final double[] array3 = maxVal;
        final int n3 = 2;
        final double n4 = -2.0E150;
        array3[n3] = n4;
        array[n] = (array2[n2] = n4);
        final double[] array4 = minVal;
        final int n5 = 0;
        final double[] array5 = minVal;
        final int n6 = 1;
        final double[] array6 = minVal;
        final int n7 = 2;
        final double n8 = 2.0E150;
        array6[n7] = n8;
        array4[n5] = (array5[n6] = n8);
        for (GLUvertex v = vHead.next; v != vHead; v = v.next) {
            for (int i = 0; i < 3; ++i) {
                final double c = v.coords[i];
                if (c < minVal[i]) {
                    minVal[i] = c;
                    minVert[i] = v;
                }
                if (c > maxVal[i]) {
                    maxVal[i] = c;
                    maxVert[i] = v;
                }
            }
        }
        int i = 0;
        if (maxVal[1] - minVal[1] > maxVal[0] - minVal[0]) {
            i = 1;
        }
        if (maxVal[2] - minVal[2] > maxVal[i] - minVal[i]) {
            i = 2;
        }
        if (minVal[i] >= maxVal[i]) {
            norm[1] = (norm[0] = 0.0);
            norm[2] = 1.0;
            return;
        }
        double maxLen2 = 0.0;
        final GLUvertex v2 = minVert[i];
        final GLUvertex v3 = maxVert[i];
        d1[0] = v2.coords[0] - v3.coords[0];
        d1[1] = v2.coords[1] - v3.coords[1];
        d1[2] = v2.coords[2] - v3.coords[2];
        for (GLUvertex v = vHead.next; v != vHead; v = v.next) {
            d2[0] = v.coords[0] - v3.coords[0];
            d2[1] = v.coords[1] - v3.coords[1];
            d2[2] = v.coords[2] - v3.coords[2];
            tNorm[0] = d1[1] * d2[2] - d1[2] * d2[1];
            tNorm[1] = d1[2] * d2[0] - d1[0] * d2[2];
            tNorm[2] = d1[0] * d2[1] - d1[1] * d2[0];
            final double tLen2 = tNorm[0] * tNorm[0] + tNorm[1] * tNorm[1] + tNorm[2] * tNorm[2];
            if (tLen2 > maxLen2) {
                maxLen2 = tLen2;
                norm[0] = tNorm[0];
                norm[1] = tNorm[1];
                norm[2] = tNorm[2];
            }
        }
        if (maxLen2 <= 0.0) {
            final int n9 = 0;
            final int n10 = 1;
            final int n11 = 2;
            final double n12 = 0.0;
            norm[n11] = n12;
            norm[n9] = (norm[n10] = n12);
            norm[LongAxis(d1)] = 1.0;
        }
    }
    
    static void CheckOrientation(final GLUtessellatorImpl tess) {
        final GLUface fHead = tess.mesh.fHead;
        final GLUvertex vHead = tess.mesh.vHead;
        double area = 0.0;
        for (GLUface f = fHead.next; f != fHead; f = f.next) {
            GLUhalfEdge e = f.anEdge;
            if (e.winding > 0) {
                do {
                    area += (e.Org.s - e.Sym.Org.s) * (e.Org.t + e.Sym.Org.t);
                    e = e.Lnext;
                } while (e != f.anEdge);
            }
        }
        if (area < 0.0) {
            for (GLUvertex v = vHead.next; v != vHead; v = v.next) {
                v.t = -v.t;
            }
            tess.tUnit[0] = -tess.tUnit[0];
            tess.tUnit[1] = -tess.tUnit[1];
            tess.tUnit[2] = -tess.tUnit[2];
        }
    }
    
    public static void __gl_projectPolygon(final GLUtessellatorImpl tess) {
        final GLUvertex vHead = tess.mesh.vHead;
        final double[] norm = new double[3];
        boolean computedNormal = false;
        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0.0 && norm[1] == 0.0 && norm[2] == 0.0) {
            ComputeNormal(tess, norm);
            computedNormal = true;
        }
        final double[] sUnit = tess.sUnit;
        final double[] tUnit = tess.tUnit;
        final int i = LongAxis(norm);
        sUnit[i] = 0.0;
        sUnit[(i + 1) % 3] = Normal.S_UNIT_X;
        sUnit[(i + 2) % 3] = Normal.S_UNIT_Y;
        tUnit[i] = 0.0;
        tUnit[(i + 1) % 3] = ((norm[i] > 0.0) ? (-Normal.S_UNIT_Y) : Normal.S_UNIT_Y);
        tUnit[(i + 2) % 3] = ((norm[i] > 0.0) ? Normal.S_UNIT_X : (-Normal.S_UNIT_X));
        for (GLUvertex v = vHead.next; v != vHead; v = v.next) {
            v.s = Dot(v.coords, sUnit);
            v.t = Dot(v.coords, tUnit);
        }
        if (computedNormal) {
            CheckOrientation(tess);
        }
    }
    
    static {
        if (Normal.SLANTED_SWEEP) {
            Normal.S_UNIT_X = 0.5094153956495538;
            Normal.S_UNIT_Y = 0.8605207462201063;
        }
        else {
            Normal.S_UNIT_X = 1.0;
            Normal.S_UNIT_Y = 0.0;
        }
    }
}
