// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import java.util.Map;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SpdyHttpEncoder extends MessageToMessageEncoder<HttpObject>
{
    private final int spdyVersion;
    private int currentStreamId;
    
    public SpdyHttpEncoder(final SpdyVersion version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.spdyVersion = version.getVersion();
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        boolean valid = false;
        boolean last = false;
        if (msg instanceof HttpRequest) {
            final HttpRequest httpRequest = (HttpRequest)msg;
            final SpdySynStreamFrame spdySynStreamFrame = this.createSynStreamFrame(httpRequest);
            out.add(spdySynStreamFrame);
            last = spdySynStreamFrame.isLast();
            valid = true;
        }
        if (msg instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse)msg;
            if (httpResponse.headers().contains("X-SPDY-Associated-To-Stream-ID")) {
                final SpdySynStreamFrame spdySynStreamFrame = this.createSynStreamFrame(httpResponse);
                last = spdySynStreamFrame.isLast();
                out.add(spdySynStreamFrame);
            }
            else {
                final SpdySynReplyFrame spdySynReplyFrame = this.createSynReplyFrame(httpResponse);
                last = spdySynReplyFrame.isLast();
                out.add(spdySynReplyFrame);
            }
            valid = true;
        }
        if (msg instanceof HttpContent && !last) {
            final HttpContent chunk = (HttpContent)msg;
            chunk.content().retain();
            final SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
            spdyDataFrame.setLast(chunk instanceof LastHttpContent);
            if (chunk instanceof LastHttpContent) {
                final LastHttpContent trailer = (LastHttpContent)chunk;
                final HttpHeaders trailers = trailer.trailingHeaders();
                if (trailers.isEmpty()) {
                    out.add(spdyDataFrame);
                }
                else {
                    final SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(this.currentStreamId);
                    for (final Map.Entry<String, String> entry : trailers) {
                        spdyHeadersFrame.headers().add(entry.getKey(), entry.getValue());
                    }
                    out.add(spdyHeadersFrame);
                    out.add(spdyDataFrame);
                }
            }
            else {
                out.add(spdyDataFrame);
            }
            valid = true;
        }
        if (!valid) {
            throw new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[0]);
        }
    }
    
    private SpdySynStreamFrame createSynStreamFrame(final HttpMessage httpMessage) throws Exception {
        final int streamID = SpdyHttpHeaders.getStreamId(httpMessage);
        final int associatedToStreamId = SpdyHttpHeaders.getAssociatedToStreamId(httpMessage);
        final byte priority = SpdyHttpHeaders.getPriority(httpMessage);
        final String URL = SpdyHttpHeaders.getUrl(httpMessage);
        String scheme = SpdyHttpHeaders.getScheme(httpMessage);
        SpdyHttpHeaders.removeStreamId(httpMessage);
        SpdyHttpHeaders.removeAssociatedToStreamId(httpMessage);
        SpdyHttpHeaders.removePriority(httpMessage);
        SpdyHttpHeaders.removeUrl(httpMessage);
        SpdyHttpHeaders.removeScheme(httpMessage);
        httpMessage.headers().remove("Connection");
        httpMessage.headers().remove("Keep-Alive");
        httpMessage.headers().remove("Proxy-Connection");
        httpMessage.headers().remove("Transfer-Encoding");
        final SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamID, associatedToStreamId, priority);
        if (httpMessage instanceof FullHttpRequest) {
            final HttpRequest httpRequest = (HttpRequest)httpMessage;
            SpdyHeaders.setMethod(this.spdyVersion, spdySynStreamFrame, httpRequest.getMethod());
            SpdyHeaders.setUrl(this.spdyVersion, spdySynStreamFrame, httpRequest.getUri());
            SpdyHeaders.setVersion(this.spdyVersion, spdySynStreamFrame, httpMessage.getProtocolVersion());
        }
        if (httpMessage instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse)httpMessage;
            SpdyHeaders.setStatus(this.spdyVersion, spdySynStreamFrame, httpResponse.getStatus());
            SpdyHeaders.setUrl(this.spdyVersion, spdySynStreamFrame, URL);
            SpdyHeaders.setVersion(this.spdyVersion, spdySynStreamFrame, httpMessage.getProtocolVersion());
            spdySynStreamFrame.setUnidirectional(true);
        }
        if (this.spdyVersion >= 3) {
            final String host = HttpHeaders.getHost(httpMessage);
            httpMessage.headers().remove("Host");
            SpdyHeaders.setHost(spdySynStreamFrame, host);
        }
        if (scheme == null) {
            scheme = "https";
        }
        SpdyHeaders.setScheme(this.spdyVersion, spdySynStreamFrame, scheme);
        for (final Map.Entry<String, String> entry : httpMessage.headers()) {
            spdySynStreamFrame.headers().add(entry.getKey(), entry.getValue());
        }
        this.currentStreamId = spdySynStreamFrame.streamId();
        spdySynStreamFrame.setLast(isLast(httpMessage));
        return spdySynStreamFrame;
    }
    
    private SpdySynReplyFrame createSynReplyFrame(final HttpResponse httpResponse) throws Exception {
        final int streamID = SpdyHttpHeaders.getStreamId(httpResponse);
        SpdyHttpHeaders.removeStreamId(httpResponse);
        httpResponse.headers().remove("Connection");
        httpResponse.headers().remove("Keep-Alive");
        httpResponse.headers().remove("Proxy-Connection");
        httpResponse.headers().remove("Transfer-Encoding");
        final SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamID);
        SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, httpResponse.getStatus());
        SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, httpResponse.getProtocolVersion());
        for (final Map.Entry<String, String> entry : httpResponse.headers()) {
            spdySynReplyFrame.headers().add(entry.getKey(), entry.getValue());
        }
        this.currentStreamId = streamID;
        spdySynReplyFrame.setLast(isLast(httpResponse));
        return spdySynReplyFrame;
    }
    
    private static boolean isLast(final HttpMessage httpMessage) {
        if (httpMessage instanceof FullHttpMessage) {
            final FullHttpMessage fullMessage = (FullHttpMessage)httpMessage;
            if (fullMessage.trailingHeaders().isEmpty() && !fullMessage.content().isReadable()) {
                return true;
            }
        }
        return false;
    }
}
