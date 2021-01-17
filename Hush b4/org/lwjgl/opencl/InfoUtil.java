// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

interface InfoUtil<T extends CLObject>
{
    int getInfoInt(final T p0, final int p1);
    
    long getInfoSize(final T p0, final int p1);
    
    long[] getInfoSizeArray(final T p0, final int p1);
    
    long getInfoLong(final T p0, final int p1);
    
    String getInfoString(final T p0, final int p1);
}
