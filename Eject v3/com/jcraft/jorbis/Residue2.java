package com.jcraft.jorbis;

class Residue2
        extends Residue0 {
    int inverse(Block paramBlock, Object paramObject, float[][] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt) {
        int i = 0;
        for (i = 0; (i < paramInt) && (paramArrayOfInt[i] == 0); i++) {
        }
        if (i == paramInt) {
            return 0;
        }
        return _2inverse(paramBlock, paramObject, paramArrayOfFloat, paramInt);
    }
}




