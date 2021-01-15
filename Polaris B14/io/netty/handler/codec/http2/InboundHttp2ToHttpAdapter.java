/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpStatusClass;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InboundHttp2ToHttpAdapter
/*     */   extends Http2EventAdapter
/*     */ {
/*  40 */   private static final ImmediateSendDetector DEFAULT_SEND_DETECTOR = new ImmediateSendDetector()
/*     */   {
/*     */     public boolean mustSendImmediately(FullHttpMessage msg) {
/*  43 */       if ((msg instanceof FullHttpResponse)) {
/*  44 */         return ((FullHttpResponse)msg).status().codeClass() == HttpStatusClass.INFORMATIONAL;
/*     */       }
/*  46 */       if ((msg instanceof FullHttpRequest)) {
/*  47 */         return msg.headers().contains(HttpHeaderNames.EXPECT);
/*     */       }
/*  49 */       return false;
/*     */     }
/*     */     
/*     */     public FullHttpMessage copyIfNeeded(FullHttpMessage msg)
/*     */     {
/*  54 */       if ((msg instanceof FullHttpRequest)) {
/*  55 */         FullHttpRequest copy = ((FullHttpRequest)msg).copy(null);
/*  56 */         copy.headers().remove(HttpHeaderNames.EXPECT);
/*  57 */         return copy;
/*     */       }
/*  59 */       return null;
/*     */     }
/*     */   };
/*     */   private final int maxContentLength;
/*     */   protected final Http2Connection connection;
/*     */   protected final boolean validateHttpHeaders;
/*     */   private final ImmediateSendDetector sendDetector;
/*     */   protected final IntObjectMap<FullHttpMessage> messageMap;
/*     */   private final boolean propagateSettings;
/*     */   
/*     */   private static abstract interface ImmediateSendDetector
/*     */   {
/*     */     public abstract boolean mustSendImmediately(FullHttpMessage paramFullHttpMessage);
/*     */     
/*     */     public abstract FullHttpMessage copyIfNeeded(FullHttpMessage paramFullHttpMessage);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Http2Connection connection;
/*     */     private int maxContentLength;
/*     */     private boolean validateHttpHeaders;
/*     */     private boolean propagateSettings;
/*     */     
/*     */     public Builder(Http2Connection connection) {
/*  83 */       this.connection = connection;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder maxContentLength(int maxContentLength)
/*     */     {
/*  94 */       this.maxContentLength = maxContentLength;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder validateHttpHeaders(boolean validate)
/*     */     {
/* 109 */       this.validateHttpHeaders = validate;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder propagateSettings(boolean propagate)
/*     */     {
/* 121 */       this.propagateSettings = propagate;
/* 122 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public InboundHttp2ToHttpAdapter build()
/*     */     {
/* 129 */       InboundHttp2ToHttpAdapter instance = new InboundHttp2ToHttpAdapter(this);
/* 130 */       this.connection.addListener(instance);
/* 131 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   protected InboundHttp2ToHttpAdapter(Builder builder) {
/* 136 */     ObjectUtil.checkNotNull(builder.connection, "connection");
/* 137 */     if (builder.maxContentLength <= 0) {
/* 138 */       throw new IllegalArgumentException("maxContentLength must be a positive integer: " + builder.maxContentLength);
/*     */     }
/*     */     
/* 141 */     this.connection = builder.connection;
/* 142 */     this.maxContentLength = builder.maxContentLength;
/* 143 */     this.validateHttpHeaders = builder.validateHttpHeaders;
/* 144 */     this.propagateSettings = builder.propagateSettings;
/* 145 */     this.sendDetector = DEFAULT_SEND_DETECTOR;
/* 146 */     this.messageMap = new IntObjectHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeMessage(int streamId)
/*     */   {
/* 154 */     this.messageMap.remove(streamId);
/*     */   }
/*     */   
/*     */   public void streamRemoved(Http2Stream stream)
/*     */   {
/* 159 */     removeMessage(stream.id());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireChannelRead(ChannelHandlerContext ctx, FullHttpMessage msg, int streamId)
/*     */   {
/* 170 */     removeMessage(streamId);
/* 171 */     HttpHeaderUtil.setContentLength(msg, msg.content().readableBytes());
/* 172 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FullHttpMessage newMessage(int streamId, Http2Headers headers, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 189 */     return this.connection.isServer() ? HttpUtil.toHttpRequest(streamId, headers, validateHttpHeaders) : HttpUtil.toHttpResponse(streamId, headers, validateHttpHeaders);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FullHttpMessage processHeadersBegin(ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean endOfStream, boolean allowAppend, boolean appendToTrailer)
/*     */     throws Http2Exception
/*     */   {
/* 219 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(streamId);
/* 220 */     if (msg == null) {
/* 221 */       msg = newMessage(streamId, headers, this.validateHttpHeaders);
/* 222 */     } else if (allowAppend) {
/*     */       try {
/* 224 */         HttpUtil.addHttp2ToHttpHeaders(streamId, headers, msg, appendToTrailer);
/*     */       } catch (Http2Exception e) {
/* 226 */         removeMessage(streamId);
/* 227 */         throw e;
/*     */       }
/*     */     } else {
/* 230 */       msg = null;
/*     */     }
/*     */     
/* 233 */     if (this.sendDetector.mustSendImmediately(msg))
/*     */     {
/*     */ 
/* 236 */       FullHttpMessage copy = endOfStream ? null : this.sendDetector.copyIfNeeded(msg);
/* 237 */       fireChannelRead(ctx, msg, streamId);
/* 238 */       return copy;
/*     */     }
/*     */     
/* 241 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processHeadersEnd(ChannelHandlerContext ctx, int streamId, FullHttpMessage msg, boolean endOfStream)
/*     */   {
/* 255 */     if (endOfStream) {
/* 256 */       fireChannelRead(ctx, msg, streamId);
/*     */     } else {
/* 258 */       this.messageMap.put(streamId, msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/* 265 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(streamId);
/* 266 */     if (msg == null) {
/* 267 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Data Frame recieved for unknown stream id %d", new Object[] { Integer.valueOf(streamId) });
/*     */     }
/*     */     
/* 270 */     ByteBuf content = msg.content();
/* 271 */     int dataReadableBytes = data.readableBytes();
/* 272 */     if (content.readableBytes() > this.maxContentLength - dataReadableBytes) {
/* 273 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Content length exceeded max of %d for stream id %d", new Object[] { Integer.valueOf(this.maxContentLength), Integer.valueOf(streamId) });
/*     */     }
/*     */     
/*     */ 
/* 277 */     content.writeBytes(data, data.readerIndex(), dataReadableBytes);
/*     */     
/* 279 */     if (endOfStream) {
/* 280 */       fireChannelRead(ctx, msg, streamId);
/*     */     }
/*     */     
/*     */ 
/* 284 */     return dataReadableBytes + padding;
/*     */   }
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/* 290 */     FullHttpMessage msg = processHeadersBegin(ctx, streamId, headers, endOfStream, true, true);
/* 291 */     if (msg != null) {
/* 292 */       processHeadersEnd(ctx, streamId, msg, endOfStream);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream)
/*     */     throws Http2Exception
/*     */   {
/* 299 */     FullHttpMessage msg = processHeadersBegin(ctx, streamId, headers, endOfStream, true, true);
/* 300 */     if (msg != null) {
/* 301 */       processHeadersEnd(ctx, streamId, msg, endOfStream);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception
/*     */   {
/* 307 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(streamId);
/* 308 */     if (msg != null) {
/* 309 */       fireChannelRead(ctx, msg, streamId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */     throws Http2Exception
/*     */   {
/* 317 */     FullHttpMessage msg = processHeadersBegin(ctx, promisedStreamId, headers, false, false, false);
/* 318 */     if (msg == null) {
/* 319 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Push Promise Frame recieved for pre-existing stream id %d", new Object[] { Integer.valueOf(promisedStreamId) });
/*     */     }
/*     */     
/*     */ 
/* 323 */     msg.headers().setInt(HttpUtil.ExtensionHeaderNames.STREAM_PROMISE_ID.text(), streamId);
/*     */     
/* 325 */     processHeadersEnd(ctx, promisedStreamId, msg, false);
/*     */   }
/*     */   
/*     */   public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception
/*     */   {
/* 330 */     if (this.propagateSettings)
/*     */     {
/* 332 */       ctx.fireChannelRead(settings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\InboundHttp2ToHttpAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */