/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerAppender;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.PrematureChannelClosureException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class HttpClientCodec
/*     */   extends ChannelHandlerAppender
/*     */   implements HttpClientUpgradeHandler.SourceCodec
/*     */ {
/*  47 */   private final Queue<HttpMethod> queue = new ArrayDeque();
/*     */   
/*     */ 
/*     */   private boolean done;
/*     */   
/*  52 */   private final AtomicLong requestResponseCounter = new AtomicLong();
/*     */   
/*     */ 
/*     */   private final boolean failOnMissingResponse;
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpClientCodec()
/*     */   {
/*  61 */     this(4096, 8192, 8192, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
/*     */   {
/*  68 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse)
/*     */   {
/*  76 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, failOnMissingResponse, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders)
/*     */   {
/*  85 */     add(new Decoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders));
/*  86 */     add(new Encoder(null));
/*  87 */     this.failOnMissingResponse = failOnMissingResponse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void upgradeFrom(ChannelHandlerContext ctx)
/*     */   {
/*  96 */     ctx.pipeline().remove(Decoder.class);
/*  97 */     ctx.pipeline().remove(Encoder.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpRequestEncoder encoder()
/*     */   {
/* 104 */     return (HttpRequestEncoder)handlerAt(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpResponseDecoder decoder()
/*     */   {
/* 111 */     return (HttpResponseDecoder)handlerAt(0);
/*     */   }
/*     */   
/*     */   public void setSingleDecode(boolean singleDecode) {
/* 115 */     decoder().setSingleDecode(singleDecode);
/*     */   }
/*     */   
/*     */   public boolean isSingleDecode() {
/* 119 */     return decoder().isSingleDecode();
/*     */   }
/*     */   
/*     */   private final class Encoder extends HttpRequestEncoder
/*     */   {
/*     */     private Encoder() {}
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/* 127 */       if (((msg instanceof HttpRequest)) && (!HttpClientCodec.this.done)) {
/* 128 */         HttpClientCodec.this.queue.offer(((HttpRequest)msg).method());
/*     */       }
/*     */       
/* 131 */       super.encode(ctx, msg, out);
/*     */       
/* 133 */       if (HttpClientCodec.this.failOnMissingResponse)
/*     */       {
/* 135 */         if ((msg instanceof LastHttpContent))
/*     */         {
/* 137 */           HttpClientCodec.this.requestResponseCounter.incrementAndGet();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Decoder extends HttpResponseDecoder {
/*     */     Decoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
/* 145 */       super(maxHeaderSize, maxChunkSize, validateHeaders);
/*     */     }
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
/*     */       throws Exception
/*     */     {
/* 151 */       if (HttpClientCodec.this.done) {
/* 152 */         int readable = actualReadableBytes();
/* 153 */         if (readable == 0)
/*     */         {
/*     */ 
/* 156 */           return;
/*     */         }
/* 158 */         out.add(buffer.readBytes(readable));
/*     */       } else {
/* 160 */         int oldSize = out.size();
/* 161 */         super.decode(ctx, buffer, out);
/* 162 */         if (HttpClientCodec.this.failOnMissingResponse) {
/* 163 */           int size = out.size();
/* 164 */           for (int i = oldSize; i < size; i++) {
/* 165 */             decrement(out.get(i));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void decrement(Object msg) {
/* 172 */       if (msg == null) {
/* 173 */         return;
/*     */       }
/*     */       
/*     */ 
/* 177 */       if ((msg instanceof LastHttpContent)) {
/* 178 */         HttpClientCodec.this.requestResponseCounter.decrementAndGet();
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean isContentAlwaysEmpty(HttpMessage msg)
/*     */     {
/* 184 */       int statusCode = ((HttpResponse)msg).status().code();
/* 185 */       if (statusCode == 100)
/*     */       {
/* 187 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 192 */       HttpMethod method = (HttpMethod)HttpClientCodec.this.queue.poll();
/*     */       
/* 194 */       char firstChar = method.name().charAt(0);
/* 195 */       switch (firstChar)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */       case 'H': 
/* 201 */         if (HttpMethod.HEAD.equals(method)) {
/* 202 */           return true;
/*     */         }
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
/*     */         break;
/*     */       case 'C': 
/* 220 */         if ((statusCode == 200) && 
/* 221 */           (HttpMethod.CONNECT.equals(method)))
/*     */         {
/* 223 */           HttpClientCodec.this.done = true;
/* 224 */           HttpClientCodec.this.queue.clear();
/* 225 */           return true;
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/* 231 */       return super.isContentAlwaysEmpty(msg);
/*     */     }
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx)
/*     */       throws Exception
/*     */     {
/* 237 */       super.channelInactive(ctx);
/*     */       
/* 239 */       if (HttpClientCodec.this.failOnMissingResponse) {
/* 240 */         long missingResponses = HttpClientCodec.this.requestResponseCounter.get();
/* 241 */         if (missingResponses > 0L) {
/* 242 */           ctx.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpClientCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */