// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class ZlibDecoder extends ByteToMessageDecoder
{
    public abstract boolean isClosed();
}
