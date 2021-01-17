// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook
{
    int dim;
    int entries;
    StaticCodeBook c;
    float[] valuelist;
    int[] codelist;
    DecodeAux decode_tree;
    private int[] t;
    
    CodeBook() {
        this.c = new StaticCodeBook();
        this.t = new int[15];
    }
    
    int encode(final int a, final Buffer b) {
        b.write(this.codelist[a], this.c.lengthlist[a]);
        return this.c.lengthlist[a];
    }
    
    int errorv(final float[] a) {
        final int best = this.best(a, 1);
        for (int k = 0; k < this.dim; ++k) {
            a[k] = this.valuelist[best * this.dim + k];
        }
        return best;
    }
    
    int encodev(final int best, final float[] a, final Buffer b) {
        for (int k = 0; k < this.dim; ++k) {
            a[k] = this.valuelist[best * this.dim + k];
        }
        return this.encode(best, b);
    }
    
    int encodevs(final float[] a, final Buffer b, final int step, final int addmul) {
        final int best = this.besterror(a, step, addmul);
        return this.encode(best, b);
    }
    
    synchronized int decodevs_add(final float[] a, final int offset, final Buffer b, final int n) {
        final int step = n / this.dim;
        if (this.t.length < step) {
            this.t = new int[step];
        }
        for (int i = 0; i < step; ++i) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            this.t[i] = entry * this.dim;
        }
        for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
            for (int j = 0; j < step; ++j) {
                final int n2 = offset + o + j;
                a[n2] += this.valuelist[this.t[j] + i];
            }
        }
        return 0;
    }
    
    int decodev_add(final float[] a, final int offset, final Buffer b, final int n) {
        if (this.dim > 8) {
            int i = 0;
            while (i < n) {
                final int entry = this.decode(b);
                if (entry == -1) {
                    return -1;
                }
                int n2;
                for (int t = entry * this.dim, j = 0; j < this.dim; a[n2] += this.valuelist[t + j++]) {
                    n2 = offset + i++;
                }
            }
        }
        else {
            int i = 0;
            while (i < n) {
                final int entry = this.decode(b);
                if (entry == -1) {
                    return -1;
                }
                final int t = entry * this.dim;
                int j = 0;
                switch (this.dim) {
                    case 8: {
                        final int n3 = offset + i++;
                        a[n3] += this.valuelist[t + j++];
                    }
                    case 7: {
                        final int n4 = offset + i++;
                        a[n4] += this.valuelist[t + j++];
                    }
                    case 6: {
                        final int n5 = offset + i++;
                        a[n5] += this.valuelist[t + j++];
                    }
                    case 5: {
                        final int n6 = offset + i++;
                        a[n6] += this.valuelist[t + j++];
                    }
                    case 4: {
                        final int n7 = offset + i++;
                        a[n7] += this.valuelist[t + j++];
                    }
                    case 3: {
                        final int n8 = offset + i++;
                        a[n8] += this.valuelist[t + j++];
                    }
                    case 2: {
                        final int n9 = offset + i++;
                        a[n9] += this.valuelist[t + j++];
                    }
                    case 1: {
                        final int n10 = offset + i++;
                        a[n10] += this.valuelist[t + j++];
                        continue;
                    }
                }
            }
        }
        return 0;
    }
    
    int decodev_set(final float[] a, final int offset, final Buffer b, final int n) {
        int i = 0;
        while (i < n) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            for (int t = entry * this.dim, j = 0; j < this.dim; a[offset + i++] = this.valuelist[t + j++]) {}
        }
        return 0;
    }
    
    int decodevv_add(final float[][] a, final int offset, final int ch, final Buffer b, final int n) {
        int chptr = 0;
        int i = offset / ch;
        while (i < (offset + n) / ch) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            final int t = entry * this.dim;
            for (int j = 0; j < this.dim; ++j) {
                final float[] array = a[chptr++];
                final int n2 = i;
                array[n2] += this.valuelist[t + j];
                if (chptr == ch) {
                    chptr = 0;
                    ++i;
                }
            }
        }
        return 0;
    }
    
    int decode(final Buffer b) {
        int ptr = 0;
        final DecodeAux t = this.decode_tree;
        final int lok = b.look(t.tabn);
        if (lok >= 0) {
            ptr = t.tab[lok];
            b.adv(t.tabl[lok]);
            if (ptr <= 0) {
                return -ptr;
            }
        }
        do {
            switch (b.read1()) {
                case 0: {
                    ptr = t.ptr0[ptr];
                    continue;
                }
                case 1: {
                    ptr = t.ptr1[ptr];
                    continue;
                }
                default: {
                    return -1;
                }
            }
        } while (ptr > 0);
        return -ptr;
    }
    
    int decodevs(final float[] a, final int index, final Buffer b, final int step, final int addmul) {
        final int entry = this.decode(b);
        if (entry == -1) {
            return -1;
        }
        switch (addmul) {
            case -1: {
                for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
                    a[index + o] = this.valuelist[entry * this.dim + i];
                }
                break;
            }
            case 0: {
                for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
                    final int n = index + o;
                    a[n] += this.valuelist[entry * this.dim + i];
                }
                break;
            }
            case 1: {
                for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
                    final int n2 = index + o;
                    a[n2] *= this.valuelist[entry * this.dim + i];
                }
                break;
            }
        }
        return entry;
    }
    
    int best(final float[] a, final int step) {
        int besti = -1;
        float best = 0.0f;
        int e = 0;
        for (int i = 0; i < this.entries; ++i) {
            if (this.c.lengthlist[i] > 0) {
                final float _this = dist(this.dim, this.valuelist, e, a, step);
                if (besti == -1 || _this < best) {
                    best = _this;
                    besti = i;
                }
            }
            e += this.dim;
        }
        return besti;
    }
    
    int besterror(final float[] a, final int step, final int addmul) {
        final int best = this.best(a, step);
        switch (addmul) {
            case 0: {
                for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
                    final int n = o;
                    a[n] -= this.valuelist[best * this.dim + i];
                }
                break;
            }
            case 1: {
                for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
                    final float val = this.valuelist[best * this.dim + i];
                    if (val == 0.0f) {
                        a[o] = 0.0f;
                    }
                    else {
                        final int n2 = o;
                        a[n2] /= val;
                    }
                }
                break;
            }
        }
        return best;
    }
    
    void clear() {
    }
    
    private static float dist(final int el, final float[] ref, final int index, final float[] b, final int step) {
        float acc = 0.0f;
        for (int i = 0; i < el; ++i) {
            final float val = ref[index + i] - b[i * step];
            acc += val * val;
        }
        return acc;
    }
    
    int init_decode(final StaticCodeBook s) {
        this.c = s;
        this.entries = s.entries;
        this.dim = s.dim;
        this.valuelist = s.unquantize();
        this.decode_tree = this.make_decode_tree();
        if (this.decode_tree == null) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    static int[] make_words(final int[] l, final int n) {
        final int[] marker = new int[33];
        final int[] r = new int[n];
        for (int i = 0; i < n; ++i) {
            final int length = l[i];
            if (length > 0) {
                int entry = marker[length];
                if (length < 32 && entry >>> length != 0) {
                    return null;
                }
                r[i] = entry;
                int j = length;
                while (j > 0) {
                    if ((marker[j] & 0x1) != 0x0) {
                        if (j == 1) {
                            final int[] array = marker;
                            final int n2 = 1;
                            ++array[n2];
                            break;
                        }
                        marker[j] = marker[j - 1] << 1;
                        break;
                    }
                    else {
                        final int[] array2 = marker;
                        final int n3 = j;
                        ++array2[n3];
                        --j;
                    }
                }
                for (j = length + 1; j < 33 && marker[j] >>> 1 == entry; entry = marker[j], marker[j] = marker[j - 1] << 1, ++j) {}
            }
        }
        for (int i = 0; i < n; ++i) {
            int temp = 0;
            for (int k = 0; k < l[i]; ++k) {
                temp <<= 1;
                temp |= (r[i] >>> k & 0x1);
            }
            r[i] = temp;
        }
        return r;
    }
    
    DecodeAux make_decode_tree() {
        int top = 0;
        final DecodeAux decodeAux;
        final DecodeAux t = decodeAux = new DecodeAux();
        final int[] ptr4 = new int[this.entries * 2];
        decodeAux.ptr0 = ptr4;
        final int[] ptr0 = ptr4;
        final DecodeAux decodeAux2 = t;
        final int[] ptr5 = new int[this.entries * 2];
        decodeAux2.ptr1 = ptr5;
        final int[] ptr2 = ptr5;
        final int[] codelist = make_words(this.c.lengthlist, this.c.entries);
        if (codelist == null) {
            return null;
        }
        t.aux = this.entries * 2;
        for (int i = 0; i < this.entries; ++i) {
            if (this.c.lengthlist[i] > 0) {
                int ptr3 = 0;
                int j;
                for (j = 0; j < this.c.lengthlist[i] - 1; ++j) {
                    final int bit = codelist[i] >>> j & 0x1;
                    if (bit == 0) {
                        if (ptr0[ptr3] == 0) {
                            ptr0[ptr3] = ++top;
                        }
                        ptr3 = ptr0[ptr3];
                    }
                    else {
                        if (ptr2[ptr3] == 0) {
                            ptr2[ptr3] = ++top;
                        }
                        ptr3 = ptr2[ptr3];
                    }
                }
                if ((codelist[i] >>> j & 0x1) == 0x0) {
                    ptr0[ptr3] = -i;
                }
                else {
                    ptr2[ptr3] = -i;
                }
            }
        }
        t.tabn = Util.ilog(this.entries) - 4;
        if (t.tabn < 5) {
            t.tabn = 5;
        }
        final int n = 1 << t.tabn;
        t.tab = new int[n];
        t.tabl = new int[n];
        for (int k = 0; k < n; ++k) {
            int p;
            int l;
            for (p = 0, l = 0, l = 0; l < t.tabn && (p > 0 || l == 0); ++l) {
                if ((k & 1 << l) != 0x0) {
                    p = ptr2[p];
                }
                else {
                    p = ptr0[p];
                }
            }
            t.tab[k] = p;
            t.tabl[k] = l;
        }
        return t;
    }
    
    class DecodeAux
    {
        int[] tab;
        int[] tabl;
        int tabn;
        int[] ptr0;
        int[] ptr1;
        int aux;
    }
}
