// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public abstract class SocksRequest extends SocksMessage
{
    private final SocksRequestType requestType;
    
    protected SocksRequest(final SocksRequestType requestType) {
        super(SocksMessageType.REQUEST);
        if (requestType == null) {
            throw new NullPointerException("requestType");
        }
        this.requestType = requestType;
    }
    
    public SocksRequestType requestType() {
        return this.requestType;
    }
}
