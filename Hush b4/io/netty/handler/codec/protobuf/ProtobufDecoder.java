// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.protobuf;

import com.google.protobuf.ExtensionRegistryLite;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class ProtobufDecoder extends MessageToMessageDecoder<ByteBuf>
{
    private static final boolean HAS_PARSER;
    private final MessageLite prototype;
    private final ExtensionRegistry extensionRegistry;
    
    public ProtobufDecoder(final MessageLite prototype) {
        this(prototype, null);
    }
    
    public ProtobufDecoder(final MessageLite prototype, final ExtensionRegistry extensionRegistry) {
        if (prototype == null) {
            throw new NullPointerException("prototype");
        }
        this.prototype = prototype.getDefaultInstanceForType();
        this.extensionRegistry = extensionRegistry;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        byte[] array;
        int offset;
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        }
        else {
            array = new byte[length];
            msg.getBytes(msg.readerIndex(), array, 0, length);
            offset = 0;
        }
        if (this.extensionRegistry == null) {
            if (ProtobufDecoder.HAS_PARSER) {
                out.add(this.prototype.getParserForType().parseFrom(array, offset, length));
            }
            else {
                out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length).build());
            }
        }
        else if (ProtobufDecoder.HAS_PARSER) {
            out.add(this.prototype.getParserForType().parseFrom(array, offset, length, (ExtensionRegistryLite)this.extensionRegistry));
        }
        else {
            out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length, (ExtensionRegistryLite)this.extensionRegistry).build());
        }
    }
    
    static {
        boolean hasParser = false;
        try {
            MessageLite.class.getDeclaredMethod("getParserForType", (Class<?>[])new Class[0]);
            hasParser = true;
        }
        catch (Throwable t) {}
        HAS_PARSER = hasParser;
    }
}
