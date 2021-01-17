// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;

public interface EofSensorWatcher
{
    boolean eofDetected(final InputStream p0) throws IOException;
    
    boolean streamClosed(final InputStream p0) throws IOException;
    
    boolean streamAbort(final InputStream p0) throws IOException;
}
