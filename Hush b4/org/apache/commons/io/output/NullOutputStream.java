// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream
{
    public static final NullOutputStream NULL_OUTPUT_STREAM;
    
    @Override
    public void write(final byte[] b, final int off, final int len) {
    }
    
    @Override
    public void write(final int b) {
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
    }
    
    static {
        NULL_OUTPUT_STREAM = new NullOutputStream();
    }
}
