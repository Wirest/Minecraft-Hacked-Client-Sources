// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.DataInputStream;

class ICUBinaryStream extends DataInputStream
{
    public ICUBinaryStream(final InputStream stream, final int size) {
        super(stream);
        this.mark(size);
    }
    
    public ICUBinaryStream(final byte[] raw) {
        this(new ByteArrayInputStream(raw), raw.length);
    }
    
    public void seek(final int offset) throws IOException {
        this.reset();
        final int actual = this.skipBytes(offset);
        if (actual != offset) {
            throw new IllegalStateException("Skip(" + offset + ") only skipped " + actual + " bytes");
        }
    }
}
