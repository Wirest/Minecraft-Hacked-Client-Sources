// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Residue2 extends Residue0
{
    @Override
    int inverse(final Block vb, final Object vl, final float[][] in, final int[] nonzero, final int ch) {
        int i;
        for (i = 0, i = 0; i < ch && nonzero[i] == 0; ++i) {}
        if (i == ch) {
            return 0;
        }
        return Residue0._2inverse(vb, vl, in, ch);
    }
}
