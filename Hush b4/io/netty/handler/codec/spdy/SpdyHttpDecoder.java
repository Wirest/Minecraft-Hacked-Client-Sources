// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import io.netty.handler.codec.http.FullHttpMessage;
import java.util.Map;
import io.netty.handler.codec.MessageToMessageDecoder;

public class SpdyHttpDecoder extends MessageToMessageDecoder<SpdyFrame>
{
    private final boolean validateHeaders;
    private final int spdyVersion;
    private final int maxContentLength;
    private final Map<Integer, FullHttpMessage> messageMap;
    
    public SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength) {
        this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), true);
    }
    
    public SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final boolean validateHeaders) {
        this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), validateHeaders);
    }
    
    protected SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final Map<Integer, FullHttpMessage> messageMap) {
        this(version, maxContentLength, messageMap, true);
    }
    
    protected SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final Map<Integer, FullHttpMessage> messageMap, final boolean validateHeaders) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
        }
        this.spdyVersion = version.getVersion();
        this.maxContentLength = maxContentLength;
        this.messageMap = messageMap;
        this.validateHeaders = validateHeaders;
    }
    
    protected FullHttpMessage putMessage(final int streamId, final FullHttpMessage message) {
        return this.messageMap.put(streamId, message);
    }
    
    protected FullHttpMessage getMessage(final int streamId) {
        return this.messageMap.get(streamId);
    }
    
    protected FullHttpMessage removeMessage(final int streamId) {
        return this.messageMap.remove(streamId);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final SpdyFrame msg, final List<Object> out) throws Exception {
        if (msg instanceof SpdySynStreamFrame) {
            final SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            final int streamId = spdySynStreamFrame.streamId();
            if (SpdyCodecUtil.isServerId(streamId)) {
                final int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
                if (associatedToStreamId == 0) {
                    final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                final String URL = SpdyHeaders.getUrl(this.spdyVersion, spdySynStreamFrame);
                if (URL == null) {
                    final SpdyRstStreamFrame spdyRstStreamFrame2 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame2);
                    return;
                }
                if (spdySynStreamFrame.isTruncated()) {
                    final SpdyRstStreamFrame spdyRstStreamFrame2 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame2);
                    return;
                }
                try {
                    final FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, this.spdyVersion, spdySynStreamFrame, this.validateHeaders);
                    SpdyHttpHeaders.setStreamId(httpResponseWithEntity, streamId);
                    SpdyHttpHeaders.setAssociatedToStreamId(httpResponseWithEntity, associatedToStreamId);
                    SpdyHttpHeaders.setPriority(httpResponseWithEntity, spdySynStreamFrame.priority());
                    SpdyHttpHeaders.setUrl(httpResponseWithEntity, URL);
                    if (spdySynStreamFrame.isLast()) {
                        HttpHeaders.setContentLength(httpResponseWithEntity, 0L);
                        out.add(httpResponseWithEntity);
                    }
                    else {
                        this.putMessage(streamId, httpResponseWithEntity);
                    }
                }
                catch (Exception e2) {
                    final SpdyRstStreamFrame spdyRstStreamFrame3 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame3);
                }
            }
            else {
                if (spdySynStreamFrame.isTruncated()) {
                    final SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                    spdySynReplyFrame.setLast(true);
                    SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
                    SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
                    ctx.writeAndFlush(spdySynReplyFrame);
                    return;
                }
                try {
                    final FullHttpRequest httpRequestWithEntity = createHttpRequest(this.spdyVersion, spdySynStreamFrame);
                    SpdyHttpHeaders.setStreamId(httpRequestWithEntity, streamId);
                    if (spdySynStreamFrame.isLast()) {
                        out.add(httpRequestWithEntity);
                    }
                    else {
                        this.putMessage(streamId, httpRequestWithEntity);
                    }
                }
                catch (Exception e3) {
                    final SpdySynReplyFrame spdySynReplyFrame2 = new DefaultSpdySynReplyFrame(streamId);
                    spdySynReplyFrame2.setLast(true);
                    SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame2, HttpResponseStatus.BAD_REQUEST);
                    SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame2, HttpVersion.HTTP_1_0);
                    ctx.writeAndFlush(spdySynReplyFrame2);
                }
            }
        }
        else if (msg instanceof SpdySynReplyFrame) {
            final SpdySynReplyFrame spdySynReplyFrame3 = (SpdySynReplyFrame)msg;
            final int streamId = spdySynReplyFrame3.streamId();
            if (spdySynReplyFrame3.isTruncated()) {
                final SpdyRstStreamFrame spdyRstStreamFrame4 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame4);
                return;
            }
            try {
                final FullHttpResponse httpResponseWithEntity2 = createHttpResponse(ctx, this.spdyVersion, spdySynReplyFrame3, this.validateHeaders);
                SpdyHttpHeaders.setStreamId(httpResponseWithEntity2, streamId);
                if (spdySynReplyFrame3.isLast()) {
                    HttpHeaders.setContentLength(httpResponseWithEntity2, 0L);
                    out.add(httpResponseWithEntity2);
                }
                else {
                    this.putMessage(streamId, httpResponseWithEntity2);
                }
            }
            catch (Exception e3) {
                final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame);
            }
        }
        else if (msg instanceof SpdyHeadersFrame) {
            final SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
            final int streamId = spdyHeadersFrame.streamId();
            final FullHttpMessage fullHttpMessage = this.getMessage(streamId);
            if (fullHttpMessage == null) {
                return;
            }
            if (!spdyHeadersFrame.isTruncated()) {
                for (final Map.Entry<String, String> e : spdyHeadersFrame.headers()) {
                    fullHttpMessage.headers().add(e.getKey(), e.getValue());
                }
            }
            if (spdyHeadersFrame.isLast()) {
                HttpHeaders.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
                this.removeMessage(streamId);
                out.add(fullHttpMessage);
            }
        }
        else if (msg instanceof SpdyDataFrame) {
            final SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            final int streamId = spdyDataFrame.streamId();
            final FullHttpMessage fullHttpMessage = this.getMessage(streamId);
            if (fullHttpMessage == null) {
                return;
            }
            final ByteBuf content = fullHttpMessage.content();
            if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
                this.removeMessage(streamId);
                throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }
            final ByteBuf spdyDataFrameData = spdyDataFrame.content();
            final int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
            content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
            if (spdyDataFrame.isLast()) {
                HttpHeaders.setContentLength(fullHttpMessage, content.readableBytes());
                this.removeMessage(streamId);
                out.add(fullHttpMessage);
            }
        }
        else if (msg instanceof SpdyRstStreamFrame) {
            final SpdyRstStreamFrame spdyRstStreamFrame5 = (SpdyRstStreamFrame)msg;
            final int streamId = spdyRstStreamFrame5.streamId();
            this.removeMessage(streamId);
        }
    }
    
    private static FullHttpRequest createHttpRequest(final int spdyVersion, final SpdyHeadersFrame requestFrame) throws Exception {
        final SpdyHeaders headers = requestFrame.headers();
        final HttpMethod method = SpdyHeaders.getMethod(spdyVersion, requestFrame);
        final String url = SpdyHeaders.getUrl(spdyVersion, requestFrame);
        final HttpVersion httpVersion = SpdyHeaders.getVersion(spdyVersion, requestFrame);
        SpdyHeaders.removeMethod(spdyVersion, requestFrame);
        SpdyHeaders.removeUrl(spdyVersion, requestFrame);
        SpdyHeaders.removeVersion(spdyVersion, requestFrame);
        final FullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url);
        SpdyHeaders.removeScheme(spdyVersion, requestFrame);
        final String host = headers.get(":host");
        headers.remove(":host");
        req.headers().set("Host", host);
        for (final Map.Entry<String, String> e : requestFrame.headers()) {
            req.headers().add(e.getKey(), e.getValue());
        }
        HttpHeaders.setKeepAlive(req, true);
        req.headers().remove("Transfer-Encoding");
        return req;
    }
    
    private static FullHttpResponse createHttpResponse(final ChannelHandlerContext ctx, final int spdyVersion, final SpdyHeadersFrame responseFrame, final boolean validateHeaders) throws Exception {
        final HttpResponseStatus status = SpdyHeaders.getStatus(spdyVersion, responseFrame);
        final HttpVersion version = SpdyHeaders.getVersion(spdyVersion, responseFrame);
        SpdyHeaders.removeStatus(spdyVersion, responseFrame);
        SpdyHeaders.removeVersion(spdyVersion, responseFrame);
        final FullHttpResponse res = new DefaultFullHttpResponse(version, status, ctx.alloc().buffer(), validateHeaders);
        for (final Map.Entry<String, String> e : responseFrame.headers()) {
            res.headers().add(e.getKey(), e.getValue());
        }
        HttpHeaders.setKeepAlive(res, true);
        res.headers().remove("Transfer-Encoding");
        res.headers().remove("Trailer");
        return res;
    }
}
