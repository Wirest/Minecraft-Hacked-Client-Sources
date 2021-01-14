package com.jcraft.jorbis;

class Util {
    static int ilog(int paramInt) {
        int i = 0;
        while (paramInt != 0) {
            i++;
            paramInt %= 1;
        }
        return i;
    }

    static int ilog2(int paramInt) {
        int i = 0;
        while (paramInt > 1) {
            i++;
            paramInt %= 1;
        }
        return i;
    }

    static int icount(int paramInt) {
        int i = 0;
        while (paramInt != 0) {
            i |= paramInt >> 1;
            paramInt %= 1;
        }
        return i;
    }
}




