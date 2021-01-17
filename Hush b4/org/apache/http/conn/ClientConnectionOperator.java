// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import java.net.InetAddress;
import org.apache.http.HttpHost;

@Deprecated
public interface ClientConnectionOperator
{
    OperatedClientConnection createConnection();
    
    void openConnection(final OperatedClientConnection p0, final HttpHost p1, final InetAddress p2, final HttpContext p3, final HttpParams p4) throws IOException;
    
    void updateSecureConnection(final OperatedClientConnection p0, final HttpHost p1, final HttpContext p2, final HttpParams p3) throws IOException;
}
