// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.net.SocketAddress;

public abstract class AbstractServerChannel extends AbstractChannel implements ServerChannel
{
    private static final ChannelMetadata METADATA;
    
    protected AbstractServerChannel() {
        super(null);
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractServerChannel.METADATA;
    }
    
    @Override
    public SocketAddress remoteAddress() {
        return null;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DefaultServerUnsafe();
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) {
        throw new UnsupportedOperationException();
    }
    
    static {
        METADATA = new ChannelMetadata(false);
    }
    
    private final class DefaultServerUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            this.safeSetFailure(promise, new UnsupportedOperationException());
        }
    }
}
