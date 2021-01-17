// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public final class Delimiters
{
    public static ByteBuf[] nulDelimiter() {
        return new ByteBuf[] { Unpooled.wrappedBuffer(new byte[] { 0 }) };
    }
    
    public static ByteBuf[] lineDelimiter() {
        return new ByteBuf[] { Unpooled.wrappedBuffer(new byte[] { 13, 10 }), Unpooled.wrappedBuffer(new byte[] { 10 }) };
    }
    
    private Delimiters() {
    }
}
