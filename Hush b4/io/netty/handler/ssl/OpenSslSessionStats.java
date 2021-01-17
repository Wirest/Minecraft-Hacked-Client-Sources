// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import org.apache.tomcat.jni.SSLContext;

public final class OpenSslSessionStats
{
    private final long context;
    
    OpenSslSessionStats(final long context) {
        this.context = context;
    }
    
    public long number() {
        return SSLContext.sessionNumber(this.context);
    }
    
    public long connect() {
        return SSLContext.sessionConnect(this.context);
    }
    
    public long connectGood() {
        return SSLContext.sessionConnectGood(this.context);
    }
    
    public long connectRenegotiate() {
        return SSLContext.sessionConnectRenegotiate(this.context);
    }
    
    public long accept() {
        return SSLContext.sessionAccept(this.context);
    }
    
    public long acceptGood() {
        return SSLContext.sessionAcceptGood(this.context);
    }
    
    public long acceptRenegotiate() {
        return SSLContext.sessionAcceptRenegotiate(this.context);
    }
    
    public long hits() {
        return SSLContext.sessionHits(this.context);
    }
    
    public long cbHits() {
        return SSLContext.sessionCbHits(this.context);
    }
    
    public long misses() {
        return SSLContext.sessionMisses(this.context);
    }
    
    public long timeouts() {
        return SSLContext.sessionTimeouts(this.context);
    }
    
    public long cacheFull() {
        return SSLContext.sessionCacheFull(this.context);
    }
}
