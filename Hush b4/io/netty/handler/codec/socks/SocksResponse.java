// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public abstract class SocksResponse extends SocksMessage
{
    private final SocksResponseType responseType;
    
    protected SocksResponse(final SocksResponseType responseType) {
        super(SocksMessageType.RESPONSE);
        if (responseType == null) {
            throw new NullPointerException("responseType");
        }
        this.responseType = responseType;
    }
    
    public SocksResponseType responseType() {
        return this.responseType;
    }
}
