// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;
import java.io.Reader;

class MultiReader extends Reader
{
    private final Iterator<? extends CharSource> it;
    private Reader current;
    
    MultiReader(final Iterator<? extends CharSource> readers) throws IOException {
        this.it = readers;
        this.advance();
    }
    
    private void advance() throws IOException {
        this.close();
        if (this.it.hasNext()) {
            this.current = ((CharSource)this.it.next()).openStream();
        }
    }
    
    @Override
    public int read(@Nullable final char[] cbuf, final int off, final int len) throws IOException {
        if (this.current == null) {
            return -1;
        }
        final int result = this.current.read(cbuf, off, len);
        if (result == -1) {
            this.advance();
            return this.read(cbuf, off, len);
        }
        return result;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        Preconditions.checkArgument(n >= 0L, (Object)"n is negative");
        if (n > 0L) {
            while (this.current != null) {
                final long result = this.current.skip(n);
                if (result > 0L) {
                    return result;
                }
                this.advance();
            }
        }
        return 0L;
    }
    
    @Override
    public boolean ready() throws IOException {
        return this.current != null && this.current.ready();
    }
    
    @Override
    public void close() throws IOException {
        if (this.current != null) {
            try {
                this.current.close();
            }
            finally {
                this.current = null;
            }
        }
    }
}
