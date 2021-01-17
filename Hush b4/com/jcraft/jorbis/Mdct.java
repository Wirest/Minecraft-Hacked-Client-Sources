// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Mdct
{
    int n;
    int log2n;
    float[] trig;
    int[] bitrev;
    float scale;
    float[] _x;
    float[] _w;
    
    Mdct() {
        this._x = new float[1024];
        this._w = new float[1024];
    }
    
    void init(final int n) {
        this.bitrev = new int[n / 4];
        this.trig = new float[n + n / 4];
        this.log2n = (int)Math.rint(Math.log(n) / Math.log(2.0));
        this.n = n;
        final int AE = 0;
        final int AO = 1;
        final int BE = AE + n / 2;
        final int BO = BE + 1;
        final int CE = BE + n / 2;
        final int CO = CE + 1;
        for (int i = 0; i < n / 4; ++i) {
            this.trig[AE + i * 2] = (float)Math.cos(3.141592653589793 / n * (4 * i));
            this.trig[AO + i * 2] = (float)(-Math.sin(3.141592653589793 / n * (4 * i)));
            this.trig[BE + i * 2] = (float)Math.cos(3.141592653589793 / (2 * n) * (2 * i + 1));
            this.trig[BO + i * 2] = (float)Math.sin(3.141592653589793 / (2 * n) * (2 * i + 1));
        }
        for (int i = 0; i < n / 8; ++i) {
            this.trig[CE + i * 2] = (float)Math.cos(3.141592653589793 / n * (4 * i + 2));
            this.trig[CO + i * 2] = (float)(-Math.sin(3.141592653589793 / n * (4 * i + 2)));
        }
        final int mask = (1 << this.log2n - 1) - 1;
        final int msb = 1 << this.log2n - 2;
        for (int j = 0; j < n / 8; ++j) {
            int acc = 0;
            for (int k = 0; msb >>> k != 0; ++k) {
                if ((msb >>> k & j) != 0x0) {
                    acc |= 1 << k;
                }
            }
            this.bitrev[j * 2] = (~acc & mask);
            this.bitrev[j * 2 + 1] = acc;
        }
        this.scale = 4.0f / n;
    }
    
    void clear() {
    }
    
    void forward(final float[] in, final float[] out) {
    }
    
    synchronized void backward(final float[] in, final float[] out) {
        if (this._x.length < this.n / 2) {
            this._x = new float[this.n / 2];
        }
        if (this._w.length < this.n / 2) {
            this._w = new float[this.n / 2];
        }
        final float[] x = this._x;
        final float[] w = this._w;
        final int n2 = this.n >>> 1;
        final int n3 = this.n >>> 2;
        final int n4 = this.n >>> 3;
        int inO = 1;
        int xO = 0;
        int A = n2;
        for (int i = 0; i < n4; ++i) {
            A -= 2;
            x[xO++] = -in[inO + 2] * this.trig[A + 1] - in[inO] * this.trig[A];
            x[xO++] = in[inO] * this.trig[A + 1] - in[inO + 2] * this.trig[A];
            inO += 4;
        }
        inO = n2 - 4;
        for (int i = 0; i < n4; ++i) {
            A -= 2;
            x[xO++] = in[inO] * this.trig[A + 1] + in[inO + 2] * this.trig[A];
            x[xO++] = in[inO] * this.trig[A] - in[inO + 2] * this.trig[A + 1];
            inO -= 4;
        }
        final float[] xxx = this.mdct_kernel(x, w, this.n, n2, n3, n4);
        int xx = 0;
        int B = n2;
        int o1 = n3;
        int o2 = o1 - 1;
        int o3 = n3 + n2;
        int o4 = o3 - 1;
        for (int j = 0; j < n3; ++j) {
            final float temp1 = xxx[xx] * this.trig[B + 1] - xxx[xx + 1] * this.trig[B];
            final float temp2 = -(xxx[xx] * this.trig[B] + xxx[xx + 1] * this.trig[B + 1]);
            out[o1] = -temp1;
            out[o2] = temp1;
            out[o4] = (out[o3] = temp2);
            ++o1;
            --o2;
            ++o3;
            --o4;
            xx += 2;
            B += 2;
        }
    }
    
    private float[] mdct_kernel(float[] x, float[] w, final int n, final int n2, final int n4, final int n8) {
        float x2;
        float x3;
        for (int xA = n4, xB = 0, w2 = n4, A = n2, i = 0; i < n4; w[i++] = x2 * this.trig[A] + x3 * this.trig[A + 1], w[i] = x3 * this.trig[A] - x2 * this.trig[A + 1], w[w2 + i] = x[xA++] + x[xB++], ++i) {
            x2 = x[xA] - x[xB];
            w[w2 + i] = x[xA++] + x[xB++];
            x3 = x[xA] - x[xB];
            A -= 4;
        }
        for (int i = 0; i < this.log2n - 3; ++i) {
            int k0 = n >>> i + 2;
            final int k2 = 1 << i + 3;
            int wbase = n2 - 2;
            for (int A = 0, r = 0; r < k0 >>> 2; --k0, A += k2, ++r) {
                int w3 = wbase;
                int w2 = w3 - (k0 >> 1);
                final float AEv = this.trig[A];
                final float AOv = this.trig[A + 1];
                wbase -= 2;
                ++k0;
                for (int s = 0; s < 2 << i; ++s) {
                    final float wB = w[w3] - w[w2];
                    x[w3] = w[w3] + w[w2];
                    final float wA = w[++w3] - w[++w2];
                    x[w3] = w[w3] + w[w2];
                    x[w2] = wA * AEv - wB * AOv;
                    x[w2 - 1] = wB * AEv + wA * AOv;
                    w3 -= k0;
                    w2 -= k0;
                }
            }
            final float[] temp = w;
            w = x;
            x = temp;
        }
        int C = n;
        int bit = 0;
        int x4 = 0;
        int x5 = n2 - 1;
        for (int j = 0; j < n8; ++j) {
            final int t1 = this.bitrev[bit++];
            final int t2 = this.bitrev[bit++];
            final float wA2 = w[t1] - w[t2 + 1];
            final float wB2 = w[t1 - 1] + w[t2];
            final float wC = w[t1] + w[t2 + 1];
            final float wD = w[t1 - 1] - w[t2];
            final float wACE = wA2 * this.trig[C];
            final float wBCE = wB2 * this.trig[C++];
            final float wACO = wA2 * this.trig[C];
            final float wBCO = wB2 * this.trig[C++];
            x[x4++] = (wC + wACO + wBCE) * 0.5f;
            x[x5--] = (-wD + wBCO - wACE) * 0.5f;
            x[x4++] = (wD + wBCO - wACE) * 0.5f;
            x[x5--] = (wC - wACO - wBCE) * 0.5f;
        }
        return x;
    }
}
