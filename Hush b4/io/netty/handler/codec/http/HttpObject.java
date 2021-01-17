// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;

public interface HttpObject
{
    DecoderResult getDecoderResult();
    
    void setDecoderResult(final DecoderResult p0);
}
