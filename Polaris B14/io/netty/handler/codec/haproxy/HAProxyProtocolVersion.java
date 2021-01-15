/*    */ package io.netty.handler.codec.haproxy;
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
/*    */ 
/*    */ public enum HAProxyProtocolVersion
/*    */ {
/* 27 */   V1((byte)16), 
/*    */   
/*    */ 
/*    */ 
/* 31 */   V2((byte)32);
/*    */   
/*    */ 
/*    */ 
/*    */   private static final byte VERSION_MASK = -16;
/*    */   
/*    */ 
/*    */   private final byte byteValue;
/*    */   
/*    */ 
/*    */ 
/*    */   private HAProxyProtocolVersion(byte byteValue)
/*    */   {
/* 44 */     this.byteValue = byteValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static HAProxyProtocolVersion valueOf(byte verCmdByte)
/*    */   {
/* 53 */     int version = verCmdByte & 0xFFFFFFF0;
/* 54 */     switch ((byte)version) {
/*    */     case 32: 
/* 56 */       return V2;
/*    */     case 16: 
/* 58 */       return V1;
/*    */     }
/* 60 */     throw new IllegalArgumentException("unknown version: " + version);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte byteValue()
/*    */   {
/* 68 */     return this.byteValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyProtocolVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */