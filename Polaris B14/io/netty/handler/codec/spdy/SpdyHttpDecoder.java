/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ public class SpdyHttpDecoder
/*     */   extends MessageToMessageDecoder<SpdyFrame>
/*     */ {
/*     */   private final boolean validateHeaders;
/*     */   private final int spdyVersion;
/*     */   private final int maxContentLength;
/*     */   private final Map<Integer, FullHttpMessage> messageMap;
/*     */   
/*     */   public SpdyHttpDecoder(SpdyVersion version, int maxContentLength)
/*     */   {
/*  60 */     this(version, maxContentLength, new HashMap(), true);
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
/*     */   public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, boolean validateHeaders)
/*     */   {
/*  73 */     this(version, maxContentLength, new HashMap(), validateHeaders);
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
/*     */   protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap)
/*     */   {
/*  86 */     this(version, maxContentLength, messageMap, true);
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
/*     */   protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, boolean validateHeaders)
/*     */   {
/* 101 */     if (version == null) {
/* 102 */       throw new NullPointerException("version");
/*     */     }
/* 104 */     if (maxContentLength <= 0) {
/* 105 */       throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
/*     */     }
/*     */     
/* 108 */     this.spdyVersion = version.getVersion();
/* 109 */     this.maxContentLength = maxContentLength;
/* 110 */     this.messageMap = messageMap;
/* 111 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */   
/*     */   protected FullHttpMessage putMessage(int streamId, FullHttpMessage message) {
/* 115 */     return (FullHttpMessage)this.messageMap.put(Integer.valueOf(streamId), message);
/*     */   }
/*     */   
/*     */   protected FullHttpMessage getMessage(int streamId) {
/* 119 */     return (FullHttpMessage)this.messageMap.get(Integer.valueOf(streamId));
/*     */   }
/*     */   
/*     */   protected FullHttpMessage removeMessage(int streamId) {
/* 123 */     return (FullHttpMessage)this.messageMap.remove(Integer.valueOf(streamId));
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 129 */     if ((msg instanceof SpdySynStreamFrame))
/*     */     {
/*     */ 
/* 132 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 133 */       int streamId = spdySynStreamFrame.streamId();
/*     */       
/* 135 */       if (SpdyCodecUtil.isServerId(streamId))
/*     */       {
/* 137 */         int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
/*     */         
/*     */ 
/*     */ 
/* 141 */         if (associatedToStreamId == 0) {
/* 142 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */           
/* 144 */           ctx.writeAndFlush(spdyRstStreamFrame);
/* 145 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 151 */         if (spdySynStreamFrame.isLast()) {
/* 152 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */           
/* 154 */           ctx.writeAndFlush(spdyRstStreamFrame);
/* 155 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 160 */         if (spdySynStreamFrame.isTruncated()) {
/* 161 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */           
/* 163 */           ctx.writeAndFlush(spdyRstStreamFrame);
/* 164 */           return;
/*     */         }
/*     */         try
/*     */         {
/* 168 */           FullHttpRequest httpRequestWithEntity = createHttpRequest(this.spdyVersion, spdySynStreamFrame);
/*     */           
/*     */ 
/* 171 */           httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
/* 172 */           httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, associatedToStreamId);
/* 173 */           httpRequestWithEntity.headers().setByte(SpdyHttpHeaders.Names.PRIORITY, spdySynStreamFrame.priority());
/*     */           
/* 175 */           out.add(httpRequestWithEntity);
/*     */         }
/*     */         catch (Exception ignored) {
/* 178 */           SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */           
/* 180 */           ctx.writeAndFlush(spdyRstStreamFrame);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 187 */         if (spdySynStreamFrame.isTruncated()) {
/* 188 */           SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
/* 189 */           spdySynReplyFrame.setLast(true);
/* 190 */           SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
/* 191 */           frameHeaders.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.code());
/* 192 */           frameHeaders.setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
/* 193 */           ctx.writeAndFlush(spdySynReplyFrame);
/* 194 */           return;
/*     */         }
/*     */         try
/*     */         {
/* 198 */           FullHttpRequest httpRequestWithEntity = createHttpRequest(this.spdyVersion, spdySynStreamFrame);
/*     */           
/*     */ 
/* 201 */           httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */           
/* 203 */           if (spdySynStreamFrame.isLast()) {
/* 204 */             out.add(httpRequestWithEntity);
/*     */           }
/*     */           else {
/* 207 */             putMessage(streamId, httpRequestWithEntity);
/*     */           }
/*     */           
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 213 */           SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
/* 214 */           spdySynReplyFrame.setLast(true);
/* 215 */           SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
/* 216 */           frameHeaders.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.BAD_REQUEST.code());
/* 217 */           frameHeaders.setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
/* 218 */           ctx.writeAndFlush(spdySynReplyFrame);
/*     */         }
/*     */       }
/*     */     }
/* 222 */     else if ((msg instanceof SpdySynReplyFrame))
/*     */     {
/* 224 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 225 */       int streamId = spdySynReplyFrame.streamId();
/*     */       
/*     */ 
/*     */ 
/* 229 */       if (spdySynReplyFrame.isTruncated()) {
/* 230 */         SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */         
/* 232 */         ctx.writeAndFlush(spdyRstStreamFrame);
/* 233 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 237 */         FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, spdySynReplyFrame, this.validateHeaders);
/*     */         
/*     */ 
/* 240 */         httpResponseWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */         
/* 242 */         if (spdySynReplyFrame.isLast()) {
/* 243 */           HttpHeaderUtil.setContentLength(httpResponseWithEntity, 0L);
/* 244 */           out.add(httpResponseWithEntity);
/*     */         }
/*     */         else {
/* 247 */           putMessage(streamId, httpResponseWithEntity);
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 252 */         SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */         
/* 254 */         ctx.writeAndFlush(spdyRstStreamFrame);
/*     */       }
/*     */     }
/* 257 */     else if ((msg instanceof SpdyHeadersFrame))
/*     */     {
/* 259 */       SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 260 */       int streamId = spdyHeadersFrame.streamId();
/* 261 */       FullHttpMessage fullHttpMessage = getMessage(streamId);
/*     */       
/* 263 */       if (fullHttpMessage == null)
/*     */       {
/* 265 */         if (SpdyCodecUtil.isServerId(streamId))
/*     */         {
/*     */ 
/*     */ 
/* 269 */           if (spdyHeadersFrame.isTruncated()) {
/* 270 */             SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
/*     */             
/* 272 */             ctx.writeAndFlush(spdyRstStreamFrame);
/* 273 */             return;
/*     */           }
/*     */           try
/*     */           {
/* 277 */             fullHttpMessage = createHttpResponse(ctx, spdyHeadersFrame, this.validateHeaders);
/*     */             
/*     */ 
/* 280 */             fullHttpMessage.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
/*     */             
/* 282 */             if (spdyHeadersFrame.isLast()) {
/* 283 */               HttpHeaderUtil.setContentLength(fullHttpMessage, 0L);
/* 284 */               out.add(fullHttpMessage);
/*     */             }
/*     */             else {
/* 287 */               putMessage(streamId, fullHttpMessage);
/*     */             }
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 292 */             SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/*     */             
/* 294 */             ctx.writeAndFlush(spdyRstStreamFrame);
/*     */           }
/*     */         }
/* 297 */         return;
/*     */       }
/*     */       
/*     */ 
/* 301 */       if (!spdyHeadersFrame.isTruncated()) {
/* 302 */         for (Map.Entry<CharSequence, CharSequence> e : spdyHeadersFrame.headers()) {
/* 303 */           fullHttpMessage.headers().add((CharSequence)e.getKey(), (CharSequence)e.getValue());
/*     */         }
/*     */       }
/*     */       
/* 307 */       if (spdyHeadersFrame.isLast()) {
/* 308 */         HttpHeaderUtil.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
/* 309 */         removeMessage(streamId);
/* 310 */         out.add(fullHttpMessage);
/*     */       }
/*     */     }
/* 313 */     else if ((msg instanceof SpdyDataFrame))
/*     */     {
/* 315 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 316 */       int streamId = spdyDataFrame.streamId();
/* 317 */       FullHttpMessage fullHttpMessage = getMessage(streamId);
/*     */       
/*     */ 
/* 320 */       if (fullHttpMessage == null) {
/* 321 */         return;
/*     */       }
/*     */       
/* 324 */       ByteBuf content = fullHttpMessage.content();
/* 325 */       if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
/* 326 */         removeMessage(streamId);
/* 327 */         throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
/*     */       }
/*     */       
/*     */ 
/* 331 */       ByteBuf spdyDataFrameData = spdyDataFrame.content();
/* 332 */       int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
/* 333 */       content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
/*     */       
/* 335 */       if (spdyDataFrame.isLast()) {
/* 336 */         HttpHeaderUtil.setContentLength(fullHttpMessage, content.readableBytes());
/* 337 */         removeMessage(streamId);
/* 338 */         out.add(fullHttpMessage);
/*     */       }
/*     */     }
/* 341 */     else if ((msg instanceof SpdyRstStreamFrame))
/*     */     {
/* 343 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 344 */       int streamId = spdyRstStreamFrame.streamId();
/* 345 */       removeMessage(streamId);
/*     */     }
/*     */   }
/*     */   
/*     */   private static FullHttpRequest createHttpRequest(int spdyVersion, SpdyHeadersFrame requestFrame)
/*     */     throws Exception
/*     */   {
/* 352 */     SpdyHeaders headers = requestFrame.headers();
/* 353 */     HttpMethod method = HttpMethod.valueOf((String)headers.getAndConvert(SpdyHeaders.HttpNames.METHOD));
/* 354 */     String url = (String)headers.getAndConvert(SpdyHeaders.HttpNames.PATH);
/* 355 */     HttpVersion httpVersion = HttpVersion.valueOf((String)headers.getAndConvert(SpdyHeaders.HttpNames.VERSION));
/* 356 */     headers.remove(SpdyHeaders.HttpNames.METHOD);
/* 357 */     headers.remove(SpdyHeaders.HttpNames.PATH);
/* 358 */     headers.remove(SpdyHeaders.HttpNames.VERSION);
/*     */     
/* 360 */     FullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url);
/*     */     
/*     */ 
/* 363 */     headers.remove(SpdyHeaders.HttpNames.SCHEME);
/*     */     
/*     */ 
/* 366 */     CharSequence host = (CharSequence)headers.get(SpdyHeaders.HttpNames.HOST);
/* 367 */     headers.remove(SpdyHeaders.HttpNames.HOST);
/* 368 */     req.headers().set(HttpHeaderNames.HOST, host);
/*     */     
/* 370 */     for (Map.Entry<CharSequence, CharSequence> e : requestFrame.headers()) {
/* 371 */       req.headers().add((CharSequence)e.getKey(), (CharSequence)e.getValue());
/*     */     }
/*     */     
/*     */ 
/* 375 */     HttpHeaderUtil.setKeepAlive(req, true);
/*     */     
/*     */ 
/* 378 */     req.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/*     */     
/* 380 */     return req;
/*     */   }
/*     */   
/*     */ 
/*     */   private static FullHttpResponse createHttpResponse(ChannelHandlerContext ctx, SpdyHeadersFrame responseFrame, boolean validateHeaders)
/*     */     throws Exception
/*     */   {
/* 387 */     SpdyHeaders headers = responseFrame.headers();
/* 388 */     HttpResponseStatus status = HttpResponseStatus.parseLine((CharSequence)headers.get(SpdyHeaders.HttpNames.STATUS));
/* 389 */     HttpVersion version = HttpVersion.valueOf((String)headers.getAndConvert(SpdyHeaders.HttpNames.VERSION));
/* 390 */     headers.remove(SpdyHeaders.HttpNames.STATUS);
/* 391 */     headers.remove(SpdyHeaders.HttpNames.VERSION);
/*     */     
/* 393 */     FullHttpResponse res = new DefaultFullHttpResponse(version, status, ctx.alloc().buffer(), validateHeaders);
/* 394 */     for (Map.Entry<CharSequence, CharSequence> e : responseFrame.headers()) {
/* 395 */       res.headers().add((CharSequence)e.getKey(), (CharSequence)e.getValue());
/*     */     }
/*     */     
/*     */ 
/* 399 */     HttpHeaderUtil.setKeepAlive(res, true);
/*     */     
/*     */ 
/* 402 */     res.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/* 403 */     res.headers().remove(HttpHeaderNames.TRAILER);
/*     */     
/* 405 */     return res;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHttpDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */