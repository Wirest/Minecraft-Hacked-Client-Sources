// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Lsp
{
    static final float M_PI = 3.1415927f;
    
    static void lsp_to_curve(final float[] curve, final int[] map, final int n, final int ln, final float[] lsp, final int m, final float amp, final float ampoffset) {
        final float wdel = 3.1415927f / ln;
        for (int i = 0; i < m; ++i) {
            lsp[i] = Lookup.coslook(lsp[i]);
        }
        final int m2 = m / 2 * 2;
        int i = 0;
        while (i < n) {
            final int k = map[i];
            float p = 0.70710677f;
            float q = 0.70710677f;
            final float w = Lookup.coslook(wdel * k);
            for (int j = 0; j < m2; j += 2) {
                q *= lsp[j] - w;
                p *= lsp[j + 1] - w;
            }
            if ((m & 0x1) != 0x0) {
                q *= lsp[m - 1] - w;
                q *= q;
                p *= p * (1.0f - w * w);
            }
            else {
                q *= q * (1.0f + w);
                p *= p * (1.0f - w);
            }
            q += p;
            int hx = Float.floatToIntBits(q);
            int ix = Integer.MAX_VALUE & hx;
            int qexp = 0;
            if (ix < 2139095040) {
                if (ix != 0) {
                    if (ix < 8388608) {
                        q *= 3.3554432E7;
                        hx = Float.floatToIntBits(q);
                        ix = (Integer.MAX_VALUE & hx);
                        qexp = -25;
                    }
                    qexp += (ix >>> 23) - 126;
                    hx = ((hx & 0x807FFFFF) | 0x3F000000);
                    q = Float.intBitsToFloat(hx);
                }
            }
            q = Lookup.fromdBlook(amp * Lookup.invsqlook(q) * Lookup.invsq2explook(qexp + m) - ampoffset);
            do {
                final int n2 = i++;
                curve[n2] *= q;
            } while (i < n && map[i] == k);
        }
    }
}
