// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Lpc
{
    Drft fft;
    int ln;
    int m;
    
    Lpc() {
        this.fft = new Drft();
    }
    
    static float lpc_from_data(final float[] data, final float[] lpc, final int n, final int m) {
        final float[] aut = new float[m + 1];
        int j = m + 1;
        while (j-- != 0) {
            float d = 0.0f;
            for (int i = j; i < n; ++i) {
                d += data[i] * data[i - j];
            }
            aut[j] = d;
        }
        float error = aut[0];
        for (int i = 0; i < m; ++i) {
            float r = -aut[i + 1];
            if (error == 0.0f) {
                for (int k = 0; k < m; ++k) {
                    lpc[k] = 0.0f;
                }
                return 0.0f;
            }
            for (j = 0; j < i; ++j) {
                r -= lpc[j] * aut[i - j];
            }
            r /= error;
            lpc[i] = r;
            for (j = 0; j < i / 2; ++j) {
                final float tmp = lpc[j];
                final int n2 = j;
                lpc[n2] += r * lpc[i - 1 - j];
                final int n3 = i - 1 - j;
                lpc[n3] += r * tmp;
            }
            if (i % 2 != 0) {
                final int n4 = j;
                lpc[n4] += lpc[j] * r;
            }
            error *= (float)(1.0 - r * r);
        }
        return error;
    }
    
    float lpc_from_curve(final float[] curve, final float[] lpc) {
        int n = this.ln;
        final float[] work = new float[n + n];
        final float fscale = (float)(0.5 / n);
        for (int i = 0; i < n; ++i) {
            work[i * 2] = curve[i] * fscale;
            work[i * 2 + 1] = 0.0f;
        }
        work[n * 2 - 1] = curve[n - 1] * fscale;
        n *= 2;
        this.fft.backward(work);
        float temp;
        for (int i = 0, j = n / 2; i < n / 2; work[i++] = work[j], work[j++] = temp) {
            temp = work[i];
        }
        return lpc_from_data(work, lpc, n, this.m);
    }
    
    void init(final int mapped, final int m) {
        this.ln = mapped;
        this.m = m;
        this.fft.init(mapped * 2);
    }
    
    void clear() {
        this.fft.clear();
    }
    
    static float FAST_HYPOT(final float a, final float b) {
        return (float)Math.sqrt(a * a + b * b);
    }
    
    void lpc_to_curve(final float[] curve, final float[] lpc, final float amp) {
        for (int i = 0; i < this.ln * 2; ++i) {
            curve[i] = 0.0f;
        }
        if (amp == 0.0f) {
            return;
        }
        for (int i = 0; i < this.m; ++i) {
            curve[i * 2 + 1] = lpc[i] / (4.0f * amp);
            curve[i * 2 + 2] = -lpc[i] / (4.0f * amp);
        }
        this.fft.backward(curve);
        final int l2 = this.ln * 2;
        final float unit = (float)(1.0 / amp);
        curve[0] = (float)(1.0 / (curve[0] * 2.0f + unit));
        for (int j = 1; j < this.ln; ++j) {
            final float real = curve[j] + curve[l2 - j];
            final float imag = curve[j] - curve[l2 - j];
            final float a = real + unit;
            curve[j] = (float)(1.0 / FAST_HYPOT(a, imag));
        }
    }
}
