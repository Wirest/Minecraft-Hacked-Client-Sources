// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncFloor
{
    public static FuncFloor[] floor_P;
    
    abstract void pack(final Object p0, final Buffer p1);
    
    abstract Object unpack(final Info p0, final Buffer p1);
    
    abstract Object look(final DspState p0, final InfoMode p1, final Object p2);
    
    abstract void free_info(final Object p0);
    
    abstract void free_look(final Object p0);
    
    abstract void free_state(final Object p0);
    
    abstract int forward(final Block p0, final Object p1, final float[] p2, final float[] p3, final Object p4);
    
    abstract Object inverse1(final Block p0, final Object p1, final Object p2);
    
    abstract int inverse2(final Block p0, final Object p1, final Object p2, final float[] p3);
    
    static {
        FuncFloor.floor_P = new FuncFloor[] { new Floor0(), new Floor1() };
    }
}
