// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

public class WebSocket07FrameDecoder extends WebSocket08FrameDecoder
{
    public WebSocket07FrameDecoder(final boolean maskedPayload, final boolean allowExtensions, final int maxFramePayloadLength) {
        super(maskedPayload, allowExtensions, maxFramePayloadLength);
    }
}
