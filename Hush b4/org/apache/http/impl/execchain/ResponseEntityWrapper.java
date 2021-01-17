// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import java.net.SocketException;
import java.io.OutputStream;
import org.apache.http.conn.EofSensorInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

@NotThreadSafe
class ResponseEntityWrapper extends HttpEntityWrapper implements EofSensorWatcher
{
    private final ConnectionHolder connReleaseTrigger;
    
    public ResponseEntityWrapper(final HttpEntity entity, final ConnectionHolder connReleaseTrigger) {
        super(entity);
        this.connReleaseTrigger = connReleaseTrigger;
    }
    
    private void cleanup() {
        if (this.connReleaseTrigger != null) {
            this.connReleaseTrigger.abortConnection();
        }
    }
    
    public void releaseConnection() throws IOException {
        if (this.connReleaseTrigger != null) {
            try {
                if (this.connReleaseTrigger.isReusable()) {
                    this.connReleaseTrigger.releaseConnection();
                }
            }
            finally {
                this.cleanup();
            }
        }
    }
    
    @Override
    public boolean isRepeatable() {
        return false;
    }
    
    @Override
    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }
    
    @Deprecated
    @Override
    public void consumeContent() throws IOException {
        this.releaseConnection();
    }
    
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        try {
            this.wrappedEntity.writeTo(outstream);
            this.releaseConnection();
        }
        finally {
            this.cleanup();
        }
    }
    
    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            wrapped.close();
            this.releaseConnection();
        }
        finally {
            this.cleanup();
        }
        return false;
    }
    
    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            final boolean open = this.connReleaseTrigger != null && !this.connReleaseTrigger.isReleased();
            try {
                wrapped.close();
                this.releaseConnection();
            }
            catch (SocketException ex) {
                if (open) {
                    throw ex;
                }
            }
        }
        finally {
            this.cleanup();
        }
        return false;
    }
    
    public boolean streamAbort(final InputStream wrapped) throws IOException {
        this.cleanup();
        return false;
    }
}
