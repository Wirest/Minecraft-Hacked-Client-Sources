// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import org.apache.http.client.methods.HttpUriRequest;
import java.util.concurrent.FutureTask;

public class HttpRequestFutureTask<V> extends FutureTask<V>
{
    private final HttpUriRequest request;
    private final HttpRequestTaskCallable<V> callable;
    
    public HttpRequestFutureTask(final HttpUriRequest request, final HttpRequestTaskCallable<V> httpCallable) {
        super(httpCallable);
        this.request = request;
        this.callable = httpCallable;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        this.callable.cancel();
        if (mayInterruptIfRunning) {
            this.request.abort();
        }
        return super.cancel(mayInterruptIfRunning);
    }
    
    public long scheduledTime() {
        return this.callable.getScheduled();
    }
    
    public long startedTime() {
        return this.callable.getStarted();
    }
    
    public long endedTime() {
        if (this.isDone()) {
            return this.callable.getEnded();
        }
        throw new IllegalStateException("Task is not done yet");
    }
    
    public long requestDuration() {
        if (this.isDone()) {
            return this.endedTime() - this.startedTime();
        }
        throw new IllegalStateException("Task is not done yet");
    }
    
    public long taskDuration() {
        if (this.isDone()) {
            return this.endedTime() - this.scheduledTime();
        }
        throw new IllegalStateException("Task is not done yet");
    }
    
    @Override
    public String toString() {
        return this.request.getRequestLine().getUri();
    }
}
