// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.concurrent.FutureCallback;
import org.apache.http.client.ResponseHandler;
import org.apache.http.protocol.HttpContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import java.util.concurrent.Callable;

class HttpRequestTaskCallable<V> implements Callable<V>
{
    private final HttpUriRequest request;
    private final HttpClient httpclient;
    private final AtomicBoolean cancelled;
    private final long scheduled;
    private long started;
    private long ended;
    private final HttpContext context;
    private final ResponseHandler<V> responseHandler;
    private final FutureCallback<V> callback;
    private final FutureRequestExecutionMetrics metrics;
    
    HttpRequestTaskCallable(final HttpClient httpClient, final HttpUriRequest request, final HttpContext context, final ResponseHandler<V> responseHandler, final FutureCallback<V> callback, final FutureRequestExecutionMetrics metrics) {
        this.cancelled = new AtomicBoolean(false);
        this.scheduled = System.currentTimeMillis();
        this.started = -1L;
        this.ended = -1L;
        this.httpclient = httpClient;
        this.responseHandler = responseHandler;
        this.request = request;
        this.context = context;
        this.callback = callback;
        this.metrics = metrics;
    }
    
    public long getScheduled() {
        return this.scheduled;
    }
    
    public long getStarted() {
        return this.started;
    }
    
    public long getEnded() {
        return this.ended;
    }
    
    public V call() throws Exception {
        if (!this.cancelled.get()) {
            try {
                this.metrics.getActiveConnections().incrementAndGet();
                this.started = System.currentTimeMillis();
                try {
                    this.metrics.getScheduledConnections().decrementAndGet();
                    final V result = this.httpclient.execute(this.request, (ResponseHandler<? extends V>)this.responseHandler, this.context);
                    this.ended = System.currentTimeMillis();
                    this.metrics.getSuccessfulConnections().increment(this.started);
                    if (this.callback != null) {
                        this.callback.completed(result);
                    }
                    return result;
                }
                catch (Exception e) {
                    this.metrics.getFailedConnections().increment(this.started);
                    this.ended = System.currentTimeMillis();
                    if (this.callback != null) {
                        this.callback.failed(e);
                    }
                    throw e;
                }
            }
            finally {
                this.metrics.getRequests().increment(this.started);
                this.metrics.getTasks().increment(this.started);
                this.metrics.getActiveConnections().decrementAndGet();
            }
        }
        throw new IllegalStateException("call has been cancelled for request " + this.request.getURI());
    }
    
    public void cancel() {
        this.cancelled.set(true);
        if (this.callback != null) {
            this.callback.cancelled();
        }
    }
}
