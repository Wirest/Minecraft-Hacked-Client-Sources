// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.io;

import org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

public interface SessionOutputBuffer
{
    void write(final byte[] p0, final int p1, final int p2) throws IOException;
    
    void write(final byte[] p0) throws IOException;
    
    void write(final int p0) throws IOException;
    
    void writeLine(final String p0) throws IOException;
    
    void writeLine(final CharArrayBuffer p0) throws IOException;
    
    void flush() throws IOException;
    
    HttpTransportMetrics getMetrics();
}
