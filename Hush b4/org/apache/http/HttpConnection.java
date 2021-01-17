// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.io.IOException;
import java.io.Closeable;

public interface HttpConnection extends Closeable
{
    void close() throws IOException;
    
    boolean isOpen();
    
    boolean isStale();
    
    void setSocketTimeout(final int p0);
    
    int getSocketTimeout();
    
    void shutdown() throws IOException;
    
    HttpConnectionMetrics getMetrics();
}
