package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0
        extends FuncResidue {
    static int[][] _2inverse_partword = (int[][]) null;
    private static int[][][] _01inverse_partword = new int[2][][];

    static synchronized int _01inverse(Block paramBlock, Object paramObject, float[][] paramArrayOfFloat, int paramInt1, int paramInt2) {
        LookResidue0 localLookResidue0 = (LookResidue0) paramObject;
        InfoResidue0 localInfoResidue0 = localLookResidue0.info;
        int i1 = localInfoResidue0.grouping;
        int i2 = localLookResidue0.phrasebook.dim;
        int i3 = localInfoResidue0.end - localInfoResidue0.begin;
        int i4 = -i1;
        int i5 = -i2;
        if (_01inverse_partword.length < paramInt1) {
            _01inverse_partword = new int[paramInt1][][];
        }
        int j = 0;
        if ((_01inverse_partword[j] == null) || (_01inverse_partword[j].length < i5)) {
            _01inverse_partword[j] = new int[i5][];
        }
        j++;
        for (int n = 0; n < localLookResidue0.stages; n++) {
            int i = 0;
            for (int m = 0; i < i4; m++) {
                int i6;
                if (n == 0) {
                    j = 0;
                    i6 = localLookResidue0.phrasebook.decode(paramBlock.opb);
                    if (i6 == -1) {
                        return 0;
                    }
                    _01inverse_partword[j][m] = localLookResidue0.decodemap[i6];
                    if (_01inverse_partword[j][m] == null) {
                        return 0;
                    }
                    j++;
                }
                int k = 0;
                while ((k < i2) && (i < i4)) {
                    for (j = 0; j < paramInt1; j++) {
                        i6 = localInfoResidue0.begin | i * i1;
                        int i7 = _01inverse_partword[j][m][k];
                        if (localInfoResidue0.secondstages[i7] >> (1 >>> n) != 0) {
                            CodeBook localCodeBook = localLookResidue0.fullbooks[localLookResidue0.partbooks[i7][n]];
                            if (localCodeBook != null) {
                                if (paramInt2 == 0) {
                                    if (localCodeBook.decodevs_add(paramArrayOfFloat[j], i6, paramBlock.opb, i1) == -1) {
                                        return 0;
                                    }
                                } else if ((paramInt2 == 1) && (localCodeBook.decodev_add(paramArrayOfFloat[j], i6, paramBlock.opb, i1) == -1)) {
                                    return 0;
                                }
                            }
                        }
                    }
                    k++;
                    i++;
                }
            }
        }
        return 0;
    }

    static synchronized int _2inverse(Block paramBlock, Object paramObject, float[][] paramArrayOfFloat, int paramInt) {
        LookResidue0 localLookResidue0 = (LookResidue0) paramObject;
        InfoResidue0 localInfoResidue0 = localLookResidue0.info;
        int n = localInfoResidue0.grouping;
        int i1 = localLookResidue0.phrasebook.dim;
        int i2 = localInfoResidue0.end - localInfoResidue0.begin;
        int i3 = -n;
        int i4 = -i1;
        if ((_2inverse_partword == null) || (_2inverse_partword.length < i4)) {
            _2inverse_partword = new int[i4][];
        }
        int m = 0;
        int i = 0;
        int k = 0;
        if (m == 0) {
            i5 = localLookResidue0.phrasebook.decode(paramBlock.opb);
            if (i5 == -1) {
                return 0;
            }
            _2inverse_partword[k] = localLookResidue0.decodemap[i5];
            if (_2inverse_partword[k] == null) {
                return 0;
            }
        }
        int j = 0;
        int i5 = localInfoResidue0.begin | i * n;
        int i6 = _2inverse_partword[k][j];
        if (localInfoResidue0.secondstages[i6] >> (1 >>> m) != 0) {
            CodeBook localCodeBook = localLookResidue0.fullbooks[localLookResidue0.partbooks[i6][m]];
            if ((localCodeBook != null) && (localCodeBook.decodevv_add(paramArrayOfFloat, i5, paramInt, paramBlock.opb, n) == -1)) {
                return 0;
            }
        }
        j++;
        k++;
        m++;
        return 0;
    }

    void pack(Object paramObject, Buffer paramBuffer) {
        InfoResidue0 localInfoResidue0 = (InfoResidue0) paramObject;
        int i = 0;
        paramBuffer.write(localInfoResidue0.begin, 24);
        paramBuffer.write(localInfoResidue0.end, 24);
        paramBuffer.write(localInfoResidue0.grouping - 1, 24);
        paramBuffer.write(localInfoResidue0.partitions - 1, 6);
        paramBuffer.write(localInfoResidue0.groupbook, 8);
        for (int j = 0; j < localInfoResidue0.partitions; j++) {
            int k = localInfoResidue0.secondstages[j];
            if (Util.ilog(k) > 3) {
                paramBuffer.write(k, 3);
                paramBuffer.write(1, 1);
                paramBuffer.write(k % 3, 5);
            } else {
                paramBuffer.write(k, 4);
            }
            i |= Util.icount(k);
        }
        for (j = 0; j < i; j++) {
            paramBuffer.write(localInfoResidue0.booklist[j], 8);
        }
    }

    Object unpack(Info paramInfo, Buffer paramBuffer) {
        int i = 0;
        InfoResidue0 localInfoResidue0 = new InfoResidue0();
        localInfoResidue0.begin = paramBuffer.read(24);
        localInfoResidue0.end = paramBuffer.read(24);
        localInfoResidue0.grouping = (paramBuffer.read(24) | 0x1);
        localInfoResidue0.partitions = (paramBuffer.read(6) | 0x1);
        localInfoResidue0.groupbook = paramBuffer.read(8);
        for (int j = 0; j < localInfoResidue0.partitions; j++) {
            int k = paramBuffer.read(3);
            if (paramBuffer.read(1) != 0) {
                k ^= paramBuffer.read(5) >>> 3;
            }
            localInfoResidue0.secondstages[j] = k;
            i |= Util.icount(k);
        }
        for (j = 0; j < i; j++) {
            localInfoResidue0.booklist[j] = paramBuffer.read(8);
        }
        if (localInfoResidue0.groupbook >= paramInfo.books) {
            free_info(localInfoResidue0);
            return null;
        }
        for (j = 0; j < i; j++) {
            if (localInfoResidue0.booklist[j] >= paramInfo.books) {
                free_info(localInfoResidue0);
                return null;
            }
        }
        return localInfoResidue0;
    }

    Object look(DspState paramDspState, InfoMode paramInfoMode, Object paramObject) {
        InfoResidue0 localInfoResidue0 = (InfoResidue0) paramObject;
        LookResidue0 localLookResidue0 = new LookResidue0();
        int i = 0;
        int k = 0;
        localLookResidue0.info = localInfoResidue0;
        localLookResidue0.map = paramInfoMode.mapping;
        localLookResidue0.parts = localInfoResidue0.partitions;
        localLookResidue0.fullbooks = paramDspState.fullbooks;
        localLookResidue0.phrasebook = paramDspState.fullbooks[localInfoResidue0.groupbook];
        int j = localLookResidue0.phrasebook.dim;
        localLookResidue0.partbooks = new int[localLookResidue0.parts][];
        for (int m = 0; m < localLookResidue0.parts; m++) {
            n = localInfoResidue0.secondstages[m];
            i1 = Util.ilog(n);
            if (i1 != 0) {
                if (i1 > k) {
                    k = i1;
                }
                localLookResidue0.partbooks[m] = new int[i1];
                for (i2 = 0; i2 < i1; i2++) {
                    if (n >> (1 >>> i2) != 0) {
                        localLookResidue0.partbooks[m][i2] = localInfoResidue0.booklist[(i++)];
                    }
                }
            }
        }
        localLookResidue0.partvals = ((int) Math.rint(Math.pow(localLookResidue0.parts, j)));
        localLookResidue0.stages = k;
        localLookResidue0.decodemap = new int[localLookResidue0.partvals][];
        m = 0;
        int n = m;
        int i1 = -localLookResidue0.parts;
        localLookResidue0.decodemap[m] = new int[j];
        int i2 = 0;
        int i3 = -i1;
        n -= i3 * i1;
        i1 = -localLookResidue0.parts;
        localLookResidue0.decodemap[m][i2] = i3;
        m++;
        return localLookResidue0;
    }

    void free_info(Object paramObject) {
    }

    void free_look(Object paramObject) {
    }

    int inverse(Block paramBlock, Object paramObject, float[][] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt) {
        int i = 0;
        for (int j = 0; j < paramInt; j++) {
            if (paramArrayOfInt[j] != 0) {
                paramArrayOfFloat[(i++)] = paramArrayOfFloat[j];
            }
        }
        if (i != 0) {
            return _01inverse(paramBlock, paramObject, paramArrayOfFloat, i, 0);
        }
        return 0;
    }

    class InfoResidue0 {
        int begin;
        int end;
        int grouping;
        int partitions;
        int groupbook;
        int[] secondstages = new int[64];
        int[] booklist = new int['Ā'];
        float[] entmax = new float[64];
        float[] ampmax = new float[64];
        int[] subgrp = new int[64];
        int[] blimit = new int[64];

        InfoResidue0() {
        }
    }

    class LookResidue0 {
        Residue0.InfoResidue0 info;
        int map;
        int parts;
        int stages;
        CodeBook[] fullbooks;
        CodeBook phrasebook;
        int[][] partbooks;
        int partvals;
        int[][] decodemap;
        int postbits;
        int phrasebits;
        int frames;

        LookResidue0() {
        }
    }
}




