// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Unmarshaller;
import io.netty.util.concurrent.FastThreadLocal;

public class ThreadLocalUnmarshallerProvider implements UnmarshallerProvider
{
    private final FastThreadLocal<Unmarshaller> unmarshallers;
    private final MarshallerFactory factory;
    private final MarshallingConfiguration config;
    
    public ThreadLocalUnmarshallerProvider(final MarshallerFactory factory, final MarshallingConfiguration config) {
        this.unmarshallers = new FastThreadLocal<Unmarshaller>();
        this.factory = factory;
        this.config = config;
    }
    
    @Override
    public Unmarshaller getUnmarshaller(final ChannelHandlerContext ctx) throws Exception {
        Unmarshaller unmarshaller = this.unmarshallers.get();
        if (unmarshaller == null) {
            unmarshaller = this.factory.createUnmarshaller(this.config);
            this.unmarshallers.set(unmarshaller);
        }
        return unmarshaller;
    }
}
