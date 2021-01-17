// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class StaticCodeBook
{
    int dim;
    int entries;
    int[] lengthlist;
    int maptype;
    int q_min;
    int q_delta;
    int q_quant;
    int q_sequencep;
    int[] quantlist;
    static final int VQ_FEXP = 10;
    static final int VQ_FMAN = 21;
    static final int VQ_FEXP_BIAS = 768;
    
    int pack(final Buffer opb) {
        boolean ordered = false;
        opb.write(5653314, 24);
        opb.write(this.dim, 16);
        opb.write(this.entries, 24);
        int i;
        for (i = 1; i < this.entries && this.lengthlist[i] >= this.lengthlist[i - 1]; ++i) {}
        if (i == this.entries) {
            ordered = true;
        }
        if (ordered) {
            int count = 0;
            opb.write(1, 1);
            opb.write(this.lengthlist[0] - 1, 5);
            for (i = 1; i < this.entries; ++i) {
                final int _this = this.lengthlist[i];
                final int _last = this.lengthlist[i - 1];
                if (_this > _last) {
                    for (int j = _last; j < _this; ++j) {
                        opb.write(i - count, Util.ilog(this.entries - count));
                        count = i;
                    }
                }
            }
            opb.write(i - count, Util.ilog(this.entries - count));
        }
        else {
            opb.write(0, 1);
            for (i = 0; i < this.entries && this.lengthlist[i] != 0; ++i) {}
            if (i == this.entries) {
                opb.write(0, 1);
                for (i = 0; i < this.entries; ++i) {
                    opb.write(this.lengthlist[i] - 1, 5);
                }
            }
            else {
                opb.write(1, 1);
                for (i = 0; i < this.entries; ++i) {
                    if (this.lengthlist[i] == 0) {
                        opb.write(0, 1);
                    }
                    else {
                        opb.write(1, 1);
                        opb.write(this.lengthlist[i] - 1, 5);
                    }
                }
            }
        }
        opb.write(this.maptype, 4);
        switch (this.maptype) {
            case 0: {
                break;
            }
            case 1:
            case 2: {
                if (this.quantlist == null) {
                    return -1;
                }
                opb.write(this.q_min, 32);
                opb.write(this.q_delta, 32);
                opb.write(this.q_quant - 1, 4);
                opb.write(this.q_sequencep, 1);
                int quantvals = 0;
                switch (this.maptype) {
                    case 1: {
                        quantvals = this.maptype1_quantvals();
                        break;
                    }
                    case 2: {
                        quantvals = this.entries * this.dim;
                        break;
                    }
                }
                for (i = 0; i < quantvals; ++i) {
                    opb.write(Math.abs(this.quantlist[i]), this.q_quant);
                }
                break;
            }
            default: {
                return -1;
            }
        }
        return 0;
    }
    
    int unpack(final Buffer opb) {
        if (opb.read(24) != 5653314) {
            this.clear();
            return -1;
        }
        this.dim = opb.read(16);
        this.entries = opb.read(24);
        if (this.entries == -1) {
            this.clear();
            return -1;
        }
        switch (opb.read(1)) {
            case 0: {
                this.lengthlist = new int[this.entries];
                if (opb.read(1) != 0) {
                    for (int i = 0; i < this.entries; ++i) {
                        if (opb.read(1) != 0) {
                            final int num = opb.read(5);
                            if (num == -1) {
                                this.clear();
                                return -1;
                            }
                            this.lengthlist[i] = num + 1;
                        }
                        else {
                            this.lengthlist[i] = 0;
                        }
                    }
                    break;
                }
                for (int i = 0; i < this.entries; ++i) {
                    final int num = opb.read(5);
                    if (num == -1) {
                        this.clear();
                        return -1;
                    }
                    this.lengthlist[i] = num + 1;
                }
                break;
            }
            case 1: {
                int length = opb.read(5) + 1;
                this.lengthlist = new int[this.entries];
                int i = 0;
                while (i < this.entries) {
                    final int num2 = opb.read(Util.ilog(this.entries - i));
                    if (num2 == -1) {
                        this.clear();
                        return -1;
                    }
                    for (int j = 0; j < num2; ++j, ++i) {
                        this.lengthlist[i] = length;
                    }
                    ++length;
                }
                break;
            }
            default: {
                return -1;
            }
        }
        switch (this.maptype = opb.read(4)) {
            case 0: {
                break;
            }
            case 1:
            case 2: {
                this.q_min = opb.read(32);
                this.q_delta = opb.read(32);
                this.q_quant = opb.read(4) + 1;
                this.q_sequencep = opb.read(1);
                int quantvals = 0;
                switch (this.maptype) {
                    case 1: {
                        quantvals = this.maptype1_quantvals();
                        break;
                    }
                    case 2: {
                        quantvals = this.entries * this.dim;
                        break;
                    }
                }
                this.quantlist = new int[quantvals];
                for (int i = 0; i < quantvals; ++i) {
                    this.quantlist[i] = opb.read(this.q_quant);
                }
                if (this.quantlist[quantvals - 1] == -1) {
                    this.clear();
                    return -1;
                }
                break;
            }
            default: {
                this.clear();
                return -1;
            }
        }
        return 0;
    }
    
    private int maptype1_quantvals() {
        int vals = (int)Math.floor(Math.pow(this.entries, 1.0 / this.dim));
        while (true) {
            int acc = 1;
            int acc2 = 1;
            for (int i = 0; i < this.dim; ++i) {
                acc *= vals;
                acc2 *= vals + 1;
            }
            if (acc <= this.entries && acc2 > this.entries) {
                break;
            }
            if (acc > this.entries) {
                --vals;
            }
            else {
                ++vals;
            }
        }
        return vals;
    }
    
    void clear() {
    }
    
    float[] unquantize() {
        if (this.maptype == 1 || this.maptype == 2) {
            final float mindel = float32_unpack(this.q_min);
            final float delta = float32_unpack(this.q_delta);
            final float[] r = new float[this.entries * this.dim];
            switch (this.maptype) {
                case 1: {
                    final int quantvals = this.maptype1_quantvals();
                    for (int j = 0; j < this.entries; ++j) {
                        float last = 0.0f;
                        int indexdiv = 1;
                        for (int k = 0; k < this.dim; ++k) {
                            final int index = j / indexdiv % quantvals;
                            float val = (float)this.quantlist[index];
                            val = Math.abs(val) * delta + mindel + last;
                            if (this.q_sequencep != 0) {
                                last = val;
                            }
                            r[j * this.dim + k] = val;
                            indexdiv *= quantvals;
                        }
                    }
                    break;
                }
                case 2: {
                    for (int j = 0; j < this.entries; ++j) {
                        float last = 0.0f;
                        for (int i = 0; i < this.dim; ++i) {
                            float val2 = (float)this.quantlist[j * this.dim + i];
                            val2 = Math.abs(val2) * delta + mindel + last;
                            if (this.q_sequencep != 0) {
                                last = val2;
                            }
                            r[j * this.dim + i] = val2;
                        }
                    }
                    break;
                }
            }
            return r;
        }
        return null;
    }
    
    static long float32_pack(float val) {
        int sign = 0;
        if (val < 0.0f) {
            sign = Integer.MIN_VALUE;
            val = -val;
        }
        int exp = (int)Math.floor(Math.log(val) / Math.log(2.0));
        final int mant = (int)Math.rint(Math.pow(val, 20 - exp));
        exp = exp + 768 << 21;
        return sign | exp | mant;
    }
    
    static float float32_unpack(final int val) {
        float mant = (float)(val & 0x1FFFFF);
        final float exp = (float)((val & 0x7FE00000) >>> 21);
        if ((val & Integer.MIN_VALUE) != 0x0) {
            mant = -mant;
        }
        return ldexp(mant, (int)exp - 20 - 768);
    }
    
    static float ldexp(final float foo, final int e) {
        return (float)(foo * Math.pow(2.0, e));
    }
}
