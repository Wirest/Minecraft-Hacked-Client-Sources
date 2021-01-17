// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.bzip2;

import java.util.BitSet;

class BlockSort
{
    private static final int QSORT_STACK_SIZE = 1000;
    private static final int FALLBACK_QSORT_STACK_SIZE = 100;
    private static final int STACK_SIZE = 1000;
    private int workDone;
    private int workLimit;
    private boolean firstAttempt;
    private final int[] stack_ll;
    private final int[] stack_hh;
    private final int[] stack_dd;
    private final int[] mainSort_runningOrder;
    private final int[] mainSort_copy;
    private final boolean[] mainSort_bigDone;
    private final int[] ftab;
    private final char[] quadrant;
    private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
    private int[] eclass;
    private static final int[] INCS;
    private static final int SMALL_THRESH = 20;
    private static final int DEPTH_THRESH = 10;
    private static final int WORK_FACTOR = 30;
    private static final int SETMASK = 2097152;
    private static final int CLEARMASK = -2097153;
    
    BlockSort(final BZip2CompressorOutputStream.Data data) {
        this.stack_ll = new int[1000];
        this.stack_hh = new int[1000];
        this.stack_dd = new int[1000];
        this.mainSort_runningOrder = new int[256];
        this.mainSort_copy = new int[256];
        this.mainSort_bigDone = new boolean[256];
        this.ftab = new int[65537];
        this.quadrant = data.sfmap;
    }
    
    void blockSort(final BZip2CompressorOutputStream.Data data, final int last) {
        this.workLimit = 30 * last;
        this.workDone = 0;
        this.firstAttempt = true;
        if (last + 1 < 10000) {
            this.fallbackSort(data, last);
        }
        else {
            this.mainSort(data, last);
            if (this.firstAttempt && this.workDone > this.workLimit) {
                this.fallbackSort(data, last);
            }
        }
        final int[] fmap = data.fmap;
        data.origPtr = -1;
        for (int i = 0; i <= last; ++i) {
            if (fmap[i] == 0) {
                data.origPtr = i;
                break;
            }
        }
    }
    
    final void fallbackSort(final BZip2CompressorOutputStream.Data data, final int last) {
        data.block[0] = data.block[last + 1];
        this.fallbackSort(data.fmap, data.block, last + 1);
        for (int i = 0; i < last + 1; ++i) {
            final int[] fmap = data.fmap;
            final int n = i;
            --fmap[n];
        }
        for (int i = 0; i < last + 1; ++i) {
            if (data.fmap[i] == -1) {
                data.fmap[i] = last;
                break;
            }
        }
    }
    
    private void fallbackSimpleSort(final int[] fmap, final int[] eclass, final int lo, final int hi) {
        if (lo == hi) {
            return;
        }
        if (hi - lo > 3) {
            for (int i = hi - 4; i >= lo; --i) {
                final int tmp = fmap[i];
                int ec_tmp;
                int j;
                for (ec_tmp = eclass[tmp], j = i + 4; j <= hi && ec_tmp > eclass[fmap[j]]; j += 4) {
                    fmap[j - 4] = fmap[j];
                }
                fmap[j - 4] = tmp;
            }
        }
        for (int i = hi - 1; i >= lo; --i) {
            final int tmp = fmap[i];
            int ec_tmp;
            int j;
            for (ec_tmp = eclass[tmp], j = i + 1; j <= hi && ec_tmp > eclass[fmap[j]]; ++j) {
                fmap[j - 1] = fmap[j];
            }
            fmap[j - 1] = tmp;
        }
    }
    
    private void fswap(final int[] fmap, final int zz1, final int zz2) {
        final int zztmp = fmap[zz1];
        fmap[zz1] = fmap[zz2];
        fmap[zz2] = zztmp;
    }
    
    private void fvswap(final int[] fmap, int yyp1, int yyp2, int yyn) {
        while (yyn > 0) {
            this.fswap(fmap, yyp1, yyp2);
            ++yyp1;
            ++yyp2;
            --yyn;
        }
    }
    
    private int fmin(final int a, final int b) {
        return (a < b) ? a : b;
    }
    
    private void fpush(final int sp, final int lz, final int hz) {
        this.stack_ll[sp] = lz;
        this.stack_hh[sp] = hz;
    }
    
    private int[] fpop(final int sp) {
        return new int[] { this.stack_ll[sp], this.stack_hh[sp] };
    }
    
    private void fallbackQSort3(final int[] fmap, final int[] eclass, final int loSt, final int hiSt) {
        long r = 0L;
        int sp = 0;
        this.fpush(sp++, loSt, hiSt);
        while (sp > 0) {
            final int[] s = this.fpop(--sp);
            final int lo = s[0];
            final int hi = s[1];
            if (hi - lo < 10) {
                this.fallbackSimpleSort(fmap, eclass, lo, hi);
            }
            else {
                r = (r * 7621L + 1L) % 32768L;
                final long r2 = r % 3L;
                long med;
                if (r2 == 0L) {
                    med = eclass[fmap[lo]];
                }
                else if (r2 == 1L) {
                    med = eclass[fmap[lo + hi >>> 1]];
                }
                else {
                    med = eclass[fmap[hi]];
                }
                int unLo;
                int ltLo = unLo = lo;
                int unHi;
                int gtHi = unHi = hi;
            Label_0275_Outer:
                while (true) {
                    if (unLo <= unHi) {
                        final int n = eclass[fmap[unLo]] - (int)med;
                        if (n == 0) {
                            this.fswap(fmap, unLo, ltLo);
                            ++ltLo;
                            ++unLo;
                            continue Label_0275_Outer;
                        }
                        if (n <= 0) {
                            ++unLo;
                            continue Label_0275_Outer;
                        }
                    }
                    while (true) {
                        while (unLo <= unHi) {
                            final int n = eclass[fmap[unHi]] - (int)med;
                            if (n == 0) {
                                this.fswap(fmap, unHi, gtHi);
                                --gtHi;
                                --unHi;
                            }
                            else if (n < 0) {
                                if (unLo > unHi) {
                                    break Label_0275_Outer;
                                }
                                this.fswap(fmap, unLo, unHi);
                                ++unLo;
                                --unHi;
                                continue Label_0275_Outer;
                            }
                            else {
                                --unHi;
                            }
                        }
                        continue;
                    }
                }
                if (gtHi < ltLo) {
                    continue;
                }
                int n = this.fmin(ltLo - lo, unLo - ltLo);
                this.fvswap(fmap, lo, unLo - n, n);
                int m = this.fmin(hi - gtHi, gtHi - unHi);
                this.fvswap(fmap, unHi + 1, hi - m + 1, m);
                n = lo + unLo - ltLo - 1;
                m = hi - (gtHi - unHi) + 1;
                if (n - lo > hi - m) {
                    this.fpush(sp++, lo, n);
                    this.fpush(sp++, m, hi);
                }
                else {
                    this.fpush(sp++, m, hi);
                    this.fpush(sp++, lo, n);
                }
            }
        }
    }
    
    private int[] getEclass() {
        return (this.eclass == null) ? (this.eclass = new int[this.quadrant.length / 2]) : this.eclass;
    }
    
    final void fallbackSort(final int[] fmap, final byte[] block, final int nblock) {
        final int[] ftab = new int[257];
        final int[] eclass = this.getEclass();
        for (int i = 0; i < nblock; ++i) {
            eclass[i] = 0;
        }
        for (int i = 0; i < nblock; ++i) {
            final int[] array = ftab;
            final int n = block[i] & 0xFF;
            ++array[n];
        }
        for (int i = 1; i < 257; ++i) {
            final int[] array2 = ftab;
            final int n2 = i;
            array2[n2] += ftab[i - 1];
        }
        for (int i = 0; i < nblock; ++i) {
            final int j = block[i] & 0xFF;
            final int k = ftab[j] - 1;
            fmap[ftab[j] = k] = i;
        }
        final int nBhtab = 64 + nblock;
        final BitSet bhtab = new BitSet(nBhtab);
        for (int i = 0; i < 256; ++i) {
            bhtab.set(ftab[i]);
        }
        for (int i = 0; i < 32; ++i) {
            bhtab.set(nblock + 2 * i);
            bhtab.clear(nblock + 2 * i + 1);
        }
        int H = 1;
        int nNotDone;
        do {
            int j = 0;
            for (int i = 0; i < nblock; ++i) {
                if (bhtab.get(i)) {
                    j = i;
                }
                int k = fmap[i] - H;
                if (k < 0) {
                    k += nblock;
                }
                eclass[k] = j;
            }
            nNotDone = 0;
            int r = -1;
            while (true) {
                int k = r + 1;
                k = bhtab.nextClearBit(k);
                final int l = k - 1;
                if (l >= nblock) {
                    break;
                }
                k = bhtab.nextSetBit(k + 1);
                r = k - 1;
                if (r >= nblock) {
                    break;
                }
                if (r <= l) {
                    continue;
                }
                nNotDone += r - l + 1;
                this.fallbackQSort3(fmap, eclass, l, r);
                int cc = -1;
                for (int i = l; i <= r; ++i) {
                    final int cc2 = eclass[fmap[i]];
                    if (cc != cc2) {
                        bhtab.set(i);
                        cc = cc2;
                    }
                }
            }
            H *= 2;
        } while (H <= nblock && nNotDone != 0);
    }
    
    private boolean mainSimpleSort(final BZip2CompressorOutputStream.Data dataShadow, final int lo, final int hi, final int d, final int lastShadow) {
        final int bigN = hi - lo + 1;
        if (bigN < 2) {
            return this.firstAttempt && this.workDone > this.workLimit;
        }
        int hp;
        for (hp = 0; BlockSort.INCS[hp] < bigN; ++hp) {}
        final int[] fmap = dataShadow.fmap;
        final char[] quadrant = this.quadrant;
        final byte[] block = dataShadow.block;
        final int lastPlus1 = lastShadow + 1;
        final boolean firstAttemptShadow = this.firstAttempt;
        final int workLimitShadow = this.workLimit;
        int workDoneShadow = this.workDone;
    Label_0905:
        while (--hp >= 0) {
            final int h = BlockSort.INCS[hp];
            final int mj = lo + h - 1;
            int i = lo + h;
            while (i <= hi) {
                for (int k = 3; i <= hi && --k >= 0; ++i) {
                    final int v = fmap[i];
                    final int vd = v + d;
                    int j = i;
                    boolean onceRunned = false;
                    int a = 0;
                Label_0176:
                    while (true) {
                        if (onceRunned) {
                            fmap[j] = a;
                            if ((j -= h) <= mj) {
                                break;
                            }
                        }
                        else {
                            onceRunned = true;
                        }
                        a = fmap[j - h];
                        int i2 = a + d;
                        int i3 = vd;
                        if (block[i2 + 1] == block[i3 + 1]) {
                            if (block[i2 + 2] == block[i3 + 2]) {
                                if (block[i2 + 3] == block[i3 + 3]) {
                                    if (block[i2 + 4] == block[i3 + 4]) {
                                        if (block[i2 + 5] == block[i3 + 5]) {
                                            final byte[] array = block;
                                            i2 += 6;
                                            final byte b = array[i2];
                                            final byte[] array2 = block;
                                            i3 += 6;
                                            if (b == array2[i3]) {
                                                int x = lastShadow;
                                                while (x > 0) {
                                                    x -= 4;
                                                    if (block[i2 + 1] == block[i3 + 1]) {
                                                        if (quadrant[i2] == quadrant[i3]) {
                                                            if (block[i2 + 2] == block[i3 + 2]) {
                                                                if (quadrant[i2 + 1] == quadrant[i3 + 1]) {
                                                                    if (block[i2 + 3] == block[i3 + 3]) {
                                                                        if (quadrant[i2 + 2] == quadrant[i3 + 2]) {
                                                                            if (block[i2 + 4] == block[i3 + 4]) {
                                                                                if (quadrant[i2 + 3] == quadrant[i3 + 3]) {
                                                                                    i2 += 4;
                                                                                    if (i2 >= lastPlus1) {
                                                                                        i2 -= lastPlus1;
                                                                                    }
                                                                                    i3 += 4;
                                                                                    if (i3 >= lastPlus1) {
                                                                                        i3 -= lastPlus1;
                                                                                    }
                                                                                    ++workDoneShadow;
                                                                                }
                                                                                else {
                                                                                    if (quadrant[i2 + 3] > quadrant[i3 + 3]) {
                                                                                        continue Label_0176;
                                                                                    }
                                                                                    break;
                                                                                }
                                                                            }
                                                                            else {
                                                                                if ((block[i2 + 4] & 0xFF) > (block[i3 + 4] & 0xFF)) {
                                                                                    continue Label_0176;
                                                                                }
                                                                                break;
                                                                            }
                                                                        }
                                                                        else {
                                                                            if (quadrant[i2 + 2] > quadrant[i3 + 2]) {
                                                                                continue Label_0176;
                                                                            }
                                                                            break;
                                                                        }
                                                                    }
                                                                    else {
                                                                        if ((block[i2 + 3] & 0xFF) > (block[i3 + 3] & 0xFF)) {
                                                                            continue Label_0176;
                                                                        }
                                                                        break;
                                                                    }
                                                                }
                                                                else {
                                                                    if (quadrant[i2 + 1] > quadrant[i3 + 1]) {
                                                                        continue Label_0176;
                                                                    }
                                                                    break;
                                                                }
                                                            }
                                                            else {
                                                                if ((block[i2 + 2] & 0xFF) > (block[i3 + 2] & 0xFF)) {
                                                                    continue Label_0176;
                                                                }
                                                                break;
                                                            }
                                                        }
                                                        else {
                                                            if (quadrant[i2] > quadrant[i3]) {
                                                                continue Label_0176;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        if ((block[i2 + 1] & 0xFF) > (block[i3 + 1] & 0xFF)) {
                                                            continue Label_0176;
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                            if ((block[i2] & 0xFF) > (block[i3] & 0xFF)) {
                                                continue;
                                            }
                                            break;
                                        }
                                        else {
                                            if ((block[i2 + 5] & 0xFF) > (block[i3 + 5] & 0xFF)) {
                                                continue;
                                            }
                                            break;
                                        }
                                    }
                                    else {
                                        if ((block[i2 + 4] & 0xFF) > (block[i3 + 4] & 0xFF)) {
                                            continue;
                                        }
                                        break;
                                    }
                                }
                                else {
                                    if ((block[i2 + 3] & 0xFF) > (block[i3 + 3] & 0xFF)) {
                                        continue;
                                    }
                                    break;
                                }
                            }
                            else {
                                if ((block[i2 + 2] & 0xFF) > (block[i3 + 2] & 0xFF)) {
                                    continue;
                                }
                                break;
                            }
                        }
                        else {
                            if ((block[i2 + 1] & 0xFF) > (block[i3 + 1] & 0xFF)) {
                                continue;
                            }
                            break;
                        }
                    }
                    fmap[j] = v;
                }
                if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow) {
                    break Label_0905;
                }
            }
        }
        this.workDone = workDoneShadow;
        return firstAttemptShadow && workDoneShadow > workLimitShadow;
    }
    
    private static void vswap(final int[] fmap, int p1, int p2, int n) {
        int t;
        for (n += p1; p1 < n; fmap[p1++] = fmap[p2], fmap[p2++] = t) {
            t = fmap[p1];
        }
    }
    
    private static byte med3(final byte a, final byte b, final byte c) {
        return (a < b) ? ((b < c) ? b : ((a < c) ? c : a)) : ((b > c) ? b : ((a > c) ? c : a));
    }
    
    private void mainQSort3(final BZip2CompressorOutputStream.Data dataShadow, final int loSt, final int hiSt, final int dSt, final int last) {
        final int[] stack_ll = this.stack_ll;
        final int[] stack_hh = this.stack_hh;
        final int[] stack_dd = this.stack_dd;
        final int[] fmap = dataShadow.fmap;
        final byte[] block = dataShadow.block;
        stack_ll[0] = loSt;
        stack_hh[0] = hiSt;
        stack_dd[0] = dSt;
        int sp = 1;
        while (--sp >= 0) {
            final int lo = stack_ll[sp];
            final int hi = stack_hh[sp];
            final int d = stack_dd[sp];
            if (hi - lo < 20 || d > 10) {
                if (this.mainSimpleSort(dataShadow, lo, hi, d, last)) {
                    return;
                }
                continue;
            }
            else {
                final int d2 = d + 1;
                final int med = med3(block[fmap[lo] + d2], block[fmap[hi] + d2], block[fmap[lo + hi >>> 1] + d2]) & 0xFF;
                int unLo = lo;
                int unHi = hi;
                int ltLo = lo;
                int gtHi = hi;
                while (true) {
                    Label_0257: {
                        if (unLo <= unHi) {
                            final int n = (block[fmap[unLo] + d2] & 0xFF) - med;
                            if (n == 0) {
                                final int temp = fmap[unLo];
                                fmap[unLo++] = fmap[ltLo];
                                fmap[ltLo++] = temp;
                            }
                            else {
                                if (n >= 0) {
                                    break Label_0257;
                                }
                                ++unLo;
                            }
                            continue;
                        }
                    }
                    while (unLo <= unHi) {
                        final int n = (block[fmap[unHi] + d2] & 0xFF) - med;
                        if (n == 0) {
                            final int temp = fmap[unHi];
                            fmap[unHi--] = fmap[gtHi];
                            fmap[gtHi--] = temp;
                        }
                        else {
                            if (n <= 0) {
                                break;
                            }
                            --unHi;
                        }
                    }
                    if (unLo > unHi) {
                        break;
                    }
                    final int temp2 = fmap[unLo];
                    fmap[unLo++] = fmap[unHi];
                    fmap[unHi--] = temp2;
                }
                if (gtHi < ltLo) {
                    stack_ll[sp] = lo;
                    stack_hh[sp] = hi;
                    stack_dd[sp] = d2;
                    ++sp;
                }
                else {
                    int n = (ltLo - lo < unLo - ltLo) ? (ltLo - lo) : (unLo - ltLo);
                    vswap(fmap, lo, unLo - n, n);
                    int m = (hi - gtHi < gtHi - unHi) ? (hi - gtHi) : (gtHi - unHi);
                    vswap(fmap, unLo, hi - m + 1, m);
                    n = lo + unLo - ltLo - 1;
                    m = hi - (gtHi - unHi) + 1;
                    stack_ll[sp] = lo;
                    stack_hh[sp] = n;
                    stack_dd[sp] = d;
                    ++sp;
                    stack_ll[sp] = n + 1;
                    stack_hh[sp] = m - 1;
                    stack_dd[sp] = d2;
                    ++sp;
                    stack_ll[sp] = m;
                    stack_hh[sp] = hi;
                    stack_dd[sp] = d;
                    ++sp;
                }
            }
        }
    }
    
    final void mainSort(final BZip2CompressorOutputStream.Data dataShadow, final int lastShadow) {
        final int[] runningOrder = this.mainSort_runningOrder;
        final int[] copy = this.mainSort_copy;
        final boolean[] bigDone = this.mainSort_bigDone;
        final int[] ftab = this.ftab;
        final byte[] block = dataShadow.block;
        final int[] fmap = dataShadow.fmap;
        final char[] quadrant = this.quadrant;
        final int workLimitShadow = this.workLimit;
        final boolean firstAttemptShadow = this.firstAttempt;
        int i = 65537;
        while (--i >= 0) {
            ftab[i] = 0;
        }
        for (i = 0; i < 20; ++i) {
            block[lastShadow + i + 2] = block[i % (lastShadow + 1) + 1];
        }
        i = lastShadow + 20 + 1;
        while (--i >= 0) {
            quadrant[i] = '\0';
        }
        block[0] = block[lastShadow + 1];
        int c1 = block[0] & 0xFF;
        for (int j = 0; j <= lastShadow; ++j) {
            final int c2 = block[j + 1] & 0xFF;
            final int[] array = ftab;
            final int n = (c1 << 8) + c2;
            ++array[n];
            c1 = c2;
        }
        for (int j = 1; j <= 65536; ++j) {
            final int[] array2 = ftab;
            final int n2 = j;
            array2[n2] += ftab[j - 1];
        }
        c1 = (block[1] & 0xFF);
        for (int j = 0; j < lastShadow; ++j) {
            final int c2 = block[j + 2] & 0xFF;
            final int[] array3 = fmap;
            final int[] array4 = ftab;
            final int n3 = (c1 << 8) + c2;
            array3[--array4[n3]] = j;
            c1 = c2;
        }
        final int[] array5 = fmap;
        final int[] array6 = ftab;
        final int n4 = ((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF);
        array5[--array6[n4]] = lastShadow;
        int j = 256;
        while (--j >= 0) {
            bigDone[j] = false;
            runningOrder[j] = j;
        }
        int h = 364;
        while (h != 1) {
            int k;
            for (h = (k = h / 3); k <= 255; ++k) {
                final int vv = runningOrder[k];
                final int a = ftab[vv + 1 << 8] - ftab[vv << 8];
                final int b = h - 1;
                int l = k;
                for (int ro = runningOrder[l - h]; ftab[ro + 1 << 8] - ftab[ro << 8] > a; ro = runningOrder[l - h]) {
                    runningOrder[l] = ro;
                    l -= h;
                    if (l <= b) {
                        break;
                    }
                }
                runningOrder[l] = vv;
            }
        }
        for (j = 0; j <= 255; ++j) {
            final int ss = runningOrder[j];
            for (int m = 0; m <= 255; ++m) {
                final int sb = (ss << 8) + m;
                final int ftab_sb = ftab[sb];
                if ((ftab_sb & 0x200000) != 0x200000) {
                    final int lo = ftab_sb & 0xFFDFFFFF;
                    final int hi = (ftab[sb + 1] & 0xFFDFFFFF) - 1;
                    if (hi > lo) {
                        this.mainQSort3(dataShadow, lo, hi, 2, lastShadow);
                        if (firstAttemptShadow && this.workDone > workLimitShadow) {
                            return;
                        }
                    }
                    ftab[sb] = (ftab_sb | 0x200000);
                }
            }
            for (int m = 0; m <= 255; ++m) {
                copy[m] = (ftab[(m << 8) + ss] & 0xFFDFFFFF);
            }
            for (int m = ftab[ss << 8] & 0xFFDFFFFF, hj = ftab[ss + 1 << 8] & 0xFFDFFFFF; m < hj; ++m) {
                final int fmap_j = fmap[m];
                c1 = (block[fmap_j] & 0xFF);
                if (!bigDone[c1]) {
                    fmap[copy[c1]] = ((fmap_j == 0) ? lastShadow : (fmap_j - 1));
                    final int[] array7 = copy;
                    final int n5 = c1;
                    ++array7[n5];
                }
            }
            int m = 256;
            while (--m >= 0) {
                final int[] array8 = ftab;
                final int n6 = (m << 8) + ss;
                array8[n6] |= 0x200000;
            }
            bigDone[ss] = true;
            if (j < 255) {
                final int bbStart = ftab[ss << 8] & 0xFFDFFFFF;
                int bbSize;
                int shifts;
                for (bbSize = (ftab[ss + 1 << 8] & 0xFFDFFFFF) - bbStart, shifts = 0; bbSize >> shifts > 65534; ++shifts) {}
                for (int l = 0; l < bbSize; ++l) {
                    final int a2update = fmap[bbStart + l];
                    final char qVal = (char)(l >> shifts);
                    quadrant[a2update] = qVal;
                    if (a2update < 20) {
                        quadrant[a2update + lastShadow + 1] = qVal;
                    }
                }
            }
        }
    }
    
    static {
        INCS = new int[] { 1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484 };
    }
}
