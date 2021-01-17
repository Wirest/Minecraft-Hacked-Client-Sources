// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.net.SocketException;
import java.io.OutputStream;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.Args;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;

@Deprecated
@NotThreadSafe
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher
{
    protected ManagedClientConnection managedConn;
    protected final boolean attemptReuse;
    
    public BasicManagedEntity(final HttpEntity entity, final ManagedClientConnection conn, final boolean reuse) {
        super(entity);
        Args.notNull(conn, "Connection");
        this.managedConn = conn;
        this.attemptReuse = reuse;
    }
    
    @Override
    public boolean isRepeatable() {
        return false;
    }
    
    @Override
    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }
    
    private void ensureConsumed() throws IOException {
        if (this.managedConn == null) {
            return;
        }
        try {
            if (this.attemptReuse) {
                EntityUtils.consume(this.wrappedEntity);
                this.managedConn.markReusable();
            }
            else {
                this.managedConn.unmarkReusable();
            }
        }
        finally {
            this.releaseManagedConnection();
        }
    }
    
    @Deprecated
    @Override
    public void consumeContent() throws IOException {
        this.ensureConsumed();
    }
    
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(outstream);
        this.ensureConsumed();
    }
    
    public void releaseConnection() throws IOException {
        this.ensureConsumed();
    }
    
    public void abortConnection() throws IOException {
        if (this.managedConn != null) {
            try {
                this.managedConn.abortConnection();
            }
            finally {
                this.managedConn = null;
            }
        }
    }
    
    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            if (this.managedConn != null) {
                if (this.attemptReuse) {
                    wrapped.close();
                    this.managedConn.markReusable();
                }
                else {
                    this.managedConn.unmarkReusable();
                }
            }
        }
        finally {
            this.releaseManagedConnection();
        }
        return false;
    }
    
    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            if (this.managedConn != null) {
                if (this.attemptReuse) {
                    final boolean valid = this.managedConn.isOpen();
                    try {
                        wrapped.close();
                        this.managedConn.markReusable();
                    }
                    catch (SocketException ex) {
                        if (valid) {
                            throw ex;
                        }
                    }
                }
                else {
                    this.managedConn.unmarkReusable();
                }
            }
        }
        finally {
            this.releaseManagedConnection();
        }
        return false;
    }
    
    public boolean streamAbort(final InputStream wrapped) throws IOException {
        if (this.managedConn != null) {
            this.managedConn.abortConnection();
        }
        return false;
    }
    
    protected void releaseManagedConnection() throws IOException {
        if (this.managedConn != null) {
            try {
                this.managedConn.releaseConnection();
            }
            finally {
                this.managedConn = null;
            }
        }
    }
}
