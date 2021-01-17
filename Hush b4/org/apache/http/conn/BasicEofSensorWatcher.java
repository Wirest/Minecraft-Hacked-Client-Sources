// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.Args;
import org.apache.http.annotation.NotThreadSafe;

@Deprecated
@NotThreadSafe
public class BasicEofSensorWatcher implements EofSensorWatcher
{
    protected final ManagedClientConnection managedConn;
    protected final boolean attemptReuse;
    
    public BasicEofSensorWatcher(final ManagedClientConnection conn, final boolean reuse) {
        Args.notNull(conn, "Connection");
        this.managedConn = conn;
        this.attemptReuse = reuse;
    }
    
    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            if (this.attemptReuse) {
                wrapped.close();
                this.managedConn.markReusable();
            }
        }
        finally {
            this.managedConn.releaseConnection();
        }
        return false;
    }
    
    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            if (this.attemptReuse) {
                wrapped.close();
                this.managedConn.markReusable();
            }
        }
        finally {
            this.managedConn.releaseConnection();
        }
        return false;
    }
    
    public boolean streamAbort(final InputStream wrapped) throws IOException {
        this.managedConn.abortConnection();
        return false;
    }
}
