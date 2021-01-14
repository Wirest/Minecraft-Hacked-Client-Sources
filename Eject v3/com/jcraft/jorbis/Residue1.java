package com.jcraft.jorbis;

class Residue1
        extends Residue0 {
    int inverse(Block paramBlock, Object paramObject, float[][] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt) {
        int i = 0;
        for (int j = 0; j < paramInt; j++) {
            if (paramArrayOfInt[j] != 0) {
                paramArrayOfFloat[(i++)] = paramArrayOfFloat[j];
            }
        }
        if (i != 0) {
            return _01inverse(paramBlock, paramObject, paramArrayOfFloat, i, 1);
        }
        return 0;
    }
}




