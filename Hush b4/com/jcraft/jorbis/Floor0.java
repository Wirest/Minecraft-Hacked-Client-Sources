// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Floor0 extends FuncFloor
{
    float[] lsp;
    
    Floor0() {
        this.lsp = null;
    }
    
    @Override
    void pack(final Object i, final Buffer opb) {
        final InfoFloor0 info = (InfoFloor0)i;
        opb.write(info.order, 8);
        opb.write(info.rate, 16);
        opb.write(info.barkmap, 16);
        opb.write(info.ampbits, 6);
        opb.write(info.ampdB, 8);
        opb.write(info.numbooks - 1, 4);
        for (int j = 0; j < info.numbooks; ++j) {
            opb.write(info.books[j], 8);
        }
    }
    
    @Override
    Object unpack(final Info vi, final Buffer opb) {
        final InfoFloor0 info = new InfoFloor0();
        info.order = opb.read(8);
        info.rate = opb.read(16);
        info.barkmap = opb.read(16);
        info.ampbits = opb.read(6);
        info.ampdB = opb.read(8);
        info.numbooks = opb.read(4) + 1;
        if (info.order < 1 || info.rate < 1 || info.barkmap < 1 || info.numbooks < 1) {
            return null;
        }
        for (int j = 0; j < info.numbooks; ++j) {
            info.books[j] = opb.read(8);
            if (info.books[j] < 0 || info.books[j] >= vi.books) {
                return null;
            }
        }
        return info;
    }
    
    @Override
    Object look(final DspState vd, final InfoMode mi, final Object i) {
        final Info vi = vd.vi;
        final InfoFloor0 info = (InfoFloor0)i;
        final LookFloor0 look = new LookFloor0();
        look.m = info.order;
        look.n = vi.blocksizes[mi.blockflag] / 2;
        look.ln = info.barkmap;
        look.vi = info;
        look.lpclook.init(look.ln, look.m);
        final float scale = look.ln / toBARK((float)(info.rate / 2.0));
        look.linearmap = new int[look.n];
        for (int j = 0; j < look.n; ++j) {
            int val = (int)Math.floor(toBARK((float)(info.rate / 2.0 / look.n * j)) * scale);
            if (val >= look.ln) {
                val = look.ln;
            }
            look.linearmap[j] = val;
        }
        return look;
    }
    
    static float toBARK(final float f) {
        return (float)(13.1 * Math.atan(7.4E-4 * f) + 2.24 * Math.atan(f * f * 1.85E-8) + 1.0E-4 * f);
    }
    
    Object state(final Object i) {
        final EchstateFloor0 state = new EchstateFloor0();
        final InfoFloor0 info = (InfoFloor0)i;
        state.codewords = new int[info.order];
        state.curve = new float[info.barkmap];
        state.frameno = -1L;
        return state;
    }
    
    @Override
    void free_info(final Object i) {
    }
    
    @Override
    void free_look(final Object i) {
    }
    
    @Override
    void free_state(final Object vs) {
    }
    
    @Override
    int forward(final Block vb, final Object i, final float[] in, final float[] out, final Object vs) {
        return 0;
    }
    
    int inverse(final Block vb, final Object i, final float[] out) {
        final LookFloor0 look = (LookFloor0)i;
        final InfoFloor0 info = look.vi;
        final int ampraw = vb.opb.read(info.ampbits);
        if (ampraw > 0) {
            final int maxval = (1 << info.ampbits) - 1;
            final float amp = ampraw / (float)maxval * info.ampdB;
            final int booknum = vb.opb.read(Util.ilog(info.numbooks));
            if (booknum != -1 && booknum < info.numbooks) {
                synchronized (this) {
                    if (this.lsp == null || this.lsp.length < look.m) {
                        this.lsp = new float[look.m];
                    }
                    else {
                        for (int j = 0; j < look.m; ++j) {
                            this.lsp[j] = 0.0f;
                        }
                    }
                    final CodeBook b = vb.vd.fullbooks[info.books[booknum]];
                    float last = 0.0f;
                    for (int k = 0; k < look.m; ++k) {
                        out[k] = 0.0f;
                    }
                    for (int k = 0; k < look.m; k += b.dim) {
                        if (b.decodevs(this.lsp, k, vb.opb, 1, -1) == -1) {
                            for (int l = 0; l < look.n; ++l) {
                                out[l] = 0.0f;
                            }
                            return 0;
                        }
                    }
                    int k = 0;
                    while (k < look.m) {
                        for (int l = 0; l < b.dim; ++l, ++k) {
                            final float[] lsp = this.lsp;
                            final int n = k;
                            lsp[n] += last;
                        }
                        last = this.lsp[k - 1];
                    }
                    Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, this.lsp, look.m, amp, (float)info.ampdB);
                    return 1;
                }
            }
        }
        return 0;
    }
    
    @Override
    Object inverse1(final Block vb, final Object i, final Object memo) {
        final LookFloor0 look = (LookFloor0)i;
        final InfoFloor0 info = look.vi;
        float[] lsp = null;
        if (memo instanceof float[]) {
            lsp = (float[])memo;
        }
        final int ampraw = vb.opb.read(info.ampbits);
        if (ampraw > 0) {
            final int maxval = (1 << info.ampbits) - 1;
            final float amp = ampraw / (float)maxval * info.ampdB;
            final int booknum = vb.opb.read(Util.ilog(info.numbooks));
            if (booknum != -1 && booknum < info.numbooks) {
                final CodeBook b = vb.vd.fullbooks[info.books[booknum]];
                float last = 0.0f;
                if (lsp == null || lsp.length < look.m + 1) {
                    lsp = new float[look.m + 1];
                }
                else {
                    for (int j = 0; j < lsp.length; ++j) {
                        lsp[j] = 0.0f;
                    }
                }
                for (int j = 0; j < look.m; j += b.dim) {
                    if (b.decodev_set(lsp, j, vb.opb, b.dim) == -1) {
                        return null;
                    }
                }
                int j = 0;
                while (j < look.m) {
                    for (int k = 0; k < b.dim; ++k, ++j) {
                        final float[] array = lsp;
                        final int n = j;
                        array[n] += last;
                    }
                    last = lsp[j - 1];
                }
                lsp[look.m] = amp;
                return lsp;
            }
        }
        return null;
    }
    
    @Override
    int inverse2(final Block vb, final Object i, final Object memo, final float[] out) {
        final LookFloor0 look = (LookFloor0)i;
        final InfoFloor0 info = look.vi;
        if (memo != null) {
            final float[] lsp = (float[])memo;
            final float amp = lsp[look.m];
            Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, lsp, look.m, amp, (float)info.ampdB);
            return 1;
        }
        for (int j = 0; j < look.n; ++j) {
            out[j] = 0.0f;
        }
        return 0;
    }
    
    static float fromdB(final float x) {
        return (float)Math.exp(x * 0.11512925);
    }
    
    static void lsp_to_lpc(final float[] lsp, final float[] lpc, final int m) {
        final int m2 = m / 2;
        final float[] O = new float[m2];
        final float[] E = new float[m2];
        final float[] Ae = new float[m2 + 1];
        final float[] Ao = new float[m2 + 1];
        final float[] Be = new float[m2];
        final float[] Bo = new float[m2];
        for (int i = 0; i < m2; ++i) {
            O[i] = (float)(-2.0 * Math.cos(lsp[i * 2]));
            E[i] = (float)(-2.0 * Math.cos(lsp[i * 2 + 1]));
        }
        int j;
        for (j = 0; j < m2; ++j) {
            Ae[j] = 0.0f;
            Ao[j] = 1.0f;
            Be[j] = 0.0f;
            Bo[j] = 1.0f;
        }
        Ae[j] = (Ao[j] = 1.0f);
        for (int i = 1; i < m + 1; ++i) {
            float A;
            float B = A = 0.0f;
            for (j = 0; j < m2; ++j) {
                float temp = O[j] * Ao[j] + Ae[j];
                Ae[j] = Ao[j];
                Ao[j] = A;
                A += temp;
                temp = E[j] * Bo[j] + Be[j];
                Be[j] = Bo[j];
                Bo[j] = B;
                B += temp;
            }
            lpc[i - 1] = (A + Ao[j] + B - Ae[j]) / 2.0f;
            Ao[j] = A;
            Ae[j] = B;
        }
    }
    
    static void lpc_to_curve(final float[] curve, final float[] lpc, final float amp, final LookFloor0 l, final String name, final int frameno) {
        final float[] lcurve = new float[Math.max(l.ln * 2, l.m * 2 + 2)];
        if (amp == 0.0f) {
            for (int j = 0; j < l.n; ++j) {
                curve[j] = 0.0f;
            }
            return;
        }
        l.lpclook.lpc_to_curve(lcurve, lpc, amp);
        for (int i = 0; i < l.n; ++i) {
            curve[i] = lcurve[l.linearmap[i]];
        }
    }
    
    class InfoFloor0
    {
        int order;
        int rate;
        int barkmap;
        int ampbits;
        int ampdB;
        int numbooks;
        int[] books;
        
        InfoFloor0() {
            this.books = new int[16];
        }
    }
    
    class LookFloor0
    {
        int n;
        int ln;
        int m;
        int[] linearmap;
        InfoFloor0 vi;
        Lpc lpclook;
        
        LookFloor0() {
            this.lpclook = new Lpc();
        }
    }
    
    class EchstateFloor0
    {
        int[] codewords;
        float[] curve;
        long frameno;
        long codes;
    }
}
