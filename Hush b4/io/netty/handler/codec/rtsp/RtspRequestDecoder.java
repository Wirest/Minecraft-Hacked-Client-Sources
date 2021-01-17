// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMessage;

public class RtspRequestDecoder extends RtspObjectDecoder
{
    public RtspRequestDecoder() {
    }
    
    public RtspRequestDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxContentLength) {
        super(maxInitialLineLength, maxHeaderSize, maxContentLength);
    }
    
    public RtspRequestDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxContentLength, final boolean validateHeaders) {
        super(maxInitialLineLength, maxHeaderSize, maxContentLength, validateHeaders);
    }
    
    @Override
    protected HttpMessage createMessage(final String[] initialLine) throws Exception {
        return new DefaultHttpRequest(RtspVersions.valueOf(initialLine[2]), RtspMethods.valueOf(initialLine[0]), initialLine[1], this.validateHeaders);
    }
    
    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
    }
    
    @Override
    protected boolean isDecodingRequest() {
        return true;
    }
}
