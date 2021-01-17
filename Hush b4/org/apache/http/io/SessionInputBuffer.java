// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.io;

import org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

public interface SessionInputBuffer
{
    int read(final byte[] p0, final int p1, final int p2) throws IOException;
    
    int read(final byte[] p0) throws IOException;
    
    int read() throws IOException;
    
    int readLine(final CharArrayBuffer p0) throws IOException;
    
    String readLine() throws IOException;
    
    @Deprecated
    boolean isDataAvailable(final int p0) throws IOException;
    
    HttpTransportMetrics getMetrics();
}
