package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;

@GwtCompatible
final class Hashing {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static int MAX_TABLE_SIZE = 1073741824;

    static int smear(int paramInt) {
        return 461845907 * Integer.rotateLeft(paramInt * -862048943, 15);
    }

    static int smearedHash(@Nullable Object paramObject) {
        return smear(paramObject == null ? 0 : paramObject.hashCode());
    }

    static int closedTableSize(int paramInt, double paramDouble) {
        paramInt = Math.max(paramInt, 2);
        int i = Integer.highestOneBit(paramInt);
        if (paramInt > (int) (paramDouble * i)) {
            i >>>= 1;
            return i > 0 ? i : MAX_TABLE_SIZE;
        }
        return i;
    }

    static boolean needsResizing(int paramInt1, int paramInt2, double paramDouble) {
        return (paramInt1 > paramDouble * paramInt2) && (paramInt2 < MAX_TABLE_SIZE);
    }
}




