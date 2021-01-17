// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import com.google.common.annotations.Beta;
import java.io.FilterOutputStream;

@Beta
public final class HashingOutputStream extends FilterOutputStream
{
    private final Hasher hasher;
    
    public HashingOutputStream(final HashFunction hashFunction, final OutputStream out) {
        super(Preconditions.checkNotNull(out));
        this.hasher = Preconditions.checkNotNull(hashFunction.newHasher());
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.hasher.putByte((byte)b);
        this.out.write(b);
    }
    
    @Override
    public void write(final byte[] bytes, final int off, final int len) throws IOException {
        this.hasher.putBytes(bytes, off, len);
        this.out.write(bytes, off, len);
    }
    
    public HashCode hash() {
        return this.hasher.hash();
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
    }
}
