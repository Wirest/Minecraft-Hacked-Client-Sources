// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.marshalling;

import io.netty.util.Attribute;
import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Unmarshaller;
import io.netty.util.AttributeKey;

public class ContextBoundUnmarshallerProvider extends DefaultUnmarshallerProvider
{
    private static final AttributeKey<Unmarshaller> UNMARSHALLER;
    
    public ContextBoundUnmarshallerProvider(final MarshallerFactory factory, final MarshallingConfiguration config) {
        super(factory, config);
    }
    
    @Override
    public Unmarshaller getUnmarshaller(final ChannelHandlerContext ctx) throws Exception {
        final Attribute<Unmarshaller> attr = ctx.attr(ContextBoundUnmarshallerProvider.UNMARSHALLER);
        Unmarshaller unmarshaller = attr.get();
        if (unmarshaller == null) {
            unmarshaller = super.getUnmarshaller(ctx);
            attr.set(unmarshaller);
        }
        return unmarshaller;
    }
    
    static {
        UNMARSHALLER = AttributeKey.valueOf(ContextBoundUnmarshallerProvider.class.getName() + ".UNMARSHALLER");
    }
}
