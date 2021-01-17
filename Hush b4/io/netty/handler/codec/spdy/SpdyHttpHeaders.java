// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;

public final class SpdyHttpHeaders
{
    private SpdyHttpHeaders() {
    }
    
    public static void removeStreamId(final HttpMessage message) {
        message.headers().remove("X-SPDY-Stream-ID");
    }
    
    public static int getStreamId(final HttpMessage message) {
        return HttpHeaders.getIntHeader(message, "X-SPDY-Stream-ID");
    }
    
    public static void setStreamId(final HttpMessage message, final int streamId) {
        HttpHeaders.setIntHeader(message, "X-SPDY-Stream-ID", streamId);
    }
    
    public static void removeAssociatedToStreamId(final HttpMessage message) {
        message.headers().remove("X-SPDY-Associated-To-Stream-ID");
    }
    
    public static int getAssociatedToStreamId(final HttpMessage message) {
        return HttpHeaders.getIntHeader(message, "X-SPDY-Associated-To-Stream-ID", 0);
    }
    
    public static void setAssociatedToStreamId(final HttpMessage message, final int associatedToStreamId) {
        HttpHeaders.setIntHeader(message, "X-SPDY-Associated-To-Stream-ID", associatedToStreamId);
    }
    
    public static void removePriority(final HttpMessage message) {
        message.headers().remove("X-SPDY-Priority");
    }
    
    public static byte getPriority(final HttpMessage message) {
        return (byte)HttpHeaders.getIntHeader(message, "X-SPDY-Priority", 0);
    }
    
    public static void setPriority(final HttpMessage message, final byte priority) {
        HttpHeaders.setIntHeader(message, "X-SPDY-Priority", priority);
    }
    
    public static void removeUrl(final HttpMessage message) {
        message.headers().remove("X-SPDY-URL");
    }
    
    public static String getUrl(final HttpMessage message) {
        return message.headers().get("X-SPDY-URL");
    }
    
    public static void setUrl(final HttpMessage message, final String url) {
        message.headers().set("X-SPDY-URL", url);
    }
    
    public static void removeScheme(final HttpMessage message) {
        message.headers().remove("X-SPDY-Scheme");
    }
    
    public static String getScheme(final HttpMessage message) {
        return message.headers().get("X-SPDY-Scheme");
    }
    
    public static void setScheme(final HttpMessage message, final String scheme) {
        message.headers().set("X-SPDY-Scheme", scheme);
    }
    
    public static final class Names
    {
        public static final String STREAM_ID = "X-SPDY-Stream-ID";
        public static final String ASSOCIATED_TO_STREAM_ID = "X-SPDY-Associated-To-Stream-ID";
        public static final String PRIORITY = "X-SPDY-Priority";
        public static final String URL = "X-SPDY-URL";
        public static final String SCHEME = "X-SPDY-Scheme";
        
        private Names() {
        }
    }
}
