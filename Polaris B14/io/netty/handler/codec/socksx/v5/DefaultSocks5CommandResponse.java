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
/*     */ public final class DefaultSocks5CommandResponse
/*     */   extends AbstractSocks5Message
/*     */   implements Socks5CommandResponse
/*     */ {
/*     */   private final Socks5CommandStatus status;
/*     */   private final Socks5AddressType bndAddrType;
/*     */   private final String bndAddr;
/*     */   private final int bndPort;
/*     */   
/*     */   public DefaultSocks5CommandResponse(Socks5CommandStatus status, Socks5AddressType bndAddrType)
/*     */   {
/*  35 */     this(status, bndAddrType, null, 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultSocks5CommandResponse(Socks5CommandStatus status, Socks5AddressType bndAddrType, String bndAddr, int bndPort)
/*     */   {
/*  41 */     if (status == null) {
/*  42 */       throw new NullPointerException("status");
/*     */     }
/*  44 */     if (bndAddrType == null) {
/*  45 */       throw new NullPointerException("bndAddrType");
/*     */     }
/*  47 */     if (bndAddr != null) {
/*  48 */       if (bndAddrType == Socks5AddressType.IPv4) {
/*  49 */         if (!NetUtil.isValidIpV4Address(bndAddr)) {
/*  50 */           throw new IllegalArgumentException("bndAddr: " + bndAddr + " (expected: a valid IPv4 address)");
/*     */         }
/*  52 */       } else if (bndAddrType == Socks5AddressType.DOMAIN) {
/*  53 */         bndAddr = IDN.toASCII(bndAddr);
/*  54 */         if (bndAddr.length() > 255) {
/*  55 */           throw new IllegalArgumentException("bndAddr: " + bndAddr + " (expected: less than 256 chars)");
/*     */         }
/*  57 */       } else if ((bndAddrType == Socks5AddressType.IPv6) && 
/*  58 */         (!NetUtil.isValidIpV6Address(bndAddr))) {
/*  59 */         throw new IllegalArgumentException("bndAddr: " + bndAddr + " (expected: a valid IPv6 address)");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  64 */     if ((bndPort < 0) || (bndPort > 65535)) {
/*  65 */       throw new IllegalArgumentException("bndPort: " + bndPort + " (expected: 0~65535)");
/*     */     }
/*  67 */     this.status = status;
/*  68 */     this.bndAddrType = bndAddrType;
/*  69 */     this.bndAddr = bndAddr;
/*  70 */     this.bndPort = bndPort;
/*     */   }
/*     */   
/*     */   public Socks5CommandStatus status()
/*     */   {
/*  75 */     return this.status;
/*     */   }
/*     */   
/*     */   public Socks5AddressType bndAddrType()
/*     */   {
/*  80 */     return this.bndAddrType;
/*     */   }
/*     */   
/*     */   public String bndAddr()
/*     */   {
/*  85 */     return this.bndAddr;
/*     */   }
/*     */   
/*     */   public int bndPort()
/*     */   {
/*  90 */     return this.bndPort;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  95 */     StringBuilder buf = new StringBuilder(128);
/*  96 */     buf.append(StringUtil.simpleClassName(this));
/*     */     
/*  98 */     DecoderResult decoderResult = decoderResult();
/*  99 */     if (!decoderResult.isSuccess()) {
/* 100 */       buf.append("(decoderResult: ");
/* 101 */       buf.append(decoderResult);
/* 102 */       buf.append(", status: ");
/*     */     } else {
/* 104 */       buf.append("(status: ");
/*     */     }
/* 106 */     buf.append(status());
/* 107 */     buf.append(", bndAddrType: ");
/* 108 */     buf.append(bndAddrType());
/* 109 */     buf.append(", bndAddr: ");
/* 110 */     buf.append(bndAddr());
/* 111 */     buf.append(", bndPort: ");
/* 112 */     buf.append(bndPort());
/* 113 */     buf.append(')');
/*     */     
/* 115 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */