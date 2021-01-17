// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.methods;

import org.apache.http.params.HttpParams;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.message.HeaderGroup;
import java.io.IOException;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.ClientConnectionRequest;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.concurrent.Cancellable;
import java.util.concurrent.locks.Lock;
import org.apache.http.HttpRequest;
import org.apache.http.message.AbstractHttpMessage;

public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest
{
    private Lock abortLock;
    private volatile boolean aborted;
    private volatile Cancellable cancellable;
    
    protected AbstractExecutionAwareRequest() {
        this.abortLock = new ReentrantLock();
    }
    
    @Deprecated
    public void setConnectionRequest(final ClientConnectionRequest connRequest) {
        if (this.aborted) {
            return;
        }
        this.abortLock.lock();
        try {
            this.cancellable = new Cancellable() {
                public boolean cancel() {
                    connRequest.abortRequest();
                    return true;
                }
            };
        }
        finally {
            this.abortLock.unlock();
        }
    }
    
    @Deprecated
    public void setReleaseTrigger(final ConnectionReleaseTrigger releaseTrigger) {
        if (this.aborted) {
            return;
        }
        this.abortLock.lock();
        try {
            this.cancellable = new Cancellable() {
                public boolean cancel() {
                    try {
                        releaseTrigger.abortConnection();
                        return true;
                    }
                    catch (IOException ex) {
                        return false;
                    }
                }
            };
        }
        finally {
            this.abortLock.unlock();
        }
    }
    
    private void cancelExecution() {
        if (this.cancellable != null) {
            this.cancellable.cancel();
            this.cancellable = null;
        }
    }
    
    public void abort() {
        if (this.aborted) {
            return;
        }
        this.abortLock.lock();
        try {
            this.aborted = true;
            this.cancelExecution();
        }
        finally {
            this.abortLock.unlock();
        }
    }
    
    public boolean isAborted() {
        return this.aborted;
    }
    
    public void setCancellable(final Cancellable cancellable) {
        if (this.aborted) {
            return;
        }
        this.abortLock.lock();
        try {
            this.cancellable = cancellable;
        }
        finally {
            this.abortLock.unlock();
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final AbstractExecutionAwareRequest clone = (AbstractExecutionAwareRequest)super.clone();
        clone.headergroup = CloneUtils.cloneObject(this.headergroup);
        clone.params = CloneUtils.cloneObject(this.params);
        clone.abortLock = new ReentrantLock();
        clone.cancellable = null;
        clone.aborted = false;
        return clone;
    }
    
    public void completed() {
        this.abortLock.lock();
        try {
            this.cancellable = null;
        }
        finally {
            this.abortLock.unlock();
        }
    }
    
    public void reset() {
        this.abortLock.lock();
        try {
            this.cancelExecution();
            this.aborted = false;
        }
        finally {
            this.abortLock.unlock();
        }
    }
}
