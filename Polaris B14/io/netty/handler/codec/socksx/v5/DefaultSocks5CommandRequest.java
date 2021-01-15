/*     */ package io.netty.handler.codec.socksx.v5;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.util.NetUtil;
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
/*     */ public final class DefaultSocks5CommandRequest
/*     */   extends AbstractSocks5Message
/*     */   implements Socks5CommandRequest
/*     */ {
/*     */   private final Socks5CommandType type;
/*     */   private final Socks5AddressType dstAddrType;
/*     */   private final String dstAddr;
/*     */   private final int dstPort;
/*     */   
/*     */   public DefaultSocks5CommandRequest(Socks5CommandType type, Socks5AddressType dstAddrType, String dstAddr, int dstPort)
/*     */   {
/*  37 */     if (type == null) {
/*  38 */       throw new NullPointerException("type");
/*     */     }
/*  40 */     if (dstAddrType == null) {
/*  41 */       throw new NullPointerException("dstAddrType");
/*     */     }
/*  43 */     if (dstAddr == null) {
/*  44 */       throw new NullPointerException("dstAddr");
/*     */     }
/*     */     
/*  47 */     if (dstAddrType == Socks5AddressType.IPv4) {
/*  48 */       if (!NetUtil.isValidIpV4Address(dstAddr)) {
/*  49 */         throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a valid IPv4 address)");
/*     */       }
/*  51 */     } else if (dstAddrType == Socks5AddressType.DOMAIN) {
/*  52 */       dstAddr = IDN.toASCII(dstAddr);
/*  53 */       if (dstAddr.length() > 255) {
/*  54 */         throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: less than 256 chars)");
/*     */       }
/*  56 */     } else if ((dstAddrType == Socks5AddressType.IPv6) && 
/*  57 */       (!NetUtil.isValidIpV6Address(dstAddr))) {
/*  58 */       throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a valid IPv6 address");
/*     */     }
/*     */     
/*     */ 
/*  62 */     if ((dstPort <= 0) || (dstPort >= 65536)) {
/*  63 */       throw new IllegalArgumentException("dstPort: " + dstPort + " (expected: 1~65535)");
/*     */     }
/*     */     
/*  66 */     this.type = type;
/*  67 */     this.dstAddrType = dstAddrType;
/*  68 */     this.dstAddr = dstAddr;
/*  69 */     this.dstPort = dstPort;
/*     */   }
/*     */   
/*     */   public Socks5CommandType type()
/*     */   {
/*  74 */     return this.type;
/*     */   }
/*     */   
/*     */   public Socks5AddressType dstAddrType()
/*     */   {
/*  79 */     return this.dstAddrType;
/*     */   }
/*     */   
/*     */   public String dstAddr()
/*     */   {
/*  84 */     return this.dstAddr;
/*     */   }
/*     */   
/*     */   public int dstPort()
/*     */   {
/*  89 */     return this.dstPort;
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
/* 106 */     buf.append(", dstAddrType: ");
/* 107 */     buf.append(dstAddrType());
/* 108 */     buf.append(", dstAddr: ");
/* 109 */     buf.append(dstAddr());
/* 110 */     buf.append(", dstPort: ");
/* 111 */     buf.append(dstPort());
/* 112 */     buf.append(')');
/*     */     
/* 114 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */