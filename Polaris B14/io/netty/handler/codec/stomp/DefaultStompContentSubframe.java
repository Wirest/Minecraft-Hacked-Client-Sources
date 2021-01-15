/*     */ package io.netty.handler.codec.stomp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.DecoderResult;
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
/*     */ public class DefaultStompContentSubframe
/*     */   implements StompContentSubframe
/*     */ {
/*  26 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultStompContentSubframe(ByteBuf content) {
/*  30 */     if (content == null) {
/*  31 */       throw new NullPointerException("content");
/*     */     }
/*  33 */     this.content = content;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  38 */     return this.content;
/*     */   }
/*     */   
/*     */   public StompContentSubframe copy()
/*     */   {
/*  43 */     return new DefaultStompContentSubframe(content().copy());
/*     */   }
/*     */   
/*     */   public StompContentSubframe duplicate()
/*     */   {
/*  48 */     return new DefaultStompContentSubframe(content().duplicate());
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  53 */     return content().refCnt();
/*     */   }
/*     */   
/*     */   public StompContentSubframe retain()
/*     */   {
/*  58 */     content().retain();
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public StompContentSubframe retain(int increment)
/*     */   {
/*  64 */     content().retain(increment);
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public StompContentSubframe touch()
/*     */   {
/*  70 */     this.content.touch();
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public StompContentSubframe touch(Object hint)
/*     */   {
/*  76 */     this.content.touch(hint);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/*  82 */     return content().release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/*  87 */     return content().release(decrement);
/*     */   }
/*     */   
/*     */   public DecoderResult decoderResult()
/*     */   {
/*  92 */     return this.decoderResult;
/*     */   }
/*     */   
/*     */   public void setDecoderResult(DecoderResult decoderResult)
/*     */   {
/*  97 */     this.decoderResult = decoderResult;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 102 */     return "DefaultStompContent{decoderResult=" + this.decoderResult + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\DefaultStompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */