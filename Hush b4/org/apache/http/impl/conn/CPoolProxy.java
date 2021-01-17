// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.pool.PoolEntry;
import org.apache.http.HttpConnection;
import java.lang.reflect.Proxy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import org.apache.http.HttpClientConnection;
import java.lang.reflect.Method;
import org.apache.http.annotation.NotThreadSafe;
import java.lang.reflect.InvocationHandler;

@NotThreadSafe
class CPoolProxy implements InvocationHandler
{
    private static final Method CLOSE_METHOD;
    private static final Method SHUTDOWN_METHOD;
    private static final Method IS_OPEN_METHOD;
    private static final Method IS_STALE_METHOD;
    private volatile CPoolEntry poolEntry;
    
    CPoolProxy(final CPoolEntry entry) {
        this.poolEntry = entry;
    }
    
    CPoolEntry getPoolEntry() {
        return this.poolEntry;
    }
    
    CPoolEntry detach() {
        final CPoolEntry local = this.poolEntry;
        this.poolEntry = null;
        return local;
    }
    
    HttpClientConnection getConnection() {
        final CPoolEntry local = this.poolEntry;
        if (local == null) {
            return null;
        }
        return ((PoolEntry<T, HttpClientConnection>)local).getConnection();
    }
    
    public void close() throws IOException {
        final CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.closeConnection();
        }
    }
    
    public void shutdown() throws IOException {
        final CPoolEntry local = this.poolEntry;
        if (local != null) {
            local.shutdownConnection();
        }
    }
    
    public boolean isOpen() {
        final CPoolEntry local = this.poolEntry;
        return local != null && !local.isClosed();
    }
    
    public boolean isStale() {
        final HttpClientConnection conn = this.getConnection();
        return conn == null || conn.isStale();
    }
    
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.equals(CPoolProxy.CLOSE_METHOD)) {
            this.close();
            return null;
        }
        if (method.equals(CPoolProxy.SHUTDOWN_METHOD)) {
            this.shutdown();
            return null;
        }
        if (method.equals(CPoolProxy.IS_OPEN_METHOD)) {
            return this.isOpen();
        }
        if (method.equals(CPoolProxy.IS_STALE_METHOD)) {
            return this.isStale();
        }
        final HttpClientConnection conn = this.getConnection();
        if (conn == null) {
            throw new ConnectionShutdownException();
        }
        try {
            return method.invoke(conn, args);
        }
        catch (InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause != null) {
                throw cause;
            }
            throw ex;
        }
    }
    
    public static HttpClientConnection newProxy(final CPoolEntry poolEntry) {
        return (HttpClientConnection)Proxy.newProxyInstance(CPoolProxy.class.getClassLoader(), new Class[] { ManagedHttpClientConnection.class, HttpContext.class }, new CPoolProxy(poolEntry));
    }
    
    private static CPoolProxy getHandler(final HttpClientConnection proxy) {
        final InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        if (!CPoolProxy.class.isInstance(handler)) {
            throw new IllegalStateException("Unexpected proxy handler class: " + handler);
        }
        return CPoolProxy.class.cast(handler);
    }
    
    public static CPoolEntry getPoolEntry(final HttpClientConnection proxy) {
        final CPoolEntry entry = getHandler(proxy).getPoolEntry();
        if (entry == null) {
            throw new ConnectionShutdownException();
        }
        return entry;
    }
    
    public static CPoolEntry detach(final HttpClientConnection proxy) {
        return getHandler(proxy).detach();
    }
    
    static {
        try {
            CLOSE_METHOD = HttpConnection.class.getMethod("close", (Class<?>[])new Class[0]);
            SHUTDOWN_METHOD = HttpConnection.class.getMethod("shutdown", (Class<?>[])new Class[0]);
            IS_OPEN_METHOD = HttpConnection.class.getMethod("isOpen", (Class<?>[])new Class[0]);
            IS_STALE_METHOD = HttpConnection.class.getMethod("isStale", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            throw new Error(ex);
        }
    }
}
