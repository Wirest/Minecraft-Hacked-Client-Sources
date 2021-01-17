// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Residue1 extends Residue0
{
    @Override
    int inverse(final Block vb, final Object vl, final float[][] in, final int[] nonzero, final int ch) {
        int used = 0;
        for (int i = 0; i < ch; ++i) {
            if (nonzero[i] != 0) {
                in[used++] = in[i];
            }
        }
        if (used != 0) {
            return Residue0._01inverse(vb, vl, in, used, 1);
        }
        return 0;
    }
}
