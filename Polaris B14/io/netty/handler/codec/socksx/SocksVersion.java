/*    */ package io.netty.handler.codec.socksx;
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
/*    */ 
/*    */ public enum SocksVersion
/*    */ {
/* 26 */   SOCKS4a((byte)4), 
/*    */   
/*    */ 
/*    */ 
/* 30 */   SOCKS5((byte)5), 
/*    */   
/*    */ 
/*    */ 
/* 34 */   UNKNOWN((byte)-1);
/*    */   
/*    */ 
/*    */ 
/*    */   private final byte b;
/*    */   
/*    */ 
/*    */   public static SocksVersion valueOf(byte b)
/*    */   {
/* 43 */     if (b == SOCKS4a.byteValue()) {
/* 44 */       return SOCKS4a;
/*    */     }
/* 46 */     if (b == SOCKS5.byteValue()) {
/* 47 */       return SOCKS5;
/*    */     }
/* 49 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */ 
/*    */   private SocksVersion(byte b)
/*    */   {
/* 55 */     this.b = b;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public byte byteValue()
/*    */   {
/* 62 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\SocksVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */