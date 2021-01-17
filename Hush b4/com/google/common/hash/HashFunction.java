// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.common.annotations.Beta;

@Beta
public interface HashFunction
{
    Hasher newHasher();
    
    Hasher newHasher(final int p0);
    
    HashCode hashInt(final int p0);
    
    HashCode hashLong(final long p0);
    
    HashCode hashBytes(final byte[] p0);
    
    HashCode hashBytes(final byte[] p0, final int p1, final int p2);
    
    HashCode hashUnencodedChars(final CharSequence p0);
    
    HashCode hashString(final CharSequence p0, final Charset p1);
    
     <T> HashCode hashObject(final T p0, final Funnel<? super T> p1);
    
    int bits();
}
