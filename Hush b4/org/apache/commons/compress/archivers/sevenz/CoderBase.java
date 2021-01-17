// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

abstract class CoderBase
{
    private final Class<?>[] acceptableOptions;
    private static final byte[] NONE;
    
    protected CoderBase(final Class<?>... acceptableOptions) {
        this.acceptableOptions = acceptableOptions;
    }
    
    boolean canAcceptOptions(final Object opts) {
        for (final Class<?> c : this.acceptableOptions) {
            if (c.isInstance(opts)) {
                return true;
            }
        }
        return false;
    }
    
    byte[] getOptionsAsProperties(final Object options) {
        return CoderBase.NONE;
    }
    
    Object getOptionsFromCoder(final Coder coder, final InputStream in) {
        return null;
    }
    
    abstract InputStream decode(final InputStream p0, final Coder p1, final byte[] p2) throws IOException;
    
    OutputStream encode(final OutputStream out, final Object options) throws IOException {
        throw new UnsupportedOperationException("method doesn't support writing");
    }
    
    protected static int numberOptionOrDefault(final Object options, final int defaultValue) {
        return (options instanceof Number) ? ((Number)options).intValue() : defaultValue;
    }
    
    static {
        NONE = new byte[0];
    }
}
