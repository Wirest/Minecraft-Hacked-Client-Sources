// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.io;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpTransportMetrics;

@NotThreadSafe
public class HttpTransportMetricsImpl implements HttpTransportMetrics
{
    private long bytesTransferred;
    
    public HttpTransportMetricsImpl() {
        this.bytesTransferred = 0L;
    }
    
    public long getBytesTransferred() {
        return this.bytesTransferred;
    }
    
    public void setBytesTransferred(final long count) {
        this.bytesTransferred = count;
    }
    
    public void incrementBytesTransferred(final long count) {
        this.bytesTransferred += count;
    }
    
    public void reset() {
        this.bytesTransferred = 0L;
    }
}
