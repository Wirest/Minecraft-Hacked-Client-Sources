/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.MessageToMessageCodec;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
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
/*     */ public abstract class HttpContentEncoder
/*     */   extends MessageToMessageCodec<HttpRequest, HttpObject>
/*     */ {
/*     */   private final Queue<CharSequence> acceptEncodingQueue;
/*     */   private CharSequence acceptEncoding;
/*     */   private EmbeddedChannel encoder;
/*     */   private State state;
/*     */   
/*     */   private static enum State
/*     */   {
/*  54 */     PASS_THROUGH, 
/*  55 */     AWAIT_HEADERS, 
/*  56 */     AWAIT_CONTENT;
/*     */     
/*     */     private State() {} }
/*  59 */   public HttpContentEncoder() { this.acceptEncodingQueue = new ArrayDeque();
/*     */     
/*     */ 
/*  62 */     this.state = State.AWAIT_HEADERS;
/*     */   }
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/*  66 */     return ((msg instanceof HttpContent)) || ((msg instanceof HttpResponse));
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  72 */     CharSequence acceptedEncoding = (CharSequence)msg.headers().get(HttpHeaderNames.ACCEPT_ENCODING);
/*  73 */     if (acceptedEncoding == null) {
/*  74 */       acceptedEncoding = HttpHeaderValues.IDENTITY;
/*     */     }
/*  76 */     this.acceptEncodingQueue.add(acceptedEncoding);
/*  77 */     out.add(ReferenceCountUtil.retain(msg));
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception
/*     */   {
/*  82 */     boolean isFull = ((msg instanceof HttpResponse)) && ((msg instanceof LastHttpContent));
/*  83 */     switch (this.state) {
/*     */     case AWAIT_HEADERS: 
/*  85 */       ensureHeaders(msg);
/*  86 */       assert (this.encoder == null);
/*     */       
/*  88 */       HttpResponse res = (HttpResponse)msg;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */       if (isPassthru(res)) {
/*  96 */         if (isFull) {
/*  97 */           out.add(ReferenceCountUtil.retain(res));
/*     */         } else {
/*  99 */           out.add(res);
/*     */           
/* 101 */           this.state = State.PASS_THROUGH;
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 107 */         this.acceptEncoding = ((CharSequence)this.acceptEncodingQueue.poll());
/* 108 */         if (this.acceptEncoding == null) {
/* 109 */           throw new IllegalStateException("cannot send more responses than requests");
/*     */         }
/*     */         
/* 112 */         if (isFull)
/*     */         {
/* 114 */           if (!((ByteBufHolder)res).content().isReadable()) {
/* 115 */             out.add(ReferenceCountUtil.retain(res));
/* 116 */             return;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 121 */         Result result = beginEncode(res, this.acceptEncoding);
/*     */         
/*     */ 
/* 124 */         if (result == null) {
/* 125 */           if (isFull) {
/* 126 */             out.add(ReferenceCountUtil.retain(res));
/*     */           } else {
/* 128 */             out.add(res);
/*     */             
/* 130 */             this.state = State.PASS_THROUGH;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 135 */           this.encoder = result.contentEncoder();
/*     */           
/*     */ 
/*     */ 
/* 139 */           res.headers().set(HttpHeaderNames.CONTENT_ENCODING, result.targetContentEncoding());
/*     */           
/*     */ 
/* 142 */           res.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
/* 143 */           res.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/*     */           
/*     */ 
/* 146 */           if (isFull)
/*     */           {
/* 148 */             HttpResponse newRes = new DefaultHttpResponse(res.protocolVersion(), res.status());
/* 149 */             newRes.headers().set(res.headers());
/* 150 */             out.add(newRes);
/*     */           }
/*     */           else {
/* 153 */             out.add(res);
/* 154 */             this.state = State.AWAIT_CONTENT;
/* 155 */             if (!(msg instanceof HttpContent)) {
/*     */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       break;
/*     */     case AWAIT_CONTENT: 
/* 164 */       ensureContent(msg);
/* 165 */       if (encodeContent((HttpContent)msg, out)) {
/* 166 */         this.state = State.AWAIT_HEADERS;
/*     */       }
/*     */       
/*     */       break;
/*     */     case PASS_THROUGH: 
/* 171 */       ensureContent(msg);
/* 172 */       out.add(ReferenceCountUtil.retain(msg));
/*     */       
/* 174 */       if ((msg instanceof LastHttpContent)) {
/* 175 */         this.state = State.AWAIT_HEADERS;
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isPassthru(HttpResponse res)
/*     */   {
/* 183 */     int code = res.status().code();
/* 184 */     return (code < 200) || (code == 204) || (code == 304);
/*     */   }
/*     */   
/*     */   private static void ensureHeaders(HttpObject msg) {
/* 188 */     if (!(msg instanceof HttpResponse)) {
/* 189 */       throw new IllegalStateException("unexpected message type: " + msg.getClass().getName() + " (expected: " + HttpResponse.class.getSimpleName() + ')');
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void ensureContent(HttpObject msg)
/*     */   {
/* 196 */     if (!(msg instanceof HttpContent)) {
/* 197 */       throw new IllegalStateException("unexpected message type: " + msg.getClass().getName() + " (expected: " + HttpContent.class.getSimpleName() + ')');
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean encodeContent(HttpContent c, List<Object> out)
/*     */   {
/* 204 */     ByteBuf content = c.content();
/*     */     
/* 206 */     encode(content, out);
/*     */     
/* 208 */     if ((c instanceof LastHttpContent)) {
/* 209 */       finishEncode(out);
/* 210 */       LastHttpContent last = (LastHttpContent)c;
/*     */       
/*     */ 
/*     */ 
/* 214 */       HttpHeaders headers = last.trailingHeaders();
/* 215 */       if (headers.isEmpty()) {
/* 216 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       } else {
/* 218 */         out.add(new ComposedLastHttpContent(headers));
/*     */       }
/* 220 */       return true;
/*     */     }
/* 222 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Result beginEncode(HttpResponse paramHttpResponse, CharSequence paramCharSequence)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 243 */     cleanup();
/* 244 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 249 */     cleanup();
/* 250 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 254 */     if (this.encoder != null)
/*     */     {
/* 256 */       if (this.encoder.finish()) {
/*     */         for (;;) {
/* 258 */           ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
/* 259 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 264 */           buf.release();
/*     */         }
/*     */       }
/* 267 */       this.encoder = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void encode(ByteBuf in, List<Object> out)
/*     */   {
/* 273 */     this.encoder.writeOutbound(new Object[] { in.retain() });
/* 274 */     fetchEncoderOutput(out);
/*     */   }
/*     */   
/*     */   private void finishEncode(List<Object> out) {
/* 278 */     if (this.encoder.finish()) {
/* 279 */       fetchEncoderOutput(out);
/*     */     }
/* 281 */     this.encoder = null;
/*     */   }
/*     */   
/*     */   private void fetchEncoderOutput(List<Object> out) {
/*     */     for (;;) {
/* 286 */       ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
/* 287 */       if (buf == null) {
/*     */         break;
/*     */       }
/* 290 */       if (!buf.isReadable()) {
/* 291 */         buf.release();
/*     */       }
/*     */       else
/* 294 */         out.add(new DefaultHttpContent(buf));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Result {
/*     */     private final String targetContentEncoding;
/*     */     private final EmbeddedChannel contentEncoder;
/*     */     
/*     */     public Result(String targetContentEncoding, EmbeddedChannel contentEncoder) {
/* 303 */       if (targetContentEncoding == null) {
/* 304 */         throw new NullPointerException("targetContentEncoding");
/*     */       }
/* 306 */       if (contentEncoder == null) {
/* 307 */         throw new NullPointerException("contentEncoder");
/*     */       }
/*     */       
/* 310 */       this.targetContentEncoding = targetContentEncoding;
/* 311 */       this.contentEncoder = contentEncoder;
/*     */     }
/*     */     
/*     */     public String targetContentEncoding() {
/* 315 */       return this.targetContentEncoding;
/*     */     }
/*     */     
/*     */     public EmbeddedChannel contentEncoder() {
/* 319 */       return this.contentEncoder;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpContentEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */