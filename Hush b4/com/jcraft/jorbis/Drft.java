// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Drft
{
    int n;
    float[] trigcache;
    int[] splitcache;
    static int[] ntryh;
    static float tpi;
    static float hsqt2;
    static float taui;
    static float taur;
    static float sqrt2;
    
    void backward(final float[] data) {
        if (this.n == 1) {
            return;
        }
        drftb1(this.n, data, this.trigcache, this.trigcache, this.n, this.splitcache);
    }
    
    void init(final int n) {
        this.n = n;
        this.trigcache = new float[3 * n];
        this.splitcache = new int[32];
        fdrffti(n, this.trigcache, this.splitcache);
    }
    
    void clear() {
        if (this.trigcache != null) {
            this.trigcache = null;
        }
        if (this.splitcache != null) {
            this.splitcache = null;
        }
    }
    
    static void drfti1(final int n, final float[] wa, final int index, final int[] ifac) {
        int ntry = 0;
        int j = -1;
        int nl = n;
        int nf = 0;
        int state = 101;
    Label_0197:
        while (true) {
            Label_0075: {
                switch (state) {
                    case 101: {
                        if (++j < 4) {
                            ntry = Drft.ntryh[j];
                            break Label_0075;
                        }
                        ntry += 2;
                        break Label_0075;
                    }
                    case 104: {
                        final int nq = nl / ntry;
                        final int nr = nl - ntry * nq;
                        if (nr != 0) {
                            state = 101;
                            continue;
                        }
                        ++nf;
                        ifac[nf + 1] = ntry;
                        nl = nq;
                        if (ntry != 2) {
                            state = 107;
                            continue;
                        }
                        if (nf == 1) {
                            state = 107;
                            continue;
                        }
                        for (int i = 1; i < nf; ++i) {
                            final int ib = nf - i + 1;
                            ifac[ib + 1] = ifac[ib];
                        }
                        ifac[2] = 2;
                    }
                    case 107: {
                        if (nl != 1) {
                            state = 104;
                            continue;
                        }
                        break Label_0197;
                    }
                }
            }
        }
        ifac[0] = n;
        ifac[1] = nf;
        final float argh = Drft.tpi / n;
        int is = 0;
        final int nfm1 = nf - 1;
        int l1 = 1;
        if (nfm1 == 0) {
            return;
        }
        for (int k1 = 0; k1 < nfm1; ++k1) {
            final int ip = ifac[k1 + 2];
            int ld = 0;
            final int l2 = l1 * ip;
            final int ido = n / l2;
            for (final int ipm = ip - 1, j = 0; j < ipm; ++j) {
                ld += l1;
                int i = is;
                final float argld = ld * argh;
                float fi = 0.0f;
                for (int ii = 2; ii < ido; ii += 2) {
                    ++fi;
                    final float arg = fi * argld;
                    wa[index + i++] = (float)Math.cos(arg);
                    wa[index + i++] = (float)Math.sin(arg);
                }
                is += ido;
            }
            l1 = l2;
        }
    }
    
    static void fdrffti(final int n, final float[] wsave, final int[] ifac) {
        if (n == 1) {
            return;
        }
        drfti1(n, wsave, n, ifac);
    }
    
    static void dradf2(final int ido, final int l1, final float[] cc, final float[] ch, final float[] wa1, final int index) {
        int t1 = 0;
        final int t3;
        int t2 = t3 = l1 * ido;
        int t4 = ido << 1;
        for (int k = 0; k < l1; ++k) {
            ch[t1 << 1] = cc[t1] + cc[t2];
            ch[(t1 << 1) + t4 - 1] = cc[t1] - cc[t2];
            t1 += ido;
            t2 += ido;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            t2 = t3;
            for (int k = 0; k < l1; ++k) {
                t4 = t2;
                int t5 = (t1 << 1) + (ido << 1);
                int t6 = t1;
                int t7 = t1 + t1;
                for (int i = 2; i < ido; i += 2) {
                    t4 += 2;
                    t5 -= 2;
                    t6 += 2;
                    t7 += 2;
                    final float tr2 = wa1[index + i - 2] * cc[t4 - 1] + wa1[index + i - 1] * cc[t4];
                    final float ti2 = wa1[index + i - 2] * cc[t4] - wa1[index + i - 1] * cc[t4 - 1];
                    ch[t7] = cc[t6] + ti2;
                    ch[t5] = ti2 - cc[t6];
                    ch[t7 - 1] = cc[t6 - 1] + tr2;
                    ch[t5 - 1] = cc[t6 - 1] - tr2;
                }
                t1 += ido;
                t2 += ido;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido;
        t2 = (t4 = ido - 1);
        t2 += t3;
        for (int k = 0; k < l1; ++k) {
            ch[t1] = -cc[t2];
            ch[t1 - 1] = cc[t4];
            t1 += ido << 1;
            t2 += ido;
            t4 += ido;
        }
    }
    
    static void dradf4(final int ido, final int l1, final float[] cc, final float[] ch, final float[] wa1, final int index1, final float[] wa2, final int index2, final float[] wa3, final int index3) {
        int t2;
        final int t0 = t2 = l1 * ido;
        int t3 = t2 << 1;
        int t4 = t2 + (t2 << 1);
        int t5 = 0;
        for (int k = 0; k < l1; ++k) {
            final float tr1 = cc[t2] + cc[t4];
            final float tr2 = cc[t5] + cc[t3];
            int t6;
            ch[t6 = t5 << 2] = tr1 + tr2;
            ch[(ido << 2) + t6 - 1] = tr2 - tr1;
            ch[(t6 += ido << 1) - 1] = cc[t5] - cc[t3];
            ch[t6] = cc[t4] - cc[t2];
            t2 += ido;
            t4 += ido;
            t5 += ido;
            t3 += ido;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t2 = 0;
            for (int k = 0; k < l1; ++k) {
                t4 = t2;
                t3 = t2 << 2;
                final int t7;
                int t6 = (t7 = ido << 1) + t3;
                for (int i = 2; i < ido; i += 2) {
                    t4 += 2;
                    t5 = t4;
                    t3 += 2;
                    t6 -= 2;
                    t5 += t0;
                    final float cr2 = wa1[index1 + i - 2] * cc[t5 - 1] + wa1[index1 + i - 1] * cc[t5];
                    final float ci2 = wa1[index1 + i - 2] * cc[t5] - wa1[index1 + i - 1] * cc[t5 - 1];
                    t5 += t0;
                    final float cr3 = wa2[index2 + i - 2] * cc[t5 - 1] + wa2[index2 + i - 1] * cc[t5];
                    final float ci3 = wa2[index2 + i - 2] * cc[t5] - wa2[index2 + i - 1] * cc[t5 - 1];
                    t5 += t0;
                    final float cr4 = wa3[index3 + i - 2] * cc[t5 - 1] + wa3[index3 + i - 1] * cc[t5];
                    final float ci4 = wa3[index3 + i - 2] * cc[t5] - wa3[index3 + i - 1] * cc[t5 - 1];
                    final float tr1 = cr2 + cr4;
                    final float tr3 = cr4 - cr2;
                    final float ti1 = ci2 + ci4;
                    final float ti2 = ci2 - ci4;
                    final float ti3 = cc[t4] + ci3;
                    final float ti4 = cc[t4] - ci3;
                    final float tr2 = cc[t4 - 1] + cr3;
                    final float tr4 = cc[t4 - 1] - cr3;
                    ch[t3 - 1] = tr1 + tr2;
                    ch[t3] = ti1 + ti3;
                    ch[t6 - 1] = tr4 - ti2;
                    ch[t6] = tr3 - ti4;
                    ch[t3 + t7 - 1] = ti2 + tr4;
                    ch[t3 + t7] = tr3 + ti4;
                    ch[t6 + t7 - 1] = tr2 - tr1;
                    ch[t6 + t7] = ti1 - ti3;
                }
                t2 += ido;
            }
            if ((ido & 0x1) != 0x0) {
                return;
            }
        }
        t4 = (t2 = t0 + ido - 1) + (t0 << 1);
        t5 = ido << 2;
        t3 = ido;
        int t6 = ido << 1;
        int t7 = ido;
        for (int k = 0; k < l1; ++k) {
            final float ti1 = -Drft.hsqt2 * (cc[t2] + cc[t4]);
            final float tr1 = Drft.hsqt2 * (cc[t2] - cc[t4]);
            ch[t3 - 1] = tr1 + cc[t7 - 1];
            ch[t3 + t6 - 1] = cc[t7 - 1] - tr1;
            ch[t3] = ti1 - cc[t2 + t0];
            ch[t3 + t6] = ti1 + cc[t2 + t0];
            t2 += ido;
            t4 += ido;
            t3 += t5;
            t7 += ido;
        }
    }
    
    static void dradfg(final int ido, final int ip, final int l1, final int idl1, final float[] cc, final float[] c1, final float[] c2, final float[] ch, final float[] ch2, final float[] wa, final int index) {
        int t2 = 0;
        float dcp = 0.0f;
        float dsp = 0.0f;
        final float arg = Drft.tpi / ip;
        dcp = (float)Math.cos(arg);
        dsp = (float)Math.sin(arg);
        final int ipph = ip + 1 >> 1;
        final int ipp2 = ip;
        final int idp2 = ido;
        final int nbd = ido - 1 >> 1;
        final int t3 = l1 * ido;
        final int t4 = ip * ido;
        int state = 100;
        while (true) {
            Label_0917: {
                switch (state) {
                    case 101: {
                        if (ido == 1) {
                            state = 119;
                            continue;
                        }
                        for (int ik = 0; ik < idl1; ++ik) {
                            ch2[ik] = c2[ik];
                        }
                        int t5 = 0;
                        for (int j = 1; j < ip; ++j) {
                            t5 = (t2 = t5 + t3);
                            for (int k = 0; k < l1; ++k) {
                                ch[t2] = c1[t2];
                                t2 += ido;
                            }
                        }
                        int is = -ido;
                        t5 = 0;
                        if (nbd > l1) {
                            for (int j = 1; j < ip; ++j) {
                                t5 += t3;
                                is += ido;
                                t2 = -ido + t5;
                                for (int k = 0; k < l1; ++k) {
                                    int idij = is - 1;
                                    int t6;
                                    t2 = (t6 = t2 + ido);
                                    for (int i = 2; i < ido; i += 2) {
                                        idij += 2;
                                        t6 += 2;
                                        ch[t6 - 1] = wa[index + idij - 1] * c1[t6 - 1] + wa[index + idij] * c1[t6];
                                        ch[t6] = wa[index + idij - 1] * c1[t6] - wa[index + idij] * c1[t6 - 1];
                                    }
                                }
                            }
                        }
                        else {
                            for (int j = 1; j < ip; ++j) {
                                is += ido;
                                int idij = is - 1;
                                t5 = (t2 = t5 + t3);
                                for (int i = 2; i < ido; i += 2) {
                                    idij += 2;
                                    t2 += 2;
                                    int t6 = t2;
                                    for (int k = 0; k < l1; ++k) {
                                        ch[t6 - 1] = wa[index + idij - 1] * c1[t6 - 1] + wa[index + idij] * c1[t6];
                                        ch[t6] = wa[index + idij - 1] * c1[t6] - wa[index + idij] * c1[t6 - 1];
                                        t6 += ido;
                                    }
                                }
                            }
                        }
                        t5 = 0;
                        t2 = ipp2 * t3;
                        if (nbd < l1) {
                            for (int j = 1; j < ipph; ++j) {
                                t5 += t3;
                                t2 -= t3;
                                int t6 = t5;
                                int t7 = t2;
                                for (int i = 2; i < ido; i += 2) {
                                    t6 += 2;
                                    t7 += 2;
                                    int t8 = t6 - ido;
                                    int t9 = t7 - ido;
                                    for (int k = 0; k < l1; ++k) {
                                        t8 += ido;
                                        t9 += ido;
                                        c1[t8 - 1] = ch[t8 - 1] + ch[t9 - 1];
                                        c1[t9 - 1] = ch[t8] - ch[t9];
                                        c1[t8] = ch[t8] + ch[t9];
                                        c1[t9] = ch[t9 - 1] - ch[t8 - 1];
                                    }
                                }
                            }
                            break Label_0917;
                        }
                        for (int j = 1; j < ipph; ++j) {
                            t5 += t3;
                            t2 -= t3;
                            int t6 = t5;
                            int t7 = t2;
                            for (int k = 0; k < l1; ++k) {
                                int t8 = t6;
                                int t9 = t7;
                                for (int i = 2; i < ido; i += 2) {
                                    t8 += 2;
                                    t9 += 2;
                                    c1[t8 - 1] = ch[t8 - 1] + ch[t9 - 1];
                                    c1[t9 - 1] = ch[t8] - ch[t9];
                                    c1[t8] = ch[t8] + ch[t9];
                                    c1[t9] = ch[t9 - 1] - ch[t8 - 1];
                                }
                                t6 += ido;
                                t7 += ido;
                            }
                        }
                        break Label_0917;
                    }
                    case 119: {
                        for (int ik = 0; ik < idl1; ++ik) {
                            c2[ik] = ch2[ik];
                        }
                        int t5 = 0;
                        t2 = ipp2 * idl1;
                        for (int j = 1; j < ipph; ++j) {
                            t5 += t3;
                            t2 -= t3;
                            int t6 = t5 - ido;
                            int t7 = t2 - ido;
                            for (int k = 0; k < l1; ++k) {
                                t6 += ido;
                                t7 += ido;
                                c1[t6] = ch[t6] + ch[t7];
                                c1[t7] = ch[t7] - ch[t6];
                            }
                        }
                        float ar1 = 1.0f;
                        float ai1 = 0.0f;
                        t5 = 0;
                        t2 = ipp2 * idl1;
                        int t6 = (ip - 1) * idl1;
                        for (int m = 1; m < ipph; ++m) {
                            t5 += idl1;
                            t2 -= idl1;
                            final float ar1h = dcp * ar1 - dsp * ai1;
                            ai1 = dcp * ai1 + dsp * ar1;
                            ar1 = ar1h;
                            int t7 = t5;
                            int t8 = t2;
                            int t9 = t6;
                            int t10 = idl1;
                            for (int ik = 0; ik < idl1; ++ik) {
                                ch2[t7++] = c2[ik] + ar1 * c2[t10++];
                                ch2[t8++] = ai1 * c2[t9++];
                            }
                            final float dc2 = ar1;
                            final float ds2 = ai1;
                            float ar2 = ar1;
                            float ai2 = ai1;
                            t7 = idl1;
                            t8 = (ipp2 - 1) * idl1;
                            for (int j = 2; j < ipph; ++j) {
                                t7 += idl1;
                                t8 -= idl1;
                                final float ar2h = dc2 * ar2 - ds2 * ai2;
                                ai2 = dc2 * ai2 + ds2 * ar2;
                                ar2 = ar2h;
                                t9 = t5;
                                t10 = t2;
                                int t11 = t7;
                                int t12 = t8;
                                for (int ik = 0; ik < idl1; ++ik) {
                                    final int n = t9++;
                                    ch2[n] += ar2 * c2[t11++];
                                    final int n2 = t10++;
                                    ch2[n2] += ai2 * c2[t12++];
                                }
                            }
                        }
                        t5 = 0;
                        for (int j = 1; j < ipph; ++j) {
                            t5 = (t2 = t5 + idl1);
                            for (int ik = 0; ik < idl1; ++ik) {
                                final int n3 = ik;
                                ch2[n3] += c2[t2++];
                            }
                        }
                        if (ido < l1) {
                            state = 132;
                            continue;
                        }
                        t5 = 0;
                        t2 = 0;
                        for (int k = 0; k < l1; ++k) {
                            t6 = t5;
                            int t7 = t2;
                            for (int i = 0; i < ido; ++i) {
                                cc[t7++] = ch[t6++];
                            }
                            t5 += ido;
                            t2 += t4;
                        }
                        state = 135;
                        continue;
                    }
                    case 132: {
                        for (int i = 0; i < ido; ++i) {
                            int t5 = i;
                            t2 = i;
                            for (int k = 0; k < l1; ++k) {
                                cc[t2] = ch[t5];
                                t5 += ido;
                                t2 += t4;
                            }
                        }
                    }
                    case 135: {
                        int t5 = 0;
                        t2 = ido << 1;
                        int t6 = 0;
                        int t7 = ipp2 * t3;
                        for (int j = 1; j < ipph; ++j) {
                            t5 += t2;
                            t6 += t3;
                            t7 -= t3;
                            int t8 = t5;
                            int t9 = t6;
                            int t10 = t7;
                            for (int k = 0; k < l1; ++k) {
                                cc[t8 - 1] = ch[t9];
                                cc[t8] = ch[t10];
                                t8 += t4;
                                t9 += ido;
                                t10 += ido;
                            }
                        }
                        if (ido == 1) {
                            return;
                        }
                        if (nbd < l1) {
                            state = 141;
                            continue;
                        }
                        t5 = -ido;
                        t6 = 0;
                        t7 = 0;
                        int t8 = ipp2 * t3;
                        for (int j = 1; j < ipph; ++j) {
                            t5 += t2;
                            t6 += t2;
                            t7 += t3;
                            t8 -= t3;
                            int t9 = t5;
                            int t10 = t6;
                            int t11 = t7;
                            int t12 = t8;
                            for (int k = 0; k < l1; ++k) {
                                for (int i = 2; i < ido; i += 2) {
                                    final int ic = idp2 - i;
                                    cc[i + t10 - 1] = ch[i + t11 - 1] + ch[i + t12 - 1];
                                    cc[ic + t9 - 1] = ch[i + t11 - 1] - ch[i + t12 - 1];
                                    cc[i + t10] = ch[i + t11] + ch[i + t12];
                                    cc[ic + t9] = ch[i + t12] - ch[i + t11];
                                }
                                t9 += t4;
                                t10 += t4;
                                t11 += ido;
                                t12 += ido;
                            }
                        }
                    }
                    case 141: {
                        int t5 = -ido;
                        int t6 = 0;
                        int t7 = 0;
                        int t8 = ipp2 * t3;
                        for (int j = 1; j < ipph; ++j) {
                            t5 += t2;
                            t6 += t2;
                            t7 += t3;
                            t8 -= t3;
                            for (int i = 2; i < ido; i += 2) {
                                int t9 = idp2 + t5 - i;
                                int t10 = i + t6;
                                int t11 = i + t7;
                                int t12 = i + t8;
                                for (int k = 0; k < l1; ++k) {
                                    cc[t10 - 1] = ch[t11 - 1] + ch[t12 - 1];
                                    cc[t9 - 1] = ch[t11 - 1] - ch[t12 - 1];
                                    cc[t10] = ch[t11] + ch[t12];
                                    cc[t9] = ch[t12] - ch[t11];
                                    t9 += t4;
                                    t10 += t4;
                                    t11 += ido;
                                    t12 += ido;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static void drftf1(final int n, final float[] c, final float[] ch, final float[] wa, final int[] ifac) {
        final int nf = ifac[1];
        int na = 1;
        int l2 = n;
        int iw = n;
        for (int k1 = 0; k1 < nf; ++k1) {
            final int kh = nf - k1;
            final int ip = ifac[kh + 1];
            final int l3 = l2 / ip;
            final int ido = n / l2;
            final int idl1 = ido * l3;
            iw -= (ip - 1) * ido;
            na = 1 - na;
            int state = 100;
        Label_0373:
            while (true) {
                switch (state) {
                    case 100: {
                        if (ip != 4) {
                            state = 102;
                            continue;
                        }
                        final int ix2 = iw + ido;
                        final int ix3 = ix2 + ido;
                        if (na != 0) {
                            dradf4(ido, l3, ch, c, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        }
                        else {
                            dradf4(ido, l3, c, ch, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        }
                        state = 110;
                        continue;
                    }
                    case 102: {
                        if (ip != 2) {
                            state = 104;
                            continue;
                        }
                        if (na != 0) {
                            state = 103;
                            continue;
                        }
                        dradf2(ido, l3, c, ch, wa, iw - 1);
                        state = 110;
                        continue;
                    }
                    case 103: {
                        dradf2(ido, l3, ch, c, wa, iw - 1);
                    }
                    case 104: {
                        if (ido == 1) {
                            na = 1 - na;
                        }
                        if (na != 0) {
                            state = 109;
                            continue;
                        }
                        dradfg(ido, ip, l3, idl1, c, c, c, ch, ch, wa, iw - 1);
                        na = 1;
                        state = 110;
                        continue;
                    }
                    case 109: {
                        dradfg(ido, ip, l3, idl1, ch, ch, ch, c, c, wa, iw - 1);
                        na = 0;
                    }
                    case 110: {
                        break Label_0373;
                    }
                }
            }
            l2 = l3;
        }
        if (na == 1) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            c[i] = ch[i];
        }
    }
    
    static void dradb2(final int ido, final int l1, final float[] cc, final float[] ch, final float[] wa1, final int index) {
        final int t0 = l1 * ido;
        int t2 = 0;
        int t3 = 0;
        int t4 = (ido << 1) - 1;
        for (int k = 0; k < l1; ++k) {
            ch[t2] = cc[t3] + cc[t4 + t3];
            ch[t2 + t0] = cc[t3] - cc[t4 + t3];
            t3 = (t2 += ido) << 1;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t2 = 0;
            t3 = 0;
            for (int k = 0; k < l1; ++k) {
                t4 = t2;
                int t6;
                int t5 = (t6 = t3) + (ido << 1);
                int t7 = t0 + t2;
                for (int i = 2; i < ido; i += 2) {
                    t4 += 2;
                    t6 += 2;
                    t5 -= 2;
                    t7 += 2;
                    ch[t4 - 1] = cc[t6 - 1] + cc[t5 - 1];
                    final float tr2 = cc[t6 - 1] - cc[t5 - 1];
                    ch[t4] = cc[t6] - cc[t5];
                    final float ti2 = cc[t6] + cc[t5];
                    ch[t7 - 1] = wa1[index + i - 2] * tr2 - wa1[index + i - 1] * ti2;
                    ch[t7] = wa1[index + i - 2] * ti2 + wa1[index + i - 1] * tr2;
                }
                t3 = (t2 += ido) << 1;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t2 = ido - 1;
        t3 = ido - 1;
        for (int k = 0; k < l1; ++k) {
            ch[t2] = cc[t3] + cc[t3];
            ch[t2 + t0] = -(cc[t3 + 1] + cc[t3 + 1]);
            t2 += ido;
            t3 += ido << 1;
        }
    }
    
    static void dradb3(final int ido, final int l1, final float[] cc, final float[] ch, final float[] wa1, final int index1, final float[] wa2, final int index2) {
        final int t0 = l1 * ido;
        int t2 = 0;
        final int t3 = t0 << 1;
        int t4 = ido << 1;
        final int t5 = ido + (ido << 1);
        int t6 = 0;
        for (int k = 0; k < l1; ++k) {
            final float tr2 = cc[t4 - 1] + cc[t4 - 1];
            final float cr2 = cc[t6] + Drft.taur * tr2;
            ch[t2] = cc[t6] + tr2;
            final float ci3 = Drft.taui * (cc[t4] + cc[t4]);
            ch[t2 + t0] = cr2 - ci3;
            ch[t2 + t3] = cr2 + ci3;
            t2 += ido;
            t4 += t5;
            t6 += t5;
        }
        if (ido == 1) {
            return;
        }
        t2 = 0;
        t4 = ido << 1;
        for (int k = 0; k < l1; ++k) {
            int t7 = t2 + (t2 << 1);
            int t8;
            t6 = (t8 = t7 + t4);
            int t9 = t2;
            int t11;
            int t10 = (t11 = t2 + t0) + t0;
            for (int i = 2; i < ido; i += 2) {
                t6 += 2;
                t8 -= 2;
                t7 += 2;
                t9 += 2;
                t11 += 2;
                t10 += 2;
                final float tr2 = cc[t6 - 1] + cc[t8 - 1];
                final float cr2 = cc[t7 - 1] + Drft.taur * tr2;
                ch[t9 - 1] = cc[t7 - 1] + tr2;
                final float ti2 = cc[t6] - cc[t8];
                final float ci4 = cc[t7] + Drft.taur * ti2;
                ch[t9] = cc[t7] + ti2;
                final float cr3 = Drft.taui * (cc[t6 - 1] - cc[t8 - 1]);
                final float ci3 = Drft.taui * (cc[t6] + cc[t8]);
                final float dr2 = cr2 - ci3;
                final float dr3 = cr2 + ci3;
                final float di2 = ci4 + cr3;
                final float di3 = ci4 - cr3;
                ch[t11 - 1] = wa1[index1 + i - 2] * dr2 - wa1[index1 + i - 1] * di2;
                ch[t11] = wa1[index1 + i - 2] * di2 + wa1[index1 + i - 1] * dr2;
                ch[t10 - 1] = wa2[index2 + i - 2] * dr3 - wa2[index2 + i - 1] * di3;
                ch[t10] = wa2[index2 + i - 2] * di3 + wa2[index2 + i - 1] * dr3;
            }
            t2 += ido;
        }
    }
    
    static void dradb4(final int ido, final int l1, final float[] cc, final float[] ch, final float[] wa1, final int index1, final float[] wa2, final int index2, final float[] wa3, final int index3) {
        final int t0 = l1 * ido;
        int t2 = 0;
        int t3 = ido << 2;
        int t4 = 0;
        final int t5 = ido << 1;
        for (int k = 0; k < l1; ++k) {
            int t6 = t4 + t5;
            int t7 = t2;
            final float tr3 = cc[t6 - 1] + cc[t6 - 1];
            final float tr4 = cc[t6] + cc[t6];
            final float tr5 = cc[t4] - cc[(t6 += t5) - 1];
            final float tr6 = cc[t4] + cc[t6 - 1];
            ch[t7] = tr6 + tr3;
            ch[t7 += t0] = tr5 - tr4;
            ch[t7 += t0] = tr6 - tr3;
            ch[t7 += t0] = tr5 + tr4;
            t2 += ido;
            t4 += t3;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t2 = 0;
            for (int k = 0; k < l1; ++k) {
                int t6;
                int t7 = (t6 = (t4 = (t3 = t2 << 2) + t5)) + t5;
                int t8 = t2;
                for (int i = 2; i < ido; i += 2) {
                    t3 += 2;
                    t4 += 2;
                    t6 -= 2;
                    t7 -= 2;
                    t8 += 2;
                    final float ti1 = cc[t3] + cc[t7];
                    final float ti2 = cc[t3] - cc[t7];
                    final float ti3 = cc[t4] - cc[t6];
                    final float tr4 = cc[t4] + cc[t6];
                    final float tr5 = cc[t3 - 1] - cc[t7 - 1];
                    final float tr6 = cc[t3 - 1] + cc[t7 - 1];
                    final float ti4 = cc[t4 - 1] - cc[t6 - 1];
                    final float tr3 = cc[t4 - 1] + cc[t6 - 1];
                    ch[t8 - 1] = tr6 + tr3;
                    final float cr3 = tr6 - tr3;
                    ch[t8] = ti2 + ti3;
                    final float ci3 = ti2 - ti3;
                    final float cr4 = tr5 - tr4;
                    final float cr5 = tr5 + tr4;
                    final float ci4 = ti1 + ti4;
                    final float ci5 = ti1 - ti4;
                    int t9;
                    ch[(t9 = t8 + t0) - 1] = wa1[index1 + i - 2] * cr4 - wa1[index1 + i - 1] * ci4;
                    ch[t9] = wa1[index1 + i - 2] * ci4 + wa1[index1 + i - 1] * cr4;
                    ch[(t9 += t0) - 1] = wa2[index2 + i - 2] * cr3 - wa2[index2 + i - 1] * ci3;
                    ch[t9] = wa2[index2 + i - 2] * ci3 + wa2[index2 + i - 1] * cr3;
                    ch[(t9 += t0) - 1] = wa3[index3 + i - 2] * cr5 - wa3[index3 + i - 1] * ci5;
                    ch[t9] = wa3[index3 + i - 2] * ci5 + wa3[index3 + i - 1] * cr5;
                }
                t2 += ido;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t2 = ido;
        t3 = ido << 2;
        t4 = ido - 1;
        int t6 = ido + (ido << 1);
        for (int k = 0; k < l1; ++k) {
            int t7 = t4;
            final float ti1 = cc[t2] + cc[t6];
            final float ti2 = cc[t6] - cc[t2];
            final float tr5 = cc[t2 - 1] - cc[t6 - 1];
            final float tr6 = cc[t2 - 1] + cc[t6 - 1];
            ch[t7] = tr6 + tr6;
            ch[t7 += t0] = Drft.sqrt2 * (tr5 - ti1);
            ch[t7 += t0] = ti2 + ti2;
            ch[t7 += t0] = -Drft.sqrt2 * (tr5 + ti1);
            t4 += ido;
            t2 += t3;
            t6 += t3;
        }
    }
    
    static void dradbg(final int ido, final int ip, final int l1, final int idl1, final float[] cc, final float[] c1, final float[] c2, final float[] ch, final float[] ch2, final float[] wa, final int index) {
        int ipph = 0;
        int t0 = 0;
        int t2 = 0;
        int nbd = 0;
        float dcp = 0.0f;
        float dsp = 0.0f;
        int ipp2 = 0;
        int state = 100;
        while (true) {
            switch (state) {
                case 100: {
                    t2 = ip * ido;
                    t0 = l1 * ido;
                    final float arg = Drft.tpi / ip;
                    dcp = (float)Math.cos(arg);
                    dsp = (float)Math.sin(arg);
                    nbd = ido - 1 >>> 1;
                    ipp2 = ip;
                    ipph = ip + 1 >>> 1;
                    if (ido < l1) {
                        state = 103;
                        continue;
                    }
                    int t3 = 0;
                    int t4 = 0;
                    for (int k = 0; k < l1; ++k) {
                        int t5 = t3;
                        int t6 = t4;
                        for (int i = 0; i < ido; ++i) {
                            ch[t5] = cc[t6];
                            ++t5;
                            ++t6;
                        }
                        t3 += ido;
                        t4 += t2;
                    }
                    state = 106;
                    continue;
                }
                case 103: {
                    int t3 = 0;
                    for (int i = 0; i < ido; ++i) {
                        int t4 = t3;
                        int t5 = t3;
                        for (int k = 0; k < l1; ++k) {
                            ch[t4] = cc[t5];
                            t4 += ido;
                            t5 += t2;
                        }
                        ++t3;
                    }
                }
                case 106: {
                    int t3 = 0;
                    int t4 = ipp2 * t0;
                    int t8;
                    int t7 = t8 = ido << 1;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        int t5 = t3;
                        int t6 = t4;
                        int t9 = t7;
                        for (int k = 0; k < l1; ++k) {
                            ch[t5] = cc[t9 - 1] + cc[t9 - 1];
                            ch[t6] = cc[t9] + cc[t9];
                            t5 += ido;
                            t6 += ido;
                            t9 += t2;
                        }
                        t7 += t8;
                    }
                    if (ido == 1) {
                        state = 116;
                        continue;
                    }
                    if (nbd < l1) {
                        state = 112;
                        continue;
                    }
                    t3 = 0;
                    t4 = ipp2 * t0;
                    t8 = 0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        int t5 = t3;
                        int t6 = t4;
                        int t10;
                        t8 = (t10 = t8 + (ido << 1));
                        for (int k = 0; k < l1; ++k) {
                            t7 = t5;
                            int t9 = t6;
                            int t11 = t10;
                            int t12 = t10;
                            for (int i = 2; i < ido; i += 2) {
                                t7 += 2;
                                t9 += 2;
                                t11 += 2;
                                t12 -= 2;
                                ch[t7 - 1] = cc[t11 - 1] + cc[t12 - 1];
                                ch[t9 - 1] = cc[t11 - 1] - cc[t12 - 1];
                                ch[t7] = cc[t11] - cc[t12];
                                ch[t9] = cc[t11] + cc[t12];
                            }
                            t5 += ido;
                            t6 += ido;
                            t10 += t2;
                        }
                    }
                    state = 116;
                    continue;
                }
                case 112: {
                    int t3 = 0;
                    int t4 = ipp2 * t0;
                    int t8 = 0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        int t5 = t3;
                        int t6 = t4;
                        int t10;
                        int t11;
                        t8 = (t11 = (t10 = t8 + (ido << 1)));
                        for (int i = 2; i < ido; i += 2) {
                            t5 += 2;
                            t6 += 2;
                            t10 += 2;
                            t11 -= 2;
                            int t7 = t5;
                            int t9 = t6;
                            int t12 = t10;
                            int t13 = t11;
                            for (int k = 0; k < l1; ++k) {
                                ch[t7 - 1] = cc[t12 - 1] + cc[t13 - 1];
                                ch[t9 - 1] = cc[t12 - 1] - cc[t13 - 1];
                                ch[t7] = cc[t12] - cc[t13];
                                ch[t9] = cc[t12] + cc[t13];
                                t7 += ido;
                                t9 += ido;
                                t12 += t2;
                                t13 += t2;
                            }
                        }
                    }
                }
                case 116: {
                    float ar1 = 1.0f;
                    float ai1 = 0.0f;
                    int t3 = 0;
                    final int t11;
                    int t4 = t11 = ipp2 * idl1;
                    int t5 = (ip - 1) * idl1;
                    for (int m = 1; m < ipph; ++m) {
                        t3 += idl1;
                        t4 -= idl1;
                        final float ar1h = dcp * ar1 - dsp * ai1;
                        ai1 = dcp * ai1 + dsp * ar1;
                        ar1 = ar1h;
                        int t6 = t3;
                        int t7 = t4;
                        int t9 = 0;
                        int t8 = idl1;
                        int t10 = t5;
                        for (int ik = 0; ik < idl1; ++ik) {
                            c2[t6++] = ch2[t9++] + ar1 * ch2[t8++];
                            c2[t7++] = ai1 * ch2[t10++];
                        }
                        final float dc2 = ar1;
                        final float ds2 = ai1;
                        float ar2 = ar1;
                        float ai2 = ai1;
                        t9 = idl1;
                        t8 = t11 - idl1;
                        for (int j = 2; j < ipph; ++j) {
                            t9 += idl1;
                            t8 -= idl1;
                            final float ar2h = dc2 * ar2 - ds2 * ai2;
                            ai2 = dc2 * ai2 + ds2 * ar2;
                            ar2 = ar2h;
                            t6 = t3;
                            t7 = t4;
                            int t12 = t9;
                            int t13 = t8;
                            for (int ik = 0; ik < idl1; ++ik) {
                                final int n = t6++;
                                c2[n] += ar2 * ch2[t12++];
                                final int n2 = t7++;
                                c2[n2] += ai2 * ch2[t13++];
                            }
                        }
                    }
                    t3 = 0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 = (t4 = t3 + idl1);
                        for (int ik = 0; ik < idl1; ++ik) {
                            final int n3 = ik;
                            ch2[n3] += ch2[t4++];
                        }
                    }
                    t3 = 0;
                    t4 = ipp2 * t0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        t5 = t3;
                        int t6 = t4;
                        for (int k = 0; k < l1; ++k) {
                            ch[t5] = c1[t5] - c1[t6];
                            ch[t6] = c1[t5] + c1[t6];
                            t5 += ido;
                            t6 += ido;
                        }
                    }
                    if (ido == 1) {
                        state = 132;
                        continue;
                    }
                    if (nbd < l1) {
                        state = 128;
                        continue;
                    }
                    t3 = 0;
                    t4 = ipp2 * t0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        t5 = t3;
                        int t6 = t4;
                        for (int k = 0; k < l1; ++k) {
                            int t7 = t5;
                            int t9 = t6;
                            for (int i = 2; i < ido; i += 2) {
                                t7 += 2;
                                t9 += 2;
                                ch[t7 - 1] = c1[t7 - 1] - c1[t9];
                                ch[t9 - 1] = c1[t7 - 1] + c1[t9];
                                ch[t7] = c1[t7] + c1[t9 - 1];
                                ch[t9] = c1[t7] - c1[t9 - 1];
                            }
                            t5 += ido;
                            t6 += ido;
                        }
                    }
                    state = 132;
                    continue;
                }
                case 128: {
                    int t3 = 0;
                    int t4 = ipp2 * t0;
                    for (int j = 1; j < ipph; ++j) {
                        t3 += t0;
                        t4 -= t0;
                        int t5 = t3;
                        int t6 = t4;
                        for (int i = 2; i < ido; i += 2) {
                            t5 += 2;
                            t6 += 2;
                            int t7 = t5;
                            int t9 = t6;
                            for (int k = 0; k < l1; ++k) {
                                ch[t7 - 1] = c1[t7 - 1] - c1[t9];
                                ch[t9 - 1] = c1[t7 - 1] + c1[t9];
                                ch[t7] = c1[t7] + c1[t9 - 1];
                                ch[t9] = c1[t7] - c1[t9 - 1];
                                t7 += ido;
                                t9 += ido;
                            }
                        }
                    }
                }
                case 132: {
                    if (ido == 1) {
                        return;
                    }
                    for (int ik = 0; ik < idl1; ++ik) {
                        c2[ik] = ch2[ik];
                    }
                    int t3 = 0;
                    for (int j = 1; j < ip; ++j) {
                        int t4;
                        t3 = (t4 = t3 + t0);
                        for (int k = 0; k < l1; ++k) {
                            c1[t4] = ch[t4];
                            t4 += ido;
                        }
                    }
                    if (nbd > l1) {
                        state = 139;
                        continue;
                    }
                    int is = -ido - 1;
                    t3 = 0;
                    for (int j = 1; j < ip; ++j) {
                        is += ido;
                        t3 += t0;
                        int idij = is;
                        int t4 = t3;
                        for (int i = 2; i < ido; i += 2) {
                            t4 += 2;
                            idij += 2;
                            int t5 = t4;
                            for (int k = 0; k < l1; ++k) {
                                c1[t5 - 1] = wa[index + idij - 1] * ch[t5 - 1] - wa[index + idij] * ch[t5];
                                c1[t5] = wa[index + idij - 1] * ch[t5] + wa[index + idij] * ch[t5 - 1];
                                t5 += ido;
                            }
                        }
                    }
                }
                case 139: {
                    int is = -ido - 1;
                    int t3 = 0;
                    for (int j = 1; j < ip; ++j) {
                        is += ido;
                        int t4;
                        t3 = (t4 = t3 + t0);
                        for (int k = 0; k < l1; ++k) {
                            int idij = is;
                            int t5 = t4;
                            for (int i = 2; i < ido; i += 2) {
                                idij += 2;
                                t5 += 2;
                                c1[t5 - 1] = wa[index + idij - 1] * ch[t5 - 1] - wa[index + idij] * ch[t5];
                                c1[t5] = wa[index + idij - 1] * ch[t5] + wa[index + idij] * ch[t5 - 1];
                            }
                            t4 += ido;
                        }
                    }
                }
            }
        }
    }
    
    static void drftb1(final int n, final float[] c, final float[] ch, final float[] wa, final int index, final int[] ifac) {
        int l2 = 0;
        int ip = 0;
        int ido = 0;
        int idl1 = 0;
        final int nf = ifac[1];
        int na = 0;
        int l3 = 1;
        int iw = 1;
        for (int k1 = 0; k1 < nf; ++k1) {
            int state = 100;
        Label_0462:
            while (true) {
                switch (state) {
                    case 100: {
                        ip = ifac[k1 + 2];
                        l2 = ip * l3;
                        ido = n / l2;
                        idl1 = ido * l3;
                        if (ip != 4) {
                            state = 103;
                            continue;
                        }
                        final int ix2 = iw + ido;
                        final int ix3 = ix2 + ido;
                        if (na != 0) {
                            dradb4(ido, l3, ch, c, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        }
                        else {
                            dradb4(ido, l3, c, ch, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue;
                    }
                    case 103: {
                        if (ip != 2) {
                            state = 106;
                            continue;
                        }
                        if (na != 0) {
                            dradb2(ido, l3, ch, c, wa, index + iw - 1);
                        }
                        else {
                            dradb2(ido, l3, c, ch, wa, index + iw - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue;
                    }
                    case 106: {
                        if (ip != 3) {
                            state = 109;
                            continue;
                        }
                        final int ix2 = iw + ido;
                        if (na != 0) {
                            dradb3(ido, l3, ch, c, wa, index + iw - 1, wa, index + ix2 - 1);
                        }
                        else {
                            dradb3(ido, l3, c, ch, wa, index + iw - 1, wa, index + ix2 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue;
                    }
                    case 109: {
                        if (na != 0) {
                            dradbg(ido, ip, l3, idl1, ch, ch, ch, c, c, wa, index + iw - 1);
                        }
                        else {
                            dradbg(ido, ip, l3, idl1, c, c, c, ch, ch, wa, index + iw - 1);
                        }
                        if (ido == 1) {
                            na = 1 - na;
                            break Label_0462;
                        }
                        break Label_0462;
                    }
                    case 115: {
                        break Label_0462;
                    }
                }
            }
            l3 = l2;
            iw += (ip - 1) * ido;
        }
        if (na == 0) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            c[i] = ch[i];
        }
    }
    
    static {
        Drft.ntryh = new int[] { 4, 2, 3, 5 };
        Drft.tpi = 6.2831855f;
        Drft.hsqt2 = 0.70710677f;
        Drft.taui = 0.8660254f;
        Drft.taur = -0.5f;
        Drft.sqrt2 = 1.4142135f;
    }
}
