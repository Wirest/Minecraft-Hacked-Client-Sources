// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class ClosedOutputStream extends OutputStream
{
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM;
    
    @Override
    public void write(final int b) throws IOException {
        throw new IOException("write(" + b + ") failed: stream is closed");
    }
    
    static {
        CLOSED_OUTPUT_STREAM = new ClosedOutputStream();
    }
}
