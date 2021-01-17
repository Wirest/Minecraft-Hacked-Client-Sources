// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import java.util.concurrent.ExecutionException;
import org.apache.http.HttpClientConnection;
import java.util.concurrent.TimeUnit;
import org.apache.http.concurrent.Cancellable;

public interface ConnectionRequest extends Cancellable
{
    HttpClientConnection get(final long p0, final TimeUnit p1) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException;
}
