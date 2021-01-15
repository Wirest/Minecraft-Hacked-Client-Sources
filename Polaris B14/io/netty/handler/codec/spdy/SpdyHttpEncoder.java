/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpObject;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import java.util.List;
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
/*     */ public class SpdyHttpEncoder
/*     */   extends MessageToMessageEncoder<HttpObject>
/*     */ {
/*     */   private int currentStreamId;
/*     */   
/*     */   public SpdyHttpEncoder(SpdyVersion version)
/*     */   {
/* 129 */     if (version == null) {
/* 130 */       throw new NullPointerException("version");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 137 */     boolean valid = false;
/* 138 */     boolean last = false;
/*     */     
/* 140 */     if ((msg instanceof HttpRequest))
/*     */     {
/* 142 */       HttpRequest httpRequest = (HttpRequest)msg;
/* 143 */       SpdySynStreamFrame spdySynStreamFrame = createSynStreamFrame(httpRequest);
/* 144 */       out.add(spdySynStreamFrame);
/*     */       
/* 146 */       last = (spdySynStreamFrame.isLast()) || (spdySynStreamFrame.isUnidirectional());
/* 147 */       valid = true;
/*     */     }
/* 149 */     if ((msg instanceof HttpResponse))
/*     */     {
/* 151 */       HttpResponse httpResponse = (HttpResponse)msg;
/* 152 */       SpdyHeadersFrame spdyHeadersFrame = createHeadersFrame(httpResponse);
/* 153 */       out.add(spdyHeadersFrame);
/*     */       
/* 155 */       last = spdyHeadersFrame.isLast();
/* 156 */       valid = true;
/*     */     }
/* 158 */     if (((msg instanceof HttpContent)) && (!last))
/*     */     {
/* 160 */       HttpContent chunk = (HttpContent)msg;
/*     */       
/* 162 */       chunk.content().retain();
/* 163 */       SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
/* 164 */       if ((chunk instanceof LastHttpContent)) {
/* 165 */         LastHttpContent trailer = (LastHttpContent)chunk;
/* 166 */         HttpHeaders trailers = trailer.trailingHeaders();
/* 167 */         if (trailers.isEmpty()) {
/* 168 */           spdyDataFrame.setLast(true);
/* 169 */           out.add(spdyDataFrame);
/*     */         }
/*     */         else {
/* 172 */           SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(this.currentStreamId);
/* 173 */           spdyHeadersFrame.setLast(true);
/* 174 */           for (Map.Entry<CharSequence, CharSequence> entry : trailers) {
/* 175 */             spdyHeadersFrame.headers().add((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
/*     */           }
/*     */           
/*     */ 
/* 179 */           out.add(spdyDataFrame);
/* 180 */           out.add(spdyHeadersFrame);
/*     */         }
/*     */       } else {
/* 183 */         out.add(spdyDataFrame);
/*     */       }
/*     */       
/* 186 */       valid = true;
/*     */     }
/*     */     
/* 189 */     if (!valid) {
/* 190 */       throw new UnsupportedMessageTypeException(msg, new Class[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private SpdySynStreamFrame createSynStreamFrame(HttpRequest httpRequest)
/*     */     throws Exception
/*     */   {
/* 197 */     HttpHeaders httpHeaders = httpRequest.headers();
/* 198 */     int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID).intValue();
/* 199 */     int associatedToStreamId = httpHeaders.getInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, 0);
/* 200 */     byte priority = (byte)httpHeaders.getInt(SpdyHttpHeaders.Names.PRIORITY, 0);
/* 201 */     CharSequence scheme = (CharSequence)httpHeaders.get(SpdyHttpHeaders.Names.SCHEME);
/* 202 */     httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
/* 203 */     httpHeaders.remove(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID);
/* 204 */     httpHeaders.remove(SpdyHttpHeaders.Names.PRIORITY);
/* 205 */     httpHeaders.remove(SpdyHttpHeaders.Names.SCHEME);
/*     */     
/*     */ 
/*     */ 
/* 209 */     httpHeaders.remove(HttpHeaderNames.CONNECTION);
/* 210 */     httpHeaders.remove(HttpHeaderNames.KEEP_ALIVE);
/* 211 */     httpHeaders.remove(HttpHeaderNames.PROXY_CONNECTION);
/* 212 */     httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
/*     */     
/* 214 */     SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority);
/*     */     
/*     */ 
/*     */ 
/* 218 */     SpdyHeaders frameHeaders = spdySynStreamFrame.headers();
/* 219 */     frameHeaders.set(SpdyHeaders.HttpNames.METHOD, httpRequest.method().name());
/* 220 */     frameHeaders.set(SpdyHeaders.HttpNames.PATH, httpRequest.uri());
/* 221 */     frameHeaders.set(SpdyHeaders.HttpNames.VERSION, httpRequest.protocolVersion().text());
/*     */     
/*     */ 
/* 224 */     CharSequence host = (CharSequence)httpHeaders.get(HttpHeaderNames.HOST);
/* 225 */     httpHeaders.remove(HttpHeaderNames.HOST);
/* 226 */     frameHeaders.set(SpdyHeaders.HttpNames.HOST, host);
/*     */     
/*     */ 
/* 229 */     if (scheme == null) {
/* 230 */       scheme = "https";
/*     */     }
/* 232 */     frameHeaders.set(SpdyHeaders.HttpNames.SCHEME, scheme);
/*     */     
/*     */ 
/* 235 */     for (Map.Entry<CharSequence, CharSequence> entry : httpHeaders) {
/* 236 */       frameHeaders.add((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
/*     */     }
/* 238 */     this.currentStreamId = spdySynStreamFrame.streamId();
/* 239 */     if (associatedToStreamId == 0) {
/* 240 */       spdySynStreamFrame.setLast(isLast(httpRequest));
/*     */     } else {
/* 242 */       spdySynStreamFrame.setUnidirectional(true);
/*     */     }
/*     */     
/* 245 */     return spdySynStreamFrame;
/*     */   }
/*     */   
/*     */   private SpdyHeadersFrame createHeadersFrame(HttpResponse httpResponse)
/*     */     throws Exception
/*     */   {
/* 251 */     HttpHeaders httpHeaders = httpResponse.headers();
/* 252 */     int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID).intValue();
/* 253 */     httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
/*     */     
/*     */ 
/*     */ 
/* 257 */     httpHeaders.remove(HttpHeaderNames.CONNECTION);
/* 258 */     httpHeaders.remove(HttpHeaderNames.KEEP_ALIVE);
/* 259 */     httpHeaders.remove(HttpHeaderNames.PROXY_CONNECTION);
/* 260 */     httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
/*     */     SpdyHeadersFrame spdyHeadersFrame;
/*     */     SpdyHeadersFrame spdyHeadersFrame;
/* 263 */     if (SpdyCodecUtil.isServerId(streamId)) {
/* 264 */       spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId);
/*     */     } else {
/* 266 */       spdyHeadersFrame = new DefaultSpdySynReplyFrame(streamId);
/*     */     }
/* 268 */     SpdyHeaders frameHeaders = spdyHeadersFrame.headers();
/*     */     
/* 270 */     frameHeaders.set(SpdyHeaders.HttpNames.STATUS, httpResponse.status().codeAsText());
/* 271 */     frameHeaders.set(SpdyHeaders.HttpNames.VERSION, httpResponse.protocolVersion().text());
/*     */     
/*     */ 
/* 274 */     for (Map.Entry<CharSequence, CharSequence> entry : httpHeaders) {
/* 275 */       spdyHeadersFrame.headers().add((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
/*     */     }
/*     */     
/* 278 */     this.currentStreamId = streamId;
/* 279 */     spdyHeadersFrame.setLast(isLast(httpResponse));
/*     */     
/* 281 */     return spdyHeadersFrame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isLast(HttpMessage httpMessage)
/*     */   {
/* 291 */     if ((httpMessage instanceof FullHttpMessage)) {
/* 292 */       FullHttpMessage fullMessage = (FullHttpMessage)httpMessage;
/* 293 */       if ((fullMessage.trailingHeaders().isEmpty()) && (!fullMessage.content().isReadable())) {
/* 294 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 298 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHttpEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */