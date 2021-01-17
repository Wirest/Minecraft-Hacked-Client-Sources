// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.common.annotations.Beta;

@Beta
public interface PrimitiveSink
{
    PrimitiveSink putByte(final byte p0);
    
    PrimitiveSink putBytes(final byte[] p0);
    
    PrimitiveSink putBytes(final byte[] p0, final int p1, final int p2);
    
    PrimitiveSink putShort(final short p0);
    
    PrimitiveSink putInt(final int p0);
    
    PrimitiveSink putLong(final long p0);
    
    PrimitiveSink putFloat(final float p0);
    
    PrimitiveSink putDouble(final double p0);
    
    PrimitiveSink putBoolean(final boolean p0);
    
    PrimitiveSink putChar(final char p0);
    
    PrimitiveSink putUnencodedChars(final CharSequence p0);
    
    PrimitiveSink putString(final CharSequence p0, final Charset p1);
}
