// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import org.apache.http.conn.ConnectionReleaseTrigger;
import java.io.IOException;
import org.apache.http.conn.ClientConnectionRequest;

@Deprecated
public interface AbortableHttpRequest
{
    void setConnectionRequest(final ClientConnectionRequest p0) throws IOException;
    
    void setReleaseTrigger(final ConnectionReleaseTrigger p0) throws IOException;
    
    void abort();
}
