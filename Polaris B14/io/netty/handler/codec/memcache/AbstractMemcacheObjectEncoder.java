/*     */ package io.netty.handler.codec.memcache;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class AbstractMemcacheObjectEncoder<M extends MemcacheMessage>
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*     */   private boolean expectingMoreContent;
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  40 */     if ((msg instanceof MemcacheMessage)) {
/*  41 */       if (this.expectingMoreContent) {
/*  42 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       }
/*     */       
/*     */ 
/*  46 */       M m = (MemcacheMessage)msg;
/*  47 */       out.add(encodeMessage(ctx, m));
/*     */     }
/*     */     
/*  50 */     if (((msg instanceof MemcacheContent)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion))) {
/*  51 */       int contentLength = contentLength(msg);
/*  52 */       if (contentLength > 0) {
/*  53 */         out.add(encodeAndRetain(msg));
/*     */       } else {
/*  55 */         out.add(Unpooled.EMPTY_BUFFER);
/*     */       }
/*     */       
/*  58 */       this.expectingMoreContent = (!(msg instanceof LastMemcacheContent));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*     */   {
/*  64 */     return ((msg instanceof MemcacheObject)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ByteBuf encodeMessage(ChannelHandlerContext paramChannelHandlerContext, M paramM);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int contentLength(Object msg)
/*     */   {
/*  83 */     if ((msg instanceof MemcacheContent)) {
/*  84 */       return ((MemcacheContent)msg).content().readableBytes();
/*     */     }
/*  86 */     if ((msg instanceof ByteBuf)) {
/*  87 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/*  89 */     if ((msg instanceof FileRegion)) {
/*  90 */       return (int)((FileRegion)msg).count();
/*     */     }
/*  92 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Object encodeAndRetain(Object msg)
/*     */   {
/* 102 */     if ((msg instanceof ByteBuf)) {
/* 103 */       return ((ByteBuf)msg).retain();
/*     */     }
/* 105 */     if ((msg instanceof MemcacheContent)) {
/* 106 */       return ((MemcacheContent)msg).content().retain();
/*     */     }
/* 108 */     if ((msg instanceof FileRegion)) {
/* 109 */       return ((FileRegion)msg).retain();
/*     */     }
/* 111 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */