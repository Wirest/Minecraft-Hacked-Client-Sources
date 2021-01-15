/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.List;
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
/*     */ public abstract class HttpContentDecoder
/*     */   extends MessageToMessageDecoder<HttpObject>
/*     */ {
/*  48 */   private static final String IDENTITY = HttpHeaderValues.IDENTITY.toString();
/*     */   private EmbeddedChannel decoder;
/*     */   private boolean continueResponse;
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  55 */     if (((msg instanceof HttpResponse)) && (((HttpResponse)msg).status().code() == 100))
/*     */     {
/*  57 */       if (!(msg instanceof LastHttpContent)) {
/*  58 */         this.continueResponse = true;
/*     */       }
/*     */       
/*  61 */       out.add(ReferenceCountUtil.retain(msg));
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     if (this.continueResponse) {
/*  66 */       if ((msg instanceof LastHttpContent)) {
/*  67 */         this.continueResponse = false;
/*     */       }
/*     */       
/*  70 */       out.add(ReferenceCountUtil.retain(msg));
/*  71 */       return;
/*     */     }
/*     */     
/*  74 */     if ((msg instanceof HttpMessage)) {
/*  75 */       cleanup();
/*  76 */       HttpMessage message = (HttpMessage)msg;
/*  77 */       HttpHeaders headers = message.headers();
/*     */       
/*     */ 
/*  80 */       String contentEncoding = (String)headers.getAndConvert(HttpHeaderNames.CONTENT_ENCODING);
/*  81 */       if (contentEncoding != null) {
/*  82 */         contentEncoding = contentEncoding.trim();
/*     */       } else {
/*  84 */         contentEncoding = IDENTITY;
/*     */       }
/*  86 */       this.decoder = newContentDecoder(contentEncoding);
/*     */       
/*  88 */       if (this.decoder == null) {
/*  89 */         if ((message instanceof HttpContent)) {
/*  90 */           ((HttpContent)message).retain();
/*     */         }
/*  92 */         out.add(message);
/*  93 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */       headers.remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */       
/*     */ 
/* 103 */       CharSequence targetContentEncoding = getTargetContentEncoding(contentEncoding);
/* 104 */       if (HttpHeaderValues.IDENTITY.equals(targetContentEncoding))
/*     */       {
/*     */ 
/* 107 */         headers.remove(HttpHeaderNames.CONTENT_ENCODING);
/*     */       } else {
/* 109 */         headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */       }
/*     */       
/* 112 */       if ((message instanceof HttpContent))
/*     */       {
/*     */         HttpMessage copy;
/*     */         
/*     */ 
/*     */ 
/* 118 */         if ((message instanceof HttpRequest)) {
/* 119 */           HttpRequest r = (HttpRequest)message;
/* 120 */           copy = new DefaultHttpRequest(r.protocolVersion(), r.method(), r.uri()); } else { HttpMessage copy;
/* 121 */           if ((message instanceof HttpResponse)) {
/* 122 */             HttpResponse r = (HttpResponse)message;
/* 123 */             copy = new DefaultHttpResponse(r.protocolVersion(), r.status());
/*     */           } else {
/* 125 */             throw new CodecException("Object of class " + message.getClass().getName() + " is not a HttpRequest or HttpResponse");
/*     */           } }
/*     */         HttpMessage copy;
/* 128 */         copy.headers().set(message.headers());
/* 129 */         copy.setDecoderResult(message.decoderResult());
/* 130 */         out.add(copy);
/*     */       } else {
/* 132 */         out.add(message);
/*     */       }
/*     */     }
/*     */     
/* 136 */     if ((msg instanceof HttpContent)) {
/* 137 */       HttpContent c = (HttpContent)msg;
/* 138 */       if (this.decoder == null) {
/* 139 */         out.add(c.retain());
/*     */       } else {
/* 141 */         decodeContent(c, out);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void decodeContent(HttpContent c, List<Object> out) {
/* 147 */     ByteBuf content = c.content();
/*     */     
/* 149 */     decode(content, out);
/*     */     
/* 151 */     if ((c instanceof LastHttpContent)) {
/* 152 */       finishDecode(out);
/*     */       
/* 154 */       LastHttpContent last = (LastHttpContent)c;
/*     */       
/*     */ 
/* 157 */       HttpHeaders headers = last.trailingHeaders();
/* 158 */       if (headers.isEmpty()) {
/* 159 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       } else {
/* 161 */         out.add(new ComposedLastHttpContent(headers));
/*     */       }
/*     */     }
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
/*     */   protected abstract EmbeddedChannel newContentDecoder(String paramString)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CharSequence getTargetContentEncoding(String contentEncoding)
/*     */     throws Exception
/*     */   {
/* 187 */     return HttpHeaderValues.IDENTITY;
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 192 */     cleanup();
/* 193 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 198 */     cleanup();
/* 199 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 203 */     if (this.decoder != null)
/*     */     {
/* 205 */       if (this.decoder.finish()) {
/*     */         for (;;) {
/* 207 */           ByteBuf buf = (ByteBuf)this.decoder.readInbound();
/* 208 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 212 */           buf.release();
/*     */         }
/*     */       }
/* 215 */       this.decoder = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void decode(ByteBuf in, List<Object> out)
/*     */   {
/* 221 */     this.decoder.writeInbound(new Object[] { in.retain() });
/* 222 */     fetchDecoderOutput(out);
/*     */   }
/*     */   
/*     */   private void finishDecode(List<Object> out) {
/* 226 */     if (this.decoder.finish()) {
/* 227 */       fetchDecoderOutput(out);
/*     */     }
/* 229 */     this.decoder = null;
/*     */   }
/*     */   
/*     */   private void fetchDecoderOutput(List<Object> out) {
/*     */     for (;;) {
/* 234 */       ByteBuf buf = (ByteBuf)this.decoder.readInbound();
/* 235 */       if (buf == null) {
/*     */         break;
/*     */       }
/* 238 */       if (!buf.isReadable()) {
/* 239 */         buf.release();
/*     */       }
/*     */       else {
/* 242 */         out.add(new DefaultHttpContent(buf));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpContentDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */