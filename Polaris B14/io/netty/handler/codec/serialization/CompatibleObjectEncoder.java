/*     */ package io.netty.handler.codec.serialization;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompatibleObjectEncoder
/*     */   extends MessageToByteEncoder<Serializable>
/*     */ {
/*  39 */   private static final AttributeKey<ObjectOutputStream> OOS = AttributeKey.valueOf(CompatibleObjectEncoder.class, "OOS");
/*     */   
/*     */ 
/*     */   private final int resetInterval;
/*     */   
/*     */   private int writtenObjects;
/*     */   
/*     */ 
/*     */   public CompatibleObjectEncoder()
/*     */   {
/*  49 */     this(16);
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
/*     */   public CompatibleObjectEncoder(int resetInterval)
/*     */   {
/*  62 */     if (resetInterval < 0) {
/*  63 */       throw new IllegalArgumentException("resetInterval: " + resetInterval);
/*     */     }
/*     */     
/*  66 */     this.resetInterval = resetInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectOutputStream newObjectOutputStream(OutputStream out)
/*     */     throws Exception
/*     */   {
/*  75 */     return new ObjectOutputStream(out);
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception
/*     */   {
/*  80 */     Attribute<ObjectOutputStream> oosAttr = ctx.attr(OOS);
/*  81 */     ObjectOutputStream oos = (ObjectOutputStream)oosAttr.get();
/*  82 */     if (oos == null) {
/*  83 */       oos = newObjectOutputStream(new ByteBufOutputStream(out));
/*  84 */       ObjectOutputStream newOos = (ObjectOutputStream)oosAttr.setIfAbsent(oos);
/*  85 */       if (newOos != null) {
/*  86 */         oos = newOos;
/*     */       }
/*     */     }
/*     */     
/*  90 */     synchronized (oos) {
/*  91 */       if (this.resetInterval != 0)
/*     */       {
/*  93 */         this.writtenObjects += 1;
/*  94 */         if (this.writtenObjects % this.resetInterval == 0) {
/*  95 */           oos.reset();
/*     */         }
/*     */       }
/*     */       
/*  99 */       oos.writeObject(msg);
/* 100 */       oos.flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\CompatibleObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */