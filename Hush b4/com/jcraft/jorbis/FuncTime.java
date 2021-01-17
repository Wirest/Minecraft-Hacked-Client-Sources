// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncTime
{
    public static FuncTime[] time_P;
    
    abstract void pack(final Object p0, final Buffer p1);
    
    abstract Object unpack(final Info p0, final Buffer p1);
    
    abstract Object look(final DspState p0, final InfoMode p1, final Object p2);
    
    abstract void free_info(final Object p0);
    
    abstract void free_look(final Object p0);
    
    abstract int inverse(final Block p0, final Object p1, final float[] p2, final float[] p3);
    
    static {
        FuncTime.time_P = new FuncTime[] { new Time0() };
    }
}
