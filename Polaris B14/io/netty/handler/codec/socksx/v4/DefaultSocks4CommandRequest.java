/*     */ package io.netty.handler.codec.socksx.v4;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.IDN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSocks4CommandRequest
/*     */   extends AbstractSocks4Message
/*     */   implements Socks4CommandRequest
/*     */ {
/*     */   private final Socks4CommandType type;
/*     */   private final String dstAddr;
/*     */   private final int dstPort;
/*     */   private final String userId;
/*     */   
/*     */   public DefaultSocks4CommandRequest(Socks4CommandType type, String dstAddr, int dstPort)
/*     */   {
/*  41 */     this(type, dstAddr, dstPort, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultSocks4CommandRequest(Socks4CommandType type, String dstAddr, int dstPort, String userId)
/*     */   {
/*  53 */     if (type == null) {
/*  54 */       throw new NullPointerException("type");
/*     */     }
/*  56 */     if (dstAddr == null) {
/*  57 */       throw new NullPointerException("dstAddr");
/*     */     }
/*  59 */     if ((dstPort <= 0) || (dstPort >= 65536)) {
/*  60 */       throw new IllegalArgumentException("dstPort: " + dstPort + " (expected: 1~65535)");
/*     */     }
/*  62 */     if (userId == null) {
/*  63 */       throw new NullPointerException("userId");
/*     */     }
/*     */     
/*  66 */     this.userId = userId;
/*  67 */     this.type = type;
/*  68 */     this.dstAddr = IDN.toASCII(dstAddr);
/*  69 */     this.dstPort = dstPort;
/*     */   }
/*     */   
/*     */   public Socks4CommandType type()
/*     */   {
/*  74 */     return this.type;
/*     */   }
/*     */   
/*     */   public String dstAddr()
/*     */   {
/*  79 */     return this.dstAddr;
/*     */   }
/*     */   
/*     */   public int dstPort()
/*     */   {
/*  84 */     return this.dstPort;
/*     */   }
/*     */   
/*     */   public String userId()
/*     */   {
/*  89 */     return this.userId;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  94 */     StringBuilder buf = new StringBuilder(128);
/*  95 */     buf.append(StringUtil.simpleClassName(this));
/*     */     
/*  97 */     DecoderResult decoderResult = decoderResult();
/*  98 */     if (!decoderResult.isSuccess()) {
/*  99 */       buf.append("(decoderResult: ");
/* 100 */       buf.append(decoderResult);
/* 101 */       buf.append(", type: ");
/*     */     } else {
/* 103 */       buf.append("(type: ");
/*     */     }
/* 105 */     buf.append(type());
/* 106 */     buf.append(", dstAddr: ");
/* 107 */     buf.append(dstAddr());
/* 108 */     buf.append(", dstPort: ");
/* 109 */     buf.append(dstPort());
/* 110 */     buf.append(", userId: ");
/* 111 */     buf.append(userId());
/* 112 */     buf.append(')');
/*     */     
/* 114 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\DefaultSocks4CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */