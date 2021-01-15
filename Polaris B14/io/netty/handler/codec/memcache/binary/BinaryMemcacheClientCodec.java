/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerAppender;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.PrematureChannelClosureException;
/*     */ import io.netty.handler.codec.memcache.LastMemcacheContent;
/*     */ import java.util.List;
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
/*     */ public final class BinaryMemcacheClientCodec
/*     */   extends ChannelHandlerAppender
/*     */ {
/*     */   private final boolean failOnMissingResponse;
/*  41 */   private final AtomicLong requestResponseCounter = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec()
/*     */   {
/*  47 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec(int decodeChunkSize)
/*     */   {
/*  56 */     this(decodeChunkSize, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec(int decodeChunkSize, boolean failOnMissingResponse)
/*     */   {
/*  66 */     this.failOnMissingResponse = failOnMissingResponse;
/*  67 */     add(new Decoder(decodeChunkSize));
/*  68 */     add(new Encoder(null));
/*     */   }
/*     */   
/*     */   private final class Encoder extends BinaryMemcacheRequestEncoder {
/*     */     private Encoder() {}
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/*  75 */       super.encode(ctx, msg, out);
/*     */       
/*  77 */       if ((BinaryMemcacheClientCodec.this.failOnMissingResponse) && ((msg instanceof LastMemcacheContent))) {
/*  78 */         BinaryMemcacheClientCodec.this.requestResponseCounter.incrementAndGet();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Decoder extends BinaryMemcacheResponseDecoder
/*     */   {
/*     */     Decoder(int chunkSize) {
/*  86 */       super();
/*     */     }
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*  91 */       int oldSize = out.size();
/*  92 */       super.decode(ctx, in, out);
/*     */       
/*  94 */       if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
/*  95 */         int size = out.size();
/*  96 */         for (int i = oldSize; i < size; i++) {
/*  97 */           Object msg = out.get(i);
/*  98 */           if ((msg instanceof LastMemcacheContent)) {
/*  99 */             BinaryMemcacheClientCodec.this.requestResponseCounter.decrementAndGet();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */     {
/* 107 */       super.channelInactive(ctx);
/*     */       
/* 109 */       if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
/* 110 */         long missingResponses = BinaryMemcacheClientCodec.this.requestResponseCounter.get();
/* 111 */         if (missingResponses > 0L) {
/* 112 */           ctx.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheClientCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */