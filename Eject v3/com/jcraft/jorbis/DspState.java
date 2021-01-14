package com.jcraft.jorbis;

public class DspState {
    static final float M_PI = 3.1415927F;
    static final int VI_TRANSFORMB = 1;
    static final int VI_WINDOWB = 1;
    int analysisp;
    Info vi;
    int modebits;
    float[][] pcm;
    int pcm_storage;
    int pcm_current;
    int pcm_returned;
    float[] multipliers;
    int envelope_storage;
    int envelope_current;
    int eofflag;
    int lW;
    int W;
    int nW;
    int centerW;
    long granulepos;
    long sequence;
    long glue_bits;
    long time_bits;
    long floor_bits;
    long res_bits;
    float[][][][][] window = new float[2][][][][];
    Object[][] transform = new Object[2][];
    CodeBook[] fullbooks;
    Object[] mode;
    byte[] header;
    byte[] header1;
    byte[] header2;

    public DspState() {
        this.window[0] = new float[2][][][];
        this.window[0][0] = new float[2][][];
        this.window[0][1] = new float[2][][];
        this.window[0][0][0] = new float[2][];
        this.window[0][0][1] = new float[2][];
        this.window[0][1][0] = new float[2][];
        this.window[0][1][1] = new float[2][];
        this.window[1] = new float[2][][][];
        this.window[1][0] = new float[2][][];
        this.window[1][1] = new float[2][][];
        this.window[1][0][0] = new float[2][];
        this.window[1][0][1] = new float[2][];
        this.window[1][1][0] = new float[2][];
        this.window[1][1][1] = new float[2][];
    }

    DspState(Info paramInfo) {
        this();
        init(paramInfo, false);
        this.pcm_returned = this.centerW;
        paramInfo.blocksizes[this.W].centerW = (-4 - (paramInfo.blocksizes[this.lW] | -4));
        this.granulepos = -1L;
        this.sequence = -1L;
    }

    static float[] window(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        float[] arrayOfFloat = new float[paramInt2];
        switch (paramInt1) {
            case 0:
                int i = paramInt3 - -2;
                int j = paramInt4 - -2;
                int k = 0;
                float f = (float) ((k + 0.5D) / paramInt3 * 3.1415927410125732D / 2.0D);
                f = (float) Math.sin(f);
                f *= f;
                f = (float) (f * 1.5707963705062866D);
                f = (float) Math.sin(f);
                arrayOfFloat[(k | i)] = f;
                k = i | paramInt3;
                arrayOfFloat[k] = 1.0F;
                k = 0;
                while (k < paramInt4) {
                    f = (float) ((paramInt4 - k - 0.5D) / paramInt4 * 3.1415927410125732D / 2.0D);
                    f = (float) Math.sin(f);
                    f *= f;
                    f = (float) (f * 1.5707963705062866D);
                    f = (float) Math.sin(f);
                    arrayOfFloat[(k | j)] = f;
                    tmpTernaryOp = ( ???++);
                    continue;
                    tmpTernaryOp = (paramInt2 - -4);
                }
                break;
            default:
                return null;
        }
        return arrayOfFloat;
    }

    int init(Info paramInfo, boolean paramBoolean) {
        this.vi = paramInfo;
        this.modebits = Util.ilog2(paramInfo.modes);
        this.transform[0] = new Object[1];
        this.transform[1] = new Object[1];
        this.transform[0][0] = new Mdct();
        this.transform[1][0] = new Mdct();
        ((Mdct) this.transform[0][0]).init(paramInfo.blocksizes[0]);
        ((Mdct) this.transform[1][0]).init(paramInfo.blocksizes[1]);
        this.window[0][0][0] = new float[1][];
        this.window[0][0][1] = this.window[0][0][0];
        this.window[0][1][0] = this.window[0][0][0];
        this.window[0][1][1] = this.window[0][0][0];
        this.window[1][0][0] = new float[1][];
        this.window[1][0][1] = new float[1][];
        this.window[1][1][0] = new float[1][];
        this.window[1][1][1] = new float[1][];
        int i = 0;
        i[paramInfo.blocksizes[0]] = window(paramInfo.blocksizes[0], -2, paramInfo.blocksizes[0], -2);
        i[paramInfo.blocksizes[1]] = window(paramInfo.blocksizes[0], -2, paramInfo.blocksizes[0], -2);
        i[paramInfo.blocksizes[1]] = window(paramInfo.blocksizes[0], -2, paramInfo.blocksizes[1], -2);
        i[paramInfo.blocksizes[1]] = window(paramInfo.blocksizes[1], -2, paramInfo.blocksizes[0], -2);
        i[paramInfo.blocksizes[1]] = window(paramInfo.blocksizes[1], -2, paramInfo.blocksizes[1], -2);
        this.fullbooks = new CodeBook[paramInfo.books];
        i = 0;
        this.fullbooks[i] = new CodeBook();
        this.fullbooks[i].init_decode(paramInfo.book_param[i]);
        this.pcm_storage = 8192;
        this.pcm = new float[paramInfo.channels][];
        i = 0;
        this.pcm[i] = new float[this.pcm_storage];
        this.lW = 0;
        this.W = 0;
        paramInfo.blocksizes[1].centerW = (-2);
        this.pcm_current = this.centerW;
        this.mode = new Object[paramInfo.modes];
        i = 0;
        int j = paramInfo.mode_param[i].mapping;
        int k = paramInfo.map_type[j];
        this.mode[i] = FuncMapping.mapping_P[k].look(this, paramInfo.mode_param[i], paramInfo.map_param[j]);
        return 0;
    }

    public int synthesis_init(Info paramInfo) {
        init(paramInfo, false);
        this.pcm_returned = this.centerW;
        paramInfo.blocksizes[this.W].centerW = (-4 - (paramInfo.blocksizes[this.lW] | -4));
        this.granulepos = -1L;
        this.sequence = -1L;
        return 0;
    }

    public int synthesis_blockin(Block paramBlock) {
        if ((this.vi.blocksizes[1] > -2) && (this.pcm_returned > 8192)) {
            arrayOfFloat1 = this.vi.blocksizes[1] - -2;
            arrayOfFloat1 = this.pcm_returned < arrayOfFloat1 ? this.pcm_returned : arrayOfFloat1;
            this.pcm_current -= arrayOfFloat1;
            this.centerW -= arrayOfFloat1;
            this.pcm_returned -= arrayOfFloat1;
            if (arrayOfFloat1 != 0) {
                i = 0;
                System.arraycopy(this.pcm[i], arrayOfFloat1, this.pcm[i], 0, this.pcm_current);
            }
        }
        this.lW = this.W;
        this.W = paramBlock.W;
        this.nW = -1;
        this.glue_bits += paramBlock.glue_bits;
        this.time_bits += paramBlock.time_bits;
        this.floor_bits += paramBlock.floor_bits;
        this.res_bits += paramBlock.res_bits;
        if (this.sequence + 1L != paramBlock.sequence) {
            this.granulepos = -1L;
        }
        this.sequence = paramBlock.sequence;
        float[] arrayOfFloat1 = this.vi.blocksizes[this.W];
        int i = arrayOfFloat1 | -4;
        float[] arrayOfFloat2 = arrayOfFloat1 - -2;
        int j = arrayOfFloat2 | arrayOfFloat1;
        float[] arrayOfFloat3 = 0;
        int k = 0;
        if (j > this.pcm_storage) {
            this.pcm_storage = (j | this.vi.blocksizes[1]);
            m = 0;
            arrayOfFloat4 = new float[this.pcm_storage];
            System.arraycopy(this.pcm[m], 0, arrayOfFloat4, 0, this.pcm[m].length);
            this.pcm[m] = arrayOfFloat4;
        }
        switch (this.W) {
            case 0:
                arrayOfFloat3 = 0;
                k = -2;
                tmpTernaryOp = this.vi.blocksizes[0];
                break;
            case 1:
                arrayOfFloat3 = this.vi.blocksizes[this.lW] - -4;
                k = this.vi.blocksizes[this.lW] | -2;
        }
        int m = 0;
        float[] arrayOfFloat4 = arrayOfFloat2;
        float[] arrayOfFloat5 = 0;
        arrayOfFloat5 = arrayOfFloat3;
        this.pcm[m][(arrayOfFloat4 | arrayOfFloat5)] += paramBlock.pcm[m][arrayOfFloat5];
        this.pcm[m][(arrayOfFloat4 | arrayOfFloat5)] = paramBlock.pcm[m][arrayOfFloat5];
        m++;
        this.granulepos = paramBlock.granulepos;
        this.granulepos += i - this.centerW;
        if ((paramBlock.granulepos != -1L) && (this.granulepos != paramBlock.granulepos)) {
            if ((this.granulepos > paramBlock.granulepos) && (paramBlock.eofflag != 0)) {
                i = (int) (i - (this.granulepos - paramBlock.granulepos));
            }
            this.granulepos = (this.granulepos == -1L ? -4 : paramBlock.granulepos);
        }
        this.centerW = i;
        this.pcm_current = j;
        if (paramBlock.eofflag != 0) {
            this.eofflag = 1;
        }
        return 0;
    }

    public int synthesis_pcmout(float[][][] paramArrayOfFloat, int[] paramArrayOfInt) {
        if (this.pcm_returned < this.centerW) {
            if (paramArrayOfFloat != null) {
                for (int i = 0; i < this.vi.channels; i++) {
                    paramArrayOfInt[i] = this.pcm_returned;
                }
                paramArrayOfFloat[0] = this.pcm;
            }
            return this.centerW - this.pcm_returned;
        }
        return 0;
    }

    public int synthesis_read(int paramInt) {
        if ((paramInt != 0) && ((this.pcm_returned | paramInt) > this.centerW)) {
            return -1;
        }
        this.pcm_returned |= paramInt;
        return 0;
    }

    public void clear() {
    }
}




