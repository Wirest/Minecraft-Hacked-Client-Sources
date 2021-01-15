/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.util.NetUtil;
/*    */ import io.netty.util.internal.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultSocks4CommandResponse
/*    */   extends AbstractSocks4Message
/*    */   implements Socks4CommandResponse
/*    */ {
/*    */   private final Socks4CommandStatus status;
/*    */   private final String dstAddr;
/*    */   private final int dstPort;
/*    */   
/*    */   public DefaultSocks4CommandResponse(Socks4CommandStatus status)
/*    */   {
/* 37 */     this(status, null, 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultSocks4CommandResponse(Socks4CommandStatus status, String dstAddr, int dstPort)
/*    */   {
/* 48 */     if (status == null) {
/* 49 */       throw new NullPointerException("cmdStatus");
/*    */     }
/* 51 */     if ((dstAddr != null) && 
/* 52 */       (!NetUtil.isValidIpV4Address(dstAddr))) {
/* 53 */       throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a valid IPv4 address)");
/*    */     }
/*    */     
/*    */ 
/* 57 */     if ((dstPort < 0) || (dstPort > 65535)) {
/* 58 */       throw new IllegalArgumentException("dstPort: " + dstPort + " (expected: 0~65535)");
/*    */     }
/*    */     
/* 61 */     this.status = status;
/* 62 */     this.dstAddr = dstAddr;
/* 63 */     this.dstPort = dstPort;
/*    */   }
/*    */   
/*    */   public Socks4CommandStatus status()
/*    */   {
/* 68 */     return this.status;
/*    */   }
/*    */   
/*    */   public String dstAddr()
/*    */   {
/* 73 */     return this.dstAddr;
/*    */   }
/*    */   
/*    */   public int dstPort()
/*    */   {
/* 78 */     return this.dstPort;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     StringBuilder buf = new StringBuilder(96);
/* 84 */     buf.append(StringUtil.simpleClassName(this));
/*    */     
/* 86 */     DecoderResult decoderResult = decoderResult();
/* 87 */     if (!decoderResult.isSuccess()) {
/* 88 */       buf.append("(decoderResult: ");
/* 89 */       buf.append(decoderResult);
/* 90 */       buf.append(", dstAddr: ");
/*    */     } else {
/* 92 */       buf.append("(dstAddr: ");
/*    */     }
/* 94 */     buf.append(dstAddr());
/* 95 */     buf.append(", dstPort: ");
/* 96 */     buf.append(dstPort());
/* 97 */     buf.append(')');
/*    */     
/* 99 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\DefaultSocks4CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */